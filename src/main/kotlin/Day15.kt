fun day15P12024() {
    val (map, commandsRaw) = 15.readDayInput().filter { it.isNotEmpty() }.partition { it.contains("#") }

    val commands = commandsRaw.joinToString("").map { Direction.fromCharacter(it) }
    var robot: Position? = null
    val obstacles = mutableSetOf<Position>()
    val walls = mutableSetOf<Position>()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, c ->
            if (c == '@') {
                robot = Position(rowIndex, colIndex)
            }
            if (c == 'O') {
                obstacles.add(Position(rowIndex, colIndex))
            }
            if (c == '#') {
                walls.add(Position(rowIndex, colIndex))
            }
        }
    }

    val widthStr = ".".repeat(map.first().length)
    val emptyMap = MutableList(map.size) { widthStr }

    robot?.let { robotPosition ->
        var updatedRobotPosition = robotPosition
        commands.forEach { direction ->
            println(direction)
//            emptyMap.draw(walls, obstacles, updatedRobotPosition)
            val updatedPos = updatedRobotPosition.moveDirection(direction)
            if (!updatedPos.isValidForDay15(map)) {
                return@forEach
            }
            if (updatedPos in obstacles) {
                val obstaclesInFront = mutableSetOf(updatedPos)
                var obstacleFound = true
                var nextPos = updatedPos
                while (obstacleFound) {
                    nextPos = nextPos.moveDirection(direction)
                    if (!nextPos.isValidForDay15(map)) {
                        return@forEach
                    } else {
                        if (nextPos in obstacles) {
                            obstaclesInFront.add(nextPos)
                        } else {
                            obstacleFound = false
                        }
                    }
                }
                obstacles.removeAll(obstaclesInFront)
                obstacles.addAll(obstaclesInFront.map { it.moveDirection(direction) })
            }
            updatedRobotPosition = updatedPos
        }
        emptyMap.draw(walls, obstacles, updatedRobotPosition)
        println(updatedRobotPosition)

        val result = obstacles.sumOf { obstacle -> obstacle.row * 100 + obstacle.column }
        println(result)
    }
}

fun Position.isValidForDay15(map: List<String>) =
    row > 0 && row < map.size - 1
            && column > 0
            && column < map.first().length - 1
            && map[row][column] != '#'

// Part 2

fun day15P22024() {
    val (originalMap, commandsRaw) = 15.readDayInput().filter { it.isNotEmpty() }.partition { it.contains("#") }

    val map = originalMap.map { row ->
        row.map { c ->
            when (c) {
                '#' -> "##"
                'O' -> "[]"
                '@' -> "@."
                else -> ".."
            }
        }.joinToString("")
    }

    val commands = commandsRaw.joinToString("").map { Direction.fromCharacter(it) }
    var robot: Position? = null
    val obstacles = mutableSetOf<Pair<Position, Position>>()
    val walls = mutableSetOf<Position>()
    map.forEachIndexed { rowIndex, row ->
        row.forEachIndexed rowForEach@{ colIndex, c ->
            if (c == '@') {
                robot = Position(rowIndex, colIndex)
            }
            if (c == '[') {
                obstacles.add(Position(rowIndex, colIndex) to Position(rowIndex, colIndex + 1))
            }
            if (c == '#') {
                walls.add(Position(rowIndex, colIndex))
            }
        }
    }

    val widthStr = ".".repeat(map.first().length)
    val emptyMap = MutableList(map.size) { widthStr }

    var foundLast = false
    var moved = false
    robot?.let { robotPosition ->
        var updatedRobotPosition = robotPosition
        commands.forEach { direction ->
            println(direction)
//            emptyMap.drawUpdated(walls, obstacles, updatedRobotPosition)
            val updatedPos = updatedRobotPosition.moveDirection(direction)
            if (!updatedPos.isValidForDay15(map)) {
                return@forEach
            }
            if (foundLast && moved) {
                1
            }
            moved = false
            try {
                val foundObstacles = updatedPos.inObstacle(obstacles)?.findNextObstacles(map, obstacles, direction)
                foundObstacles?.let { found ->
                    obstacles.removeAll(found)
                    obstacles.addAll(found.map { (start, end) ->
                        start.moveDirection(direction) to end.moveDirection(
                            direction
                        )
                    })
                }
                updatedRobotPosition = updatedPos
            } catch (e: HitAWallException) {
                return@forEach
            }
        }
        emptyMap.drawUpdated(walls, obstacles, updatedRobotPosition)
        val result = obstacles.sumOf { (first, second) -> first.row * 100 + first.column }
        println(result)
    }
}

