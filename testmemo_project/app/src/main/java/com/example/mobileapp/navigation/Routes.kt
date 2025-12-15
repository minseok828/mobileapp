package com.example.mobileapp.navigation

object Routes {
    const val LIST = "list"
    const val DETAIL = "detail/{id}"
    const val EDIT = "edit?taskId={taskId}"

    fun detail(id: Long) = "detail/$id"
    fun edit(taskId: Long? = null) = "edit?taskId=${taskId ?: -1}"
}
