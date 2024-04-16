package com.lions.aplication.android.articler2

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.grpc.ManagedChannelBuilder
import protos.ArticleForm
import protos.ArticlerServiceGrpc
import protos.DelateArticleForm
import protos.ListArticles
import protos.LoginForm
import protos.Message
import protos.delateArticleForm



@Composable
fun HomePage(navController: NavController) {

    if (token == ""){
        navController.navigate("login")
    }


    NavigationBottom(navController = navController, position = 1, content = { Lista(sort = "all", navController = navController)})
}


data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val route: String

    )


@Composable
fun NavigationBottom(navController: NavController, position: Int, content: @Composable () ->  Unit) {
    val items = listOf(
        BottomNavigationItem(
            title = "Add",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add,
            hasNews = false,
            route = "addArticle"
        ),
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false,
            route = "home"
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle,
            hasNews = false,
            route = "profile"
        )
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(position)
    }


    Scaffold(
        bottomBar = {
            NavigationBar(

                Modifier.height(50.dp),
                ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.route)

                        },
                        icon = {
                            if (index == selectedItemIndex) {
                                Icon(item.selectedIcon, null)
                            } else {
                                Icon(item.unselectedIcon, null)
                            }
                        })
                }
            }
        }
    ) {
        it.calculateBottomPadding()

        content()

    }





}

@Composable
fun Lista(sort: String, navController: NavController){
    val channel = ManagedChannelBuilder.forAddress(adressIP, 9009)
        .usePlaintext()
        .build()


        val stub = ArticlerServiceGrpc.newBlockingStub(channel)

    println(sort)

        val input = Message.newBuilder()
            .setBody(sort) // token
            .build()
        val reply = stub.getArticles(input)

        println(reply)
        channel.shutdown()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(reply.artsList.size) {index ->
            val article = reply.artsList[index]
            ArticleCard(
                editable = false,
                title = article.title,
                articleID = article.articleId,
                content = article.content,
                summary = article.shortContent,
                author = article.authorId,
                navController = navController,
                time = article.createTime)


        }
    }
}

@Composable
fun ArticleCard(
    title: String,
    summary: String,
    content: String,
    author: String,
    articleID: String,
    time: String,
    editable: Boolean,
    navController: NavController
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White,
        modifier = Modifier

            .padding(16.dp)
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
            .clickable {
                navController.navigate("contentPage/$title/$content")
        },



    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                        .padding(end = if (editable) 8.dp else 0.dp)
                        .align(Alignment.CenterVertically)
                )
                if (editable) {
                    IconButton(
                        onClick = { navController.navigate("updateArticlePage/$title/$summary/$content/$articleID") },
                        modifier = Modifier.size(18.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(
                        onClick = { deleteArticle(articleID) },
                        modifier = Modifier.size(18.dp)

                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
            Text(
                text = summary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row (
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = author,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,

                )
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall

                )
            }

        }
    }
}

fun deleteArticle(articleID: String){
    val channel = ManagedChannelBuilder.forAddress(adressIP, 9009)
        .usePlaintext()
        .build()

    val stub = ArticlerServiceGrpc.newBlockingStub(channel)

    val input = DelateArticleForm.newBuilder()
        .setToken(token)
        .setIdArticle(articleID)
        .build()
    val reply = stub.deleteArticle(input)

    println(reply)

    channel.shutdown()
}


