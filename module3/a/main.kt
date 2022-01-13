import java.util.*

class KnapsackSolver(private var capacity: Int) {
    data class Item(
        var weight: Int,
        val cost: Int
    )
    data class TakenItems(var indexes: BitSet) {
        // indexes show us items that we have taken
        var cost = 0
        var weight = 0
    }
    private var items = mutableListOf<Item>()
    private var dp = mutableListOf<TakenItems>()
    private var weightGcd = 0 // great common divisor of all weights allows us to shorten length of dp

    fun insert(weight: Int, cost: Int) {
        items.add(Item(weight, cost))
    }
    fun calculate(): Result {
        modifyWeights()
        fillTable()
        return formResult()
    }
    private fun modifyWeights() { // divides capacity and items' weights on greater common divisor
        weightGcd = getGcdOfAll()
        capacity /= weightGcd
        for (item in items) {
            item.weight /= weightGcd
        }
    }

    private fun getGcdOfAll(): Int { // returns gcd of capacity and all items' weights
        var tmpGcd = capacity
        for (i in 0 until items.size) {
            tmpGcd = gcd(tmpGcd, items[i].weight)
        }
        return tmpGcd
    }

    private fun gcd(value1: Int, value2: Int): Int { // gcd of two values
        var a = value1
        var b = value2
        while (a > 0 && b > 0) {
            if (a > b) {
                a = a.rem(b)
            } else {
                b = b.rem(a)
            }
        }
        return (a + b)
    }

    private fun fillTable() {
        // initializes empty dp
        // index of dp is weight
        for (i in 0..capacity) {
            dp.add(TakenItems(BitSet(items.size)))
        }
        for (i in 0 until items.size) {
            for (j in capacity downTo 0) {
                if (items[i].weight == 0) {
                    // if weight of item is 0, we will take this item
                    dp[j].indexes.set(i)
                    dp[j].cost += items[i].cost
                } else if (items[i].weight == j) {
                    if (items[i].cost > dp[j].cost) {
                        dp[j].cost = items[i].cost + dp[0].cost
                        dp[j].indexes.clear()
                        //logic or for setting up all 0-weight items
                        dp[j].indexes.or(dp[0].indexes)
                        dp[j].indexes.set(i)
                        dp[j].weight = items[i].weight
                    }
                } else if (items[i].weight < j) {
                    if (items[i].cost + dp[j - items[i].weight].cost > dp[j].cost) {
                        dp[j].indexes.clear()
                        // logic or for setting up all indexes in dp[j - items[i].weight]
                        dp[j].indexes.or(dp[j - items[i].weight].indexes)
                        dp[j].weight = items[i].weight + dp[j - items[i].weight].weight
                        dp[j].indexes.set(i)
                        dp[j].cost = items[i].cost + dp[j - items[i].weight].cost
                    }
                }  else break // if we can't take item, then break
            }
        }
    }

    fun formResult(): Result {
        val totalCost = dp.last().cost
        val totalWeight = dp.last().weight * weightGcd

        var indexes = mutableListOf<Int>()
        var tmpIndex = dp.last().indexes.nextSetBit(0)
        while (tmpIndex != -1) {
            // adds indexes of taken items
            indexes.add(tmpIndex + 1)
            tmpIndex = dp.last().indexes.nextSetBit(tmpIndex + 1)
        }
        return Result(totalWeight, totalCost, indexes)
    }
    data class Result(
        val totalWeight: Int,
        val totalCost: Int,
        val indexes: List<Int>
    )
}

fun main() {
    val scanner = Scanner(System.`in`)
    var stringWithCapacity = ""
    while (scanner.hasNextLine()) {
        try {
            // first string must have one positive number
            stringWithCapacity = scanner.nextLine()
            if (stringWithCapacity.isEmpty()) continue
            for (symbol in stringWithCapacity) {
                if (!symbol.isDigit()) throw IllegalArgumentException()
            }
            break
        } catch (exception: Exception) {
            println("error")
        }
    }
    val capacity = stringWithCapacity.toInt()
    val solver = KnapsackSolver(capacity)
    while (scanner.hasNextLine()) {
        try {
            val stringWithWeightAndCost = scanner.nextLine()

            if (stringWithWeightAndCost.isEmpty()) continue

            val parsedValues = parseInput(stringWithWeightAndCost)
            val weight = parsedValues.first
            val cost = parsedValues.second
            solver.insert(weight, cost)
        } catch (exception: Exception) {
            println("error")
        }
    }
    val answer = solver.calculate()
    print(answer.totalWeight)
    print(" ")
    println(answer.totalCost)
    for (index in answer.indexes) {
        println(index)
    }
}

fun parseInput(input: String): Pair<Int, Int> {
    // used for parsing item
    if (!input[0].isDigit()) throw IllegalArgumentException()
    var indexOfSpace = -1
    for ((index, symbol) in input.withIndex()) {
        if (symbol == ' ' && index != input.lastIndex) {
            if (indexOfSpace != -1) throw IllegalArgumentException() // if input has more than one ' ' symbol
            indexOfSpace = index
        } else if (!symbol.isDigit()) throw IllegalArgumentException()
    }
    if (indexOfSpace == -1) throw IllegalArgumentException()
    val weight = input.substring(0, indexOfSpace).toInt()
    val cost = input.substring(indexOfSpace + 1,input.length).toInt()
    return Pair(weight, cost)
}
