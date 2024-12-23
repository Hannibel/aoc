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
    // Part 1
    println(valid)
    // Part 2
    println(validSecond)
}