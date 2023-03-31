import Utils.Companion.fileForExample
import java.io.File
import java.util.stream.IntStream.range

val CURRENT_LEVEL = "level3"

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
        val fighterPairs = tournament.split(" ")
        check(fighterPairs.size <= 3)
        val fighterMap =
            fighterPairs.associate { it[it.length - 1] to it.substring(0, it.length - 1).toInt() }.toMutableMap()

        var pool = Pool(fighterMap, fighterCount)
        pool.getPair("SP")
        val canAssureSPreachesAndWinsRound2 = pool.getPair("PR") || pool.getPair("PP")
        check(canAssureSPreachesAndWinsRound2) { " Cant assure" }

        while (!pool.getTournamentFull()) {
            var testPool = pool.copy()
            if ((testPool.getPair("RR")) && (testPool.getPair("PR") || testPool.getPair("PP"))) {
                pool = testPool
                continue
            }
            testPool = pool.copy()
            if ((testPool.getPair("RS")) && (testPool.getPair("PR") || testPool.getPair("PP"))) {
                pool = testPool
                continue
            }
            else
            {
                pool = pool.get2PairsFrom(setOf("PR", "SS", "PP", "SP"))
                continue;
            }
//            else if (setOf("PR", "SS", "PP", "SP").any {
//                    val testPool2 = testPool.copy()
//                    val a = testPool2.getPair(it)
//                    val b = setOf("PR", "SS", "PP", "SP").any {
//                        testPool2.getPair(it)
//                    }
//                    if (a && b) {
//                        testPool = testPool2
//                        true
//                    } else
//                        false
//                })
//            else if((testPool.getPair("PR") || testPool.getPair("SS") || testPool.getPair("PP") || testPool.getPair("SP") ) && (testPool.getPair("PR")  || testPool.getPair("SS") ||testPool.getPair("PP") || testPool.getPair("SP")))
//            {
//                pool = testPool
//                continue
//            } else {
//                println("dont know")
//            }
        }
        check(validateSolution(pool.tournament))
        result += pool.tournament + "\n"

//        fighterMap['S'] = fighterMap.getValue('S') - 1
//        fighterMap['P'] = fighterMap.getValue('P') - 1

//        var myTournament = "SP"


//        var currentRound = tournament
//        for (rount in range(0, roundsPerLine)) {
//            currentRound = currentRound.windowed(2, 2).map {
//                determineFightWinner(it)
//            }.joinToString("")
//        }
//        result += currentRound + "\n"
    }

    outputFile.writeText(result.replace("\n", "\r\n"))
}

data class Pool(var fighterMap: MutableMap<Char, Int>, val tournamentSize: Int, var tournament: String = "") {

    fun getTournamentFull(): Boolean {
        check(tournament.length <= tournamentSize)
        return tournament.length == tournamentSize
    }

    fun testPair(pair: String): Boolean {
        val tmp = fighterMap.toMutableMap()
        for (c in pair) {
            val cnt = tmp[c] ?: 0
            if (cnt <= 0) {
                return false;
            }
            tmp[c] = cnt - 1
        }
        return true;
    }
    fun get2PairsFrom(possible: Set<String>) : Pool
    {
        val new = possible.map {
            val tmp = this.copy()
            val a = tmp.getPair(it)
            if(!a) {
                null
            }
            else {
                val b = possible.any { tmp.getPair(it) }
                if(!b){
                    null
                }
                else
                {
                    tmp
                }
            }
        }.filterNotNull()


        check(new.size >= 1) {

            println("here") }
        return new.first()
    }

    fun getPair(pair: String): Boolean {
        val tmp = fighterMap.toMutableMap()
        for (c in pair) {
            val cnt = tmp[c] ?: 0
            if (cnt <= 0) {
                return false;
            }
            tmp[c] = cnt - 1
        }
        fighterMap = tmp;
        tournament += pair
        return true;
    }

    fun getPairs(pair: String, cnt: Int): Int {
        for (i in range(0, cnt)) {
            if (!getPair(pair)) {
                return i
            }
        }
        return cnt;
    }
}

private fun validateSolution(tournament: String): Boolean {
    val roundsPerLine = 2
    var currentRound = tournament
    for (rount in range(0, roundsPerLine)) {
        currentRound = currentRound.windowed(2, 2).map {
            determineFightWinner(it)
        }.joinToString("")
    }
    return !currentRound.contains("R") && currentRound.contains("S")
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