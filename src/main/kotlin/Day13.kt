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