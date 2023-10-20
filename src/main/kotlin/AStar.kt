fun shortestIlandSurroundingPath(
    minRouteLength: Int,
    current: Vector2D,
    target: Vector2D,
    surroundingWater: Set<Vector2D>,
    visited: MutableSet<Vector2D> = mutableSetOf(),
    currentPath: MutableList<Vector2D> = mutableListOf()
): List<Vector2D>? {
    currentPath.add(current)
    if (!checkRoute(currentPath)) {
        currentPath.removeAt(currentPath.size - 1)
        return null
    }
    if (current == target) {
        if (currentPath.size <= minRouteLength) {
            currentPath.removeAt(currentPath.size - 1)
            return null
        }
        return currentPath
    }
    visited.add(current)
    for (d in Vector2D.allDirections) {
        val newPos = current + d
        if (!surroundingWater.contains(newPos)) {
            //Move not possible 1
            continue
        }
        //Move possible
        if (!visited.contains(newPos)) {
            val result =
                shortestIlandSurroundingPath(minRouteLength, newPos, target, surroundingWater, visited, currentPath)
            if (result != null) {
                return result
            }
        }
    }

    visited.remove(current)
    currentPath.removeAt(currentPath.size - 1)
    return null
}
