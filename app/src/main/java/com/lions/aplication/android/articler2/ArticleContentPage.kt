package com.lions.aplication.android.articler2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ArticleContentPage(myTitle: String?, myContent: String?){

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = myTitle.toString(),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = myContent.toString(),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(top = 30.dp, bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
                .width(300.dp)
        )
    }
}