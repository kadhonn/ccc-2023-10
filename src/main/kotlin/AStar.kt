import java.util.*

data class State(val pos: Vector2D, val distance: Int, val time: Int) : Comparable<State> {
    override fun compareTo(other: State): Int {
        return (time + distance).compareTo(other.time + other.distance)
    }
}

private fun shortestPath(
    start: Vector2D,
    target: Vector2D,
    startTime: Int,
    walls: MutableSet<Vector2D>,
    width: Int,
    height: Int,
    blizzardsUp: MutableMap<Int, MutableSet<Int>>,
    blizzardsDown: MutableMap<Int, MutableSet<Int>>,
    blizzardsLeft: MutableMap<Int, MutableSet<Int>>,
    blizzardsRight: MutableMap<Int, MutableSet<Int>>
): Int {
    val queue = PriorityQueue<State>()
    val visited = mutableSetOf<State>()
    val initialState = State(start, (target - start).manhattan(), startTime)
    queue.add(initialState)
    visited.add(initialState)

    var current = initialState
    while (current.pos != target) {
        val newTime = current.time + 1
        for (d in Vector2D.allDirections) {
            val newPos = current.pos + d
            if (walls.contains(newPos) || newPos.y < 0 || newPos.x < 0 || newPos.x > (width + 1) || newPos.y > (height + 1)) {
                //Move not possible 1
                continue;
            }
            val containsBlizzard =
                blizzardsUp[newPos.x].orEmpty()
                    .contains(((newPos.y + (newTime % height) - 1 + height) % height) + 1)
                        || blizzardsDown[newPos.x].orEmpty()
                    .contains(((newPos.y - (newTime % height) - 1 + height) % height) + 1)
                        || blizzardsLeft[newPos.y].orEmpty()
                    .contains(((newPos.x + (newTime % width) - 1 + width) % width) + 1)
                        || blizzardsRight[newPos.y].orEmpty()
                    .contains(((newPos.x - (newTime % width) - 1 + width) % width) + 1)
            if (!containsBlizzard) {
                //Move possible
                val newState = State(newPos, (target - newPos).manhattan(), newTime)
                if (!visited.contains(newState)) {
                    visited.add(newState)
                    queue.add(newState)
                }
            }
        }
        current = queue.first()
        queue.remove(current)
    }
    val time = current.time
    return time
}
