import java.io.File
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    Day12().runA()
    Day12().runB()
}

class Day12 {

    data class Vertex(
        val row: Int,
        val column: Int,
        val height: Char,
        val neighbors: MutableList<Vertex> = ArrayList(),
        var dist: Double = Double.POSITIVE_INFINITY,
    )

    fun runA() {
        val heights = File("src/main/resources/day12.txt")
            .readLines()
            .map { it.toList() }

        var start: Pair<Int, Int>? = null
        for (i in heights.indices) {
            for (j in heights.indices) {
                if (heights[i][j] == 'S') {
                    start = Pair(i, j)
                }
            }
            if (start != null) {
                break
            }
        }

        val queue = calculateGraph(heights, start!!)
        val done = ArrayList<Vertex>()
        while (queue.isNotEmpty()) {
            val u = queue.poll()

            u.neighbors
                .filter { neighbor -> queue.any { queueElem -> queueElem == neighbor } }
                .forEach { neighbor ->
                    runDijkstra(neighbor, u, queue)
                }
            done.add(u)
        }

        val distance = done.find { it.height == 'E' }?.dist
        println("A: $distance")
    }

    fun runB() {
        val heights = File("src/main/resources/day12.txt")
            .readLines()
            .map { it.toList() }

        val start: MutableList<Pair<Int, Int>> = ArrayList()
        for (i in heights.indices) {
            for (j in heights.indices) {
                if (heights[i][j] == 'S' || heights[i][j] == 'a') {
                    start.add(Pair(i, j))
                }
            }
        }

        val fastest = start.mapNotNull { pair ->
            val queue = calculateGraph(heights, pair)
            val done = ArrayList<Vertex>()
            while (queue.isNotEmpty()) {
                val u = queue.poll()

                u.neighbors
                    .filter { neighbor -> queue.any { queueElem -> queueElem == neighbor } }
                    .forEach { neighbor ->
                        runDijkstra(neighbor, u, queue)
                    }
                done.add(u)
            }
            done.find { it.height == 'E' }?.dist
        }
            .min()

        println("B: $fastest")
    }

    private fun runDijkstra(
        v: Vertex,
        u: Vertex,
        queue: PriorityQueue<Vertex>
    ) {
        val alternativeCost = u.dist + 1
        if (alternativeCost < v.dist) {
            v.dist = alternativeCost
            queue.remove(v)
            queue.add(v)
        }
    }

    private fun calculateGraph(
        heights: List<List<Char>>,
        start: Pair<Int, Int>
    ): PriorityQueue<Vertex> {
        val vertexQueue = PriorityQueue(compareBy(Vertex::dist))
        val vertices = ArrayList<MutableList<Vertex>>()

        for (i in heights.indices) {
            val row = ArrayList<Vertex>()
            for (j in heights[i].indices) {
                val vertex = Vertex(i, j, heights[i][j])

                if (i == start.first && j == start.second) {
                    vertex.dist = 0.0
                }

                vertexQueue.add(vertex)
                row.add(vertex)
            }
            vertices.add(row)
        }

        for (i in vertices.indices) {
            for (j in vertices[i].indices) {
                val current = vertices[i][j]

                if (i > 0) {
                    val other = vertices[i - 1][j]
                    if (isReachable(current.height, other.height)) {
                        current.neighbors.add(other)
                    }
                }
                if (i < heights.size - 1) {
                    val other = vertices[i + 1][j]
                    if (isReachable(current.height, other.height)) {
                        current.neighbors.add(other)
                    }
                }
                if (j > 0) {
                    val other = vertices[i][j - 1]
                    if (isReachable(current.height, other.height)) {
                        current.neighbors.add(other)
                    }
                }
                if (j < heights[i].size - 1) {
                    val other = vertices[i][j + 1]
                    if (isReachable(current.height, other.height)) {
                        current.neighbors.add(other)
                    }
                }
            }
        }
        return vertexQueue
    }

    private fun isReachable(curr: Char, other: Char): Boolean {
        var currTranslated = curr
        var otherTranslated = other

        if (curr == 'S') {
            currTranslated = 'a'
        }
        if (curr == 'E') {
            currTranslated = 'z'
        }
        if (other == 'S') {
            otherTranslated = 'a'
        }
        if (other == 'E') {
            otherTranslated = 'z'
        }

        return currTranslated + 1 >= otherTranslated
    }

}
