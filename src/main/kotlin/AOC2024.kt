import java.io.PrintWriter
import java.math.BigInteger
import java.util.*
import kotlin.math.floor

internal fun Int.readDayInput() = readFile("2024/$this")

class AOC2024 {
    fun execute() {
//        day2P1()
//        day2P22024()
//        day3P12024()
//        day3P22024()
//        day4P12024()
//        day4P22024()
//        day5P12024()
//        day5P22024()
//        day6P2024()
//        day72024()
//        day82024()
//        day9P12024()
//        day9P22024_retry()
//        day112024()
//        day132024()
//        day132024(true)
//        day142024()
//        day142024(true)
//        day15P12024()
        day15P22024()
    }
}

fun List<Int>.isSafe(isIncreasing: Boolean) = this.windowed(2).all { (a, b) ->
    if (isIncreasing) {
        a < b && (b - a) <= 3
    } else {
        a > b && (a - b) <= 3
    }
}

fun day2P1() {
    val input = 2.readDayInput().map { report -> report.split(" ").map { it.toInt() } }

    val result = input.count { report ->
        val isIncreasing = report.first() < report[2]
        if (report.first() == report[1]) return@count false

        report.isSafe(isIncreasing)
    }

    println(result)
}

fun day2P22024() {
    val input = 2.readDayInput().map { report -> report.split(" ").map { it.toInt() } }

    val invalid = mutableListOf<List<Int>>()

    val result = input.count { report ->
        val isIncreasing = report.first() < report[1]
        if (report.isSafe(isIncreasing)) return@count true

        report.forEachIndexed { index, _ ->
            val modified = report.toMutableList()
            modified.removeAt(index)
            if (modified.isSafe(true) || modified.isSafe(false)) return@count true
        }
        invalid.add(report)
        false
    }
    println(result)
}

fun day3P12024() {
    val regex = Regex("mul\\((\\d+,\\d+)\\)")

    val result = 3.readDayInput().sumOf { command ->
        val results = regex.findAll(command)
        results.sumOf {
            val (a, b) = it.groups[1]!!.value.split(",")
            a.toLong() * b.toLong()
        }
    }
    println(result)
}

fun day3P22024() {
    val regex = Regex("mul\\((\\d+,\\d+)\\)|do\\(\\)|don't\\(\\)")

    var mulEnabled = true
    val result = 3.readDayInput().sumOf { command ->
        val results = regex.findAll(command)
        results.sumOf {
            var toSum = 0L
            if (mulEnabled && it.groups.first()!!.value.startsWith("mul")) {
                val (a, b) = it.groups[1]!!.value.split(",")
                toSum = a.toLong() * b.toLong()
            } else if (it.groups.first()!!.value == "do()") {
                mulEnabled = true
            } else if (it.groups.first()!!.value == "don't()") {
                mulEnabled = false
            }
            toSum
        }
    }
    println(result)
}

data class Position(val row: Int, val column: Int)
data class Vector(val rowOffset: Int, val columnOffset: Int)

fun Position.add(vector: Vector) = Position(row + vector.rowOffset, column + vector.columnOffset)
fun Vector.inverse() = Vector(-rowOffset, -columnOffset)
fun Vector.times(n: Int) = Vector(rowOffset * n, columnOffset * n)

fun List<String>.rightValid(pos: Position, text: String) = pos.column + text.length <= this[pos.row].length
fun List<String>.leftValid(pos: Position, text: String) = pos.column - text.length + 1 >= 0
fun List<String>.downValid(pos: Position, text: String) = pos.row + text.length <= this.size
fun List<String>.upValid(pos: Position, text: String) = pos.row - text.length + 1 >= 0

fun List<String>.checkRight(pos: Position, text: String): Boolean {
    if (this.rightValid(pos, text)) {
        val right = this[pos.row].substring(pos.column, pos.column + text.length)
        val match = right == text
        println("R  $right $match")
        return match
    }
    return false
}

fun List<String>.checkLeft(pos: Position, text: String): Boolean {
    if (this.leftValid(pos, text)) {
        val left = this[pos.row].substring(pos.column - text.length + 1, pos.column + 1)
        val match = left.reversed() == text
        println("L  $left $match")
        return match
    }
    return false
}

fun List<String>.checkDown(pos: Position, text: String): Boolean {
    if (this.downValid(pos, text)) {
        print("D  ")
        var match = true
        for (i in 0 until text.length) {
            val c = this[pos.row + i][pos.column]
            print(c)
            if (c != text[i]) match = false
        }
        print(" $match")
        println()
        return match
    }
    return false
}

fun List<String>.checkUp(pos: Position, text: String): Boolean {
    if (this.upValid(pos, text)) {
        print("U  ")
        var match = true
        for (i in 0 until text.length) {
            val c = this[pos.row - i][pos.column]
            print(c)
            if (c != text[i]) match = false
        }
        print(" $match")
        println()
        return match
    }
    return false
}

fun List<String>.checkDownRight(pos: Position, text: String): Boolean {
    if (this.downValid(pos, text) && this.rightValid(pos, text)) {
        print("DR ")
        var match = true
        for (i in 0 until text.length) {
            val c = this[pos.row + i][pos.column + i]
            print(c)
            if (c != text[i]) match = false
        }
        print(" $match")
        println()
        return match
    }
    return false
}

fun List<String>.checkDownLeft(pos: Position, text: String): Boolean {
    if (this.downValid(pos, text) && this.leftValid(pos, text)) {
        print("DL ")
        var match = true
        for (i in 0 until text.length) {
            val c = this[pos.row + i][pos.column - i]
            print(c)
            if (c != text[i]) match = false
        }
        print(" $match")
        println()
        return match
    }
    return false
}


fun List<String>.checkUpRight(pos: Position, text: String): Boolean {
    if (this.upValid(pos, text) && this.rightValid(pos, text)) {
        print("UR ")
        var match = true
        for (i in 0 until text.length) {
            val c = this[pos.row - i][pos.column + i]
            print(c)
            if (c != text[i]) match = false
        }
        print(" $match")
        println()
        return match
    }
    return false
}

