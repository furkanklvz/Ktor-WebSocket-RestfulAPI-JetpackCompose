package com.klavs.networkcommunications.uix.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.klavs.networkcommunications.data.resource.Resource
import com.klavs.networkcommunications.uix.viewmodel.WebSocketViewModel

enum class MessageType {
    SUCCESS, ERROR, INFO
}

@Composable
fun WebSocketTrailPage(viewModel: WebSocketViewModel) {
    val initSessionResource by viewModel.initSessionResourceFlow.collectAsStateWithLifecycle()
    val messagesResource by viewModel.messagesResourceFlow.collectAsStateWithLifecycle()
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearMessages()
        }
    }
    WebSocketContent(
        messagesResource = messagesResource,
        initSessionResource = initSessionResource,
        sendMessage = { viewModel.sendMessage(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun WebSocketContent(
    messagesResource: Resource<String>,
    initSessionResource: Resource<Unit>,
    sendMessage: (String) -> Unit = {}
) {
    val messages = remember { mutableStateListOf<Pair<String, MessageType>>() }
    LaunchedEffect(messagesResource) {
        when (messagesResource) {
            is Resource.Error -> {
                messages.add(messagesResource.message to MessageType.ERROR)
            }

            is Resource.Success -> messages.add(messagesResource.data to MessageType.SUCCESS)
            else -> {}
        }
    }
    LaunchedEffect(initSessionResource) {
        when (initSessionResource) {
            is Resource.Error -> {
                messages.add(initSessionResource.message to MessageType.ERROR)
            }

            Resource.Idle -> {}
            Resource.Loading -> {
                messages.add("Loading..." to MessageType.INFO)
            }

            is Resource.Success -> {
                messages.add("Connected to socket" to MessageType.INFO)
            }
        }
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("WebSocket Trial")
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(top = innerPadding.calculateTopPadding())) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.95f)
                ) {
                    Text("Responses:", style = MaterialTheme.typography.titleSmall)
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        messages.forEach { message ->
                            when (message.second) {
                                MessageType.SUCCESS -> {
                                    Card(modifier = Modifier.padding(10.dp)) {
                                        Text(message.first, modifier = Modifier.padding(10.dp))
                                    }
                                }

                                MessageType.ERROR -> {
                                    Card(
                                        modifier = Modifier.padding(10.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer,
                                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    ) {
                                        Text(message.first, modifier = Modifier.padding(10.dp))
                                    }
                                }

                                MessageType.INFO -> {
                                    Text(
                                        message.first, modifier = Modifier.padding(10.dp),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(vertical = 10.dp)
                ) {
                    val sampleMessages = listOf(
                        "Hello World", "Ktor", "Test Message", "Android with Ktor",
                        "Jetpack Compose", "WebSockets"
                    )
                    Text("Messages:", style = MaterialTheme.typography.titleSmall)
                    FlowRow(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        sampleMessages.forEach { sampleMessage ->
                            FilledTonalButton (
                                onClick = { sendMessage(sampleMessage) },
                            ) { Text(sampleMessage) }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun WebSocketContentPreview() {
    WebSocketContent(
        messagesResource = Resource.Success(data = "test"),
        initSessionResource = Resource.Success(data = Unit)
    )
}