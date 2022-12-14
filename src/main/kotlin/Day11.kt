import java.io.File
import java.math.BigDecimal

fun main() {
    Day11().runA()
    Day11().runB()
}

class Day11 {

    data class Monkey(
        val items: MutableList<Long> = ArrayList(),
        val operation: (Long) -> Long,
        val testDivision: Long,
        val nextTrue: Int,
        val nextFalse: Int,
        var inspections: Long = 0
    )

    fun runA() {
        val inputs = File("src/main/resources/day11.txt").readLines().filter { it.isNotEmpty() }.chunked(6)
        val monkeys = ArrayList<Monkey>()

        createMonkeys(inputs, monkeys)

        for (round in 1..20) {
            println("ROUND: $round")
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    val worry = monkey.operation(item) / 3

                    if (worry % monkey.testDivision == 0L) {
                        monkeys[monkey.nextTrue].items.add(worry)
                    } else {
                        monkeys[monkey.nextFalse].items.add(worry)
                    }
                    monkey.inspections += 1
                }
                monkey.items.clear()
            }
            monkeys.forEachIndexed { index, monkey -> println("Monkey $index: ${monkey.items}, inspections: ${monkey.inspections}") }
        }

        val resultA = monkeys.map { it.inspections }.sorted().takeLast(2).reduce { acc, i -> acc * i }
        println("A: $resultA")
    }

    private fun gcd(n1: Long, n2: Long): Long {
        var i1 = n1
        var i2 = n2

        while (i1 != i2) {
            if (i1 > i2)
                i1 -= i2
            else
                i2 -= i1
        }

        return i1
    }

    fun runB() {
        val inputs = File("src/main/resources/day11.txt").readLines().filter { it.isNotEmpty() }.chunked(6)
        val monkeys = ArrayList<Monkey>()

        createMonkeys(inputs, monkeys)
        val lcm = monkeys.map { it.testDivision }.fold(1L) { acc, num -> acc * (num / gcd(acc, num)) }

        for (round in 1..10000) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    val worry = monkey.operation(item) % lcm
                    if (worry % monkey.testDivision == 0L) {
                        monkeys[monkey.nextTrue].items.add(worry)
                    } else {
                        monkeys[monkey.nextFalse].items.add(worry)
                    }
                    monkey.inspections += 1
                }
                monkey.items.clear()
            }

            if (round == 1 || round == 20 || round % 1000 == 0) {
                println("ROUND: $round")
                monkeys.forEachIndexed { index, monkey ->
                    println("Monkey $index: ${monkey.items}, inspections: ${monkey.inspections}")
                }
            }
        }

        val lastValues = monkeys.map { it.inspections }.sorted().takeLast(2)
        val resultB = BigDecimal.valueOf(lastValues[0]).multiply(BigDecimal.valueOf(lastValues[1]))
        println("B: $resultB")
    }

    private fun createMonkeys(
        inputs: List<List<String>>,
        monkeys: ArrayList<Monkey>
    ) {
        inputs.forEach { monkeyChunk ->
            val items = monkeyChunk[1].removePrefix("  Starting items: ").split(", ").map { item -> item.toLong() }
                .toMutableList()
            val ops = monkeyChunk[2].removePrefix("  Operation: new = ").split(" ")

            val funcOperation = fun(old: Long): Long {
                val firstVal = if (ops[0] == "old") {
                    old
                } else {
                    ops[0].toLong()
                }

                val secondVal = if (ops[2] == "old") {
                    old
                } else {
                    ops[2].toLong()
                }

                return if (ops[1] == "+") {
                    firstVal + secondVal
                } else if (ops[1] == "*") {
                    firstVal * secondVal
                } else {
                    0
                }
            }

            val division = monkeyChunk[3].split(" ").last().toLong()
            val trueNext = monkeyChunk[4].split(" ").last().toInt()
            val falseNext = monkeyChunk[5].split(" ").last().toInt()

            monkeys.add(Monkey(items, funcOperation, division, trueNext, falseNext))
        }
    }
}