import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day14().run()
}

class Day14 {

    fun run() {
        val map = HashMap<Pair<Int, Int>, Char>()
        File("src/main/resources/day14.txt").forEachLine {
            val coords = it.split(" -> ")
                .map { pair ->
                    val (x, y) = pair.split(",").map(String::toInt)
                    Pair(x, y)
                }
            println(coords)

            for (i in 1 until coords.size) {
                val (sX, sY) = coords[i - 1]
                val (eX, eY) = coords[i]

                if (sX != eX) {
                    (min(sX, eX)..max(sX, eX)).forEach { x -> map[Pair(x, sY)] = '#' }
                } else {
                    (min(sY, eY)..max(sY, eY)).forEach { y -> map[Pair(sX, y)] = '#' }
                }
            }
        }
        map[Pair(500, 0)] = '+'

        val minX = map.map { it.key.first }.min()
        val maxX = map.map { it.key.first }.max()
        val minY = map.map { it.key.second }.min()
        val maxY = map.map { it.key.second }.max()

        var grains = 0
        var solutionA = 0
        var doneA = false
        var doneB = false
        while (true) {
            var currPos = Pair(500, 0)

            while (true) {
                if (!doneA && currPos.second >= maxY) {
                    doneA = true
                    solutionA = grains
                }

                if (currPos.second < maxY + 1) {
                    if (!map.containsKey(Pair(currPos.first, currPos.second + 1))) {
                        currPos = Pair(currPos.first, currPos.second + 1)
                        continue
                    }
                    if (!map.containsKey(Pair(currPos.first - 1, currPos.second + 1))) {
                        currPos = Pair(currPos.first - 1, currPos.second + 1)
                        continue
                    }
                    if (!map.containsKey(Pair(currPos.first + 1, currPos.second + 1))) {
                        currPos = Pair(currPos.first + 1, currPos.second + 1)
                        continue
                    }
                }

                if (currPos == Pair(500, 0)) {
                    doneB = true
                }
                map[currPos] = 'O'
                grains++
                break
            }
            if (doneB) {
                break
            }
        }

        println("A: $solutionA")
        println("B: $grains")
    }
}
