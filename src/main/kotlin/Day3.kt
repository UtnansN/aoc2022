import java.io.File

fun main() {
    Day3().runA()
    Day3().runB()
}

class Day3 {

    fun runA() {
        var sum = 0
        File("src/main/resources/day3.txt").forEachLine { it ->
            val mid = it.length / 2
            val v1 = it.substring(0, mid)
            val v2 = it.substring(mid)

            val v1List = v1.toSet()
            val v2List = v2.toSet()
            val result = v1List.intersect(v2List)
            sum += sumResult(result)
        }

        println("A: $sum")
    }

    fun runB() {
        val inputs = File("src/main/resources/day3.txt").readLines()

        var sum = 0
        val groups = inputs.chunked(3)

        groups.forEach { it ->
            val v1Set = it[0].toSet()
            val v2Set = it[1].toSet()
            val v3Set = it[2].toSet()

            val result = v1Set.intersect(v2Set).intersect(v3Set)

            sum += sumResult(result)
        }

        println("B: $sum")
    }

    private fun sumResult(overlap: Set<Char>): Int {
        return overlap.fold(0) { acc, c ->
            acc + if (c.isLowerCase()) {
                c.minus('a') + 1
            } else {
                c.minus('A') + 27
            }
        }
    }
}