fun List<String>.checkUpLeft(pos: Position, text: String): Boolean {
    if (this.upValid(pos, text) && this.leftValid(pos, text)) {
        print("UL ")
        var match = true
        for (i in 0 until text.length) {
            val c = this[pos.row - i][pos.column - i]
            print(c)
            if (c != text[i]) match = false
        }
        print(" $match")
        println()
        return match
    }
    return false
}


fun List<String>.assert(pos: Position, text: String): Int {
    var count = 0
    if (this[pos.row][pos.column] != text.first()) {
        return 0
    }
    if (this.checkRight(pos, text)) count++
    if (this.checkLeft(pos, text)) count++
    if (this.checkDown(pos, text)) count++
    if (this.checkUp(pos, text)) count++
    if (this.checkDownRight(pos, text)) count++
    if (this.checkDownLeft(pos, text)) count++
    if (this.checkUpRight(pos, text)) count++
    if (this.checkUpLeft(pos, text)) count++
    println()
    return count
}

fun day4P12024() {
    val input = 4.readDayInput()
    val toFind = "XMAS"

    var sum = 0

    val updated = input.mapIndexed { rowIndex, line ->
        line.mapIndexed { columnIndex, c ->
            val contains = input.assert(Position(rowIndex, columnIndex), toFind)
            sum += contains
            if (contains > 0) {
                contains
            } else {
                '.'
            }
        }.joinToString(" ")
    }

    updated.forEach { println(it) }
    println(sum)
}

fun List<String>.assertP2(pos: Position, text: String): Int {
    var count = 0
    // M.S
    // .A.
    // M.S
    if (this.rightValid(pos, text) && this.downValid(pos, text)) {
        if (this.checkUpRight(Position(pos.row + 2, pos.column), text) && this.checkDownRight(pos, text)) count++
    }
    // M.M
    // .A.
    // S.S
    if (this.rightValid(pos, text) && this.downValid(pos, text)) {
        if (this.checkDownLeft(Position(pos.row, pos.column + 2), text) && this.checkDownRight(pos, text)) count++
    }
    // S.S
    // .A.
    // M.M
    if (this.rightValid(pos, text) && this.upValid(pos, text)) {
        if (this.checkUpLeft(Position(pos.row, pos.column + 2), text) && this.checkUpRight(pos, text)) count++
    }
    // S.M
    // .A.
    // S.M
    if (this.leftValid(pos, text) && this.upValid(pos, text)) {
        if (this.checkDownLeft(Position(pos.row - 2, pos.column), text) && this.checkUpLeft(pos, text)) count++
    }

    return count
}

fun day4P22024() {
    val input = 4.readDayInput()
    val toFind = "MAS"
    var sum = 0

    val updated = input.mapIndexed { rowIndex, line ->
        line.mapIndexed { columnIndex, c ->
            val contains = input.assertP2(Position(rowIndex, columnIndex), toFind)
            sum += contains
            if (contains > 0) {
                contains
            } else {
                '.'
            }
        }.joinToString(" ")
    }

    updated.forEach { println(it) }
    println(sum)
}

fun List<Long>.isValid(rules: Map<Long, Set<Long>>): Boolean {
    val cache = mutableSetOf<Long>()
    this.forEach { page ->
        cache.add(page)
        rules[page]?.let { rule ->
            if (cache.intersect(rule).isNotEmpty()) {
                return false
            }
        }
    }
    return true
}

fun day5P12024() {
    val (rules, updates) = 5.readDayInput().partition { it.contains("|") }
    val ruleMap = rules.map { rule ->
        val (first, second) = rule.split("|").map { it.toLong() }
        first to second
    }.groupBy { it.first }.map { (key, pairList) -> key to pairList.map { it.second }.toSet() }.toMap()

    val sum = updates
        .takeLast(updates.size - 1)
        .sumOf { updatesStr ->
        val updateList = updatesStr.split(",").map { it.toLong() }

        if (updateList.isValid(ruleMap)) {
            updateList[floor(updateList.size.toDouble() / 2).toInt()]
        } else {
            0L
        }
    }
    println(sum)
}

fun List<Long>.makeValid(rules: Map<Long, Set<Long>>): List<Long> {
    val cache = LinkedList<Long>()
    val copy = this.toMutableList()
    var valid = false
    while (!valid) {
        cache.clear()
        valid = true
        copy.forEach { page ->
            cache.addLast(page)
            rules[page]?.let { rule ->
                val invalid = cache.intersect(rule)
                if (invalid.isNotEmpty()) {
                    cache.removeLast()
                    cache.add(cache.indexOf(invalid.first()), page)
                    valid = false
                }
            }
        }
        copy.clear()
        copy.addAll(cache)
    }
    return cache
}

fun day5P22024() {
    val (rules, updates) = 5.readDayInput().partition { it.contains("|") }
    val ruleMap = rules.map { rule ->
        val (first, second) = rule.split("|").map { it.toLong() }
        first to second
    }.groupBy { it.first }.map { (key, pairList) -> key to pairList.map { it.second }.toSet() }.toMap()

    val sum = updates
        .takeLast(updates.size - 1)
        .sumOf { updatesStr ->
            val updateList = updatesStr.split(",").map { it.toLong() }

            if (!updateList.isValid(ruleMap)) {
                val updated = updateList.makeValid(ruleMap)
                updated[floor(updateList.size.toDouble() / 2).toInt()]
            } else {
                0L
            }
        }
    println(sum)
}

fun Position.up() = this.copy(row = row - 1, column = column)
fun Position.down() = this.copy(row = row + 1, column = column)
fun Position.left() = this.copy(row = row, column = column - 1)
fun Position.right() = this.copy(row = row, column = column + 1)
fun Position.moveDirection(direction: Direction): Position {
    return when (direction) {
        Direction.UP -> this.up()
        Direction.RIGHT -> this.right()
        Direction.DOWN -> this.down()
        Direction.LEFT -> this.left()
    }
}


