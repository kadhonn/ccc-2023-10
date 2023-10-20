import java.io.File
import java.util.*
import kotlin.math.max
import kotlin.math.min

val CURRENT_LEVEL = "level3"

fun compute(inputFile: File, outputFile: File) {

    val scanner = Scanner(inputFile.inputStream())

    val mapSize = scanner.nextInt()
    scanner.nextLine();

    val map = mutableMapOf<Vector2D, Char>()
    for (i in 0 until mapSize) {
        val currentLine = scanner.nextLine()
        check(currentLine.length == mapSize)

        for (j in 0 until mapSize) {
            val currentPos = Vector2D(j, i)
            map[currentPos] = currentLine[j]
        }
    }

    val coordinateCount = scanner.nextInt()
    scanner.nextLine()
    var result = ""

    for (i in 0 until coordinateCount) {
        val coordinatePair = scanner.nextLine()
        val coords = coordinatePair.split(" ")
        val currentRoute = coords.map { Vector2D.fromString(it) }
        check(currentRoute.isNotEmpty())

        val routeHasDistinctCoordinates = currentRoute.toSet().size == currentRoute.size

        val interRoutePoints = mutableListOf<Pair<Pair<Int,Int>,Pair<Int,Int>>>()

        currentRoute.windowed(2,1,false).map {
            val a = it[0]
            val b = it[1]
            val intersection = Pair(Pair(min(a.x, b.x), max(a.x,b.x)),Pair(min(a.y, b.y), max(a.y,b.y)))
            interRoutePoints.add(intersection)
        }

        val routeHasNoIntersection = interRoutePoints.toSet().size == interRoutePoints.size

        val currentResult = if (routeHasNoIntersection && routeHasDistinctCoordinates) {
            "VALID"
        } else {
            "INVALID"
        }

        result = result + "\n" + currentResult
    }
    val finalResult = result.trim().replace("\n", "\r\n") + "\r\n"
    outputFile.writeText(finalResult)
}

fun checkSameIsland(map: MutableMap<Vector2D, Char>, start: Vector2D, end: Vector2D): Boolean {
    val alreadyChecked = mutableSetOf<Vector2D>()

    val toCheck = mutableSetOf(start)
    while (toCheck.isNotEmpty()) {
        val current = toCheck.first()
        if (current == end) {
            return true
        }

        toCheck.remove(current)
        if (alreadyChecked.contains(current)) {
            continue
        }
        alreadyChecked.add(current)

        val type = map[current]
        if (type == null || type == 'W') {
            continue
        }

        Vector2D.allDirections.forEach {
            toCheck.add(current.plus(it))
        }
    }

    return false
}


data class Vector2D(val x: Int, val y: Int){
    operator fun plus(b: Vector2D): Vector2D {
        return Vector2D(x + b.x, y + b.y)
    }

    operator fun times(b: Int): Vector2D {
        return Vector2D(x * b, y * b)
    }

    operator fun minus(b: Vector2D): Vector2D {
        return Vector2D(x - b.x, y - b.y)
    }
    fun rotateClockwise() : Vector2D
    {
        return Vector2D(y, -x)
    }
    fun rotateCounterClockwise() : Vector2D
    {
        return Vector2D(-y, x)
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

        val allDirections = setOf(LEFT, UP, RIGHT, DOWN)

        fun fromString(str: String): Vector2D {
            val parts = str.split(",").map { it.toInt() }
            check(parts.size == 2)
            return Vector2D(parts[0], parts[1])
        }
    }
}