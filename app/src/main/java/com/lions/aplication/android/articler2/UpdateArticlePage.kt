package com.lions.aplication.android.articler2

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.grpc.ManagedChannelBuilder
import protos.ArticleForm
import protos.ArticlerServiceGrpc
import protos.Message

@Composable
fun UpdateArticlePage(navController: NavController, myTitle: String?, mySummary: String?, myContent: String?, myArticleId: String? ){


    var title by remember { mutableStateOf(TextFieldValue(myTitle ?: "")) }
    var contentArticle by remember { mutableStateOf(TextFieldValue(myContent ?: "")) }
    var shortContent by remember { mutableStateOf(TextFieldValue(mySummary ?: "")) }

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Update Article")

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
            onClick = {
                updateArticle(
                    articleId = myArticleId.toString(),
                    title = title.text,
                    summary = shortContent.text,
                    content = contentArticle.text
                )
            }
        ) {
            Text(text = "Save")
        }


    }

}

fun updateArticle( articleId: String, title:String, summary:String, content:String){
    val channel = ManagedChannelBuilder.forAddress(adressIP, 9009)
        .usePlaintext()
        .build()

    val stub = ArticlerServiceGrpc.newBlockingStub(channel)

    val input = ArticleForm.newBuilder()
        .setArticleId(articleId)
        .setTitle(title)
        .setShortContent(summary)
        .setContent(content)
        .setToken(token)
        .build()


    val reply = stub.updateArticle(input)


    channel.shutdown()
}