fun List<String>.getCharInPos(pos: Position) = this[pos.row][pos.column]
fun Position.isValid(map: List<String>) =
    row >= 0 && row < map.size && column >= 0 && column < map.first().length

enum class Direction(val repr: Char) {
    UP('^'), DOWN('v'), LEFT('<'), RIGHT('>');

    fun rotateClockWise(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }

    fun rotateCounterClockWise(): Direction {
        return when (this) {
            UP -> LEFT
            RIGHT -> UP
            DOWN -> RIGHT
            LEFT -> DOWN
        }
    }

    companion object {
        fun fromCharacter(c: Char): Direction {
            return values().find { it.repr == c } ?: throw java.lang.IllegalArgumentException("Wrong direction input")
        }
    }
}

fun Pair<Position, Direction>.nextUntilObstacle(obstructions: Set<Position>): Pair<Position, Direction>? {
    val (position, direction) = this

    val rowMap = obstructions
        .groupBy { it.row }
        .map { (key, value) ->
            key to value.map { it.column }.sorted()
        }.toMap()

    val columnMap = obstructions
        .groupBy { it.column }
        .map { (key, value) ->
            key to value.map { it.row }.sorted()
        }.toMap()

    return when (direction.rotateClockWise()) {
        Direction.RIGHT -> {
            rowMap[position.row]
                ?.firstOrNull { column -> position.column < column }
                ?.let { column -> Position(position.row, column - 1) to Direction.RIGHT }
        }
        Direction.LEFT -> {
            rowMap[position.row]
                ?.lastOrNull { column -> position.column > column }
                ?.let { column -> Position(position.row, column + 1) to Direction.LEFT }
        }
        Direction.UP -> {
            columnMap[position.column]
                ?.lastOrNull { row -> position.row > row }
                ?.let { row -> Position(row + 1, position.column) to Direction.UP }
        }
        Direction.DOWN -> {
            columnMap[position.column]
                ?.firstOrNull { row -> position.row < row }
                ?.let { row -> Position(row - 1, position.column) to Direction.DOWN }
        }
    }
}

fun List<String>.calculateLoops(guardPosition: Position, guardDirection: Direction, traversedOrder: LinkedList<Pair<Position, Direction>>) {
    val obstructions = mutableSetOf<Position>()
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, column ->
            val position = Position(rowIndex, columnIndex)
            if (this.getCharInPos(position) == '#') {
                obstructions.add(position)
            }
        }
    }

    val validLoopObstacles = mutableSetOf<Position>()
    val cache = mutableSetOf<Position>()
    // check for loops
    var cnt = traversedOrder.size
    traversedOrder.forEach { (position, direction) ->
        cnt--
        if (cnt % 100 == 0) {
            println("To go: $cnt")
        }

        cache.add(position)
        val potentialObstacle = position.moveDirection(direction)
        if (potentialObstacle in cache) {
            return@forEach
        }

        val modifiedObstructions = obstructions.toMutableSet()
        modifiedObstructions.add(potentialObstacle)

        var turn: Position? = position
        var turnDirection: Direction? = direction
        var obstaclesHit = 1
        val turnsTaken = mutableSetOf<Position>()
        while (turn != null) {
            turn.let { t ->
                turnDirection?.let { d ->
                    turnsTaken.add(t)
                    val next = (t to d).nextUntilObstacle(modifiedObstructions)
                    obstaclesHit++
                    turn = next?.first
                    turnDirection = next?.second

                    if (turn in turnsTaken && turn != t) {
                        validLoopObstacles.add(potentialObstacle)
                        turn = null
                    }
                }
            }
        }
    }
    this.draw(traversedOrder, validLoopObstacles)
    println(validLoopObstacles.size)
}

fun List<String>.moveGuard(guardPosition: Position, guardDirection: Direction): Set<Position> {
    val traversed = mutableSetOf<Position>()
    val traversedOrder = LinkedList<Pair<Position, Direction>>()
    var doneWalking = false

    var direction = guardDirection
    var newPosition = guardPosition

    while (!doneWalking) {
        var obstructed = true
        var obstructedCnt = 0
        traversed.add(newPosition)
        traversedOrder.add(newPosition to direction)
        while (obstructed) {
            val prevPosition = newPosition
            newPosition = newPosition.moveDirection(direction)
            if (!newPosition.isValid(this)) {
                obstructed = false
                doneWalking = true
                continue
            }
            val newPositionChar = this.getCharInPos(newPosition)
            if (newPositionChar == '#') {
                newPosition = prevPosition
                direction = direction.rotateClockWise()
                traversedOrder.removeLast()
                traversedOrder.add(newPosition to direction)
                obstructedCnt++
            } else {
                obstructed = false
            }
        }
        if (obstructedCnt > 1) {
            println("more than once")
        }
    }

    this.calculateLoops(guardPosition, guardDirection, traversedOrder)

    return traversed
}

fun List<String>.draw(traversedOrder: LinkedList<Pair<Position, Direction>>, loopObstacles: Set<Position>) {
    val traversed = traversedOrder.groupBy { it.first }
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, column ->
            if (Position(rowIndex, columnIndex) in loopObstacles) {
                print('O')
            } else if (traversed.contains(Position(rowIndex, columnIndex))) {
                print(traversed[Position(rowIndex, columnIndex)]!!.first().second.repr)
            } else {
                print(column)
            }
        }
        println()
    }
    println()
}


fun List<String>.draw(traversed: Set<Position>) {
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, column ->
            if (Position(rowIndex, columnIndex) in traversed) {
                print('X')
            } else {
                print(column)
            }
        }
        println()
    }
    println()
}

