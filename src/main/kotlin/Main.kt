import Utils.Companion.fileForExample
import java.io.File
import java.util.Scanner
import java.util.stream.IntStream.range

val CURRENT_LEVEL = "level2"

fun compute(inputFile: File, outputFile: File) {

    val scanner = Scanner(inputFile.inputStream())

    val mapSize = scanner.nextInt()
    scanner.nextLine();

    val map = mutableMapOf<Vector2D,Char>()
    for(i in 0 until mapSize) {
        val currentLine = scanner.nextLine()
        check(currentLine.length == mapSize)

        for(j in 0 until mapSize) {
            val currentPos = Vector2D(j,i)
            map[currentPos] = currentLine[j]
        }
    }

    val coordinateCount = scanner.nextInt()
    scanner.nextLine()
    var result = ""

    for(i in 0  until coordinateCount) {
        val currentCoordinate = Vector2D.fromString(scanner.nextLine())
        val tileType = map.getValue(currentCoordinate)
        result = result + "\n" + tileType;
    }
    val finalResult = result.trim().replace("\n", "\r\n") + "\r\n"
    outputFile.writeText(finalResult)
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

        fun fromString(str : String) : Vector2D {
            val parts = str.split(",").map { it.toInt() }
            check(parts.size == 2)
            return Vector2D(parts[0], parts[1])
        }
    }
}