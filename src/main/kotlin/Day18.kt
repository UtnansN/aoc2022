import java.io.File

fun main() {
    Day18().run()
}

class Day18 {

    fun run() {
        val cubes = HashSet<Triple<Int, Int, Int>>()
        File("src/main/resources/day18.txt").forEachLine {
            val (x, y, z) = it.split(",").map(String::toInt)
            cubes.add(Triple(x, y, z))
        }

        // Check only the positive values and remove 2 if any matches. Positive side covers negative side of other cube, thus -2
        var free = 0
        cubes.forEach { cube ->
            free += 6

            val others = cubes.filter { localCube -> localCube != cube }

            if (others.any { localCube -> Triple(localCube.first-1, localCube.second, localCube.third) == cube }) {
                free -= 2
            }
            if (others.any { localCube -> Triple(localCube.first, localCube.second-1, localCube.third) == cube }) {
                free -= 2
            }
            if (others.any { localCube -> Triple(localCube.first, localCube.second, localCube.third-1) == cube }) {
                free -= 2
            }
        }

        println("A: $free")
    }

}
