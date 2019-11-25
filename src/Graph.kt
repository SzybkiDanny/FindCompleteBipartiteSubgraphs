class Graph private constructor(private val adjacencyList: Map<Int, Set<Int>>) {
    var vertices = adjacencyList.keys; private set

    fun getSuccessors(vertex: Int) = adjacencyList[vertex] ?: emptySet()

    fun hasAnyEdgeBetween(vertex1: Int, vertex2: Int) =
        getSuccessors(vertex1).contains(vertex2) || getSuccessors(vertex2).contains(vertex1)

    fun hasDirectedEdgeBetween(vertexFrom: Int, vertexTo: Int) = getSuccessors(vertexFrom).contains(vertexTo)

    fun hasSelfLoop(vertex: Int) = hasDirectedEdgeBetween(vertex, vertex)

    companion object {
        fun loadFromFile(dataLoader: EventLogGraphLoader, inputDataPath: String) : Graph {
            val adjacencyList = mutableMapOf<Int, MutableSet<Int>>()

            dataLoader.getEdges(inputDataPath).forEach {
                adjacencyList
                    .getOrPut(it.first) { mutableSetOf() }
                    .add(it.second)
                adjacencyList
                    .getOrPut(it.second) { mutableSetOf() }
            }

            return Graph(adjacencyList)
        }
    }
}