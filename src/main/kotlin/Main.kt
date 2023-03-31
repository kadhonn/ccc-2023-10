import Utils.Companion.fileForExample
import java.io.File

val CURRENT_LEVEL = "level1"

fun compute(inputFile: File, outputFile: File) {

    var n = 0
    val lines = inputFile.readLines();

    outputFile.writeText("18")
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