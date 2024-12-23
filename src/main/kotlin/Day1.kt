import kotlin.math.absoluteValue

fun day1P12024() {

    val file = 1.readDayInput()

    val leftL = mutableListOf<Long>()
    val rightL = mutableListOf<Long>()
    file.map { line ->
        val (left, right) = line.trim().split(" ").filter { it.isNotBlank() }
        leftL.add(left.toLong())
        rightL.add(right.toLong())
    }
    leftL.sorted()
    val rightSorted = rightL.sorted()

    var result: Long = 0
    leftL.sorted().forEachIndexed { index, left ->
        result += (rightSorted[index] - left).absoluteValue
    }

    println(result)
}

fun day1P22024() {

    val file = 1.readDayInput()

    val leftL = mutableListOf<Long>()
    val rightL = mutableListOf<Long>()
    file.map { line ->
        val (left, right) = line.trim().split(" ").filter { it.isNotBlank() }
        leftL.add(left.toLong())
        rightL.add(right.toLong())
    }
    val right = rightL.groupingBy { it }.eachCount()

    var result: Long = 0
    leftL.forEach { left ->
        result += left * right.getOrDefault(left, 0)
    }

    println(result)
}