import java.io.File

fun main() {
    Day4().runA()
    Day4().runB()
}

class Day4 {

    fun runA() {
        var sum = 0
        File("src/main/resources/day4.txt").forEachLine { it ->
            val ranges = it.split(",")
            val range1 = ranges[0].split("-").map { it.toInt() }
            val range2 = ranges[1].split("-").map { it.toInt() }

            val r1min = range1[0]
            val r1max = range1[1]

            val r2min = range2[0]
            val r2max = range2[1]

            if ((r1min >= r2min && r1max <= r2max) || (r2min >= r1min && r2max <= r1max)) {
                sum++;
            }
        }

        println("A: $sum")
    }

    fun runB() {
        var sum = 0
        File("src/main/resources/day4.txt").forEachLine { it ->
            val ranges = it.split(",")
            val range1Arr = ranges[0].split("-").map { it.toInt() }
            val range2Arr = ranges[1].split("-").map { it.toInt() }

            val range1 = IntRange(range1Arr[0], range1Arr[1])
            val range2 = IntRange(range2Arr[0], range2Arr[1])

            if (range1.intersect(range2).isNotEmpty()) {
                sum++;
            }
        }

        println("B: $sum")
    }
}
