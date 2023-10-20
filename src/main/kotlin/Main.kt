import java.io.File
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

val CURRENT_LEVEL = "level5"

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
        check(currentRoute.size == 1)
        val currentLandStart = currentRoute[0]

        val surroundingWater = getSurroundingWater(map, currentLandStart)
        val route = getSortedRoute(surroundingWater)

        //val route = shortestPath(currentRoute[0], currentRoute[1], map)
        check(route.isNotEmpty())

        val currentResult =
            route.joinToString(" ")

        result = result + "\n" + currentResult
    }
    val finalResult = result.trim().replace("\n", "\r\n") + "\r\n"
    outputFile.writeText(finalResult)
}

fun getSortedRoute(tiles : Set<Vector2D>): List<Vector2D> {
    val moves = mutableListOf<Vector2D>()
    val toPickFrom = tiles.toMutableSet()


    var current = toPickFrom.first()
    toPickFrom.remove(current)
    moves.add(current)

    val possibleNext = Vector2D.allDirections.map { current + it }
    val target = possibleNext.first { toPickFrom.contains(it) }

    val currentRoute = shortestIlandSurroundingPath(current, target, toPickFrom)

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

    return currentRoute

//
//    for(i in 1 until tiles.size) {
//        val possibleNext = Vector2D.allDirections.map { current + it }
//        val firstPossible = possibleNext.first { toPickFrom.contains(it) }
//        toPickFrom.remove(firstPossible)
//        moves.add(firstPossible)
//        current = firstPossible
//    }
//    return moves
}

fun getSurroundingWater(map: Map<Vector2D, Char>, start: Vector2D): MutableSet<Vector2D> {
    val sameIlandTiles = mutableSetOf<Vector2D>()
    val surroundingWaterTiles = mutableSetOf<Vector2D>()

    val toCheck = mutableSetOf(start)
    while (toCheck.isNotEmpty()) {
        val current = toCheck.first()
        toCheck.remove(current)

        if (sameIlandTiles.contains(current)) {
            continue
        }
        val currentTerrain = map[current]
        if(currentTerrain == null) {
        }
        else if(currentTerrain == 'L') {
            sameIlandTiles.add(current)
            Vector2D.landDirections.forEach {
                toCheck.add(current.plus(it))
            }
        }
        else if(currentTerrain == 'W') {
            surroundingWaterTiles.add(current)
        }
        else{
            throw RuntimeException("cant happen")
        }
    }

    return surroundingWaterTiles
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

    fun rotateClockwise(): Vector2D {
        return Vector2D(y, -x)
    }

    fun rotateCounterClockwise(): Vector2D {
        return Vector2D(-y, x)
    }

    fun shortestPath(to: Vector2D): Int {
        val xDist = abs(to.x - x)
        val yDist = abs(to.y - y)
        return max(xDist, yDist)
    }
    fun noHeuristic(to: Vector2D): Int {
        return 0;
    }

    override fun toString(): String {
        return "$x,$y"
    }

    companion object {
        val ZERO = Vector2D(0, 0)
        val LEFT = Vector2D(-1, 0)
        val UP = Vector2D(0, -1)
        val RIGHT = Vector2D(1, 0)
        val DOWN = Vector2D(0, 1)
        val UP_LEFT = UP + LEFT
        val UP_RIGHT = UP + RIGHT
        val DOWN_LEFT = DOWN + LEFT
        val DOWN_RIGHT = DOWN + RIGHT

        val landDirections = listOf(
            LEFT, UP, RIGHT, DOWN)
        val allDirections =  listOf(
            UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT,
        ) + landDirections

        fun fromString(str: String): Vector2D {
            val parts = str.split(",").map { it.toInt() }
            check(parts.size == 2)
            return Vector2D(parts[0], parts[1])
        }
    }


}