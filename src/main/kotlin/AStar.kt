fun shortestIlandSurroundingPath(
    minRouteLength: Int,
    current: Vector2D,
    target: Vector2D,
    surroundingWater: Set<Vector2D>,
    allSolutions: MutableList<List<Vector2D>>,
    visited: MutableSet<Vector2D> = mutableSetOf(),
    currentPath: MutableList<Vector2D> = mutableListOf()
) {
    currentPath.add(current)
    if (!checkRoute(currentPath)) {
        currentPath.removeAt(currentPath.size - 1)
        return
    }
    if (current == target) {
        if (currentPath.size <= minRouteLength) {
            currentPath.removeAt(currentPath.size - 1)
            return
        }
        allSolutions.add(currentPath.toList())
        currentPath.removeAt(currentPath.size - 1)
        return
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
            shortestIlandSurroundingPath(
                minRouteLength,
                newPos,
                target,
                surroundingWater,
                allSolutions,
                visited,
                currentPath
            )
        }
    }

    visited.remove(current)
    currentPath.removeAt(currentPath.size - 1)
}
