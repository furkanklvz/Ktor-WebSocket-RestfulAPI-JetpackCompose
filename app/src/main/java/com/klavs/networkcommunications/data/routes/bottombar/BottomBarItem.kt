package com.klavs.networkcommunications.data.routes.bottombar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Chat
import androidx.compose.material.icons.rounded.Http
import androidx.compose.ui.graphics.vector.ImageVector
import com.klavs.networkcommunications.data.routes.RestfulAPITrailPage
import com.klavs.networkcommunications.data.routes.WebSocketTrailPage

sealed class BottomBarItem(val route: Any, val title: String, val imageVector: ImageVector) {
    data object RestfulAPIPage: BottomBarItem(route = RestfulAPITrailPage, title = "Restful API", imageVector = Icons.Rounded.Http)
    data object WebSocketPage: BottomBarItem(route = WebSocketTrailPage, title = "WebSocket", imageVector = Icons.AutoMirrored.Rounded.Chat)
    companion object{
        val items by lazy { listOf(RestfulAPIPage, WebSocketPage) }
    }
}