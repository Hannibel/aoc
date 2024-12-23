package other

import java.io.File
import kotlin.math.absoluteValue

fun main(args: Array<String>) {
//    dayFrom2024(1, 1)
//    day1P2()
//    day10()
    day12()
}

fun day5() {

    val file = File("src/main/resources/5.txt").readLines()

    val separatorIndex = file.withIndex().find { it.value.isBlank() }!!.index
    val columnNumber = file[separatorIndex - 1].last().toString().toInt()

    var columns = MutableList(columnNumber) { "" }
    file.subList(0, separatorIndex - 1).forEach { line ->
        for (i in 0 until columnNumber) {
            line.getOrNull(i * 4 + 1)?.takeIf { it != ' ' }?.let { c ->
                val column = columns[i]
                columns[i] = column + c
            }
        }
    }
    columns = columns.map { it.reversed() }.toMutableList()

    val moves = file.subList(separatorIndex + 1, file.size).map { line ->
        val split = line.split(" ")
        val move = split[1].toInt()
        val from = split[3].toInt() - 1
        val to = split[5].toInt() - 1
        listOf(move, from, to)
    }

    moves.forEach { (move, from, to) ->
        val fromC = columns[from]
        val toMove = fromC.takeLast(move).reversed()
        columns[from] = fromC.take(fromC.length - move)
        columns[to] = columns[to] + toMove
    }

    println(columns.map { it.last() }.joinToString(""))
//    println(moves)
}

fun day5P2() {

    val file = File("src/main/resources/5.txt").readLines()

    val separatorIndex = file.withIndex().find { it.value.isBlank() }!!.index
    val columnNumber = file[separatorIndex - 1].last().toString().toInt()

    var columns = MutableList(columnNumber) { "" }
    file.subList(0, separatorIndex - 1).forEach { line ->
        for (i in 0 until columnNumber) {
            line.getOrNull(i * 4 + 1)?.takeIf { it != ' ' }?.let { c ->
                val column = columns[i]
                columns[i] = column + c
            }
        }
    }
    columns = columns.map { it.reversed() }.toMutableList()

    val moves = file.subList(separatorIndex + 1, file.size).map { line ->
        val split = line.split(" ")
        val move = split[1].toInt()
        val from = split[3].toInt() - 1
        val to = split[5].toInt() - 1
        listOf(move, from, to)
    }

    moves.forEach { (move, from, to) ->
        val fromC = columns[from]
        val toMove = fromC.takeLast(move)
        columns[from] = fromC.take(fromC.length - move)
        columns[to] = columns[to] + toMove
    }

    println(columns.map { it.last() }.joinToString(""))
//    println(moves)
}

fun day6() {

    val file = File("src/main/resources/6.txt").readLines()

    val reduced = file.map { line ->
        line.withIndex().find {
            if (it.index < 4) {
                false
            } else {
                line.substring(it.index - 4, it.index).toCharArray().toSet().size == 4
            }
        }?.index
    }

    println(reduced)
}

fun day6P2() {

    val file = File("src/main/resources/6.txt").readLines()

    val reduced = file.map { line ->
        line.withIndex().find {
            if (it.index < 14) {
                false
            } else {
                line.substring(it.index - 14, it.index).toCharArray().toSet().size == 14
            }
        }?.index
    }

    println(reduced)
}

//// 2024
fun Number.loadFrom2024() = File("src/main/resources/2024/$this.txt").readLines()

enum class Part {
    FIRST, SECOND
}

fun dayFrom2024(day: Number, part: Part) {
    when (day) {
//        1 ->
    }
}

