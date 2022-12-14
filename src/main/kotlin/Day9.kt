import java.io.File
import kotlin.math.abs
import kotlin.math.sign

fun main() {
    Day9().runA()
    Day9().runB()
}

class Day9 {

    fun runA() {
        val visited = ArrayList<Pair<Int, Int>>()
        var currentHead = Pair(0, 0)
        var currentTail = Pair(0, 0)
        visited.add(currentTail)

        File("src/main/resources/day9.txt").forEachLine {
            val instructions = it.split(" ")
            val direction = instructions[0]
            val spaces = instructions[1].toInt()

            repeat(spaces) {
                val previous = currentHead.copy()

                currentHead = when (direction) {
                    "R" -> {
                        Pair(currentHead.first + 1, currentHead.second)
                    }
                    "L" -> {
                        Pair(currentHead.first - 1, currentHead.second)
                    }
                    "U" -> {
                        Pair(currentHead.first, currentHead.second + 1)
                    }
                    else -> {
                        Pair(currentHead.first, currentHead.second - 1)
                    }
                }

                if (abs(currentHead.first - currentTail.first) > 1 || abs(currentHead.second - currentTail.second) > 1) {
                    currentTail = previous;

                    if (currentTail !in visited) {
                        visited.add(currentTail)
                    }
                }
            }
        }

        println("A: ${visited.size}")
    }

    fun runB() {
        val visited = ArrayList<Pair<Int, Int>>()
        val nodes = ArrayList<Pair<Int, Int>>()
        repeat(10) {
            nodes.add(Pair(0, 0))
        }
        visited.add(Pair(0, 0))

        File("src/main/resources/day9.txt").forEachLine {
            val instructions = it.split(" ")
            val direction = instructions[0]
            val spaces = instructions[1].toInt()

//            println("\n=========== $direction $spaces ===========")

            repeat(spaces) {
                when (direction) {
                    "R" -> {
                        nodes[0] = Pair(nodes[0].first + 1, nodes[0].second)
                    }
                    "L" -> {
                        nodes[0] = Pair(nodes[0].first - 1, nodes[0].second)
                    }
                    "U" -> {
                        nodes[0] = Pair(nodes[0].first, nodes[0].second + 1)
                    }
                    else -> {
                        nodes[0] = Pair(nodes[0].first, nodes[0].second - 1)
                    }
                }

                for (i in 1 until nodes.size) {
                    val diffX = nodes[i - 1].first - nodes[i].first
                    val diffY = nodes[i - 1].second - nodes[i].second

                    if ((abs(diffX) > 1 && abs(diffY) > 0) || (abs(diffX) > 0 && abs(diffY) > 1)) {
                        nodes[i] = Pair(nodes[i].first + diffX.sign, nodes[i].second + diffY.sign)
                    } else if (abs(diffX) > 1) {
                        nodes[i] = Pair(nodes[i].first + diffX.sign, nodes[i].second)
                    } else if (abs(diffY) > 1) {
                        nodes[i] = Pair(nodes[i].first, nodes[i].second + diffY.sign)
                    }

                }

                if (nodes.last() !in visited) {
                    visited.add(nodes.last().copy())
                }
//                printOutput(nodes, visited)
            }
        }

        println("B: ${visited.size}")
    }

    private fun printOutput(
        nodes: ArrayList<Pair<Int, Int>>,
        visited: ArrayList<Pair<Int, Int>>
    ) {
        for (i in -15..15) {
            for (j in -15..15) {
                val foundIndex = nodes.indexOfFirst { node -> node == Pair(j, -i) }
                if (foundIndex > -1) {
                    print(foundIndex)
                } else {
                    if (Pair(j, -i) in visited) {
                        print("#")
                    } else {
                        print(".")
                    }
                }
            }
            println()
        }
    }
}