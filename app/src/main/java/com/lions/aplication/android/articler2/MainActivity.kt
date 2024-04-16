package com.lions.aplication.android.articler2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lions.aplication.android.articler2.ui.theme.Articler2Theme
import io.grpc.ManagedChannelBuilder
import io.grpc.Status
import io.grpc.StatusRuntimeException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import protos.ArticlerServiceGrpc
import protos.ArticlerServiceGrpcKt
import protos.Message


var token: String = ""
val adressIP: String = "192.168.100.177"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Articler2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                    //MainContent()

                }
            }
        }
    }
}



@OptIn(DelicateCoroutinesApi::class)
@Composable
fun MainContent() {
    var response by remember { mutableStateOf("") }

    Column {
        Button(onClick = {
            GlobalScope.launch(Dispatchers.IO) {
                val channel = ManagedChannelBuilder.forAddress(adressIP, 9008)
                    .usePlaintext()
                    .build()

                try {
                    val stub = ArticlerServiceGrpc.newBlockingStub(channel)

                    val message = Message.newBuilder().setBody("Hello from the client").build()
                    val reply = stub.sayHello(message)

                    response = reply.body
                    channel.shutdown()
                }
                
                catch (e: Exception) {
                    response = "Error: ${e.message}"
                }

            }
        }) {
            Text("Send Message")
        }

        Text(text = "Response from Server: $response")
    }
}
