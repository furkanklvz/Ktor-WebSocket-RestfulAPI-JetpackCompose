package com.klavs.networkcommunications.uix.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.klavs.networkcommunications.data.entity.ResponseObject
import com.klavs.networkcommunications.data.resource.Resource
import com.klavs.networkcommunications.uix.viewmodel.RestfulViewModel
import kotlinx.coroutines.launch

@Composable
fun RestfulAPITrialPage(viewModel: RestfulViewModel) {
    val getResource by viewModel.getResource.collectAsStateWithLifecycle()
    val postResource by viewModel.postResource.collectAsStateWithLifecycle()
    val deleteResource by viewModel.deleteResource.collectAsStateWithLifecycle()
    val putResource by viewModel.putResource.collectAsStateWithLifecycle()
    val patchResource by viewModel.patchResource.collectAsStateWithLifecycle()
    val filterResource by viewModel.filterResource.collectAsStateWithLifecycle()
    RestfulAPIContent(
        get = viewModel::get,
        post = { data -> viewModel.post(data) },
        delete = viewModel::delete,
        getResource = getResource,
        postResource = postResource,
        deleteResource = deleteResource,
        put = { viewModel.put(it) },
        putResource = putResource,
        patch = { viewModel.patch(it) },
        patchResource = patchResource,
        filter = {viewModel.filter(it)},
        filterResource = filterResource
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalLayoutApi::class
)
@Composable
private fun RestfulAPIContent(
    get: () -> Unit = {},
    delete: () -> Unit = {},
    post: (ResponseObject) -> Unit = {},
    put: (ResponseObject) -> Unit = {},
    patch: (String) -> Unit = {},
    filter: (Int) -> Unit = {},
    getResource: Resource<ResponseObject>,
    postResource: Resource<ResponseObject>,
    deleteResource: Resource<String>,
    putResource: Resource<ResponseObject>,
    patchResource: Resource<ResponseObject>,
    filterResource: Resource<String>
) {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var userId by remember { mutableIntStateOf(0) }
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Restful API Trial")
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(top = innerPadding.calculateTopPadding())) {
            Column(
                Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    TextField(
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(0.8f),
                        value = userId.toString(),
                        onValueChange = {
                            runCatching { userId = it.toInt() }
                        },
                        label = { Text("User Id") }
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Title") }
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        value = body,
                        onValueChange = { body = it },
                        label = { Text("Body") }
                    )
                    FlowRow (
                        modifier = Modifier.fillMaxWidth(0.95f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { get() },
                        ) {
                            Text("GET")
                        }
                        OutlinedButton(
                            onClick = {
                                post(
                                    ResponseObject(
                                        body = body,
                                        title = title,
                                        userId = userId
                                    )
                                )
                            },
                        ) {
                            Text("POST")
                        }
                        OutlinedButton(
                            onClick = {
                                put(
                                    ResponseObject(
                                        id = 1,
                                        title = title,
                                        body = body,
                                        userId = userId
                                    )
                                )
                            },
                        ) {
                            Text("PUT")
                        }
                        OutlinedButton(
                            onClick = { delete() },
                        ) {
                            Text("DELETE")
                        }
                        OutlinedButton(
                            onClick = {
                                if (title.isNotBlank()) {
                                    patch(title)
                                } else {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            message = "Title cannot be empty!"
                                        )
                                    }
                                }
                            },
                        ) {
                            Text("PATCH")
                        }
                        OutlinedButton(
                            onClick = {
                                filter(userId)
                            },
                        ) {
                            Text("FILTER")
                        }
                    }
                }

                Column {
                    Text(
                        "Response:", style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .fillMaxHeight(0.6f)
                    ) {
                        var isLoading by remember { mutableStateOf(false) }
                        var text by remember { mutableStateOf(buildAnnotatedString {}) }
                        var errorMessage by remember { mutableStateOf<String?>(null) }
                        LaunchedEffect(getResource) {
                            when (getResource) {
                                is Resource.Error -> {
                                    isLoading = false
                                    errorMessage = getResource.message
                                }

                                Resource.Idle -> {}
                                Resource.Loading -> {
                                    isLoading = true
                                }

                                is Resource.Success -> {
                                    isLoading = false
                                    text = getDataFeaturesString(getResource.data)
                                }
                            }
                        }
                        LaunchedEffect(postResource) {
                            when (postResource) {
                                is Resource.Error -> {
                                    isLoading = false
                                    errorMessage = postResource.message
                                }

                                Resource.Idle -> {}
                                Resource.Loading -> {
                                    isLoading = true
                                }

                                is Resource.Success -> {
                                    isLoading = false
                                    text = getDataFeaturesString(postResource.data)
                                }
                            }
                        }
                        LaunchedEffect(deleteResource) {
                            when (deleteResource) {
                                is Resource.Error -> {
                                    isLoading = false
                                    errorMessage = deleteResource.message
                                }

                                Resource.Idle -> {}
                                Resource.Loading -> {
                                    isLoading = true
                                }

                                is Resource.Success -> {
                                    isLoading = false
                                    text = buildAnnotatedString { append(deleteResource.data) }
                                }
                            }
                        }
                        LaunchedEffect(putResource) {
                            when (putResource) {
                                is Resource.Error -> {
                                    isLoading = false
                                    errorMessage = putResource.message
                                }

                                Resource.Idle -> {}
                                Resource.Loading -> {
                                    isLoading = true
                                }

                                is Resource.Success -> {
                                    isLoading = false
                                    text = getDataFeaturesString(putResource.data)
                                }
                            }
                        }
                        LaunchedEffect(patchResource) {
                            when (patchResource) {
                                is Resource.Error -> {
                                    isLoading = false
                                    errorMessage = patchResource.message
                                }

                                Resource.Idle -> {}
                                Resource.Loading -> {
                                    isLoading = true
                                }

                                is Resource.Success -> {
                                    isLoading = false
                                    text = getDataFeaturesString(patchResource.data)
                                }
                            }
                        }
                        LaunchedEffect(filterResource) {
                            when (filterResource) {
                                is Resource.Error -> {
                                    isLoading = false
                                    errorMessage = filterResource.message
                                }

                                Resource.Idle -> {}
                                Resource.Loading -> {
                                    isLoading = true
                                }

                                is Resource.Success -> {
                                    isLoading = false
                                    text = buildAnnotatedString { append(filterResource.data) }
                                }
                            }
                        }
                        if (isLoading) {
                            CircularWavyProgressIndicator(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        } else if (errorMessage != null) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                                    .padding(top = 10.dp)
                                    .align(Alignment.CenterHorizontally),
                                verticalArrangement = Arrangement.spacedBy(15.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.ErrorOutline,
                                    contentDescription = "error",
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Text(errorMessage!!)
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                                    .padding(10.dp)
                            ) {
                                Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.selectable(selected = true, onClick = {}))
                            }
                        }
                    }
                }

            }
        }
    }
}

private fun getDataFeaturesString(data: ResponseObject): AnnotatedString {
    fun getAnnotatedString(title: String, body: String): AnnotatedString {
        return buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(title)
            }
            append(body)
        }
    }
    return buildAnnotatedString {
        append(getAnnotatedString("Id: ", data.id.toString()))
        append("\n")
        append(getAnnotatedString("User Id: ", data.userId.toString()))
        append("\n")
        append(getAnnotatedString("Title: ", data.title))
        append("\n")
        append(getAnnotatedString("Body: ", data.body))
    }
}

@Preview
@Composable
private fun RestfulAPIContentPreview() {
    val sampleObject = ResponseObject(
        body = "body",
        id = 21,
        title = "title",
        userId = 1
    )
    RestfulAPIContent(
        getResource = Resource.Success(data = sampleObject),
        postResource = Resource.Idle,
        deleteResource = Resource.Idle,
        putResource = Resource.Idle,
        patchResource = Resource.Idle,
        filterResource = Resource.Idle
    )
}