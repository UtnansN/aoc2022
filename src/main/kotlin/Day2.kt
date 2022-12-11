import java.io.File

fun main() {
    Day2().run()
}

class Day2 {

    fun run() {
        var aScore = 0
        var bScore = 0
        File("src/main/resources/day2.txt").forEachLine {
            val them = it[0].minus('A') + 1
            val you = it[2].minus('X') + 1

            aScore += you
            if (you == them) {
                aScore += 3
            } else if ((you - them == 1) || (you - them == -2)) {
                aScore += 6
            }

            bScore += ((you - 1) * 3)
            bScore += when (you) {
                1 -> {
                    if (them == 1) 3 else them - 1
                }
                2 -> {
                    them
                }
                else -> {
                    if (them == 3) 1 else them + 1
                }
            }
        }

        println("A: $aScore")
        println("B: $bScore")
    }
}
