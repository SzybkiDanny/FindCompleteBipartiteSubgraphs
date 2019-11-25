import java.nio.file.Files
import java.nio.file.Path

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Specify input data file path")
        return
    }

    val inputPath = args.first()

    if (Files.notExists(Path.of(inputPath))) {
        println("The specified file does not exist")
        return
    }

    try {
        val graphLoader = EventLogGraphLoader()
        val bicliqueCoverProblem = BicliqueCoverProblem(Graph.loadFromFile(graphLoader, inputPath))

        bicliqueCoverProblem.solve()

        bicliqueCoverProblem.getResult().forEach {
            println("({${it.first.map { vertex -> FirstCharacter.plus(vertex) }.joinToString()}}, " +
                    "{${it.second.map { vertex -> FirstCharacter.plus(vertex) }.joinToString()}}})")
        }
    }
    catch (e: IllegalArgumentException) {
        println("Unexpected input sequence. Only characters from [a-z] range are permitted. ${e.message}")
    }
    catch (e: Exception) {
        println("Unknown problem occurred: ${e.message}")
    }
}