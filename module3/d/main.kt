import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.util.*
import kotlin.math.ln
import kotlin.math.log2
import kotlin.math.roundToInt

class BloomFilter() {
    private var primeNumbers = mutableListOf<UInt>()
    private var m: Int = 0 // size of bitset
    private lateinit var bitset: BitSet
    private val M = 2147483647u // 31-st Mersenne prime

    fun print(out: OutputStream) {
        val writer = BufferedWriter(OutputStreamWriter(out))
        val sb = StringBuilder()
        for (i in 0 until m) {
            if (bitset[i]) {
                sb.append(1)
            } else {
                sb.append(0)
            }
        }
        writer.write(sb.toString())
        
        //TODO writer.write("\n") technically disrupts encapsulation

        writer.write("\n")
        writer.flush()
    }

    fun add(key: ULong) {
        for (i in 0u until primeNumbers.size.toUInt()) {
            bitset.set(hash(i, key))
        }
    }

    fun search(key: ULong): Boolean {
        for (i in 0u until primeNumbers.size.toUInt()) {
            if (!bitset[hash(i, key)]) return false
        }
        return true
    }

    fun set(n: Int, P: Float): Pair<Int, Int> {
        if (n == 0 || P == 0.0f) throw IllegalArgumentException("n == 0 or p == 0")
        m = (-n * log2(P) / ln(2.0)).roundToInt()
        bitset = BitSet(m)
        val k = -log2(P).roundToInt()
        if (m <= 0 || k <= 0) throw IllegalArgumentException("m <= 0 or k <= 0")
        setPrimeNumbers(k)
        return Pair(m, k)
    }

    private fun setPrimeNumbers(amount: Int) {
        var sieveSequence = sequence {
            var value = 2u
            while (true) {
                yield(value++)
            }
        }
        for (i in 0 until amount) {
            val primeNumber = sieveSequence.first()
            primeNumbers.add(primeNumber)
            sieveSequence = sieveSequence
                .filter {
                    it.rem(primeNumber) != 0u
                }
        }
    }

    private fun hash(i: UInt, x: ULong) =
        ((i + 1u).rem(M) * x.rem(M) + primeNumbers[i.toInt()].rem(M)).rem(M).rem(m.toUInt()).toInt()
}

fun main() {
    val scanner = Scanner(System.`in`)
    val bf = BloomFilter()
    var n = 0 // approximate amount of elements
    var p = 0f // probability of false-positive answer
    if (scanner.hasNextLine()) {
        while (scanner.hasNextLine()) {
            try {
                // parsing "set" command
                val line = scanner.nextLine()
                if (line.isNotEmpty()) {
                    if (line.substring(0, 4) != "set ") throw NoSuchMethodException("unknown command '$line'")
                    // parsing arguments of set command
                    var indexOfSecondSpace = 0
                    // parsing firstArgument
                    for (i in 4 until line.length) {
                        if (line[i] == ' ') {
                            indexOfSecondSpace = i
                            break
                        }
                        if (!line[i].isDigit()) throw IllegalArgumentException("symbol '${line[i]}' in '$line' is not a digit")
                    }
                    if (indexOfSecondSpace < 5 || indexOfSecondSpace > line.length - 3) {
                        // 6 is because of shortest Int number # "set 1 xxxxx", where second space has 5th index
                        // 3 is because of shortest Float number # set xxxxx 0.0" where second space has length - 3 index
                        throw IllegalArgumentException("bad argument in '$line'")
                    }

                    // parsing second argument
                    if (line[indexOfSecondSpace + 1] != '0' || line[indexOfSecondSpace + 2] != '.') {
                        throw IllegalArgumentException("bad arguments in '$line'")
                    }
                    for (i in indexOfSecondSpace + 3 until line.length) {
                        if (!line[i].isDigit()) throw IllegalArgumentException("symbol '${line[i]}' in '$line' is not a digit")
                    }

                    n = line.substring(4, indexOfSecondSpace).toInt()
                    p = line.substring(indexOfSecondSpace + 1, line.length).toFloat()

                    // using set command
                    val setResult: Pair<Int, Int> = bf.set(n, p)
                    println("${setResult.first} ${setResult.second}")
                    break
                } else {
                    throw NoSuchMethodException("unknown command '$line'")
                }
            } catch (exception: Exception) {
                println("error")
            }
        }
        while (scanner.hasNext()) {
            try {
                val line = scanner.nextLine()
                val commandAndKey = parseLine(line)
                if (line.isNotEmpty()) {
                    when (commandAndKey.first) {
                        "add" -> {
                            bf.add(commandAndKey.second!!)
                        }
                        "search" -> {
                            val isFound = bf.search(commandAndKey.second!!)
                            if (isFound) println("1")
                            else println("0")
                        }
                        "print" -> {
                            if (commandAndKey.second != null) throw NoSuchMethodException("error")
                            bf.print(System.out)
                        }
                        else -> {
                            throw NoSuchMethodException("error")
                        }
                    }
                }
            } catch (exception: Exception) {
                println("error")
            }
        }
    }
}

fun parseLine(line: String): Pair<String, ULong?> {
    // for parsing all commands except "set"
    var lengthOfCommand = line.length
    for ((index, symbol) in line.withIndex()) {
        if (symbol == ' ') {
            lengthOfCommand = index
            break
        } else if (!symbol.isLetter()) {
            throw NoSuchMethodException("error")
        }
    }
    if (lengthOfCommand == line.length) {
        return Pair(line, null)
    } else {
        val command = line.substring(0, lengthOfCommand)
        for (i in lengthOfCommand + 1 until line.length) {
            if (!line[i].isDigit()) throw IllegalArgumentException("error")
        }
        val key = line.substring(lengthOfCommand + 1, line.length).toULong()
        return Pair(command, key)
    }
}


// This method is available only in Kotlin 1.5.0 and greater
// I've copied and simplified code from kotlin standard library 1.5.0
fun String.toULong(): ULong {
    val length = this.length
    if (length == 0) throw NullPointerException()

    val limit: ULong = ULong.MAX_VALUE
    val start: Int

    val firstChar = this[0]
    if (firstChar < '0') {
        if (length == 1 || firstChar != '+') throw NullPointerException()
        start = 1
    } else {
        start = 0
    }

    val limitForMaxRadix = 512409557603043100uL  //  limit / 36

    var limitBeforeMul = limitForMaxRadix
    var result = 0uL
    for (i in start until length) {
        val digit = this[i].digitToInt()

        if (digit < 0) throw NullPointerException()
        if (result > limitBeforeMul) {
            if (limitBeforeMul == limitForMaxRadix) {
                limitBeforeMul = limit / 10u

                if (result > limitBeforeMul) {
                    throw NullPointerException()
                }
            } else {
                throw NullPointerException()
            }
        }

        result *= 10u

        val beforeAdding = result
        result += digit.toUInt()
        if (result < beforeAdding) throw Exception() // overflow has happened
    }

    return result

}

// Same as String.toULong()
fun Char.digitToInt(): Int {
    return this.toInt() - 48
}
