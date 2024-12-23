import java.util.*

fun day6P2024() {
    val map = 6.readDayInput()

    val guardDirections = Direction.values().map { it.repr }
    var guardPosition: Position? = null
    var guardDirection: Direction? = null
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed row@{ columnIndex, cell ->
            if (cell in guardDirections) {
                guardPosition = Position(rowIndex, columnIndex)
                guardDirection = Direction.fromCharacter(map.getCharInPos(Position(rowIndex, columnIndex)))
                return@row
            }
        }
    }
    guardPosition?.let { pos ->
        guardDirection?.let { direction ->
            val traversed = map.moveGuard(pos, direction)
//            map.draw(traversed)
            // Part 1 result
            println(traversed.size)
        }
    } ?: println("No guard position found")
}

fun List<String>.moveGuard(guardPosition: Position, guardDirection: Direction): Set<Position> {
    val traversed = mutableSetOf<Position>()
    val traversedOrder = LinkedList<Pair<Position, Direction>>()
    var doneWalking = false

    var direction = guardDirection
    var newPosition = guardPosition

    while (!doneWalking) {
        var obstructed = true
        var obstructedCnt = 0
        traversed.add(newPosition)
        traversedOrder.add(newPosition to direction)
        while (obstructed) {
            val prevPosition = newPosition
            newPosition = newPosition.moveDirection(direction)
            if (!newPosition.isValid(this)) {
                obstructed = false
                doneWalking = true
                continue
            }
            val newPositionChar = this.getCharInPos(newPosition)
            if (newPositionChar == '#') {
                newPosition = prevPosition
                direction = direction.rotateClockWise()
                traversedOrder.removeLast()
                traversedOrder.add(newPosition to direction)
                obstructedCnt++
            } else {
                obstructed = false
            }
        }
        if (obstructedCnt > 1) {
            println("more than once")
        }
    }

    this.calculateLoops(guardPosition, guardDirection, traversedOrder)

    return traversed
}

fun Pair<Position, Direction>.nextUntilObstacle(obstructions: Set<Position>): Pair<Position, Direction>? {
    val (position, direction) = this

    val rowMap = obstructions
        .groupBy { it.row }
        .map { (key, value) ->
            key to value.map { it.column }.sorted()
        }.toMap()

    val columnMap = obstructions
        .groupBy { it.column }
        .map { (key, value) ->
            key to value.map { it.row }.sorted()
        }.toMap()

    return when (direction.rotateClockWise()) {
        Direction.RIGHT -> {
            rowMap[position.row]
                ?.firstOrNull { column -> position.column < column }
                ?.let { column -> Position(position.row, column - 1) to Direction.RIGHT }
        }
        Direction.LEFT -> {
            rowMap[position.row]
                ?.lastOrNull { column -> position.column > column }
                ?.let { column -> Position(position.row, column + 1) to Direction.LEFT }
        }
        Direction.UP -> {
            columnMap[position.column]
                ?.lastOrNull { row -> position.row > row }
                ?.let { row -> Position(row + 1, position.column) to Direction.UP }
        }
        Direction.DOWN -> {
            columnMap[position.column]
                ?.firstOrNull { row -> position.row < row }
                ?.let { row -> Position(row - 1, position.column) to Direction.DOWN }
        }
    }
}

fun List<String>.calculateLoops(guardPosition: Position, guardDirection: Direction, traversedOrder: LinkedList<Pair<Position, Direction>>) {
    val obstructions = mutableSetOf<Position>()
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, column ->
            val position = Position(rowIndex, columnIndex)
            if (this.getCharInPos(position) == '#') {
                obstructions.add(position)
            }
        }
    }

    val validLoopObstacles = mutableSetOf<Position>()
    val cache = mutableSetOf<Position>()
    // check for loops
    var cnt = traversedOrder.size
    traversedOrder.forEach { (position, direction) ->
        cnt--
        if (cnt % 100 == 0) {
            println("To go: $cnt")
        }

        cache.add(position)
        val potentialObstacle = position.moveDirection(direction)
        if (potentialObstacle in cache) {
            return@forEach
        }

        val modifiedObstructions = obstructions.toMutableSet()
        modifiedObstructions.add(potentialObstacle)

        var turn: Position? = position
        var turnDirection: Direction? = direction
        var obstaclesHit = 1
        val turnsTaken = mutableSetOf<Position>()
        while (turn != null) {
            turn.let { t ->
                turnDirection?.let { d ->
                    turnsTaken.add(t)
                    val next = (t to d).nextUntilObstacle(modifiedObstructions)
                    obstaclesHit++
                    turn = next?.first
                    turnDirection = next?.second

                    if (turn in turnsTaken && turn != t) {
                        validLoopObstacles.add(potentialObstacle)
                        turn = null
                    }
                }
            }
        }
    }
    this.draw(traversedOrder, validLoopObstacles)
    // Part 2 result
    println(validLoopObstacles.size)
}

fun List<String>.draw(traversedOrder: LinkedList<Pair<Position, Direction>>, loopObstacles: Set<Position>) {
    val traversed = traversedOrder.groupBy { it.first }
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, column ->
            if (Position(rowIndex, columnIndex) in loopObstacles) {
                print('O')
            } else if (traversed.contains(Position(rowIndex, columnIndex))) {
                print(traversed[Position(rowIndex, columnIndex)]!!.first().second.repr)
            } else {
                print(column)
            }
        }
        println()
    }
    println()
}
