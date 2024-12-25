import java.util.ArrayDeque
import java.util.LinkedList
import kotlin.math.pow

fun day172024() {
    val input = 17.readDayInput()
    val registerA = input.first().split("Register A: ").last().toLong()
    val registerB = input[1].split("Register B: ").last().toLong()
    val registerC = input[2].split("Register C: ").last().toLong()
    val program = input[4].split("Program: ").last().split(",").map(String::toLong)
    val machine = Machine(
        registerA = registerA,
        registerB = registerB,
        registerC = registerC,
        initialProgram = program,
    )
    machine.run()
    machine.selfCreation()
}

class Machine(
    private var registerA: Long,
    private var registerB: Long,
    private var registerC: Long,
    private val initialProgram: List<Long>,
) {
    private val program = LinkedList<Long>()
    private var instructionPointer = 0L
    private val out = LinkedList<String>()

    init {
        program.addAll(initialProgram)
    }

    private fun Long.getComboValue(): Long =
        when (this) {
            4L -> registerA
            5L -> registerB
            6L -> registerC
            else -> this
        }

    private fun instructionPointerIncrease() {
        instructionPointer += 2
    }

    private fun dv(operand: Long): Long {
        val raiseTo = operand.getComboValue()
        return (registerA / 2.0.pow(raiseTo.toInt())).toLong()
    }

    private fun adv(operand: Long) {
        registerA = dv(operand)
        instructionPointerIncrease()
    }

    // 0101010
    // 1010101
    // 1111111
    private fun bxl(operand: Long) {
        registerB = registerB.xor(operand)
        instructionPointerIncrease()
    }

    private fun bst(operand: Long) {
        registerB = operand.getComboValue() % 8
        instructionPointerIncrease()
    }

    private fun jnz(operand: Long) {
        if (registerA == 0L) {
            instructionPointerIncrease()
            return
        }
        instructionPointer = operand
    }

    private fun bxc(operand: Long) {
        registerB = registerB.xor(registerC)
        instructionPointerIncrease()
    }

    private fun out(operand: Long) {
        out.add((operand.getComboValue() % 8).toString())
        instructionPointerIncrease()
    }

    private fun bdv(operand: Long) {
        registerB = dv(operand)
        instructionPointerIncrease()
    }

    private fun cdv(operand: Long) {
        registerC = dv(operand)
        instructionPointerIncrease()
    }

    fun run() {
        while (instructionPointer < program.size) {
            val opCode = program[instructionPointer.toInt()]
            val operand = program[instructionPointer.toInt()+1]
            println("Op $opCode operand $operand regA $registerA mod 8 ${registerA % 8}")
            when (opCode) {
                0L -> adv(operand)
                1L -> bxl(operand)
                2L -> bst(operand)
                3L -> jnz(operand)
                4L -> bxc(operand)
                5L -> out(operand)
                6L -> bdv(operand)
                7L -> cdv(operand)
            }
        }
        println(out.joinToString(","))
    }

    private fun inverseAdv(operand: Long) {
        val raiseTo = operand.getComboValue()
        registerA = (registerA * 2.0.pow(raiseTo.toInt())).toLong()
    }

    private fun inverseBdv(operand: Long) {
        // ignore
        val raiseTo = operand.getComboValue()
        registerA = (registerB * 2.0.pow(raiseTo.toInt())).toLong()
    }

    private fun inverseCdv(operand: Long) {
        // ignore
//        val raiseTo = operand.getComboValue()
//        registerA = (registerC * 2.0.pow(raiseTo.toInt())).toLong()
    }

    private fun inverseBst(operand: Long) {
        when (operand) {
            4L -> registerA = (registerA shl 3) + registerB
            5L -> registerB = (registerB shl 3) + registerB
            6L -> registerC = (registerC shl 3) + registerB
            else -> registerB = operand
        }
    }

    private fun inverseOut(operand: Long, expected: Long) {
        when(operand) {
            4L -> registerA = (registerA shl 3) + expected
            5L -> registerB = (registerB shl 3) + expected
            6L -> registerC = (registerC shl 3) + expected
        }
    }

    fun selfCreation() {
        val instructions = program.windowed(2, 2)
        val gotoInstruction  = instructions.find { it.first() == 3L }!!
        val gotoInstructionIndex = instructions.indexOf(gotoInstruction)
        val gotoIndex = gotoInstruction[1]
        val register = "B"
        val reversedInstructions = instructions
            .takeLast(instructions.size - (gotoIndex.toInt() / 2))
            .dropLast(1)
            .reversed()
            .toMutableList()
        // not dynamic solution

        registerA = 0
        registerB = 0
        registerC = 0

//        val toProcess = ArrayDeque(program.reversed())
        val toProcess = ArrayDeque<Long>()
        toProcess.add(5)

        while (toProcess.isNotEmpty()) {
            reversedInstructions.forEach { (opCode, operand) ->
                println("Op $opCode operand $operand")
                when (opCode) {
                    0L -> inverseAdv(operand)
                    1L -> bxl(operand)
                    2L -> inverseBst(operand)
                    3L -> jnz(operand)
                    4L -> bxc(operand)
                    5L -> inverseOut(operand, toProcess.removeFirst())
                    6L -> inverseBdv(operand)
                    7L -> inverseCdv(operand)
                }
            }
        }
        println(registerA)
    }
}

// Notes
// Only care about regA
// regA changed by opcodes: 0
// regB changed by opcodes: 1, 2, 4, 6

// Needed for out: 5 4 -> from regB 5 5 from regB 5 6 from regC
// We need any number ending with the desired output in the registry
// 1000 % 8 = 000
// 1010 % 8 = 010
// 1110 % 8 = 110
// ...XYZ % 8 = XYZ

// 2,4   |  1,7    |  7,5    |  0,3    |  1,7    |  4,1  |  5,5 |  3,0   |
// B=A%8 | B=Bx111 | C=A/2^B | A=A/2^3 | B=Bx111 | B=BxC | O->B | GOTO 0 |


