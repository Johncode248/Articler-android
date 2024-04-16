package com.lions.aplication.android.articler2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lions.aplication.android.articler2.ui.theme.Articler2Theme
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import protos.ArticlerServiceGrpc
import protos.LoginForm
import java.util.concurrent.TimeUnit


@Composable
fun LoginContent(navController: NavController) {

        var username by remember { mutableStateOf(TextFieldValue()) }
        var password by remember { mutableStateOf(TextFieldValue()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login page",

                modifier = Modifier
                    .padding(
                    bottom = 40.dp
                )
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Button(
                onClick = { onLoginClicked(username.text, password.text, navController) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Button(onClick = {navController.navigate("register")}) {
                Text(text = "Register now")
            }
        }

}

fun onLoginClicked(username: String, password: String, navController: NavController){

    if (username == "" || password == "") {
        return
    }


    CoroutineScope(Dispatchers.IO).launch {
            try {
                val channel = ManagedChannelBuilder.forAddress(adressIP, 9009)
                    .usePlaintext()
                    .build()
                val stub = ArticlerServiceGrpc.newBlockingStub(channel)
                val input = LoginForm.newBuilder().setUsername(username).setPassword(password).build()
                val reply = stub.login(input)

                channel.shutdown()

                if (reply != null){
                    token = reply.body
                }else{
                    navController.navigate("login")
                }

                if (token.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        navController.navigate("home")
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        navController.navigate("login")
                    }
                }
            }
            catch (e: TimeoutCancellationException) {
                navController.navigate("login")
            }catch (e: Exception) {
                navController.navigate("login")
                println(e)
            }

    }




}