import java.io.File

fun main() {
    Day18().run()
}

class Day18 {

    private val cubes = HashSet<Triple<Int, Int, Int>>()
    private val searchSet = HashSet<Triple<Int, Int, Int>>()
    private val workQueue = ArrayDeque<Triple<Int, Int, Int>>()

    fun run() {
        File("src/main/resources/day18.txt").forEachLine {
            val (x, y, z) = it.split(",").map(String::toInt)
            cubes.add(Triple(x, y, z))
        }

        var free = countSides()
        println("A: $free")

        // Get bounds with buffer space for air to flow around whole area
        val minX = cubes.minOf { it.first } - 1
        val maxX = cubes.maxOf{ it.first } + 1
        val minY = cubes.minOf { it.second } - 1
        val maxY = cubes.maxOf { it.second } + 1
        val minZ = cubes.minOf { it.third } - 1
        val maxZ = cubes.maxOf { it.third } + 1

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    searchSet.add(Triple(x, y, z))
                }
            }
        }
        val startPoint = Triple(minX, minY, minZ)
        searchSet.remove(startPoint)

        // BFS to get unreachable spaces
        workQueue.add(startPoint)
        while (workQueue.isNotEmpty()) {
            val (cX, cY, cZ) = workQueue.removeFirst()

            checkAdjacent(minX, cX, cX-1, cY, cZ)
            checkAdjacent(maxX, cX, cX+1, cY, cZ)
            checkAdjacent(minY, cY, cX, cY-1, cZ)
            checkAdjacent(maxY, cY, cX, cY+1, cZ)
            checkAdjacent(minZ, cZ, cX, cY, cZ-1)
            checkAdjacent(maxZ, cZ, cX, cY, cZ+1)
        }

        // For any space that is not reached and is not a lava cube, check adjacent sides and remove 1 if adj. is lava
        searchSet
            .filter { notFound -> notFound !in cubes }
            .forEach { (aX, aY, aZ) ->
                free -= testSide(aX-1, aY, aZ)
                free -= testSide(aX+1, aY, aZ)
                free -= testSide(aX, aY-1, aZ)
                free -= testSide(aX, aY+1, aZ)
                free -= testSide(aX, aY, aZ-1)
                free -= testSide(aX, aY, aZ+1)
            }

        println("B: $free")
    }

    private fun testSide(aX: Int, aY: Int, aZ: Int): Int {
        return if (Triple(aX, aY, aZ) in cubes) 1 else 0
    }

    private fun checkAdjacent(
        borderValue: Int,
        compareValue: Int,
        cX: Int,
        cY: Int,
        cZ: Int
    ) {
        if (compareValue != borderValue) {
            val attempt = Triple(cX, cY, cZ)

            if (searchSet.remove(attempt)) {
                if (attempt !in cubes) {
                    workQueue.add(attempt)
                }
            }
        }
    }

    private fun countSides(): Int {
        // Check only the positive values and remove 2 if any matches. Positive side covers negative side of other cube, thus -2
        var free = 0
        cubes.forEach { cube ->
            free += 6

            val others = cubes.filter { localCube -> localCube != cube }

            if (others.any { localCube -> Triple(localCube.first - 1, localCube.second, localCube.third) == cube }) {
                free -= 2
            }
            if (others.any { localCube -> Triple(localCube.first, localCube.second - 1, localCube.third) == cube }) {
                free -= 2
            }
            if (others.any { localCube -> Triple(localCube.first, localCube.second, localCube.third - 1) == cube }) {
                free -= 2
            }
        }
        return free
    }
}
