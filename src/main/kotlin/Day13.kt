import java.io.File

fun main() {
    Day13().run()
}

class Day13 {

    fun run() {
        val heights = File("src/main/resources/day13.txt")
            .readLines()
            .filter { it.isNotBlank() }
            .chunked(2)

        val sum = heights
            .map { (first, second) ->
                Pair(first.slice(1 until first.length-1), second.slice(1 until second.length-1))
            }
            .foldIndexed(0) { idx, acc, (first, second) ->
                if (compareLevels(first, second) == -1) {
                    acc + idx + 1
                } else {
                    acc
                }
            }

        val comparator = Comparator<String>(::compareLevels)
        val sorted = heights
            .flatten()
            .toMutableList()
            .apply {
                add("[[2]]")
                add("[[6]]")
            }
            .sortedWith(comparator)

        println("A: $sum")
        println("B: ${(sorted.indexOf("[[2]]") + 1) * (sorted.indexOf("[[6]]") + 1)}")
    }

    private fun compareLevels(first: String, second: String): Int {
        val firstInputs = splitLevel(first)
        val secondInputs = splitLevel(second)

        for (idx in firstInputs.indices) {
            if (idx >= secondInputs.size) {
                return 1
            }

            val firstStr = firstInputs[idx]
            val secondStr = secondInputs[idx]

            val firstDigit = firstStr.toIntOrNull()
            val secondDigit = secondStr.toIntOrNull()

            if (firstDigit != null && secondDigit != null) {
                if (firstDigit < secondDigit) {
                    return -1
                } else if (firstDigit > secondDigit) {
                    return 1
                }
            } else if ((firstDigit == null) xor (secondDigit == null)) {
                val valid = if (firstDigit == null) {
                    compareLevels(firstStr.slice(1 until firstStr.length-1), secondStr)
                } else {
                    compareLevels(firstStr, secondStr.slice(1 until secondStr.length-1))
                }
                if (valid != 0) return valid
            } else {
                val valid = compareLevels(firstStr.slice(1 until firstStr.length-1), secondStr.slice(1 until secondStr.length-1))
                if (valid != 0) return valid
            }
        }

        if (firstInputs.size < secondInputs.size) {
            return -1
        }
        return 0
    }

    private fun splitLevel(input: String) : List<String> {
        val output = ArrayList<String>()
        var level = 0
        val current = StringBuilder()

        input.forEach {
            if (it == ',' && level == 0) {
                output.add(current.toString())
                current.clear()

            } else {
                if (it == '[') {
                    level++
                }
                if (it == ']') {
                    level--
                }
                current.append(it)
            }
        }
        if (current.isNotEmpty()) {
            output.add(current.toString())
        }

        return output
    }
}