fun List<String>.draw(walls: Set<Position>, obstacles: Set<Position>, robot: Position) {
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, column ->
            val pos = Position(rowIndex, columnIndex)
            if (pos in obstacles) {
                print('X')
            } else if (pos == robot) {
                print('@')
            } else if (pos in walls) {
                print('#')
            } else {
                print(column)
            }
        }
        println()
    }
    println()
}

fun List<String>.drawUpdated(walls: Set<Position>, obstacles: Set<Pair<Position, Position>>, robot: Position) {
    val obstaclesStart = obstacles.map { it.first }
    var skip = false
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed rowForEach@{ columnIndex, column ->
            if (skip) {
                skip = false
                return@rowForEach
            }
            val pos = Position(rowIndex, columnIndex)
            if (pos in obstaclesStart) {
                print("[]")
                skip = true
            } else if (pos == robot) {
                print('@')
            } else if (pos in walls) {
                print('#')
            } else {
                print(column)
            }
        }
        println()
    }
    println()
}

fun day6P2024() {
    val map = 6.readDayInput()

    val guardDirections = Direction.values().map { it.repr }
    var guardPosition: Position? = null
    var guardDirection: Direction? = null
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed row@{ columnIndex, cell ->
            if (cell in guardDirections) {
                guardPosition = Position(rowIndex, columnIndex)
                guardDirection = Direction.fromCharacter(map.getCharInPos(Position(rowIndex, columnIndex)))
                return@row
            }
        }
    }
    guardPosition?.let { pos ->
        guardDirection?.let { direction ->
            val traversed = map.moveGuard(pos, direction)
//            map.draw(traversed)
            println(traversed.size)
        }
    } ?: println("No guard position found")
}

fun <T> List<T>.takeExceptFirst() = this.takeLast(this.size - 1)
fun Long.concatenate(other: Long) = if (this == 0L) {
    other
} else {
    (this.toString() + other.toString()).toLong()
}

fun Long.resolve(totalToFind: Long, rest: List<Long>, secondEnabled: Boolean): Boolean {
    if (this > totalToFind) {
        return false
    }

    return if (rest.size == 1) {
        val last = rest.last()
        (this + last) == totalToFind
                || (this * last) == totalToFind
                || (secondEnabled && this.concatenate(last) == totalToFind)
    } else {
        val first = rest.first()
        return (this + first).resolve(totalToFind, rest.takeExceptFirst(), secondEnabled)
                || (this * first).resolve(totalToFind, rest.takeExceptFirst(), secondEnabled)
                || (secondEnabled && this.concatenate(first).resolve(totalToFind, rest.takeExceptFirst(), true))
    }
}


fun day72024() {
    val tests = 7.readDayInput()

    val valid = tests.sumOf { test ->
        val (toFindStr, rest) = test.split(":")
        val toFind = toFindStr.toLong()
        val equationList = rest.trim().split(" ").map { it.toLong() }
        if (0L.resolve(toFind, equationList, false)) {
            toFind
        } else {
            0L
        }
    }

    val validSecond = tests.sumOf { test ->
        val (toFindStr, rest) = test.split(":")
        val toFind = toFindStr.toLong()
        val equationList = rest.trim().split(" ").map { it.toLong() }
        if (0L.resolve(toFind, equationList, true)) {
            toFind
        } else {
            0L
        }
    }
    println(valid)
    println(validSecond)
}

fun Pair<Position, Position>.validAntiNodes(map: List<String>): Set<Position> {
    // #...
    // .a..
    // ..a.
    // ...#
    // a(1,1) a(2,2)
    // #(0,0) #(3,3)
    val (first, second) = this
    val offset = Vector (first.row - second.row, first.column - second.column)
    val antiNode1 = first.add(offset).takeIf { it.isValid(map) }
    val antiNode2 = second.add(offset.inverse()).takeIf { it.isValid(map) }
    return setOfNotNull(antiNode1, antiNode2)
}

fun Pair<Position, Position>.validAntiNodesPart2(map: List<String>): Set<Position> {
    var (firstNext, secondNext) = this
    val offset = Vector (first.row - second.row, first.column - second.column)

    val antiNodes = mutableSetOf<Position>()
    var isValid1 = true
    var isValid2 = true
    while (isValid1 || isValid2) {
        val antiNode1 = firstNext.add(offset)
        val antiNode2 = secondNext.add(offset.inverse())
        isValid1 = antiNode1.isValid(map)
        isValid2 = antiNode2.isValid(map)
        if (isValid1) {
            antiNodes.add(antiNode1)
        }
        if (isValid2) {
            antiNodes.add(antiNode2)
        }
        firstNext = antiNode1
        secondNext = antiNode2
    }
    println("_${antiNodes.size}")
    return antiNodes
}

fun day82024() {
    val map = 8.readDayInput()

    val obstructed = mutableSetOf<Position>()
    val antennaMap = mutableMapOf<Char, List<Position>>()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, cell ->
            if (cell != '.') {
                val position = Position(rowIndex, columnIndex)
                obstructed.add(position)
                val updated = antennaMap.getOrDefault(cell, setOf()).toMutableList()
                updated.add(position)
                antennaMap[cell] = updated
            }
        }
    }
    val antiNodes = mutableSetOf<Position>()
    antennaMap.forEach { (_, positions) ->
        for (i in positions.indices) {
            for (j in positions.indices) {
                if (i == j) {
                    continue
                }
                val first = positions[i]
                val second = positions[j]
                val subAntiNodes = (first to second).validAntiNodes(map)
                antiNodes.addAll(subAntiNodes)
            }
        }
    }
    map.draw(antiNodes)
    println(antiNodes.size)

    val antiNodes2 = mutableSetOf<Position>()
    antennaMap.forEach { (_, positions) ->
        for (i in positions.indices) {
            for (j in positions.indices) {
                if (i == j) {
                    continue
                }
                val first = positions[i]
                val second = positions[j]
                val subAntiNodes = (first to second).validAntiNodesPart2(map)
                antiNodes2.addAll(subAntiNodes)
                antiNodes2.add(first)
                antiNodes2.add(second)
            }
        }
    }
    map.draw(antiNodes2)
    println(antiNodes2.size)
}