fun Position.inObstacle(obstacles: Set<Pair<Position, Position>>) = obstacles.find { (start, end) ->
    this == start || end == this
}

class HitAWallException() : Exception()

fun Pair<Position, Position>.findNextObstacles(map: List<String>, obstacles: Set<Pair<Position, Position>>, direction: Direction): Set<Pair<Position, Position>> {
    val (left, right) = this
    return when (direction) {
        Direction.RIGHT -> {
            val nextPos = right.moveDirection(direction)
            if (!nextPos.isValidForDay15(map)) {
                throw HitAWallException()
            }
            val nextObstacle = nextPos.inObstacle(obstacles)
            val nextObstacles = nextObstacle?.findNextObstacles(map, obstacles, direction) ?: setOf()
            setOf(this) + nextObstacles
        }
        Direction.LEFT -> {
            val nextPos = left.moveDirection(direction)
            if (!nextPos.isValidForDay15(map)) {
                throw HitAWallException()
            }
            val nextObstacle = nextPos.inObstacle(obstacles)
            val nextObstacles = nextObstacle?.findNextObstacles(map, obstacles, direction) ?: setOf()
            setOf(this) + nextObstacles
        }
        Direction.UP -> {
            val nextPosRight = right.moveDirection(direction)
            val nextPosLeft = left.moveDirection(direction)
            if (!nextPosRight.isValidForDay15(map) || !nextPosLeft.isValidForDay15(map)) {
                throw HitAWallException()
            }
            val nextObstacleRight = nextPosRight.inObstacle(obstacles)
            val nextObstacleLeft = nextPosLeft.inObstacle(obstacles)
            val nextObstaclesRight = nextObstacleRight?.findNextObstacles(map, obstacles, direction) ?: setOf()
            val nextObstaclesLeft = nextObstacleLeft?.findNextObstacles(map, obstacles, direction) ?: setOf()
            setOf(this) + nextObstaclesRight + nextObstaclesLeft
        }
        Direction.DOWN -> {
            val nextPosRight = right.moveDirection(direction)
            val nextPosLeft = left.moveDirection(direction)
            if (!nextPosRight.isValidForDay15(map) || !nextPosLeft.isValidForDay15(map)) {
                throw HitAWallException()
            }
            val nextObstacleRight = nextPosRight.inObstacle(obstacles)
            val nextObstacleLeft = nextPosLeft.inObstacle(obstacles)
            val nextObstaclesRight = nextObstacleRight?.findNextObstacles(map, obstacles, direction) ?: setOf()
            val nextObstaclesLeft = nextObstacleLeft?.findNextObstacles(map, obstacles, direction) ?: setOf()
            setOf(this) + nextObstaclesRight + nextObstaclesLeft
        }
    }
}

fun List<String>.drawUpdated(walls: Set<Position>, obstacles: Set<Pair<Position, Position>>, robot: Position) {
    val obstaclesStart = obstacles.map { it.first }
    var skip = false
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed rowForEach@{ columnIndex, column ->
            if (skip) {
                skip = false
                return@rowForEach
            }
            val pos = Position(rowIndex, columnIndex)
            if (pos in obstaclesStart) {
                print("[]")
                skip = true
            } else if (pos == robot) {
                print('@')
            } else if (pos in walls) {
                print('#')
            } else {
                print(column)
            }
        }
        println()
    }
    println()
}

fun List<String>.draw(walls: Set<Position>, obstacles: Set<Position>, robot: Position) {
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, column ->
            val pos = Position(rowIndex, columnIndex)
            if (pos in obstacles) {
                print('X')
            } else if (pos == robot) {
                print('@')
            } else if (pos in walls) {
                print('#')
            } else {
                print(column)
            }
        }
        println()
    }
    println()
}
