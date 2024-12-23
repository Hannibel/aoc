package day10

import other.loadFrom2024

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