fun String.toDiskMap(): List<Int> {
    return this.mapIndexed { index, c ->
        val digit = c.toString().toInt()

        if (index % 2 == 0) {
            val fileIndex = (index / 2)
            MutableList(digit) { fileIndex }
        } else {
            MutableList(digit) { -1 }
        }
    }.flatten()
}

fun day9P12024() {
    val diskMap = 9.readDayInput().first()
    val maxFileIndex = floor(diskMap.length.toDouble() / 2).toLong()
    val fileMap =
        diskMap.filterIndexed { index, c -> index % 2 == 0 }
            .mapIndexed { fileIndex, c -> fileIndex to c.toString().toLong() }
            .groupBy { it.first }
    println(fileMap)
    println(maxFileIndex)

    val originalMap = diskMap.toDiskMap()
    println(originalMap)

    var cnt = 0
    var endCnt = originalMap.size - 1
    val result = mutableListOf<Long>()
    while (cnt <= endCnt) {
        val cell = originalMap[cnt]
        if (cell == -1) {
            var endCell: Int = -1
            while (endCell == -1) {
                endCell = originalMap[endCnt]
                endCnt--
            }
            if (cnt > endCnt) {
                break;
            }
            result.add(endCell.toLong())
        } else {
            result.add(cell.toLong())
        }
        cnt++
    }


    println(result.joinToString(","))
    var sum = BigInteger.ZERO
    result.withIndex().forEach { (index, digit) ->
        println("[$index * $digit]")
        sum = sum.add(BigInteger.valueOf(index.toLong()).times(BigInteger.valueOf(digit)))
    }
    println(sum)
} // 6398608069280

fun List<Pair<Int, Int>>.draw() = this.forEach { (fileIndex, size) ->
    repeat(size) {
        if (fileIndex == -1) {
            print('.')
        } else {
            print(fileIndex)
        }
    }
}

data class EmptyNode(
    val index: Int,
    val size: Int,
) {
    override fun toString(): String {
        return "($index, $size)"
    }
}

data class Node(
    val index: Int,
    val size: Int,
    val fileIndex: Int,
) {
    override fun toString(): String {
        return "($index, $size, $fileIndex)"
    }
}

fun Pair<List<Node>, List<EmptyNode>>.draw() {
    val (nodes, emptyNodes) = this
    val lastNode = nodes.last()
    val emptyLast = emptyNodes.last()
    val max = Math.max(lastNode.index + lastNode.size, emptyLast.index + emptyLast.size)

    val line = MutableList(max) { "_" }
    for (node in nodes) {
        repeat(node.size) { i ->
            line[node.index + i] = node.fileIndex.toString()
        }
    }
    for (emptyNode in emptyNodes) {
        repeat(emptyNode.size) { i ->
            line[emptyNode.index + i] = "."
        }
    }
    println(line.joinToString(""))
}

fun day9P22024_retry() {
    val diskMap = 9.readDayInput().first()

    val nodeList = LinkedList<Node>()
    val emptyNodeList = LinkedList<EmptyNode>()
    var realIndex = 0
    diskMap.forEachIndexed { index, c ->
        val size = c.toString().toInt()

        if (index % 2 == 0) {
            val fileIndex = (index / 2)
            val node = Node(realIndex, size, fileIndex)
            nodeList.add(node)
        } else {
            val emptyNode = EmptyNode(realIndex, size)
            if (size > 0) {
                emptyNodeList.add(emptyNode)
            }
        }
        realIndex += size
    }
    println(nodeList)
    println(emptyNodeList)
    (nodeList to emptyNodeList).draw()

    val cache = mutableSetOf<Int>()
    nodeList.reversed().forEach { node ->
        println(node.fileIndex)
        cache.add(node.fileIndex)
        emptyNodeList.firstOrNull { it.index < node.index && it.size >= node.size }?.let { emptyNode ->
            val emptyNodeIndex = emptyNodeList.indexOf(emptyNode)
            emptyNodeList.remove(emptyNode)
            if (emptyNode.size > node.size) {
                emptyNodeList.add(emptyNodeIndex, EmptyNode(emptyNode.index + node.size, emptyNode.size  - node.size))
            }
            emptyNodeList.add(EmptyNode(node.index, node.size))

            val nextNodeIndex = nodeList.indexOfFirst { it.index > emptyNode.index }
            nodeList.remove(node)
            nodeList.add(nextNodeIndex, node.copy(index = emptyNode.index))
        }
        emptyNodeList.sortBy { it.index }
//        println(nodeList)
//        (nodeList to emptyNodeList).draw()
    }
    println(nodeList)
    println(emptyNodeList.sortedBy { it.index })

    var sum = BigInteger.ZERO
    nodeList.forEach { node ->
        repeat(node.size) { i ->
            sum = sum.add(BigInteger.valueOf((node.index + i).toLong()).times(BigInteger.valueOf(node.fileIndex.toLong())))
        }
    }
    println(sum)
}

