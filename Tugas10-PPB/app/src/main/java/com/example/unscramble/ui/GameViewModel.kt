package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())

    private val hintPenaltyValue  = 5
    private val maxTimePerWordValue  = 30

    private val _timeLeft = MutableStateFlow(maxTimePerWordValue )
    private var timerJob: Job? = null

    val uiState: StateFlow<GameUiState> = combine(_uiState, _timeLeft) { uiState, timeLeft ->
        uiState.copy(timeLeft = timeLeft)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        GameUiState()
    )


    var userGuess by mutableStateOf("")
        private set

    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()


    init {
        resetGame()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            _timeLeft.value = maxTimePerWordValue
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.value--
            }
            if (!_uiState.value.isGameOver) {
                skipWord(autoSkipped = true)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
        userGuess = ""
        startTimer()
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGameOver = true,
                    score = updatedScore,
                    isGuessedWordWrong = false,
                    isHintUsedForCurrentWord = false,
                    hintLetter = null
                )
            }
            timerJob?.cancel()
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    currentWordCount = currentState.currentWordCount.inc(),
                    isHintUsedForCurrentWord = false,
                    hintLetter = null
                )
            }
            startTimer()
        }
        userGuess = ""
    }

    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.score + SCORE_INCREASE
            updateGameState(updatedScore)
        } else {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
    }

    fun skipWord(autoSkipped: Boolean = false) {
        val currentScore = _uiState.value.score
        val updatedScore = if (autoSkipped) currentScore - (SCORE_INCREASE / 2) else currentScore
        updateGameState(updatedScore)
    }

    fun useHint() {
        if (!_uiState.value.isHintUsedForCurrentWord) {
            _uiState.update { currentState ->
                currentState.copy(
                    score = currentState.score - hintPenaltyValue,
                    isHintUsedForCurrentWord = true,
                    hintLetter = currentWord.first()
                )
            }
        }
    }

    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()
        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        tempWord.shuffle()
        while (String(tempWord) == word) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }
}