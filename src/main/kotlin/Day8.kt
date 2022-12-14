import java.io.File

fun main() {
    Day8().run()
}

class Day8 {

    fun run() {
        val grid = ArrayList<ArrayList<Int>>();
        File("src/main/resources/day8.txt").forEachLine {
            grid.add(it.map(Char::digitToInt).toCollection(ArrayList()))
        }
        var visible = grid.size * 4 - 4
        var bestScore = 0

        grid.forEach(::println)
        for (i in 1 until grid.size - 1) {
            for (j in 1 until grid[i].size - 1) {
                val current = grid[i][j]
                val leftSlice = grid[i].subList(0, j)
                val rightSlice = grid[i].subList(j+1, grid[i].size)
                val topSlice = grid.subList(0, i)
                val bottomSlice = grid.subList(i+1, grid.size)

                if (leftSlice.all { height -> height < current } ||
                    rightSlice.all { height -> height < current } ||
                    topSlice.all { row -> row[j] < current } ||
                    bottomSlice.all { row -> row[j] < current }) {
                    visible++
                }

                var lScore = leftSlice.reversed().indexOfFirst { it >= current } + 1
                var rScore = rightSlice.indexOfFirst { it >= current } + 1
                var tScore = topSlice.reversed().map { it[j] }.indexOfFirst { it >= current } + 1
                var bScore = bottomSlice.map { it[j] }.indexOfFirst { it >= current } + 1

                if (lScore == 0) {
                    lScore = leftSlice.size
                }
                if (rScore == 0) {
                    rScore = rightSlice.size
                }
                if (tScore == 0) {
                    tScore = topSlice.size
                }
                if (bScore == 0) {
                    bScore = bottomSlice.size
                }

                val candidate = lScore * rScore * tScore * bScore
                if (bestScore < candidate) {
                    bestScore = candidate
                }
            }
        }

        println("A: $visible")
        println("B: $bestScore")
    }

}