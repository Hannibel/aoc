import java.util.*
import kotlin.math.floor

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
