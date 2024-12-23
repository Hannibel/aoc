fun day142024(part2: Boolean = false) {
    val width = 101
    val height = 103
    val robots = 14.readDayInput().map { input ->
        val (robot, vel) = input.split(" v=")
        val (robotX, robotY) = robot.replace("p=", "").split(",").map { it.toInt() }
        val robotPosition = Position(robotY, robotX)

        val (velX, velY) = vel.split(",").map { it.toInt() }
        val velocity = Vector(velY, velX)
        robotPosition to velocity
    }
    var updatedRobots = robots
    if (!part2) {
        repeat(100) {
            updatedRobots = updatedRobots.map { (position, vector) ->
                position.moveRobot(vector, roomWidth = width, roomHeight = height) to vector
            }
        }
        println(updatedRobots)
        val topLeft = updatedRobots.filter { (position, _) ->
            position.row < (height / 2) && position.column < (width / 2)
        }.map { (first, _) -> first }
        val topRight = updatedRobots.filter { (position, _) ->
            position.row < (height / 2) && position.column > (width / 2)
        }.map { (first, _) -> first }
        val bottomLeft = updatedRobots.filter { (position, _) ->
            position.row > (height / 2) && position.column < (width / 2)
        }.map { (first, _) -> first }
        val bottomRight = updatedRobots.filter { (position, _) ->
            position.row > (height / 2) && position.column > (width / 2)
        }.map { (first, _) -> first }
        println()
        println(topLeft)
        println(topRight)
        println(bottomLeft)
        println(bottomRight)
        // Part 1 solution
        println(topLeft.size * topRight.size * bottomLeft.size * bottomRight.size)
    } else {
        val widthStr = ".".repeat(width)
        val map = MutableList(height) { widthStr }
        var seconds = 1

        var uniquePlacements = 0
        while (uniquePlacements != robots.size) {
            updatedRobots = updatedRobots.map { (position, vector) ->
                position.moveRobot(vector, roomWidth = width, roomHeight = height) to vector
            }
            val uniquePositions = updatedRobots.map { (first, _) -> first }.toSet()
            uniquePlacements = uniquePositions.size
            // Seconds is solution for part 2
            println("Seconds $seconds; Unique placements: $uniquePlacements")
            if (uniquePlacements == robots.size) {
                map.draw(uniquePositions)
            }
            seconds++
        }
    }
}

fun Position.moveRobot(vector: Vector, roomWidth: Int = 11, roomHeight: Int = 7) =
    this.add(vector = vector).normalise(roomHeight, roomWidth)

fun Position.normalise(maxRow: Int, maxColumn: Int): Position {
    var newRow = this.row
    var newColumn = this.column
    if (this.row < 0) {
        newRow = maxRow + this.row
    }
    if (this.row >= maxRow) {
        newRow = this.row - maxRow
    }
    if (this.column < 0) {
        newColumn = maxColumn + this.column
    }
    if (this.column >= maxColumn) {
        newColumn = this.column - maxColumn
    }
    return Position(newRow, newColumn)
}

fun List<String>.draw(traversed: Set<Position>) {
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, column ->
            if (Position(rowIndex, columnIndex) in traversed) {
                print('X')
            } else {
                print(column)
            }
        }
        println()
    }
    println()
}