// Big ooefff, doesnt work
fun day9P22024() {
    val diskMap = 9.readDayInput().first()
    val originalMap = diskMap.mapIndexed { index, c ->
        val digit = c.toString().toInt()

        if (index % 2 == 0) {
            val fileIndex = (index / 2)
            fileIndex to digit
        } else {
            -1 to digit
        }
    }
    println(originalMap)
    originalMap.draw()
    println()
    println()

    val updatedMap = LinkedList(originalMap)
    var cnt = 0
    var endCnt = originalMap.size - 1
    val visited = mutableSetOf<Int>()
    val sizeAvailable = HashMap(MutableList(9) { index -> index + 1 }.associateWith { true })
    while (cnt <= endCnt) {
        var swapped = false
        val (fileIndex, size) = updatedMap[endCnt]
        if (fileIndex != -1 && (fileIndex !in visited)) {
            if (sizeAvailable[size]!!) {
                println("no available size: $size")
                endCnt--
                continue
            }
            visited.add(fileIndex)
            var startCell = 0
            var startCellSize = 0
            while (startCell != -1 || startCellSize < size) {
                if (cnt >= endCnt) {
                    break
                }
                val startCellPair = updatedMap[cnt]
                startCell = startCellPair.first
                startCellSize = startCellPair.second
                cnt++
            }

            if (cnt < endCnt) {
                swapped = true
                val remaining = startCellSize - size
                val updatedEmptySpace = -1 to remaining
                val usedEmptySpace = -1 to size
                // order important
                updatedMap.removeAt(endCnt)
                val freePrev = updatedMap[endCnt - 1]
                if (freePrev.first == -1) {
                    updatedMap[endCnt - 1] = freePrev.first to freePrev.second + size
                } else {
                    updatedMap.add(endCnt, usedEmptySpace)
                }
                updatedMap.removeAt(cnt - 1)
                if (updatedEmptySpace.second > 0) {
                    val freeNext = updatedMap[cnt - 1]
                    if (freeNext.first == -1) {
                        updatedMap[cnt - 1] = freeNext.first to freeNext.second + remaining
                    } else {
                        updatedMap.add(cnt - 1, updatedEmptySpace)
                    }
                }
                updatedMap.add(cnt - 1, fileIndex to size)
            }
            println("$fileIndex,$size, swapped: $swapped")

            cnt = updatedMap.indexOfFirst { it.first == -1 }
            if (cnt > endCnt) {
                break
            }
            if (cnt == -1) {
                break
            }
    //        println(updatedMap)
//            updatedMap.draw()
//            println()
        }
        if (!swapped) {
            sizeAvailable[size] = false
        }
        endCnt--
        if (cnt > endCnt) {
            break
        }
//        println("$endCnt;")
    }
//    println(updatedMap)
//    updatedMap.draw()
    var sum = BigInteger.ZERO
    updatedMap.map { (fileIndex, size) ->
        MutableList(size) { fileIndex.toLong() }
    }.flatten().withIndex().forEach { (index, digit) ->
        if (digit > -1) {
//            println("[$index * $digit]")
            sum = sum.add(BigInteger.valueOf(index.toLong()).times(BigInteger.valueOf(digit)))
        }
    }
//    println("[$index * $digit]")
//    sum = sum.add(BigInteger.valueOf(index.toLong()).times(BigInteger.valueOf(digit)))
    println(sum)
}

fun Long.evolve(): List<Long> {
    return if (this == 0L) {
        listOf(1L)
    } else if (this.toString().length % 2 == 0) {
        val stoneStr = this.toString()
        val half = stoneStr.length / 2
        val first = stoneStr.substring(0, half).toLong()
        val second = stoneStr.substring(half, stoneStr.length).toLong()
        listOf(first, second)
    } else {
        listOf(this * 2024)
    }
}

fun BigInteger.evolve(): List<BigInteger> {
    return if (this == BigInteger.ZERO) {
        listOf(BigInteger.ONE)
    } else if (this.toString().length % 2 == 0) {
        val stoneStr = this.toString()
        val half = stoneStr.length / 2
        val first = stoneStr.substring(0, half).toBigInteger()
        val second = stoneStr.substring(half, stoneStr.length).toBigInteger()
        listOf(first, second)
    } else {
        listOf(this.times(BigInteger.valueOf(2024)))
    }
}

fun day112024(part2: Boolean = true) {
    val stones = 11.readDayInput().first().split(" ").map(String::toLong)
    if (!part2) {
        var updatedStoned = stones
        repeat(40) { index ->
            updatedStoned = updatedStoned.map { stone -> stone.evolve() }.flatten()
//            println("$index: ${updatedStoned.sorted()}")
        }
        println(updatedStoned.size)
    }

    if (part2) {
        var cache = stones.groupingBy { it }.eachCount().map { (stone, cnt) -> stone.toBigInteger() to cnt.toBigInteger() }.toMap().toMutableMap()
        repeat(75) { index ->
            val updatedCache = cache.toMutableMap()
            cache.forEach { (stone, stoneCount) ->
                if (updatedCache[stone]!! == stoneCount) {
                    updatedCache.remove(stone)
                } else {
                    updatedCache[stone] = updatedCache[stone]!! - stoneCount
                }
                val evolved = stone.evolve()
                evolved.forEach { evolvedStone ->
                    val oldCount = updatedCache.getOrDefault(evolvedStone, BigInteger.ZERO)
                    updatedCache[evolvedStone] = oldCount + stoneCount
                }
            }
            cache = updatedCache
            println("$index: ${cache.toSortedMap()}")
            //println(cache.values.sum())
        }
        // 22938365706844 -> prob overflow? -> no I'm stupid, wrong input :(
        // 255758646442399 -> correct
        var result = BigInteger.ZERO
        cache.values.forEach { value -> result = result.add(value) }
        println(result)
    }
}

