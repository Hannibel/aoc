import kotlin.collections.ArrayDeque

typealias DirectedPosition = Pair<Position, Direction>

fun Position.isValidWithWalls(map: List<String>) =
    row >= 0 && row < map.size && column >= 0 && column < map.first().length &&  map[row][column] != '#'

fun Position.getValidNeighbours(map: List<String>): Set<DirectedPosition> {
    val up = (this.up() to Direction.UP).takeIf { (pos, _) -> pos.isValidWithWalls(map) }
    val down = (this.down() to Direction.DOWN).takeIf { (pos, _) -> pos.isValidWithWalls(map) }
    val right = (this.right() to Direction.RIGHT).takeIf { (pos, _) -> pos.isValidWithWalls(map) }
    val left = (this.left() to Direction.LEFT).takeIf { (pos, _) -> pos.isValidWithWalls(map) }
    return setOfNotNull(up, down, left, right)
}

fun Pair<Position, Long>.getValidNeighboursBack(direction: Direction, map: List<String>, weights: Map<Position, Long>): Set<DirectedPosition> {
    val (thisPos, weight) = this
    val takeIfLambda: (DirectedPosition) -> Boolean = { (pos, d) ->
        val w = weights.getOrDefault(pos, Long.MAX_VALUE)
        val valid = pos.isValidWithWalls(map)
        val sameDirection = direction == d
        valid && ((!sameDirection && w < weight) || (sameDirection && w - 1000 < weight)) && w > 0
    }
    val up = (thisPos.up() to Direction.UP).takeIf(takeIfLambda)
    val down = (thisPos.down() to Direction.DOWN).takeIf(takeIfLambda)
    val right = (thisPos.right() to Direction.RIGHT).takeIf(takeIfLambda)
    val left = (thisPos.left() to Direction.LEFT).takeIf(takeIfLambda)
    return setOfNotNull(up, down, left, right)
}

fun Triple<Position, Direction, Long>.getValidNeighboursBackTest(map: List<String>, weights: Map<Position, List<Long>>): Set<Triple<Position, Direction, Long>> {
    val (thisPos, oldDirection, weight) = this
    val takeIfLambda: (Pair<Position, Direction>) -> Triple<Position, Direction, Long>? = { (pos, direction) ->
        val w = weights.getOrDefault(pos, listOf(Long.MAX_VALUE))
        val valid = pos.isValidWithWalls(map)
        val correctWeight = w.find { ((weight - 1001) == it) || ((weight - 1) == it) }
        if (valid && correctWeight != null) {
            Triple(pos, direction, correctWeight)
        } else {
            null
        }
    }
    val up = (thisPos.up() to Direction.UP).let(takeIfLambda)
    val down = (thisPos.down() to Direction.DOWN).let(takeIfLambda)
    val right = (thisPos.right() to Direction.RIGHT).let(takeIfLambda)
    val left = (thisPos.left() to Direction.LEFT).let(takeIfLambda)
    return setOfNotNull(up, down, left, right)
}

fun List<String>.bfs(start: Position, startDirection: Direction, end: Position): Map<Position, List<Long>> {
    val traversed = mutableSetOf<DirectedPosition>()
    val parents = mutableMapOf<DirectedPosition, DirectedPosition>()
    val weightedTraversal = mutableMapOf<DirectedPosition, Long>()

    val queue = ArrayDeque<DirectedPosition>()
    val queuePrio = mutableMapOf<Position, Long>()
    queue.add(start to startDirection)

    var endVisited = false
    while (queue.isNotEmpty() && !endVisited) {
        val vertex = queue.removeFirst()
        if (vertex !in traversed) {
//            println(vertex)
            if (vertex.first == end) {
                endVisited = true
            }
            traversed.add(vertex)
            var turn = false
            val weight = parents[vertex]?.let { parent ->
                weightedTraversal[parent]!! + if (vertex.second != parent.second) {
                    turn = true
                    1001
                } else {
                    1
                }
            } ?: 0
            val setPreviously = weightedTraversal[vertex]
            if (setPreviously == null || weight < setPreviously) {
                weightedTraversal[vertex] = weight
            }

            val neighbours = vertex.first.getValidNeighbours(this).filter { (pos, _) ->
                pos != parents.getOrDefault(vertex, Position(-1,-1) to Direction.UP).first
            }
            neighbours.forEach { neighbour ->
                parents[neighbour] = vertex
            }
            queue.addAll(neighbours.filterNot { it in traversed })
            neighbours.forEach { (neighbour, neighbourDirection) ->
                if (neighbourDirection == vertex.second) {
                    queuePrio[neighbour] = weight
                } else {
                    queuePrio[neighbour] = weight + 1000
                }
            }
            queue.sortBy { (neighbour, _) -> queuePrio[neighbour] }
//            println(vertex)
//            println(queue)
        }
    }

    val traversal = weightedTraversal.toList().groupBy { it.first.first  }
        .map { (pos, second) ->
            pos to second.map { it.second }
        }.toMap()

//    val minWeights = traversal.map { (pos, list) ->
//        pos to list.map { it to it % 1000 }.minBy { it.second }.first
//    }.toMap()
    val minWeights = traversal.map { (pos, list) -> pos to list.min() }.toMap()
    var traversedBackwards = mutableSetOf<Position>()
    val traversalBack = mutableMapOf<Position, List<Long>>()
//    val secondQueue = ArrayDeque<Triple<Position, Direction, Long>>()
//    secondQueue.add(Triple(end, Direction.DOWN, traversal[end]!!.first()))
    val secondQueue = ArrayDeque<DirectedPosition>()
    secondQueue.add(end to Direction.DOWN)
    while (secondQueue.isNotEmpty()) {
//        val (next, nextDirection, nextWeight) = secondQueue.removeFirst()
        val (next, nextDirection) = secondQueue.removeFirst()
        val nextWeight = minWeights[next]!!
//        println(next)
        if (next !in traversedBackwards) {
            traversedBackwards.add(next)
            traversalBack[next] = listOf(nextWeight)
            val neighbours = (next to nextWeight).getValidNeighboursBack(nextDirection, this, minWeights)
//            val neighbours = Triple(next, nextDirection, nextWeight).getValidNeighboursBackTest(this, traversal)
            secondQueue.addAll(neighbours)
        }
    }
    println(traversedBackwards.size)
//    this.draw(traversedBackwards)
    // Part 2: 543 Manually deducted the piece of shit part that does not work :(
    this.draw(traversalBack)
    return traversal
}


fun day16P12024() {
    val map = 16.readDayInput()

    var reindeer: Position? = null
    var end: Position? = null
    map.forEachIndexed { row, s ->
        s.forEachIndexed { col, c ->
            if (c == 'S') { reindeer = Position(row, col) }
            if (c == 'E') { end = Position(row, col) }
        }
    }
    reindeer?.let { r ->
        end?.let { e ->
            val weightedTraversal = map.bfs(r, Direction.RIGHT, e)
            map.draw(weightedTraversal)
            println(weightedTraversal[e])
        }
    }
}

fun List<String>.draw(weights: Map<Position, List<Long>>) {
    this.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, column ->
            val pos = Position(rowIndex, columnIndex)
            val toPrint = (weights[pos]?.min() ?: column).toString().padEnd(6, ' ')
            print(toPrint)
        }
        println()
    }
    println()
}