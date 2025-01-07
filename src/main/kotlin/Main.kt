import java.io.File

fun main() {
    AOC2024().execute()
}

private fun logic() {
    db()
}

private fun db() {
    println("hello")
}

fun readFile(fileName: String) = File("src/main/resources/$fileName").readLines()