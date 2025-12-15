package com.example.mobileapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapp.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    task: Task,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleDone: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("상세") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "수정")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Filled.Delete, contentDescription = "삭제")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            InfoCard("제목", task.title)
            InfoCard("과목", task.subject)
            InfoCard("유형", if (task.type.name == "ASSIGNMENT") "과제" else "시험")
            InfoCard("마감", formatDate(task.dueAt))
            InfoCard("메모", if (task.memo.isBlank()) "없음" else task.memo)

            Button(
                onClick = onToggleDone,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (task.isDone) "미완료로 되돌리기" else "완료 처리")
            }
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        ListItem(
            headlineContent = { Text(label) },
            supportingContent = { Text(value) }
        )
    }
}

private fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd (E) HH:mm", Locale.KOREA)
    return sdf.format(Date(millis))
}
