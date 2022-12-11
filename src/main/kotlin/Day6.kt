import java.io.File

fun main() {
    Day6().run()
}

class Day6 {

    fun run() {
        val input = File("src/main/resources/day6.txt").readText()
        for (i in 3 until input.length) {
            if (input.slice(i-3..i).toSet().size == 4) {
                println("A: ${i+1}")
                break;
            }
        }

        for (i in 14 until input.length) {
            if (input.slice(i-14..i).toSet().size == 14) {
                println("B: ${i+1}")
                break;
            }
        }
    }

}
