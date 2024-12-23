import java.io.File

fun main() {
    AOC2024().execute()
}

fun readFile(fileName: String) = File("src/main/resources/$fileName").readLines()