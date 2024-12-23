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

// ----------- P2 ------------------

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