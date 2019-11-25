class BicliqueCoverProblem(private val problemGraph: Graph) {
    private val solution = mutableSetOf<BipartiteGraph>()

    fun solve() {
        val vertices = problemGraph.vertices.filterNot { problemGraph.hasSelfLoop(it) }.toHashSet()
        var isAnyChange = false

        generateInitialSolution(vertices)

        do {
            isAnyChange = mergeSubgraphs().or(removeRedundantSubgraphs())
        } while (isAnyChange)
    }

    fun getResult() = solution.map { Pair(it.predecessors, it.successors) }

    private fun generateInitialSolution(vertices: Set<Int>) {
        vertices.forEach { predecessor ->
            problemGraph.getSuccessors(predecessor)
                .filter { successor -> vertices.contains(successor) }
                .filterNot { successor -> problemGraph.hasDirectedEdgeBetween(successor, predecessor) }
                .forEach { successor -> solution.add(BipartiteGraph(mutableSetOf(predecessor), mutableSetOf(successor))) }
        }
    }

    private fun mergeSubgraphs(): Boolean {
        val currentSubgraphs = solution.toList()
        var isAnyChange = false

        for (x in currentSubgraphs.indices) {
            for (y in x + 1 until currentSubgraphs.size) {
                val newSubgraph = tryToMergeSubgraphs(
                    currentSubgraphs[x],
                    currentSubgraphs[y]
                )

                if (newSubgraph != null) {
                    isAnyChange = solution.add(newSubgraph) || isAnyChange
                }
            }
        }

        return isAnyChange
    }

    private fun removeRedundantSubgraphs(): Boolean {
        val currentSubgraphs = solution.toList()
        var isAnyChange = false

        for (x in currentSubgraphs.indices) {
            for (y in x + 1 until currentSubgraphs.size) {
                if (currentSubgraphs[x] isSubgraphOf currentSubgraphs[y]) {
                    isAnyChange = solution.remove(currentSubgraphs[x]) || isAnyChange
                }
                else if (currentSubgraphs[y] isSubgraphOf currentSubgraphs[x]) {
                    isAnyChange = solution.remove(currentSubgraphs[y]) || isAnyChange
                }
            }
        }

        return isAnyChange
    }

    private fun tryToMergeSubgraphs(
        firstSubgraph: BipartiteGraph,
        secondSubgraph: BipartiteGraph): BipartiteGraph? {
        if (canSetsBeMerged(firstSubgraph.predecessors, secondSubgraph.predecessors)) {
            val newSubgraph = firstSubgraph.unionPredecessorsIntersectSuccessors(secondSubgraph)

            if (!newSubgraph.isAnyPartEmpty() && !newSubgraph.isSubgraphOf(firstSubgraph)) {
                return newSubgraph
            }
        }

        if (canSetsBeMerged(firstSubgraph.successors, secondSubgraph.successors)) {
            val newSubgraph = firstSubgraph.intersectPredecessorsUnionSuccessors(secondSubgraph)

            if (!newSubgraph.isAnyPartEmpty() && !newSubgraph.isSubgraphOf(firstSubgraph)) {
                return newSubgraph
            }
        }

        return null
    }

    private fun canSetsBeMerged(firstSet: Set<Int>, secondSet: Set<Int>) =
        firstSet.all { firstSetElement ->
            secondSet.all { secondSetElement ->
                firstSetElement == secondSetElement || !problemGraph.hasAnyEdgeBetween(
                    firstSetElement,
                    secondSetElement
                )
            }
        }

    private inner class BipartiteGraph(val predecessors: Set<Int>, val successors: Set<Int>) {
        fun unionPredecessorsIntersectSuccessors(otherGraph: BipartiteGraph) =
            BipartiteGraph(predecessors.union(otherGraph.predecessors), successors.intersect(otherGraph.successors))

        fun intersectPredecessorsUnionSuccessors(otherGraph: BipartiteGraph) =
            BipartiteGraph(predecessors.intersect(otherGraph.predecessors), successors.union(otherGraph.successors))

        fun isAnyPartEmpty() = predecessors.isEmpty() || successors.isEmpty()

        infix fun isSubgraphOf(otherGraph: BipartiteGraph) =
            otherGraph.predecessors.containsAll(predecessors) && otherGraph.successors.containsAll(successors)

        override fun hashCode() = predecessors.hashCode() + successors.hashCode()

        override fun equals(other: Any?) = if (other != null) equals(other as BipartiteGraph) else false

        private fun equals(other: BipartiteGraph) = predecessors == other.predecessors && successors == other.successors
    }
}