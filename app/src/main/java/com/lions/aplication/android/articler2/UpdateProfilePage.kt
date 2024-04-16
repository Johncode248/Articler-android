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
import protos.UpdateUserForm

@Composable
fun UpdateProfilePage(navController: NavController, myName: String?, myPassword: String?){

    if (token == ""){
        navController.navigate("login")
    }


    var username by remember { mutableStateOf(TextFieldValue(myName ?: "")) }
    var password by remember { mutableStateOf(TextFieldValue()) }




    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Update Profile")


        OutlinedTextField(
            value = username,
            onValueChange = {
                if (it.text.length <= 45) {
                    username = it
                }},
            label = { Text(text = "Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, top = 16.dp)

        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                if (it.text.length <= 45) {
                    password = it
                }},
            label = { Text(text = "Password") },
            minLines = 2,
            maxLines = 3,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)

        )

        OutlinedButton(

            onClick = { updateProfile(username.text, password.text) }) {
            Text(text = "Save")
        }
    }
}

fun updateProfile(name: String?, password: String?){
    val channel = ManagedChannelBuilder.forAddress(adressIP, 9009)
        .usePlaintext()
        .build()


    try {
        val stub = ArticlerServiceGrpc.newBlockingStub(channel)

        val input = UpdateUserForm.newBuilder()
            .setToken(token)
            .setUsername(name)
            .setPassword(password)
            .build()
        val reply = stub.updateUser(input)

        println(reply.body)


        channel.shutdown()
    }
    catch (e: Exception) {
        println(e)
    }
}





