package no.finntech.facade

import kotlinx.coroutines.channels.Channel
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

class IntComputer(private var program: List<BigInteger>, private var channels: IntComputerChannels) {
    suspend fun start() {
        var state = State(0, program, channels.inputChannel, channels.outputChannel)

        while (!state.halt) {
            state = calcIteration(state)
        }
        channels.closeChannel.send(true)
    }


    private suspend fun calcIteration(state: State): State {
        val program = state.program
        val index = state.index
        val completeInstruction = program[index].toInt()
        val instruction = completeInstruction % 100
        val paramMode = listOf((completeInstruction % 1000) / 100, (completeInstruction % 10000) / 1000, completeInstruction / 10000)

        return when (instruction) {
            1 -> add(state, paramMode)
            2 -> multiply(state, paramMode)
            3 -> input(state, paramMode)
            4 -> output(state, paramMode)
            5 -> jumpIfTrue(state, paramMode)
            6 -> jumpIfFalse(state, paramMode)
            7 -> lessThan(state, paramMode)
            8 -> equals(state, paramMode)
            9 -> setRelativeBase(state, paramMode)
            99 -> halt(state)
            else -> throw IllegalStateException("Illegal instruction: $instruction")
        }
    }

    private fun setRelativeBase(state: State, paramMode: List<Int>): State {
        val program = state.program
        val index = state.index
        val val1 = indexForRead(program, paramMode[0], index + 1, state)

        state.index += 2
        state.relativeBase += val1.toInt()

        println("Setting relative base to ${state.relativeBase}, with value: $val1, instruction: ${program[index]}, ${program[index+1]}")
        return state
    }

    private fun equals(state: State, paramMode: List<Int>): State {
        val function: (BigInteger, BigInteger) -> BigInteger = { v1, v2 -> if (v1 == v2) ONE else ZERO }
        return executeFunction(state, function, paramMode)
    }

    private fun lessThan(state: State, paramMode: List<Int>): State {
        val function: (BigInteger, BigInteger) -> BigInteger = { v1, v2 -> if (v1 < v2) ONE else ZERO }
        return executeFunction(state, function, paramMode)
    }

    private fun jumpIfFalse(state: State, paramMode: List<Int>): State {
        val program = state.program
        val index = state.index
        val val1 = indexForRead(program, paramMode[0], index + 1, state)

        if (val1 != ZERO) {
            state.index += 3
        } else {
            state.index = indexForRead(program, paramMode[1], index + 2, state).toInt()
        }
        return state
    }

    private fun jumpIfTrue(state: State, paramMode: List<Int>): State {
        val program = state.program
        val index = state.index
        val val1 = indexForRead(program, paramMode[0], index + 1, state)

        if (val1 == ZERO) {
            state.index += 3
        } else {
            state.index = indexForRead(program, paramMode[1], index + 2, state).toInt()
        }
        return state
    }

    private suspend fun output(state: State, paramMode: List<Int>): State {
        val program = state.program
        val index = state.index
        val output = indexForRead(program, paramMode[0], index + 1, state)
        println("Output: $output")
        state.index += 2
        state.output.send(output)
        return state
    }

    private suspend fun input(state: State, paramMode: List<Int>): State {
        val program = state.program
        val index = state.index
        val copy = program.toMutableList()
        val nextInput = state.input.receive()

        val insertIndex = indexForWrite(program, paramMode[0], index+1, state)
        copy[insertIndex] = nextInput
        state.index += 2
        state.program = copy
        println("Input $nextInput added to index $insertIndex, instruction: ${program[index]}, ${program[index+1]}")

        return state
    }

    private fun add(state: State, paramMode: List<Int>): State {
        val function: (BigInteger, BigInteger) -> BigInteger = { v1, v2 -> v1 + v2 }
        return executeFunction(state, function, paramMode)
    }

    private fun multiply(state: State, paramMode: List<Int>): State {
        val function: (BigInteger, BigInteger) -> BigInteger = { v1, v2 -> v1 * v2 }
        return executeFunction(state, function, paramMode)
    }

    private fun executeFunction(state: State, function: (BigInteger, BigInteger) -> BigInteger, paramMode: List<Int>): State {
        val program = state.program
        val index = state.index
        val val1 = indexForRead(program, paramMode[0], index + 1, state)
        val val2 = indexForRead(program, paramMode[1], index + 2, state)
        val copy = program.toMutableList()
        val insertIndex = indexForWrite(program, paramMode[2], index+3, state)

        if (insertIndex > copy.size - 1) {
            val numToAdd = insertIndex - copy.size + 1
            repeat(numToAdd) {
                copy.add(ZERO)
            }
        }
        copy[insertIndex] = function.invoke(val1, val2)

        state.program = copy
        state.index += 4
        return state
    }

    private fun indexForRead(program: List<BigInteger>, param: Int, index: Int, state: State): BigInteger {
        return when (param) {
            0 -> program.getValue(index)
            1 -> program[index]
            2 -> (program.getValue(index, state.relativeBase))
            else -> throw IllegalArgumentException("Param not supported: $param")
        }
    }

    private fun indexForWrite(program: List<BigInteger>, param: Int, index: Int, state: State): Int {
        return when (param) {
            0 -> program[index].toInt()
            1 -> index
            2 -> program[index].toInt() + state.relativeBase
            else -> throw IllegalArgumentException("Param not supported: $param")
        }
    }

    private fun List<BigInteger>.getValue(index: Int, addition: Int = 0): BigInteger {
        return try {
            this[this[index].toInt() + addition]
        } catch (e:IndexOutOfBoundsException) {
            ZERO
        }
    }

    private fun halt(state: State): State {
        state.halt = true
        return state
    }

    private class State(
            var index: Int,
            var program: List<BigInteger>,
            var input: Channel<BigInteger>,
            var output: Channel<BigInteger>,
            var halt: Boolean = false,
            var relativeBase: Int = 0
    )
}

class IntComputerChannels() {
    val outputChannel = Channel<BigInteger>()
    val inputChannel = Channel<BigInteger>(Channel.UNLIMITED)
    val closeChannel = Channel<Boolean>()
}