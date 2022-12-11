import java.io.File

fun main() {
    Day1().run()
}

class Day1 {

    fun run() {
        val calorieList = ArrayList<Int>()
        var counter = 0
        File("src/main/resources/day1.txt").forEachLine {
            if (it.isEmpty()) {
                calorieList.add(counter)
                counter = 0
            } else {
                counter += it.toInt()
            }
        }
        calorieList.add(counter)

        println("Day1 A: ${calorieList.max()}")
        println("Day1 B: ${calorieList.sorted().takeLast(3).sum()}")
    }
}
