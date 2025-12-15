package com.example.mobileapp.data

import androidx.lifecycle.ViewModel
import com.example.mobileapp.model.Priority
import com.example.mobileapp.model.Task
import com.example.mobileapp.model.TaskType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

data class UiFilter(
    val hideDone: Boolean = false,
    val type: TaskType? = null,
    val priority: Priority? = null
)

class TaskViewModel : ViewModel() {

    private val _tasks = MutableStateFlow(seedDummy())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _filter = MutableStateFlow(UiFilter())
    val filter: StateFlow<UiFilter> = _filter

    fun setHideDone(value: Boolean) = _filter.update { it.copy(hideDone = value) }
    fun setType(value: TaskType?) = _filter.update { it.copy(type = value) }
    fun setPriority(value: Priority?) = _filter.update { it.copy(priority = value) }

    fun filteredTasks(): List<Task> {
        val f = _filter.value
        return _tasks.value
            .asSequence()
            .filter { if (f.hideDone) !it.isDone else true }
            .filter { if (f.type != null) it.type == f.type else true }
            .filter { if (f.priority != null) it.priority == f.priority else true }
            .sortedWith(compareBy<Task>({ it.isDone }, { it.dueAt })) // 미완료 먼저, 마감 빠른 순
            .toList()
    }

    fun getById(id: Long): Task? = _tasks.value.firstOrNull { it.id == id }

    fun add(task: Task) {
        _tasks.update { it + task }
    }

    fun update(task: Task) {
        _tasks.update { list -> list.map { if (it.id == task.id) task else it } }
    }

    fun delete(id: Long) {
        _tasks.update { it.filterNot { t -> t.id == id } }
    }

    fun toggleDone(id: Long) {
        _tasks.update { list ->
            list.map { if (it.id == id) it.copy(isDone = !it.isDone) else it }
        }
    }

    companion object {
        private fun seedDummy(): List<Task> {
            fun daysFromNow(days: Int): Long {
                val cal = Calendar.getInstance()
                cal.add(Calendar.DAY_OF_YEAR, days)
                return cal.timeInMillis
            }

            return listOf(
                Task(
                    id = 1L,
                    title = "과제 기간 메모",
                    subject = "모바일앱실습",
                    dueAt = daysFromNow(3),
                    priority = Priority.HIGH,
                    type = TaskType.ASSIGNMENT,
                    memo = "Compose 화면 3개 구현(목록/상세/추가)"
                ),
                Task(
                    id = 2L,
                    title = "시험 기간 메모",
                    subject = "모바일앱실습",
                    dueAt = daysFromNow(10),
                    priority = Priority.MEDIUM,
                    type = TaskType.EXAM,
                    memo = "네비게이션, ViewModel, 상태관리 정리"
                )
            )
        }
    }
}
