import java.io.File
import kotlin.math.abs

fun main() {
    Day10().run()
}

class Day10 {

    private var sum = 0
    private var cycle = 1
    private val imageMap = ArrayList<Char>()

    fun run() {
        var x = 1

        val cycles = ArrayList<Int>().apply {
            add(20)
            add(60)
            add(100)
            add(140)
            add(180)
            add(220)
        }

        File("src/main/resources/day10.txt").forEachLine {
            runCycle(x, cycles)
            if (it.startsWith("addx")) {
                val addable = it.split(" ")[1].toInt()
                runCycle(x, cycles)
                x += addable
            }
        }

        println("A: $sum")
        println("B:")
        imageMap.chunked(40).map { it.joinToString(separator = "") }.forEach(::println)
    }

    private fun runCycle(
        x: Int,
        cycles: ArrayList<Int>,
    ) {
        println("Cycle: $cycle, x: $x")

        if (abs(x + 1 - (cycle % 40)) > 1) {
            imageMap.add('.')
        } else {
            imageMap.add('#')
        }

        if (cycle in cycles) {
            println("$cycle * $x = ${cycle * x}")
            sum += cycle * x
        }
        cycle++
    }

}