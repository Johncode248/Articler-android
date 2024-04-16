package com.lions.aplication.android.articler2

import android.graphics.Paint.Style
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.grpc.ManagedChannelBuilder
import protos.ArticleForm
import protos.ArticlerServiceGrpc
import protos.LoginForm

@Composable
fun Workbench(navController: NavController){

    if (token == ""){
        navController.navigate("login")
    }

    NavigationBottom(navController = navController, position = 0, content = { WorkbenchContent() })
}


@Composable
fun WorkbenchContent(){



    var title by remember { mutableStateOf(TextFieldValue()) }
    var contentArticle by remember { mutableStateOf(TextFieldValue()) }
    var shortContent by remember { mutableStateOf(TextFieldValue()) }


    Column(
        modifier = Modifier
            //.fillMaxSize()
            .padding(16.dp)
            ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Article")


        OutlinedTextField(
            value = title,
            onValueChange = {
                if (it.text.length <= 45) {
                    title = it
                }},
            label = { Text(text = "Title")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 16.dp)

        )

        OutlinedTextField(
            value = shortContent,
            onValueChange = {
                if (it.text.length <= 45) {
                    shortContent = it
                }},
            label = { Text(text = "Summary")},
            minLines = 2,
            maxLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)

        )


        OutlinedTextField(
            value = contentArticle,
            onValueChange = {
                if (it.text.length <= 300) {
                    contentArticle = it
                }},
            label = { Text(text = "Content")},
            minLines = 6,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)

        )
        
        OutlinedButton(
            onClick = { addArticle(title = title.text, fullContent = contentArticle.text, shortContent = shortContent.text  ) }) {
            Text(text = "Create")
        }
    }
}

fun addArticle(title: String, fullContent: String, shortContent: String ){

    val channel = ManagedChannelBuilder.forAddress(adressIP, 9009)
        .usePlaintext()
        .build()

    try {
        val stub = ArticlerServiceGrpc.newBlockingStub(channel)
        println("token $token")


        val input = ArticleForm.newBuilder()
            .setTitle(title)
            .setContent(fullContent)
            .setShortContent(shortContent)
            .setToken(token)
                .build()
        val reply = stub.createArticle(input)

        val x = reply.body
        println(x)
        channel.shutdown()


    }
    catch (e: Exception) {
        println(e)
    }
}