import java.io.File

const val FirstCharacter = 'a' - 1

class EventLogGraphLoader {
    fun getEdges(inputDataPath: String) = sequence {
        val inputData = File(inputDataPath)

        inputData.readLines().forEach {
            if (it.isBlank()) {
                return@forEach
            }
            else if (!it.matches("[a-z]+".toRegex())) {
                throw IllegalArgumentException("Unexpected character found in input line: $it")
            }

            for (x in 0 until it.length - 1) {
                yield(Pair(it[x] - FirstCharacter, it[x + 1] - FirstCharacter))
            }
        }
    }
}