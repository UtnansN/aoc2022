import java.io.File

fun main() {
    Day5().runA()
    Day5().runB()
}

class Day5 {

    fun runA() {
        val crateRows = ArrayList<String>()
        val valueList = ArrayList<ArrayList<Char>>()

        File("src/main/resources/day5.txt").forEachLine { row ->
            if (row.isNotEmpty() && row[1].isDigit()) {
                buildValueList(row, valueList, crateRows)
            } else if (row.isNotEmpty()) {
                if (valueList.isEmpty()) {
                    crateRows.add(row)
                } else {
                    val args = row.split(" ").mapNotNull { it.toIntOrNull() }
                    val count = args[0]
                    val source = args[1] - 1
                    val target = args[2] - 1

                    repeat(count) {
                        val value = valueList[source].removeFirst()
                        valueList[target].add(0, value)

                    }
                }
            }
        }

        val resultA = valueList.map { it[0] }.fold("") { acc, c -> acc + c }
        println("A: $resultA")
    }

    fun runB() {
        val crateRows = ArrayList<String>()
        val valueList = ArrayList<ArrayList<Char>>()

        File("src/main/resources/day5.txt").forEachLine { row ->
            if (row.isNotEmpty() && row[1].isDigit()) {
                buildValueList(row, valueList, crateRows)
            } else if (row.isNotEmpty()) {
                if (valueList.isEmpty()) {
                    crateRows.add(row)
                } else {
                    val args = row.split(" ").mapNotNull { it.toIntOrNull() }
                    val count = args[0]
                    val source = args[1] - 1
                    val target = args[2] - 1

                    val temp = valueList[source].subList(0, count)
                    valueList[source] = ArrayList(valueList[source].drop(count))
                    valueList[target].addAll(0, temp)
                }
            }
        }

        val resultB = valueList.map { it[0] }.fold("") { acc, c -> acc + c }
        println("B: $resultB")
    }

    private fun buildValueList(
        row: String,
        valueList: ArrayList<ArrayList<Char>>,
        crateRows: ArrayList<String>
    ) {
        repeat(row.trim().last().digitToInt()) { valueList.add(ArrayList()) }

        crateRows.forEach { crateRow ->
            val chunkedValues = crateRow.chunked(4).map { it.trim() }

            chunkedValues.forEachIndexed { index, s ->
                if (chunkedValues[index].isNotEmpty()) {
                    valueList[index].add(s[1])
                }
            }
        }
    }
}
