import java.io.File

fun main() {
    Day7().run()
}

class Day7 {

    class Node(var type: String = "dir", var size: Int = 0, var path: String, val parent: Node?) {
        val children: MutableList<Node> = ArrayList()
    }

    fun run() {
        val root = Node("dir", 0, "/", null)
        var current = root;
        var readMode = false;

        File("src/main/resources/day7.txt").forEachLine {
            if (readMode && !it.startsWith("$")) {
                val args = it.split(" ", limit = 2)

                if (current.children.none { child -> child.path == args[1] }) {
                    if (args[0] == "dir") {
                        current.children.add(Node("dir", 0, args[1], current))
                    } else {
                        current.children.add(Node("file", args[0].toInt(), args[1], current))
                    }
                }
            }

            if (it.startsWith("$")) {
                readMode = false;
                val command = it.substring(2)
                if (command.startsWith("cd ")) {
                    when (val path = command.substring(3)) {
                        "/" -> {
                            current = root
                        }
                        ".." -> {
                            current = current.parent ?: current
                        }
                        else -> {
                            var pathNode = current.children.find { node -> node.path == path }
                            if (pathNode == null) {
                                pathNode = Node("dir", 0, path, current)
                                current.children.add(pathNode)
                            }
                            current = pathNode
                        }
                    }
                } else if (command.startsWith("ls")) {
                    readMode = true;
                }
            }
        }

        calculateDirectorySizes(root)
        val sum = getSummedDirectories(root)
        val unused = 70000000 - root.size
        val best = findBest(root, unused)

        println("A: $sum")
        println("B: $best")
    }

    private fun findBest(currentNode: Node, unused: Int): Int {
        var best = currentNode.size

        currentNode.children.forEach { child ->
            if (child.type == "dir") {
                if (child.size + unused >= 30000000) {
                    val candidate = findBest(child, unused)
                    if (candidate <= best) {
                        best = candidate
                    }
                }
            }
        }

        return best
    }

    private fun calculateDirectorySizes(currentNode: Node): Int {
        var total = 0

        currentNode.children.forEach { child ->
            total += if (child.type == "file") {
                child.size
            } else {
                calculateDirectorySizes(child)
            }
        }

        currentNode.size = total
        return total
    }

    private fun getSummedDirectories(currentNode: Node): Int {
        var total = 0

        currentNode.children.forEach {child ->
            if (child.type == "dir") {
                total += getSummedDirectories(child)
            }
        }

        if (currentNode.size <= 100000) {
            total += currentNode.size
        }
        return total
    }

}
