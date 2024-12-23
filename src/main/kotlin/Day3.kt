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