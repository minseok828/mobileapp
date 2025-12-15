package com.example.mobileapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mobileapp.model.Task
import com.example.mobileapp.model.TaskType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.layout.padding

@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit,
    onToggleDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dDay = calcDDay(task.dueAt)
    val due = formatDateOnly(task.dueAt)

    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        ListItem(
            headlineContent = { Text(task.title) },
            supportingContent = {
                Text("${task.subject} · ${typeLabel(task.type)} · ${due}까지")
            },
            trailingContent = {
                Row {
                    Surface(
                        shape = RoundedCornerShape(999.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = dDay,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier
                                .width(44.dp)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Checkbox(
                        checked = task.isDone,
                        onCheckedChange = { onToggleDone() }
                    )
                }
            }
        )
    }
}

private fun typeLabel(type: TaskType): String =
    when (type) {
        TaskType.ASSIGNMENT -> "과제"
        TaskType.EXAM -> "시험"
    }

private fun formatDateOnly(millis: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    return sdf.format(Date(millis))
}

private fun calcDDay(dueAt: Long): String {
    val now = System.currentTimeMillis()
    val diff = dueAt - now
    val days = TimeUnit.MILLISECONDS.toDays(diff)
    return if (days >= 0) "D-$days" else "D+${-days}"
}
