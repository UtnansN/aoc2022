import java.io.File

fun main() {
    Day21().runA()
    Day21().runB()
}

class Day21 {

    class OperationMonkey(
        val name: String,
        val v1: String,
        val v2: String,
        val op: Char
    )

    private val valueMap = HashMap<String, Long>()
    private val opMonkeys = HashSet<OperationMonkey>()

    fun runA() {
        val regex = Regex("([a-zA-Z]+): (.+)")
        File("src/main/resources/day21.txt").forEachLine { readFile(regex, it) }

        while (opMonkeys.isNotEmpty()) {
            val toRemove = HashSet<OperationMonkey>()
            for (monkey in opMonkeys) {
                if (valueMap.containsKey(monkey.v1) && valueMap.containsKey(monkey.v2)) {
                    val calculated = doOp(valueMap[monkey.v1]!!, valueMap[monkey.v2]!!, monkey.op)
                    valueMap[monkey.name] = calculated
                    toRemove.add(monkey)
                }
            }
            opMonkeys.removeAll(toRemove)
        }
        println("A: ${valueMap.getOrDefault("root", "NOT FOUND")}")
    }

    fun runB() {
        val regex = Regex("([a-zA-Z]+): (.+)")
        File("src/main/resources/day21.txt").forEachLine { readFile(regex, it) }
        valueMap.remove("humn")

        while (opMonkeys.isNotEmpty()) {
            val toRemove = HashSet<OperationMonkey>()
            for (monkey in opMonkeys) {
                if (valueMap.containsKey(monkey.v1) && valueMap.containsKey(monkey.v2)) {
                    val calculated = doOp(valueMap[monkey.v1]!!, valueMap[monkey.v2]!!, monkey.op)
                    valueMap[monkey.name] = calculated
                    toRemove.add(monkey)
                }
            }

            if (toRemove.size == 0) break
            opMonkeys.removeAll(toRemove)
        }


        val root = opMonkeys.first { it.name == "root" }
        var currentNode = if (valueMap[root.v1] == null) root.v1 else root.v2
        var currentValue = valueMap[root.v1] ?: valueMap[root.v2]!!
        opMonkeys.remove(root)

        while (opMonkeys.isNotEmpty()) {
            val current = opMonkeys.first { it.name == currentNode }
            currentNode = if (valueMap[current.v1] == null) current.v1 else current.v2
            currentValue = doReverseOp(
                currentValue,
                valueMap[current.v1] ?: valueMap[current.v2]!!,
                current.op,
                valueMap[current.v1] == null
            )
            opMonkeys.remove(current)
        }
        println("B: $currentValue")
    }

    private fun readFile(regex: Regex, it: String) {
        val (_, name, expr) = regex.find(it)!!.groupValues

        if (expr.toIntOrNull() != null) {
            valueMap[name] = expr.toLong()
        } else {
            val (v1, op, v2) = expr.split(" ")
            opMonkeys.add(OperationMonkey(name, v1, v2, op[0]))
        }
    }

    private fun doOp(v1: Long, v2: Long, op: Char): Long {
        return when (op) {
            '+' -> v1 + v2
            '-' -> v1 - v2
            '*' -> v1 * v2
            '/' -> v1 / v2
            else -> throw Exception()
        }
    }

    private fun doReverseOp(res: Long, variable: Long, op: Char, isFirst: Boolean): Long {
        if (isFirst) {
            val reverseOp = when (op) {
                '+' -> '-'
                '-' -> '+'
                '*' -> '/'
                '/' -> '*'
                else -> throw Exception()
            }

            return doOp(res, variable, reverseOp)
        } else {
            return when (op) {
                '+' -> doOp(res, variable, '-')
                '-' -> doOp(variable, res, op)
                '*' -> doOp(res, variable, '/')
                '/' -> doOp(variable, res, op)
                else -> doOp(res, variable, op)
            }
        }
    }
}
