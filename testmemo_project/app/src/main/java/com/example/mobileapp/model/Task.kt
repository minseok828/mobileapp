package com.example.mobileapp.model

data class Task(
    val id: Long,
    val title: String,
    val subject: String,
    val dueAt: Long,
    val priority: Priority,
    val type: TaskType,
    val memo: String = "",
    val isDone: Boolean = false
)

enum class Priority { HIGH, MEDIUM, LOW }
enum class TaskType { ASSIGNMENT, EXAM }