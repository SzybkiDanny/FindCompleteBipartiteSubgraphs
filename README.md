# FindCompleteBipartiteSubgraphs

## Design motivation and considerations:

### Responsibilites:
* `EventLogGraphLoader` - loads input data.
* `Graph` - represents instance of a problem in a form of graph. It contains basic operations assosiated with accessing its structure.
* `BicliqueProblemSolver` - contains logic for solving a specific problem. This is the place to put all the hacks and assumptions related to a problem.

## Potential improvements:

`EventLogGraphLoader`:
* it could be abstracted to support different input data formats. Each implementation should be input format specific.

`Graph`:
* it could be abstracted to support different internal structures of graphs that would serve specific purposes (e.g. memory efficient vs performance efficient, getting successors vs getting predecessors).

Some logical (universal) representation of input data could be used:
* it could be returned by every data loading component to completely decouple it from `Graph` class,
* it would enable easy transition between different `Graph` representations: every `Graph` implementation would need to implement two transition methods: *universal -> graph specific* and *graph specific -> universal*.

`ProblemSolver`:
* it could be abstracted. Existing solvers could be used to solve subproblems of other (new) problems,
* it would reduce code duplication,
* problems could be solved by e.g. transforming them to some other problems that already have existing solvers.

Displaying output:
* another component dedicated to displaying results of solved problems.