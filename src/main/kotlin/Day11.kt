import java.math.BigInteger

fun day112024(part2: Boolean = true) {
    val stones = 11.readDayInput().first().split(" ").map(String::toLong)
    if (!part2) {
        var updatedStoned = stones
        repeat(25) { index ->
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
//            println("$index: ${cache.toSortedMap()}")
            //println(cache.values.sum())
        }
        // 22938365706844 -> prob overflow? -> no I'm stupid, wrong input :(
        // 255758646442399 -> correct
        var result = BigInteger.ZERO
        cache.values.forEach { value -> result = result.add(value) }
        println(result)
    }
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