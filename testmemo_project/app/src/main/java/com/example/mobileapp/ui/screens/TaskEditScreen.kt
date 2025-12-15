package com.example.mobileapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapp.model.Priority
import com.example.mobileapp.model.Task
import com.example.mobileapp.model.TaskType
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    initial: Task?,
    onCancel: () -> Unit,
    onSave: (Task) -> Unit
) {
    var title by remember { mutableStateOf(initial?.title ?: "") }
    var subject by remember { mutableStateOf(initial?.subject ?: "") }
    var memo by remember { mutableStateOf(initial?.memo ?: "") }
    var type by remember { mutableStateOf(initial?.type ?: TaskType.ASSIGNMENT) }

    val defaultDueMillis = remember {
        (initial?.dueAt ?: Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 3) }.timeInMillis)
    }
    var dueText by remember { mutableStateOf(formatDateOnly(defaultDueMillis)) }

    val parsedDueMillis = remember(dueText) { parseDateOnlyToMillis(dueText) }
    val dueValid = parsedDueMillis != null

    val canSave = title.isNotBlank() && dueValid

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (initial == null) "추가" else "수정") }) }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("제목(필수)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = subject,
                onValueChange = { subject = it },
                label = { Text("과목") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = type == TaskType.ASSIGNMENT,
                    onClick = { type = TaskType.ASSIGNMENT },
                    label = { Text("과제") }
                )
                FilterChip(
                    selected = type == TaskType.EXAM,
                    onClick = { type = TaskType.EXAM },
                    label = { Text("시험") }
                )
            }

            OutlinedTextField(
                value = dueText,
                onValueChange = { dueText = it.trim() },
                label = { Text("마감 날짜 (yyyy-MM-dd)") },
                placeholder = { Text("예: 2025-12-18") },
                isError = !dueValid,
                supportingText = {
                    if (!dueValid) Text("날짜 형식이 올바르지 않아요. 예: 2025-12-18")
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = memo,
                onValueChange = { memo = it },
                label = { Text("메모") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("취소")
                }

                Button(
                    onClick = {
                        val id = initial?.id ?: System.currentTimeMillis()
                        onSave(
                            Task(
                                id = id,
                                title = title,
                                subject = subject,
                                dueAt = parsedDueMillis ?: defaultDueMillis,
                                priority = Priority.MEDIUM,
                                type = type,
                                memo = memo,
                                isDone = initial?.isDone ?: false
                            )
                        )
                    },
                    enabled = canSave,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("저장")
                }
            }
        }
    }
}

private fun formatDateOnly(millis: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    return sdf.format(millis)
}

private fun parseDateOnlyToMillis(text: String): Long? {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA).apply { isLenient = false }
        val date = sdf.parse(text) ?: return null
        date.time
    } catch (e: Exception) {
        null
    }
}
