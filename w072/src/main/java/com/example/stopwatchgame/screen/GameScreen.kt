package com.example.stopwatchgame.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stopwatchgame.ui.theme.Donghyun1234Theme
import java.util.concurrent.TimeUnit

@Composable
fun GameScreen(
    // ViewModel을 생성하고, Composable의 Lifecycle에 연결합니다.
    viewModel: GameViewModel = viewModel()
) {
    // 1. 상태 관찰 (State Collection):
    // ViewModel의 단일 StateFlow<GameUiState>를 관찰하여 상태가 변경될 때마다 UI를 재구성합니다.
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 사용자 및 점수 정보
        Text(
            text = "플레이어: ${uiState.userData.userId} (총점: ${uiState.userData.totalScore}점)",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "현재 레벨: ${uiState. userData.level}",
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 설정 정보
        Text(
            text = "목표 시간: ${formatTime(uiState.gameConfig.targetTimeMs)}",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "오차 범위: ±${formatTime(uiState.gameConfig.toleranceMs)}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 40.dp)
        )