fun day132024(part2: Boolean = false) {
    //ax, ay
    //bx, by
    //
    //k*ax + i*bx = X
    //k*ay + i*by = Y
    //
    //k*by*ax + i*bx*by = X*by
    //k*bx*ay + i*bx*by = Y*bx
    //
    //(by*ax - bx*ay)k = X*by - Y*bx
    //k = (X*by - Y*bx)/(by*ax - bx*ay)
    //i = (X - k*ax)/bx

    val offset = 10000000000000L
    val tokensNeeded = 13.readDayInput()
        .filter { it.isNotEmpty() }
        .windowed(3, 3)
        .map { (buttonA, buttonB, prize) ->
            val (ax, ay) = buttonA.split(", Y+").map {
                it.replace("Button A: X+", "").toLong()
            }

            val (bx, by) = buttonB.split(", Y+").map {
                it.replace("Button B: X+", "").toLong()
            }

            val (x, y) = prize.split(", Y=").map {
                val number = it.replace("Prize: X=", "").toLong()
                if (part2) {
                    number + offset
                } else {
                    number
                }
            }
            println("$ax $ay")
            println("$bx $by")
            println("$x $y")

            val divisor = by*ax - bx*ay
            if (divisor == 0L || bx == 0L) {
                println("Skipped, zero division")
                println()
                return@map 0L
            }
            val k = (Math.multiplyExact(x, by) - Math.multiplyExact(y, bx))/divisor
            val i = (x - Math.multiplyExact(k, ax))/bx
            if (!part2 && (k > 100 || i > 100)) {
                println("Skipped, too many tries")
                println()
                return@map 0L
            }
            if (k <= 0 || i <= 0) {
                println("Skipped")
                println()
                return@map 0L
            }
            if (k*ax + i*bx != x || k*ay + i*by != y) {
                println("Skipped non divisible")
                println()
                return@map 0L
            }
            println("A needs to be pressed: $k; B needs to be pressed: $i")
            println()
            3*k + i
        }.sum()
    println(tokensNeeded)
}

fun Position.normalise(maxRow: Int, maxColumn: Int): Position {
    var newRow = this.row
    var newColumn = this.column
    if (this.row < 0) {
        newRow = maxRow + this.row
    }
    if (this.row >= maxRow) {
        newRow = this.row - maxRow
    }
    if (this.column < 0) {
        newColumn = maxColumn + this.column
    }
    if (this.column >= maxColumn) {
        newColumn = this.column - maxColumn
    }
    return Position(newRow, newColumn)
}

fun Position.moveRobot(vector: Vector, roomWidth: Int = 11, roomHeight: Int = 7) =
    this.add(vector = vector).normalise(roomHeight, roomWidth)

fun List<String>.drawTo(traversed: Set<Position>, out: PrintWriter) {
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, column ->
            if (Position(rowIndex, columnIndex) in traversed) {
                out.print('X')
            } else {
                out.print(column)
            }
        }
        out.println()
    }
    out.println()
}

fun day142024(part2: Boolean = false) {
    val width = 101
    val height = 103
    val robots = 14.readDayInput().map { input ->
        val (robot, vel) = input.split(" v=")
        val (robotX, robotY) = robot.replace("p=", "").split(",").map { it.toInt() }
        val robotPosition = Position(robotY, robotX)

        val (velX, velY) = vel.split(",").map { it.toInt() }
        val velocity = Vector(velY, velX)
        robotPosition to velocity
    }
    var updatedRobots = robots
    if (!part2) {
        repeat(100) {
            updatedRobots = updatedRobots.map { (position, vector) ->
                position.moveRobot(vector, roomWidth = width, roomHeight = height) to vector
            }
        }
        println(updatedRobots)
        val topLeft = updatedRobots.filter { (position, _) ->
            position.row < (height / 2) && position.column < (width / 2)
        }.map { (first, _) -> first }
        val topRight = updatedRobots.filter { (position, _) ->
            position.row < (height / 2) && position.column > (width / 2)
        }.map { (first, _) -> first }
        val bottomLeft = updatedRobots.filter { (position, _) ->
            position.row > (height / 2) && position.column < (width / 2)
        }.map { (first, _) -> first }
        val bottomRight = updatedRobots.filter { (position, _) ->
            position.row > (height / 2) && position.column > (width / 2)
        }.map { (first, _) -> first }
        println()
        println(topLeft)
        println(topRight)
        println(bottomLeft)
        println(bottomRight)
        println(topLeft.size * topRight.size * bottomLeft.size * bottomRight.size)
    } else {
        val widthStr = ".".repeat(width)
        val map = MutableList(height) { widthStr }
        var seconds = 1

        var uniquePlacements = 0
        while (uniquePlacements != robots.size) {
            updatedRobots = updatedRobots.map { (position, vector) ->
                position.moveRobot(vector, roomWidth = width, roomHeight = height) to vector
            }
            val uniquePositions = updatedRobots.map { (first, _) -> first }.toSet()
            uniquePlacements = uniquePositions.size
            println("Seconds $seconds; Unique placements: $uniquePlacements")
            if (uniquePlacements == robots.size) {
                map.draw(uniquePositions)
            }
            seconds++
        }
    }
}

fun Position?.orIsValidForDay15(map: List<String>) = this == null || this.isValidForDay15(map)

fun Position.isValidForDay15(map: List<String>) =
    row > 0 && row < map.size - 1
            && column > 0
            && column < map.first().length - 1
            && map[row][column] != '#'

fun day15P12024() {
    val (map, commandsRaw) = 15.readDayInput().filter { it.isNotEmpty() }.partition { it.contains("#") }

    val commands = commandsRaw.joinToString("").map { Direction.fromCharacter(it) }
    var robot: Position? = null
    val obstacles = mutableSetOf<Position>()
    val walls = mutableSetOf<Position>()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, c ->
            if (c == '@') {
                robot = Position(rowIndex, colIndex)
            }
            if (c == 'O') {
                obstacles.add(Position(rowIndex, colIndex))
            }
            if (c == '#') {
                walls.add(Position(rowIndex, colIndex))
            }
        }
    }

    val widthStr = ".".repeat(map.first().length)
    val emptyMap = MutableList(map.size) { widthStr }

    robot?.let { robotPosition ->
        var updatedRobotPosition = robotPosition
        commands.forEach { direction ->
            println(direction)
//            emptyMap.draw(walls, obstacles, updatedRobotPosition)
            val updatedPos = updatedRobotPosition.moveDirection(direction)
            if (!updatedPos.isValidForDay15(map)) {
                return@forEach
            }
            if (updatedPos in obstacles) {
                val obstaclesInFront = mutableSetOf(updatedPos)
                var obstacleFound = true
                var nextPos = updatedPos
                while (obstacleFound) {
                    nextPos = nextPos.moveDirection(direction)
                    if (!nextPos.isValidForDay15(map)) {
                        return@forEach
                    } else {
                        if (nextPos in obstacles) {
                            obstaclesInFront.add(nextPos)
                        } else {
                            obstacleFound = false
                        }
                    }
                }
                obstacles.removeAll(obstaclesInFront)
                obstacles.addAll(obstaclesInFront.map { it.moveDirection(direction) })
            }
            updatedRobotPosition = updatedPos
        }
        emptyMap.draw(walls, obstacles, updatedRobotPosition)
        println(updatedRobotPosition)

        val result = obstacles.sumOf { obstacle -> obstacle.row * 100 + obstacle.column }
        println(result)
    }
}

