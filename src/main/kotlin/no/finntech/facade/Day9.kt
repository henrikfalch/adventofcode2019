package no.finntech.facade

import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

class Day9 {

    fun runProgram(program: List<BigInteger>): List<BigInteger> {
        val instructions = Instructions(0, program, mutableListOf(ONE))

        return runThruster(instructions).output
    }

    fun runThruster(instructions: Instructions): Instructions {
        var current: Instructions = instructions
        while (!current.halt && !current.suspended) {
            current = calcIteration(current)
        }
        return current
    }


    fun calcIteration(instructions: Instructions): Instructions {
        val program = instructions.program
        val index = instructions.index
        val completeInstruction = program[index].toInt()
        val instruction = completeInstruction % 100
        val paramMode = listOf((completeInstruction % 1000) / 100, (completeInstruction % 10000) / 1000, completeInstruction / 10000)

        println("instruction: $instruction")
        return when (instruction) {
            1 -> add(instructions, paramMode)
            2 -> multiply(instructions, paramMode)
            3 -> input(instructions, paramMode)
            4 -> output(instructions, paramMode)
            5 -> jumpIfTrue(instructions, paramMode)
            6 -> jumpIfFalse(instructions, paramMode)
            7 -> lessThan(instructions, paramMode)
            8 -> equals(instructions, paramMode)
            9 -> setRelativeBase(instructions, paramMode)
            99 -> halt(instructions)
            else -> throw IllegalStateException("Illegal instruction: $instruction")
        }
    }

    private fun setRelativeBase(instructions: Instructions, paramMode: List<Int>): Instructions {
        val program = instructions.program
        val index = instructions.index
        val val1 = getValueFromMode(program, paramMode[0], index + 1, instructions)

        instructions.index += 2
        instructions.relativeBase += val1.toInt()

        println("Setting relative base to ${instructions.relativeBase}, with value: $val1, instruction: ${program[index]}, ${program[index+1]}")
        return instructions
    }

    private fun equals(instructions: Instructions, paramMode: List<Int>): Instructions {
        val function: (BigInteger, BigInteger) -> BigInteger = { v1, v2 -> if (v1 == v2) ONE else ZERO }
        return executeFunction(instructions, function, paramMode)
    }

    private fun lessThan(instructions: Instructions, paramMode: List<Int>): Instructions {
        val function: (BigInteger, BigInteger) -> BigInteger = { v1, v2 -> if (v1 < v2) ONE else ZERO }
        return executeFunction(instructions, function, paramMode)
    }

    private fun jumpIfFalse(instructions: Instructions, paramMode: List<Int>): Instructions {
        val program = instructions.program
        val index = instructions.index
        val val1 = getValueFromMode(program, paramMode[0], index + 1, instructions)

        if (val1 != ZERO) {
            instructions.index += 3
        } else {
            instructions.index = getValueFromMode(program, paramMode[1], index + 2, instructions).toInt()
        }
        return instructions
    }

    private fun jumpIfTrue(instructions: Instructions, paramMode: List<Int>): Instructions {
        val program = instructions.program
        val index = instructions.index
        val val1 = getValueFromMode(program, paramMode[0], index + 1, instructions)

        if (val1 == ZERO) {
            instructions.index += 3
        } else {
            instructions.index = getValueFromMode(program, paramMode[1], index + 2, instructions).toInt()
        }
        return instructions
    }

    private fun output(instructions: Instructions, paramMode: List<Int>): Instructions {
        val program = instructions.program
        val index = instructions.index
        val output = getValueFromMode(program, paramMode[0], index + 1, instructions)
        println("Output: $output")
        instructions.index += 2
        instructions.output.add(output)
        return instructions
    }

    private fun input(instructions: Instructions, paramMode: List<Int>): Instructions {
        val program = instructions.program
        val index = instructions.index
        val copy = program.toMutableList()
        val nextInput = instructions.nextInput()

        val insertINdex = getValueFromMode(program, paramMode[0], index+1, instructions).toInt()
        copy[insertINdex] = nextInput
        instructions.index += 2
        instructions.program = copy
        println("Input $nextInput added to index $insertINdex, instruction: ${program[index]}, ${program[index+1]}")

        return instructions
    }

    private fun add(instructions: Instructions, paramMode: List<Int>): Instructions {
        val function: (BigInteger, BigInteger) -> BigInteger = { v1, v2 -> v1 + v2 }
        return executeFunction(instructions, function, paramMode)
    }

    private fun multiply(instructions: Instructions, paramMode: List<Int>): Instructions {
        val function: (BigInteger, BigInteger) -> BigInteger = { v1, v2 -> v1 * v2 }
        return executeFunction(instructions, function, paramMode)
    }

    private fun executeFunction(instructions: Instructions, function: (BigInteger, BigInteger) -> BigInteger, paramMode: List<Int>): Instructions {
        val program = instructions.program
        val index = instructions.index
        val val1 = getValueFromMode(program, paramMode[0], index + 1, instructions)
        val val2 = getValueFromMode(program, paramMode[1], index + 2, instructions)
        val copy = program.toMutableList()
        val insertIndex = program[index + 3].toInt()
        if (insertIndex > copy.size - 1) {
            val numToAdd = copy.size - insertIndex
            //repeat(numToAdd) { DOES NOT WORK?? WTF??
            repeat(100) {
                copy.add(ZERO)
            }
        }
        copy[insertIndex] = function.invoke(val1, val2)

        instructions.program = copy
        instructions.index += 4
        return instructions
    }

    private fun getValueFromMode(program: List<BigInteger>, param: Int, index: Int, instructions: Instructions): BigInteger {
        return when (param) {
            0 -> program.getValue(index)
            1 -> program[index]
            2 -> (program.getValue(index, instructions.relativeBase))
            else -> throw IllegalArgumentException("Param not supported: $param")
        }
    }

    private fun List<BigInteger>.getValue(index: Int, addition: Int = 0): BigInteger {
        try {
            return this[this[index].toInt() + addition]
        } catch (e:IndexOutOfBoundsException) {
            return ZERO
        }
    }

    private fun halt(instructions: Instructions): Instructions {
        instructions.halt = true
        return instructions
    }

    fun excercise1(): String {
        val input = fileAsBigIntList("day9.txt")
        return runProgram(input).joinToString(", ")
    }

    class Instructions(
            var index: Int,
            var program: List<BigInteger>,
            var inputSignal: MutableList<BigInteger> = mutableListOf(),
            var output: MutableList<BigInteger> = mutableListOf(),
            var halt: Boolean = false
    ) {
        var inputsUsed = 0
        var suspended = false
        var relativeBase = 0

        fun nextInput(): BigInteger {
            return inputSignal[inputsUsed++]
        }
    }

}

fun main() {
    println("Excercise1: ${Day9().excercise1()}")
}