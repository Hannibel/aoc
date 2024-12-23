fun Pair<Position, Position>.validAntiNodes(map: List<String>): Set<Position> {
    // #...
    // .a..
    // ..a.
    // ...#
    // a(1,1) a(2,2)
    // #(0,0) #(3,3)
    val (first, second) = this
    val offset = Vector (first.row - second.row, first.column - second.column)
    val antiNode1 = first.add(offset).takeIf { it.isValid(map) }
    val antiNode2 = second.add(offset.inverse()).takeIf { it.isValid(map) }
    return setOfNotNull(antiNode1, antiNode2)
}

fun Pair<Position, Position>.validAntiNodesPart2(map: List<String>): Set<Position> {
    var (firstNext, secondNext) = this
    val offset = Vector (first.row - second.row, first.column - second.column)

    val antiNodes = mutableSetOf<Position>()
    var isValid1 = true
    var isValid2 = true
    while (isValid1 || isValid2) {
        val antiNode1 = firstNext.add(offset)
        val antiNode2 = secondNext.add(offset.inverse())
        isValid1 = antiNode1.isValid(map)
        isValid2 = antiNode2.isValid(map)
        if (isValid1) {
            antiNodes.add(antiNode1)
        }
        if (isValid2) {
            antiNodes.add(antiNode2)
        }
        firstNext = antiNode1
        secondNext = antiNode2
    }
//    println("_${antiNodes.size}")
    return antiNodes
}

fun day82024() {
    val map = 8.readDayInput()

    val obstructed = mutableSetOf<Position>()
    val antennaMap = mutableMapOf<Char, List<Position>>()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, cell ->
            if (cell != '.') {
                val position = Position(rowIndex, columnIndex)
                obstructed.add(position)
                val updated = antennaMap.getOrDefault(cell, setOf()).toMutableList()
                updated.add(position)
                antennaMap[cell] = updated
            }
        }
    }
    val antiNodes = mutableSetOf<Position>()
    antennaMap.forEach { (_, positions) ->
        for (i in positions.indices) {
            for (j in positions.indices) {
                if (i == j) {
                    continue
                }
                val first = positions[i]
                val second = positions[j]
                val subAntiNodes = (first to second).validAntiNodes(map)
                antiNodes.addAll(subAntiNodes)
            }
        }
    }
//    map.draw(antiNodes)
    // Part 1 result
    println(antiNodes.size)

    val antiNodes2 = mutableSetOf<Position>()
    antennaMap.forEach { (_, positions) ->
        for (i in positions.indices) {
            for (j in positions.indices) {
                if (i == j) {
                    continue
                }
                val first = positions[i]
                val second = positions[j]
                val subAntiNodes = (first to second).validAntiNodesPart2(map)
                antiNodes2.addAll(subAntiNodes)
                antiNodes2.add(first)
                antiNodes2.add(second)
            }
        }
    }
//    map.draw(antiNodes2)
    // Part 2 result
    println(antiNodes2.size)
}