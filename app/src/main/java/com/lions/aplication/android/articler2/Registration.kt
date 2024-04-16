package com.lions.aplication.android.articler2

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.lions.aplication.android.articler2.ui.theme.Articler2Theme
import androidx.navigation.NavController
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.CoroutineScope

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import protos.ArticlerServiceGrpc
import protos.LoginForm
import protos.Message


@Composable
fun RegistrationContent(navController: NavController){
    var username by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }

    val context = LocalContext.current
    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Register page",

            modifier = Modifier
                .padding(
                    bottom = 40.dp
                )
        )
        OutlinedTextField(
            value = username,
            onValueChange = {
                if (it.text.length <= 20) {
                username = it
                }

            },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { if (it.text.length <= 20) {
                password = it
            } },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Button(
            onClick = { onRegistrationClicked(username.text, password.text, navController, context = context) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
        Button(onClick = {navController.navigate("login")}) {
            Text(text = "Login now")
        }
    }
}


/*
fun onRegistrationClicked(username: String, password: String, navController: NavController, context: android.content.Context) {

    showToast(context = context, message = "haloo?")

    try {
        val channel = ManagedChannelBuilder.forAddress(adressIP, 9009)
            .usePlaintext()
            .build()

        val stub = ArticlerServiceGrpc.newBlockingStub(channel)

        val input = LoginForm.newBuilder().setUsername(username).setPassword(password).build()
        val reply = stub.register(input)

        token = reply.body
        channel.shutdown()

        if (token != "") {
            navController.navigate("home")
        }
    }
    catch (e: Exception) {
        println(e)
        showToast(context, "error")
    }
}

*/





@OptIn(DelicateCoroutinesApi::class)
fun onRegistrationClicked(username: String, password: String, navController: NavController, context: android.content.Context) {

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

            val reply = withTimeout(3_000){
                stub.register(input)
            }

            if (reply != null) {
                token = reply.body
                channel.shutdown()

                if (token != "") {
                    GlobalScope.launch(Dispatchers.Main) {
                        navController.navigate("home")
                    }
                }else{
                    withContext(Dispatchers.Main) {
                        navController.navigate("home")

                    }
                }
            }else {
                withContext(Dispatchers.Main) {
                    showToast(context, "Server timeout")
                }
            }
        }
        catch (e: Exception) {
            println("error")
            println(e)

            withContext(Dispatchers.Main) {
                navController.navigate("login")
                println("error2")
                //showToast(context, "error")
            }
        }
    }
}






fun showToast(context: android.content.Context, message: String) {

    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
    toast.show()
}



