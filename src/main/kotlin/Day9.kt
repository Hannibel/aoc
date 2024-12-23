import java.math.BigInteger
import java.util.*
import kotlin.math.floor

fun String.toDiskMap(): List<Int> {
    return this.mapIndexed { index, c ->
        val digit = c.toString().toInt()

        if (index % 2 == 0) {
            val fileIndex = (index / 2)
            MutableList(digit) { fileIndex }
        } else {
            MutableList(digit) { -1 }
        }
    }.flatten()
}

fun day9P12024() {
    val diskMap = 9.readDayInput().first()
    val maxFileIndex = floor(diskMap.length.toDouble() / 2).toLong()
    val fileMap =
        diskMap.filterIndexed { index, c -> index % 2 == 0 }
            .mapIndexed { fileIndex, c -> fileIndex to c.toString().toLong() }
            .groupBy { it.first }
    println(fileMap)
    println(maxFileIndex)

    val originalMap = diskMap.toDiskMap()
    println(originalMap)

    var cnt = 0
    var endCnt = originalMap.size - 1
    val result = mutableListOf<Long>()
    while (cnt <= endCnt) {
        val cell = originalMap[cnt]
        if (cell == -1) {
            var endCell: Int = -1
            while (endCell == -1) {
                endCell = originalMap[endCnt]
                endCnt--
            }
            if (cnt > endCnt) {
                break;
            }
            result.add(endCell.toLong())
        } else {
            result.add(cell.toLong())
        }
        cnt++
    }


    println(result.joinToString(","))
    var sum = BigInteger.ZERO
    result.withIndex().forEach { (index, digit) ->
        println("[$index * $digit]")
        sum = sum.add(BigInteger.valueOf(index.toLong()).times(BigInteger.valueOf(digit)))
    }
    println(sum)
} // 6398608069280


// Functioning part 2
fun day9P22024_retry() {
    val diskMap = 9.readDayInput().first()

    val nodeList = LinkedList<Node>()
    val emptyNodeList = LinkedList<EmptyNode>()
    var realIndex = 0
    diskMap.forEachIndexed { index, c ->
        val size = c.toString().toInt()

        if (index % 2 == 0) {
            val fileIndex = (index / 2)
            val node = Node(realIndex, size, fileIndex)
            nodeList.add(node)
        } else {
            val emptyNode = EmptyNode(realIndex, size)
            if (size > 0) {
                emptyNodeList.add(emptyNode)
            }
        }
        realIndex += size
    }
    println(nodeList)
    println(emptyNodeList)
    (nodeList to emptyNodeList).draw()

    val cache = mutableSetOf<Int>()
    nodeList.reversed().forEach { node ->
        println(node.fileIndex)
        cache.add(node.fileIndex)
        emptyNodeList.firstOrNull { it.index < node.index && it.size >= node.size }?.let { emptyNode ->
            val emptyNodeIndex = emptyNodeList.indexOf(emptyNode)
            emptyNodeList.remove(emptyNode)
            if (emptyNode.size > node.size) {
                emptyNodeList.add(emptyNodeIndex, EmptyNode(emptyNode.index + node.size, emptyNode.size  - node.size))
            }
            emptyNodeList.add(EmptyNode(node.index, node.size))

            val nextNodeIndex = nodeList.indexOfFirst { it.index > emptyNode.index }
            nodeList.remove(node)
            nodeList.add(nextNodeIndex, node.copy(index = emptyNode.index))
        }
        emptyNodeList.sortBy { it.index }
//        println(nodeList)
//        (nodeList to emptyNodeList).draw()
    }
    println(nodeList)
    println(emptyNodeList.sortedBy { it.index })

    var sum = BigInteger.ZERO
    nodeList.forEach { node ->
        repeat(node.size) { i ->
            sum = sum.add(BigInteger.valueOf((node.index + i).toLong()).times(BigInteger.valueOf(node.fileIndex.toLong())))
        }
    }
    println(sum)
}

fun Pair<List<Node>, List<EmptyNode>>.draw() {
    val (nodes, emptyNodes) = this
    val lastNode = nodes.last()
    val emptyLast = emptyNodes.last()
    val max = Math.max(lastNode.index + lastNode.size, emptyLast.index + emptyLast.size)

    val line = MutableList(max) { "_" }
    for (node in nodes) {
        repeat(node.size) { i ->
            line[node.index + i] = node.fileIndex.toString()
        }
    }
    for (emptyNode in emptyNodes) {
        repeat(emptyNode.size) { i ->
            line[emptyNode.index + i] = "."
        }
    }
    println(line.joinToString(""))
}