fun Position.inObstacle(obstacles: Set<Pair<Position, Position>>) = obstacles.find { (start, end) ->
    this == start || end == this
}

fun Pair<Position, Position>.getOpposite(of: Position): Position {
    val (first, second) = this
    return if (first == of) {
        second
    } else if (second == of) {
        first
    } else {
        throw RuntimeException("Parameter should exist in pair")
    }
}

class HitAWallException() : Exception()

fun Pair<Position, Position>.findNextObstacles(map: List<String>, obstacles: Set<Pair<Position, Position>>, direction: Direction): Set<Pair<Position, Position>> {
    val (left, right) = this
    return when (direction) {
        Direction.RIGHT -> {
            val nextPos = right.moveDirection(direction)
            if (!nextPos.isValidForDay15(map)) {
                throw HitAWallException()
            }
            val nextObstacle = nextPos.inObstacle(obstacles)
            val nextObstacles = nextObstacle?.findNextObstacles(map, obstacles, direction) ?: setOf()
            setOf(this) + nextObstacles
        }
        Direction.LEFT -> {
            val nextPos = left.moveDirection(direction)
            if (!nextPos.isValidForDay15(map)) {
                throw HitAWallException()
            }
            val nextObstacle = nextPos.inObstacle(obstacles)
            val nextObstacles = nextObstacle?.findNextObstacles(map, obstacles, direction) ?: setOf()
            setOf(this) + nextObstacles
        }
        Direction.UP -> {
            val nextPosRight = right.moveDirection(direction)
            val nextPosLeft = left.moveDirection(direction)
            if (!nextPosRight.isValidForDay15(map) || !nextPosLeft.isValidForDay15(map)) {
                throw HitAWallException()
            }
            val nextObstacleRight = nextPosRight.inObstacle(obstacles)
            val nextObstacleLeft = nextPosLeft.inObstacle(obstacles)
            val nextObstaclesRight = nextObstacleRight?.findNextObstacles(map, obstacles, direction) ?: setOf()
            val nextObstaclesLeft = nextObstacleLeft?.findNextObstacles(map, obstacles, direction) ?: setOf()
            setOf(this) + nextObstaclesRight + nextObstaclesLeft
        }
        Direction.DOWN -> {
            val nextPosRight = right.moveDirection(direction)
            val nextPosLeft = left.moveDirection(direction)
            if (!nextPosRight.isValidForDay15(map) || !nextPosLeft.isValidForDay15(map)) {
                throw HitAWallException()
            }
            val nextObstacleRight = nextPosRight.inObstacle(obstacles)
            val nextObstacleLeft = nextPosLeft.inObstacle(obstacles)
            val nextObstaclesRight = nextObstacleRight?.findNextObstacles(map, obstacles, direction) ?: setOf()
            val nextObstaclesLeft = nextObstacleLeft?.findNextObstacles(map, obstacles, direction) ?: setOf()
            setOf(this) + nextObstaclesRight + nextObstaclesLeft
        }
    }
}

fun day15P22024() {
    val (originalMap, commandsRaw) = 15.readDayInput().filter { it.isNotEmpty() }.partition { it.contains("#") }

    val map = originalMap.map { row ->
        row.map { c ->
            when (c) {
                '#' -> "##"
                'O' -> "[]"
                '@' -> "@."
                else -> ".."
            }
        }.joinToString("")
    }

    val commands = commandsRaw.joinToString("").map { Direction.fromCharacter(it) }
    var robot: Position? = null
    val obstacles = mutableSetOf<Pair<Position, Position>>()
    val walls = mutableSetOf<Position>()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed rowForEach@{ colIndex, c ->
            if (c == '@') {
                robot = Position(rowIndex, colIndex)
            }
            if (c == '[') {
                obstacles.add(Position(rowIndex, colIndex) to Position(rowIndex, colIndex + 1))
            }
            if (c == '#') {
                walls.add(Position(rowIndex, colIndex))
            }
        }
    }

    val widthStr = ".".repeat(map.first().length)
    val emptyMap = MutableList(map.size) { widthStr }

    var foundLast = false
    var moved = false
    robot?.let { robotPosition ->
        var updatedRobotPosition = robotPosition
        commands.forEach { direction ->
            println(direction)
//            emptyMap.drawUpdated(walls, obstacles, updatedRobotPosition)
            val updatedPos = updatedRobotPosition.moveDirection(direction)
            if (!updatedPos.isValidForDay15(map)) {
                return@forEach
            }
            if (foundLast && moved) {
                1
            }
            moved = false
            try {
                val foundObstacles = updatedPos.inObstacle(obstacles)?.findNextObstacles(map, obstacles, direction)
                foundObstacles?.let { found ->
                    obstacles.removeAll(found)
                    obstacles.addAll(found.map { (start, end) ->
                        start.moveDirection(direction) to end.moveDirection(
                            direction
                        )
                    })
                }
                updatedRobotPosition = updatedPos
            } catch (e: HitAWallException) {
                return@forEach
            }
        }
        emptyMap.drawUpdated(walls, obstacles, updatedRobotPosition)
        val result = obstacles.sumOf { (first, second) -> first.row * 100 + first.column }
        println(result)
    }
}