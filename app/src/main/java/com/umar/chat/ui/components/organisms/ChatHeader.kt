package com.umar.chat.ui.components.organisms

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.umar.chat.ui.components.atoms.IconType
import com.umar.chat.ui.components.atoms.MsIconRounded

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatHeader() {
   TopAppBar(
       title = {
           Text(
               text = "Chat",
           )
       },
       actions = {
           IconButton(onClick = { }) {
               MsIconRounded(icon = IconType.SEARCH)
           }
           IconButton(onClick = { }) {
               MsIconRounded(icon = IconType.MORE_VERT)
           }
       }
   )
}