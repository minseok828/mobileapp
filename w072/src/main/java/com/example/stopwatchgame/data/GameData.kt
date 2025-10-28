package com.example.stopwatchgame.data

data class GameData(
    val currentTimeMs: Long = 0L,
    val isRunning: Boolean = false,
    val currentPoint: Int = 0 // 현재 게임 세션에서 획득한 포인트
)
