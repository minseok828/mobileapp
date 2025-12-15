package com.example.mobileapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mobileapp.data.TaskViewModel
import com.example.mobileapp.model.Task
import com.example.mobileapp.model.TaskType
import com.example.mobileapp.ui.components.SummaryCard
import com.example.mobileapp.ui.components.TaskCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    vm: TaskViewModel,
    onOpenDetail: (Long) -> Unit,
    onOpenAdd: () -> Unit
) {
    val tasks by vm.tasks.collectAsState()
    val filter by vm.filter.collectAsState()

    val filtered = remember(tasks, filter.type) {
        val base = if (filter.type == null) tasks else tasks.filter { it.type == filter.type }
        base.sortedWith(compareBy<Task>({ it.isDone }, { it.dueAt }))
    }

    val (remainingAll, completionRate) = remember(tasks) {
        val remain = tasks.count { !it.isDone }
        val done = tasks.count { it.isDone }
        val rate = if (tasks.isEmpty()) 0 else (done * 100 / tasks.size)
        remain to rate
    }

    val selectedBlue = Color(0xFFD7ECFF)
    val screenBg = Color(0xFFF6F8FB)

    Scaffold(
        containerColor = screenBg,
        topBar = { TopAppBar(title = { Text("과제·시험 일정 메모장") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onOpenAdd) {
                Icon(Icons.Default.Add, contentDescription = "추가")
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            SummaryCard(
                remainingAll = remainingAll,
                completionRate = completionRate,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = (filter.type == null),
                    onClick = { vm.setType(null) },
                    label = { Text("전체") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = selectedBlue,
                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                FilterChip(
                    selected = (filter.type == TaskType.ASSIGNMENT),
                    onClick = { vm.setType(TaskType.ASSIGNMENT) },
                    label = { Text("과제") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = selectedBlue,
                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
                FilterChip(
                    selected = (filter.type == TaskType.EXAM),
                    onClick = { vm.setType(TaskType.EXAM) },
                    label = { Text("시험") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = selectedBlue,
                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered, key = { it.id }) { t ->
                    TaskCard(
                        task = t,
                        onClick = { onOpenDetail(t.id) },
                        onToggleDone = { vm.toggleDone(t.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
