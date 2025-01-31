package com.example.caalculator_app

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    var state = mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> {
                enterNumber(action.number)
            }

            is CalculatorAction.Clear -> {
                state.value = CalculatorState()
            }

            is CalculatorAction.Delete -> {
                performDeletion()
            }

            is CalculatorAction.Decimal -> {
                enterDecimal()
            }

            is CalculatorAction.Operation -> {
                enterOperation(action.operation)
            }

            is CalculatorAction.Calculate -> {
                performCalculation()

            }
        }
    }

    private fun performDeletion() {
        when {
            state.value.number2.isNotBlank() -> {
                state.value = state.value.copy(number2 = state.value.number2.dropLast(1))
            }

            state.value.operation != null -> {
                state.value = state.value.copy(operation = null)
            }

            state.value.number1.isNotBlank() -> {
                state.value = state.value.copy(number1 = state.value.number1.dropLast(1))
            }

        }
    }

    private fun enterDecimal() {
        if (state.value.operation == null && state.value.number1.isNotBlank() && !state.value.number1.contains(
                "."
            )
        ) {
            state.value = state.value.copy(number1 = state.value.number1 + ".")
            return
        } else if (state.value.number2.isNotBlank() && !state.value.number2.contains(".")) {
            state.value = state.value.copy(number2 = state.value.number2 + ".")
            return
        }
    }

    private fun performCalculation() {
        if (state.value.number1.isNotBlank() && state.value.number2.isNotBlank() && state.value.operation != null) {
            val result = when (state.value.operation) {
                CalculatorOperation.Add -> state.value.number1.toDouble() + state.value.number2.toDouble()
                CalculatorOperation.Subtract -> state.value.number1.toDouble() - state.value.number2.toDouble()
                CalculatorOperation.Multiply -> state.value.number1.toDouble() * state.value.number2.toDouble()
                CalculatorOperation.Divide -> state.value.number1.toDouble() / state.value.number2.toDouble()
                null -> 0.0
            }
            state.value = CalculatorState(number1 = result.toString().take(15), number2 = "", operation = null)
        }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.value.number1.isNotBlank()) {
            state.value = state.value.copy(operation = operation)
        }
    }

    private fun enterNumber(number: Int) {
        if (state.value.operation == null) {
            if (state.value.number1.length < 9) {
                return
            }
            state.value = state.value.copy(number1 = state.value.number1 + number)
            return
        }
        if (state.value.operation == null) {
            if (state.value.number2.length < 9) {
                return
            }
            state.value = state.value.copy(number2 = state.value.number2 + number)
            return
        }
    }
}