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