data class EmptyNode(
    val index: Int,
    val size: Int,
) {
    override fun toString(): String {
        return "($index, $size)"
    }
}

data class Node(
    val index: Int,
    val size: Int,
    val fileIndex: Int,
) {
    override fun toString(): String {
        return "($index, $size, $fileIndex)"
    }
}

// Graveyard, too buggy, rewrote this:
// Big ooefff, doesnt work
fun day9P22024() {
    val diskMap = 9.readDayInput().first()
    val originalMap = diskMap.mapIndexed { index, c ->
        val digit = c.toString().toInt()

        if (index % 2 == 0) {
            val fileIndex = (index / 2)
            fileIndex to digit
        } else {
            -1 to digit
        }
    }
    println(originalMap)
    originalMap.draw()
    println()
    println()

    val updatedMap = LinkedList(originalMap)
    var cnt = 0
    var endCnt = originalMap.size - 1
    val visited = mutableSetOf<Int>()
    val sizeAvailable = HashMap(MutableList(9) { index -> index + 1 }.associateWith { true })
    while (cnt <= endCnt) {
        var swapped = false
        val (fileIndex, size) = updatedMap[endCnt]
        if (fileIndex != -1 && (fileIndex !in visited)) {
            if (sizeAvailable[size]!!) {
                println("no available size: $size")
                endCnt--
                continue
            }
            visited.add(fileIndex)
            var startCell = 0
            var startCellSize = 0
            while (startCell != -1 || startCellSize < size) {
                if (cnt >= endCnt) {
                    break
                }
                val startCellPair = updatedMap[cnt]
                startCell = startCellPair.first
                startCellSize = startCellPair.second
                cnt++
            }

            if (cnt < endCnt) {
                swapped = true
                val remaining = startCellSize - size
                val updatedEmptySpace = -1 to remaining
                val usedEmptySpace = -1 to size
                // order important
                updatedMap.removeAt(endCnt)
                val freePrev = updatedMap[endCnt - 1]
                if (freePrev.first == -1) {
                    updatedMap[endCnt - 1] = freePrev.first to freePrev.second + size
                } else {
                    updatedMap.add(endCnt, usedEmptySpace)
                }
                updatedMap.removeAt(cnt - 1)
                if (updatedEmptySpace.second > 0) {
                    val freeNext = updatedMap[cnt - 1]
                    if (freeNext.first == -1) {
                        updatedMap[cnt - 1] = freeNext.first to freeNext.second + remaining
                    } else {
                        updatedMap.add(cnt - 1, updatedEmptySpace)
                    }
                }
                updatedMap.add(cnt - 1, fileIndex to size)
            }
            println("$fileIndex,$size, swapped: $swapped")

            cnt = updatedMap.indexOfFirst { it.first == -1 }
            if (cnt > endCnt) {
                break
            }
            if (cnt == -1) {
                break
            }
            //        println(updatedMap)
//            updatedMap.draw()
//            println()
        }
        if (!swapped) {
            sizeAvailable[size] = false
        }
        endCnt--
        if (cnt > endCnt) {
            break
        }
//        println("$endCnt;")
    }
//    println(updatedMap)
//    updatedMap.draw()
    var sum = BigInteger.ZERO
    updatedMap.map { (fileIndex, size) ->
        MutableList(size) { fileIndex.toLong() }
    }.flatten().withIndex().forEach { (index, digit) ->
        if (digit > -1) {
//            println("[$index * $digit]")
            sum = sum.add(BigInteger.valueOf(index.toLong()).times(BigInteger.valueOf(digit)))
        }
    }
//    println("[$index * $digit]")
//    sum = sum.add(BigInteger.valueOf(index.toLong()).times(BigInteger.valueOf(digit)))
    println(sum)
}

fun List<Pair<Int, Int>>.draw() = this.forEach { (fileIndex, size) ->
    repeat(size) {
        if (fileIndex == -1) {
            print('.')
        } else {
            print(fileIndex)
        }
    }
}