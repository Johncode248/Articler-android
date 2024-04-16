package com.lions.aplication.android.articler2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import io.grpc.ManagedChannelBuilder
import protos.ArticlerServiceGrpc
import protos.Message

@Composable
fun ProfilePage(navController: NavController){

    if (token == ""){
        navController.navigate("login")
    }

    NavigationBottom(navController = navController, position = 2, content = { ListProfile(navController = navController)})

}


@Composable
fun ListProfile(navController: NavController){
    val channel = ManagedChannelBuilder.forAddress(adressIP, 9009)
        .usePlaintext()
        .build()


    val stub = ArticlerServiceGrpc.newBlockingStub(channel)

    val input = Message.newBuilder()
        .setBody(token) // token
        .build()

    val replySecond = stub.getAuthorByID(input)

    val reply = stub.getArticles(input)

    channel.shutdown()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = "Account Circle",
                modifier = Modifier.size(68.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = replySecond.username,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedButton(
                onClick = {
                    navController.navigate("updateProfile/${replySecond.username}/${replySecond.password}")
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(0.dp, 0.dp)
            ) {
                Text(text = "Edit",
                    style = MaterialTheme.typography.bodySmall)
            }
            OutlinedButton(
                onClick = {
                    deleteAccount(navController)
                    navController.navigate("login")
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    //.size(width = 110.dp, height = 30.dp)
                    .padding(0.dp, 0.dp)
            ) {
                Text(text = "Delete",
                    style = MaterialTheme.typography.bodySmall)
            }
        }
        LazyColumn(
            modifier = Modifier
                //.fillMaxSize()
                .padding(bottom =  25.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            items(reply.artsList.size) {index ->
                val article = reply.artsList[index]
                ArticleCard(
                    editable = true,
                    title = article.title,
                    content = article.content,
                    summary = article.shortContent,
                    author = article.authorId,
                    articleID = article.articleId,
                    navController = navController,
                    time = article.createTime)

            }
    }
    }
}

fun deleteAccount(navController: NavController){
    val channel = ManagedChannelBuilder.forAddress(adressIP, 9009)
        .usePlaintext()
        .build()

    val stub = ArticlerServiceGrpc.newBlockingStub(channel)

    val input = Message.newBuilder()
        .setBody(token)
        .build()
    val reply = stub.deleteUser(input)

    println(reply)

    channel.shutdown()

    navController.navigate("login")
}







