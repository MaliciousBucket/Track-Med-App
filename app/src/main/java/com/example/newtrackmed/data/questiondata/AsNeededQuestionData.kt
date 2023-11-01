package com.example.newtrackmed.data.questiondata

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import com.example.newtrackmed.R

class AsNeededQuestionData{
    private val _asNeededAnswer = mutableStateOf<Boolean>(false)
   val asNeededAnswer: Boolean
       get() = _asNeededAnswer.value
   val selectedIndex: State<Int> = derivedStateOf { if (_asNeededAnswer.value) 0 else 1 }

    val options = listOf<AsNeededOption>(
        AsNeededOption(R.string.as_needed, true),
        AsNeededOption(R.string.scheduled, false)
    )

    fun onAsNeededChanged(newValue: Boolean){
        _asNeededAnswer.value = newValue
    }

}

data class AsNeededOption(
    val labelResourceId: Int,
    val value: Boolean,
)