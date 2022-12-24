import java.io.File

fun main() {
    Day16().run()
}

class Day16 {

    class Node(
        val name: String,
        var flow: Int? = null,
        val connections: MutableList<Node> = ArrayList()
    ) {
        override fun toString(): String {
            return "Name: $name, flow: $flow, connections: ${connections.joinToString { it.name }}"
        }
    }

    private val stepMap = HashMap<String, MutableList<Pair<String, Int>>>()
    private val nodes = HashMap<String, Node>()

    fun run() {
        File("src/main/resources/day16.txt").forEachLine { row ->
            val regex = Regex("Valve ([A-Z]{2}) has flow rate=(\\d+); tunnels? leads? to valves? (.+)")
            val all = regex.find(row)?.groupValues
            if (all != null) {
                val (_, name, flow, neighbors) = all
                val neighborNodes = neighbors.split(", ")

                if (!nodes.containsKey(name)) {
                    nodes[name] = Node(name)
                }

                val curr = nodes[name]
                curr!!.flow = flow.toInt()
                neighborNodes.forEach {
                    if (nodes[it] != null) {
                        val other = nodes[it]!!
                        curr.connections.add(other)
                        other.connections.add(curr)
                    }
                }
            }
        }

        nodes.values
            .filter { it.flow != null && (it.flow!! > 0 || it.name == "AA") }
            .map { getAdjacencyValues(nodes, it) }
            .forEach { stepMap[it.first] = it.second }

        val best = explore("AA", 30, 0, HashSet())
        println("A: $best")
    }

    private fun getAdjacencyValues(nodes: HashMap<String, Node>, node: Node): Pair<String, MutableList<Pair<String, Int>>> {
        val visited = HashSet<String>()
        visited.add(node.name)

        val returnable = ArrayList<Pair<String, Int>>()

        val toVisit = node.connections
            .map { it.name }
            .toMutableSet()

        var distance = 1
        while (toVisit.isNotEmpty()) {
            toVisit.forEach { returnable.add(Pair(it, distance)) }
            visited.addAll(toVisit)

            val newVisitList = toVisit
                .asSequence()
                .mapNotNull { nodes[it]?.connections }
                .flatten()
                .map { it.name }
                .filterNot { visited.contains(it) }
                .toMutableSet()


            toVisit.clear()
            toVisit.addAll(newVisitList)

            distance++
        }


        return Pair(node.name, returnable.filter { nodes[it.first]?.flow != null && nodes[it.first]?.flow != 0 }.toMutableList())
    }

    private fun explore(currentNode: String, minutesLeft: Int, score: Int, visited: MutableSet<String>): Int {
        val newScore = score + minutesLeft * nodes[currentNode]!!.flow!!

        return stepMap[currentNode]!!
            .filter { it.first !in visited }
            .filter { it.second < minutesLeft }
            .maxOfOrNull {
                val newVisited = HashSet(visited).apply<HashSet<String>> { add(it.first) }
                explore(it.first, minutesLeft - it.second - 1, newScore, newVisited)
            } ?: newScore
    }
}