fun day1P1() {

    val file = 1.loadFrom2024()

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

fun day1P2() {

    val file = 1.loadFrom2024()

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

data class Position(val row: Int, val col: Int)
enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    fun rotateRight(): Direction {
        return when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }
}

fun Position.up() = this.copy(row = row - 1, col = col)
fun Position.down() = this.copy(row = row + 1, col = col)
fun Position.right() = this.copy(row = row, col = col + 1)
fun Position.left() = this.copy(row = row, col = col - 1)
fun Position.isValid(map: List<String>) =
    this.row >= 0 && this.col >= 0 && this.row < map.size && this.col < map.first().length

fun List<String>.get(position: Position) = if (position.isValid(this)) {
    this[position.row][position.col]
} else null

fun Position.dfs(cache: Set<Position>, prev: Int, map: List<String>): List<Position> {
    if (this in cache) {
        return listOf()
    }
    val updatedCache = cache + setOf(this)
    return map.get(this)?.let { currentC ->
        val current = currentC.toString().toInt()
        if ((current - prev) != 1) {
            return listOf()
        }
        println(this)
        if (current.toString().toInt() == 9) {
            return listOf(this)
        }
        return this.up().dfs(updatedCache, current, map) +
                this.down().dfs(updatedCache, current, map) +
                this.left().dfs(updatedCache, current, map) +
                this.right().dfs(updatedCache, current, map)
    } ?: listOf()
}

//+-+-+-+-+
//|R R R R|
//+       +
//|R R R R|
//+-+-+   +-+
//|R R R|
//+ +-+-+
//|R|
//+-+

fun Position.dfsGetBorders(cache: MutableSet<Position>, prev: Char, map: List<String>): Long {
    if (this in cache) {
        return 0L
    }
//    println("Processing: $this")
    cache.add(this)
    return map.get(this)?.let { current ->
        if (current != prev && prev != '.') {
//            println("Border: $this")
            cache.remove(this)
            return 1L
        }
        return this.up().dfsGetBorders(cache, current, map) +
                this.down().dfsGetBorders(cache, current, map) +
                this.left().dfsGetBorders(cache, current, map) +
                this.right().dfsGetBorders(cache, current, map)
    } ?: run {
//        println("Out of border: $this")
        cache.remove(this)
        1
    }
}

fun Position.dfsGetArea(cache: MutableSet<Position>, prev: Char, map: List<String>) {
    if (this in cache) {
        return
    }
    map.get(this)?.let { current ->
        if (current != prev && prev != '.') {
            return
        }
        cache.add(this)
        this.up().dfsGetArea(cache, current, map)
        this.down().dfsGetArea(cache, current, map)
        this.left().dfsGetArea(cache, current, map)
        this.right().dfsGetArea(cache, current, map)
    }
}

fun day10() {
    val map = 10.loadFrom2024()

    val zeroStarts = mutableListOf<Position>()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, value ->
            if (value.toString().toInt() == 0) {
                zeroStarts.add(Position(rowIndex, colIndex))
            }
        }
    }

    val result = zeroStarts.map { it.dfs(setOf(), -1, map).toSet() }.flatten()
    val result2 = zeroStarts.map { it.dfs(setOf(), -1, map) }.flatten()

    println(result.size)
    println(result2.size)
}

fun Set<Position>.traverseBorder(start: Position) {
    var turns = 0
    var prevPosition = start
    var newPosition = start.up()
    var direction: Direction? = null
    while (newPosition != start || direction != Direction.UP) {
        if (direction == null) {
            direction = Direction.UP
        }
        if (newPosition !in this) {
            newPosition = prevPosition
            direction = direction.rotateRight()
            println("Turn $direction")
            turns++
        }
        prevPosition = newPosition
        newPosition = when (direction) {
            Direction.UP -> newPosition.up()
            Direction.RIGHT -> newPosition.right()
            Direction.DOWN -> newPosition.down()
            Direction.LEFT -> newPosition.left()
        }
        println("Position $prevPosition")
    }
}

fun day12() {
    val map = 12.loadFrom2024()

    var totalPrice = 0L
    val cache = mutableSetOf<Position>()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, value ->
            val position = Position(rowIndex, colIndex)
            if (position in cache) {
                return@forEachIndexed
            }
            val areaCache = mutableSetOf<Position>()
            position.dfsGetArea(areaCache, '.', map)
            cache.addAll(areaCache)
            val border = position.dfsGetBorders(mutableSetOf(), '.', map)
            val price = areaCache.size * border
            totalPrice += price
            println("price=$border*${areaCache.size}=$price")


//            val row = areaCache.groupBy { it.row }.map { (key, positions) ->
//                val sorted = positions.sortedBy { it.col }
//                val first = positions.first()
//                val reduced = sorted.windowed(2).mapNotNull { (a, b) -> if (a.col - b.col > 1) a to b else null  }
//                key to (listOf(first) + reduced).toSet()
//            }.toMap()
//            val col = areaCache.groupBy { it.col }
//            println()

            areaCache.traverseBorder(areaCache.first())
        }
    }
    println(totalPrice)
}

// 4
// ----
// -AA-
// ----


// 6
// ----
// -AA-
// -A--
// ----


// 8
// -----
// -AAA-
// -A-A-
// -----

