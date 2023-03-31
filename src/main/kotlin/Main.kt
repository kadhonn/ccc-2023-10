import Utils.Companion.fileForExample
import java.io.File
import java.util.stream.IntStream.range

val CURRENT_LEVEL = "level2"

fun compute(inputFile: File, outputFile: File) {

//    var n = 0
    val lines = inputFile.readLines();

    val tournamentCount = lines[0].split(" ")[0].toInt()
    val fighterCount = lines[0].split(" ")[1].toInt()
//    var n = lines[0].toInt()

    val roundsPerLine = 2

    val tournamentLines = lines.subList(1, lines.size)
    var result = ""

    for(tournament in tournamentLines) {
        var currentRound = tournament
        for(rount in range(0, roundsPerLine)) {
            currentRound = currentRound.windowed(2, 2).map {
                determineFightWinner(it)
            }.joinToString("")
        }
        result += currentRound + "\n"
    }

//    for (i in range(0, n)) {
//        val fight = lines[1 + i]
//
//        result += determineFightWinner(fight)
//        result += "\n"
//    }
    outputFile.writeText(result.replace("\n", "\r\n"))
}

private fun determineFightWinner(fight: String) = if (fight.contains('R') && fight.contains('P')) {
    "P"
} else if (fight.contains('S') && fight.contains('P')) {
    "S"
} else if (fight.contains('R') && fight.contains('S')) {
    "R"
} else {
    fight[0].toString()
}

fun main() {
    Utils.testFile(fileForExample("example"))
}


data class Vector2D(val x: Int, val y: Int) {
    operator fun plus(b: Vector2D): Vector2D {
        return Vector2D(x + b.x, y + b.y)
    }

    operator fun times(b: Int): Vector2D {
        return Vector2D(x * b, y * b)
    }

    operator fun minus(b: Vector2D): Vector2D {
        return Vector2D(x - b.x, y - b.y)
    }

    fun manhattan(): Int {
        return Math.abs(x) + Math.abs(y)
    }

    companion object {
        val ZERO = Vector2D(0, 0)
        val LEFT = Vector2D(-1, 0)
        val UP = Vector2D(0, -1)
        val RIGHT = Vector2D(1, 0)
        val DOWN = Vector2D(0, 1)

        val allDirections = setOf(ZERO, LEFT, UP, RIGHT, DOWN)
    }
}