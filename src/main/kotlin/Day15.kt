import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    Day15().runA()
    Day15().runB()
}

class Day15 {

    fun runA() {
        val targetRow = 2000000
        val targetRowRanges = ArrayList<IntRange>()
        val beaconsInRow = HashMap<Int, MutableSet<Int>>()
        File("src/main/resources/day15.txt").forEachLine { row ->
            val x = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)").findAll(row)

            val (sX, sY, eX, eY) = x.asIterable()
                .map { it.groupValues.slice(1 until it.groupValues.size) }
                .flatten()
                .map(String::toInt)

            if (beaconsInRow.containsKey(eY)) {
                beaconsInRow[eY]!!.add(eX)
            } else {
                beaconsInRow[eY] = HashSet<Int>().apply { add(eX) }
            }

            val dX = eX - sX
            val dY = eY - sY
            val totalOffset = abs(dX) + abs(dY)

            val topRange = (sY-totalOffset) until sY
            val bottomRange = (sY+totalOffset) downTo sY + 1

            var addable: IntRange? = null
            if (eY == targetRow) {
                addable = (sX - abs(totalOffset))..(sX + abs(totalOffset))
            }
            if (topRange.contains(targetRow)) {
                val idx = topRange.indexOf(targetRow)
                addable = (sX-idx)..(sX+idx)
            }
            if (bottomRange.contains(targetRow)) {
                val idx = bottomRange.indexOf(targetRow)
                addable = (sX-idx)..(sX+idx)
            }

            if (addable != null) {
                combineOverlaps(targetRowRanges, addable)
            }
        }
        val impossible = targetRowRanges.fold(0) { acc, range ->
            val occupied = beaconsInRow.getOrDefault(targetRow, HashSet())
                .count { range.contains(it) }

            acc + (range.count() - occupied)
        }

        println("A: $impossible")
    }

    fun runB() {
        val targetRowMap = HashMap<Int, MutableList<IntRange>>()
        val beaconsInRow = HashMap<Int, MutableSet<Int>>()

        val maxCoord = 4000000
        File("src/main/resources/day15.txt").forEachLine { row ->
            val x = Regex("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)").findAll(row)

            val (sX, sY, eX, eY) = x.asIterable()
                .map { it.groupValues.slice(1 until it.groupValues.size) }
                .flatten()
                .map(String::toInt)

            if (beaconsInRow.containsKey(eY)) {
                beaconsInRow[eY]!!.add(eX)
            } else {
                beaconsInRow[eY] = HashSet<Int>().apply { add(eX) }
            }

            val dX = eX - sX
            val dY = eY - sY
            val totalOffset = abs(dX) + abs(dY)

            val topRange = (sY-totalOffset) until sY
            val bottomRange = (sY+totalOffset) downTo sY + 1

            if (targetRowMap.containsKey(sY)) {
                combineOverlaps(targetRowMap[sY]!!, (sX - abs(totalOffset))..(sX + abs(totalOffset)))
            } else {
                targetRowMap[sY] = ArrayList<IntRange>().apply { add((sX - abs(totalOffset))..(sX + abs(totalOffset))) }
            }

            topRange.forEachIndexed { idx, i ->
                if (targetRowMap.containsKey(i)) {
                    combineOverlaps(targetRowMap[i]!!, (sX-idx)..(sX+idx))
                } else {
                    targetRowMap[i] = ArrayList<IntRange>().apply { add((sX-idx)..(sX+idx)) }
                }
            }

            bottomRange.forEachIndexed { idx, i ->
                if (targetRowMap.containsKey(i)) {
                    combineOverlaps(targetRowMap[i]!!, (sX-idx)..(sX+idx))
                } else {
                    targetRowMap[i] = ArrayList<IntRange>().apply { add((sX-idx)..(sX+idx)) }
                }
            }
        }

        var fX = 0
        var fY = 0
        targetRowMap
            .filterKeys { it in 0..maxCoord }
            .forEach {
                if (it.value.size > 1) {
                    fY = it.key

                    val (first, second) = it.value
                    fX = if (first.last < second.first) {
                        first.last + 1
                    } else {
                        second.last + 1
                    }
                }
            }

        println("B: ${4000000L * fX + fY}")
    }

    private fun combineOverlaps(current: MutableList<IntRange>, addable: IntRange) {
        val overlaps = current
            .filter {
                !(addable.last+1 < it.first || addable.first-1 > it.last)
            }
            .toSet()

        current.removeAll(overlaps)
        val combined = overlaps.fold(addable) { acc, intRange ->
            val minVal = min(acc.first, intRange.first)
            val maxVal = max(acc.last, intRange.last)
            minVal..maxVal
        }
        current.add(combined)
    }
}
