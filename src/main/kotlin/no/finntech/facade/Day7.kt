package no.finntech.facade

class Day7 {

    fun runProgram(program: List<Int>, phaseSettings: List<Int>): Int {
        var input = 0
        val allInstructions = phaseSettings.map { phaseSetting -> Instructions(0, program, input, phaseSetting) }.toMutableList()

        var instIndex = 0
        var counter=0
        while (allInstructions.filter { !it.halt }.any()) {
            val currentInst = allInstructions[instIndex]
            currentInst.inputSignal = input
            currentInst.inputsUsed--
            currentInst.suspended = false
            val runThruster = runThruster(currentInst)
            input = runThruster.output!!

            instIndex++
            if (instIndex > allInstructions.size - 1) {
                instIndex = 0
            }
            counter++
        }

        return input
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
        val completeInstruction = program[index]
        val instruction = completeInstruction % 100
        val paramMode = listOf((completeInstruction % 1000) / 100, (completeInstruction % 10000) / 1000, completeInstruction / 10000)

        return when (instruction) {
            1 -> add(instructions, paramMode)
            2 -> multiply(instructions, paramMode)
            3 -> input(instructions)
            4 -> output(instructions, paramMode)
            5 -> jumpIfTrue(instructions, paramMode)
            6 -> jumpIfFalse(instructions, paramMode)
            7 -> lessThan(instructions, paramMode)
            8 -> equals(instructions, paramMode)
            99 -> halt(instructions)
            else -> throw IllegalStateException("Illegal instruction: $instruction")
        }
    }

    private fun equals(instructions: Instructions, paramMode: List<Int>): Instructions {
        val function: (Int, Int) -> Int = { v1, v2 -> if (v1 == v2) 1 else 0 }
        return executeFunction(instructions, function, paramMode)
    }

    private fun lessThan(instructions: Instructions, paramMode: List<Int>): Instructions {
        val function: (Int, Int) -> Int = { v1, v2 -> if (v1 < v2) 1 else 0 }
        return executeFunction(instructions, function, paramMode)
    }

    private fun jumpIfFalse(instructions: Instructions, paramMode: List<Int>): Instructions {
        val program = instructions.program
        val index = instructions.index
        val val1 = getValueFromMode(program, paramMode[0], index + 1)

        if (val1 != 0) {
            instructions.index += 3
        } else {
            instructions.index = getValueFromMode(program, paramMode[1], index + 2)
        }
        return instructions
    }

    private fun jumpIfTrue(instructions: Instructions, paramMode: List<Int>): Instructions {
        val program = instructions.program
        val index = instructions.index
        val val1 = getValueFromMode(program, paramMode[0], index + 1)

        if (val1 == 0) {
            instructions.index += 3
        } else {
            instructions.index = getValueFromMode(program, paramMode[1], index + 2)
        }
        return instructions
    }

    private fun output(instructions: Instructions, paramMode: List<Int>): Instructions {
        val program = instructions.program
        val index = instructions.index
        val output = getValueFromMode(program, paramMode[0], index + 1)
        println("Output: $output")
        instructions.index += 2
        instructions.output = output
        return instructions
    }

    private fun input(instructions: Instructions): Instructions {
        val program = instructions.program
        val index = instructions.index
        val copy = program.toMutableList()
        val nextInput = instructions.nextInput()
        if (nextInput == null) {
            instructions.suspended = true
            instructions.inputsUsed--
            println("Suspend")
            return instructions
        }

        copy[program[index + 1]] = nextInput
        instructions.index += 2
        instructions.program = copy
        return instructions
    }

    private fun add(instructions: Instructions, paramMode: List<Int>): Instructions {
        val function: (Int, Int) -> Int = { v1, v2 -> v1 + v2 }
        return executeFunction(instructions, function, paramMode)
    }

    private fun multiply(instructions: Instructions, paramMode: List<Int>): Instructions {
        val function: (Int, Int) -> Int = { v1, v2 -> v1 * v2 }
        return executeFunction(instructions, function, paramMode)
    }

    private fun executeFunction(instructions: Instructions, function: (Int, Int) -> Int, paramMode: List<Int>): Instructions {
        val program = instructions.program
        val index = instructions.index
        val val1 = getValueFromMode(program, paramMode[0], index + 1)
        val val2 = getValueFromMode(program, paramMode[1], index + 2)
        val copy = program.toMutableList()
        copy[program[index + 3]] = function.invoke(val1, val2)

        instructions.program = copy
        instructions.index += 4
        return instructions
    }

    private fun getValueFromMode(program: List<Int>, param: Int, index: Int): Int {
        return if (param == 0) program.getValue(index) else program[index]
    }

    private fun List<Int>.getValue(index: Int): Int {
        return this[this[index]]
    }

    private fun halt(instructions: Instructions): Instructions {
        instructions.halt = true
        return instructions
    }

    fun excercise1(): Int {
        val program = fileAsIntList("day7.txt").toMutableList()
        var maxOutput = 0

        (0..4).forEach { val1 ->
            val val2AllValues = (0..4).filter { it != val1 }
            val2AllValues.forEach { val2 ->
                val val3AllValues = val2AllValues.filter { it != val2 }
                val3AllValues.forEach { val3 ->
                    val val4AllValues = val3AllValues.filter { it != val3 }
                    val4AllValues.forEach { val4 ->
                        val val5AllValues = val4AllValues.filter { it != val4 }
                        val5AllValues.forEach { val5 ->
                            val phaseSettings = listOf(val1, val2, val3, val4, val5)
                            println("Phasesettings: ${phaseSettings.joinToString(",")}")
                            val output = runProgram(program, phaseSettings)
                            if (output > maxOutput) {
                                maxOutput = output
                            }
                        }
                    }
                }
            }
        }
        return maxOutput
    }

    fun excercise2(): Int {
        val program = fileAsIntList("day7.txt").toMutableList()
        var maxOutput = 0

        (5..9).forEach { val1 ->
            val val2AllValues = (5..9).filter { it != val1 }
            val2AllValues.forEach { val2 ->
                val val3AllValues = val2AllValues.filter { it != val2 }
                val3AllValues.forEach { val3 ->
                    val val4AllValues = val3AllValues.filter { it != val3 }
                    val4AllValues.forEach { val4 ->
                        val val5AllValues = val4AllValues.filter { it != val4 }
                        val5AllValues.forEach { val5 ->
                            val phaseSettings = listOf(val1, val2, val3, val4, val5)
                            println("Phasesettings: ${phaseSettings.joinToString(",")}")
                            val output = runProgram(program, phaseSettings)
                            if (output > maxOutput) {
                                maxOutput = output
                            }
                        }
                    }
                }
            }
        }
        return maxOutput
    }
    class Instructions(
            var index: Int,
            var program: List<Int>,
            var inputSignal: Int,
            val phaseSetting: Int,
            var output: Int? = null,
            var halt: Boolean = false
    ) {
        var inputsUsed = 1
        var suspended = false

        fun nextInput(): Int? {
            inputsUsed++
            return when (inputsUsed) {
                1 -> phaseSetting
                2 -> inputSignal
                else -> null
            }
        }
    }

}


fun main() {
    println("Excercise2: ${Day7().excercise2()}")
}