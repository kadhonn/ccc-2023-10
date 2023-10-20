import java.util.*

data class State(val pos: Vector2D, val distance: Int, val time: Int, val path: List<Vector2D>) : Comparable<State> {
    override fun compareTo(other: State): Int {
        return (time + distance).compareTo(other.time + other.distance)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State

        if (pos != other.pos) return false
        if (distance != other.distance) return false
        if (time != other.time) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pos.hashCode()
        result = 31 * result + distance
        result = 31 * result + time
        return result
    }


}

 fun shortestPath(
    start: Vector2D,
    target: Vector2D,
    map: Map<Vector2D, Char>,
): List<Vector2D> {
    val queue = PriorityQueue<State>()
    val visited = mutableSetOf<State>()
    val initialState = State(start, start.shortestPath(target), 0, listOf(start))
    queue.add(initialState)
    visited.add(initialState)

    var current = initialState
    while (current.pos != target) {
        val newTime = current.time + 1
        for (d in Vector2D.allDirections) {
            val newPos = current.pos + d
            if (!map.containsKey(newPos) || map[newPos] == 'L') {
                //Move not possible 1
                continue
            }
            //Move possible
            val newState = State(newPos, newPos.shortestPath(target), newTime, current.path + newPos)
            if (!visited.contains(newState)) {
                visited.add(newState)
                queue.add(newState)
            }
        }
        current = queue.first()
        queue.remove(current)
    }
    return current.path
}
 fun shortestIlandSurroundingPath(
    start: Vector2D,
    target: Vector2D,
    surroundingWater : Set<Vector2D>,
): List<Vector2D> {
    val queue = PriorityQueue<State>()
    val visited = mutableSetOf<State>()
    val initialState = State(start, start.noHeuristic(target), 0, listOf(start))
    queue.add(initialState)
    visited.add(initialState)

    var current = initialState
    while (current.pos != target) {
        val newTime = current.time + 1
        for (d in Vector2D.allDirections) {
            val newPos = current.pos + d
            if (!surroundingWater.contains(newPos) || (newPos == target && current.path.last() == start && newTime < 3)) {
                //Move not possible 1
                continue
            }
            //Move possible
            val newState = State(newPos, newPos.noHeuristic(target), newTime, current.path + newPos)
            if (!visited.contains(newState)) {
                visited.add(newState)
                queue.add(newState)
            }
        }
        current = queue.first()
        queue.remove(current)
    }
    return current.path
}
