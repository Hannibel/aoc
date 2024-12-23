
internal fun Int.readDayInput() = readFile("2024/$this")

class AOC2024 {
    fun execute() {
//        day1P12024()
//        day1P22024()
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
//        day112024(false)
//        day112024()
//        day132024()
//        day132024(true)
//        day142024()
//        day142024(true)
//        day15P12024()
//        day15P22024()
    }
}

fun List<Int>.isSafe(isIncreasing: Boolean) = this.windowed(2).all { (a, b) ->
    if (isIncreasing) {
        a < b && (b - a) <= 3
    } else {
        a > b && (a - b) <= 3
    }
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