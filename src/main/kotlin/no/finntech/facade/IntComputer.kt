package no.finntech.facade

import kotlinx.coroutines.channels.Channel
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

class IntComputer(
        inputProgram: List<BigInteger>,
        private val channels: IntComputerChannels,
        private val debugMode: Boolean = false
) {
    private val state = State(0, inputProgram)

    suspend fun start() {
        while (!state.halt) {
            calcIteration()
        }
        channels.exit.send(true)
    }

    private suspend fun calcIteration() {
        val completeInstruction = state.program[state.index].toInt()
        val instruction = completeInstruction % 100
        val paramMode = listOf((completeInstruction % 1000) / 100, (completeInstruction % 10000) / 1000, completeInstruction / 10000)

        when (instruction) {
            1 -> add(paramMode)
            2 -> multiply(paramMode)
            3 -> input(paramMode)
            4 -> output(paramMode)
            5 -> jumpIfTrue(paramMode)
            6 -> jumpIfFalse(paramMode)
            7 -> lessThan(paramMode)
            8 -> equals(paramMode)
            9 -> setRelativeBase(paramMode)
            99 -> halt()
            else -> throw IllegalStateException("Illegal instruction: $instruction")
        }
    }

    private fun setRelativeBase(paramMode: List<Int>) {
        val val1 = indexForRead(paramMode[0], 1)

        state.index += 2
        state.relativeBase += val1.toInt()

        if (debugMode) {
            println("Setting relative base to ${state.relativeBase}, with value: $val1, instruction: ${state.program[state.index]}, ${state.program[state.index + 1]}")
        }
    }

    private fun equals(paramMode: List<Int>) {
        val function: (BigInteger, BigInteger) -> BigInteger = { v1, v2 -> if (v1 == v2) ONE else ZERO }
        return executeFunction(function, paramMode)
    }

    private fun lessThan(paramMode: List<Int>) {
        val function: (BigInteger, BigInteger) -> BigInteger = { v1, v2 -> if (v1 < v2) ONE else ZERO }
        return executeFunction(function, paramMode)
    }

    private fun jumpIfFalse(paramMode: List<Int>) {
        val val1 = indexForRead(paramMode[0], 1)

        if (val1 != ZERO) {
            state.index += 3
        } else {
            state.index = indexForRead(paramMode[1], 2).toInt()
        }
    }

    private fun jumpIfTrue(paramMode: List<Int>) {
        val val1 = indexForRead(paramMode[0], 1)

        if (val1 == ZERO) {
            state.index += 3
        } else {
            state.index = indexForRead(paramMode[1], 2).toInt()
        }
    }

    private suspend fun output(paramMode: List<Int>) {
        val output = indexForRead(paramMode[0], 1)

        if (debugMode) {
            println("Output: $output")
        }

        state.index += 2
        channels.output.send(output)
    }

    private suspend fun input(paramMode: List<Int>) {
        val nextInput = channels.input.receive()

        val insertIndex = indexForWrite(paramMode[0], 1)
        writeValueToProgram(nextInput, insertIndex)
        state.index += 2
        if (debugMode) {
            println("Input $nextInput added to index $insertIndex, instruction: ${state.program[state.index]}, ${state.program[state.index + 1]}")
        }
    }

    private fun add(paramMode: List<Int>) {
        val function: (BigInteger, BigInteger) -> BigInteger = { v1, v2 -> v1 + v2 }
        return executeFunction(function, paramMode)
    }

    private fun multiply(paramMode: List<Int>) {
        val function: (BigInteger, BigInteger) -> BigInteger = { v1, v2 -> v1 * v2 }
        return executeFunction(function, paramMode)
    }

    private fun executeFunction(function: (BigInteger, BigInteger) -> BigInteger, paramMode: List<Int>) {
        val val1 = indexForRead(paramMode[0], 1)
        val val2 = indexForRead(paramMode[1], 2)
        val value = function.invoke(val1, val2)
        val insertIndex = indexForWrite(paramMode[2], 3)

        writeValueToProgram(value, insertIndex)
        state.index += 4
    }

    private fun writeValueToProgram(value: BigInteger, insertIndex: Int) {
        val program = state.program.toMutableList()
        if (insertIndex > program.size - 1) {
            val numToAdd = insertIndex - program.size + 1
            repeat(numToAdd) {
                program.add(ZERO)
            }
        }
        program[insertIndex] = value
        state.program = program
    }

    private fun indexForRead(param: Int, indexOffset: Int): BigInteger {
        return try {
            state.program[indexForWrite(param, indexOffset)]
        } catch (e: IndexOutOfBoundsException) {
            ZERO
        }
    }

    private fun indexForWrite(param: Int, indexOffset: Int): Int {
        val index = state.index + indexOffset
        return when (param) {
            0 -> state.program[index].toInt()
            1 -> index
            2 -> state.program[index].toInt() + state.relativeBase
            else -> throw IllegalArgumentException("Param not supported: $param")
        }
    }

    private fun halt() {
        state.halt = true
    }

    private class State(
            var index: Int,
            var program: List<BigInteger>,
            var halt: Boolean = false,
            var relativeBase: Int = 0
    )
}

class IntComputerChannels() {
    val output = Channel<BigInteger>()
    val input = Channel<BigInteger>(Channel.UNLIMITED)
    val exit = Channel<Boolean>()
}