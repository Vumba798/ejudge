import java.util.*

enum class Operation {
    INC, DEC, DBL
}

class CrazyRich(private val money: Long) {
    private var operations = Stack<Operation>()
    init {
        calculate()
    }
    private fun calculate() {
        var currentMoney = money
        while(currentMoney > 1) {
            if (currentMoney == 3L) {
                // in case of currentMoney == 3 we have special algorithm
                currentMoney--
                operations.push(Operation.INC)
                currentMoney /= 2L
                operations.push(Operation.DBL)
                break
            } else if (currentMoney.rem(2) == 1L) {
                // if last bit == 1, then we must decide, how we will get rid of it
                if (currentMoney.countOneBitsInRow() > currentMoney.countZeroBitsInRow()) {
                    // if  its more effective to decrease value
                    // # 101000 => 100111
                    currentMoney++
                    operations.push(Operation.DEC)
                } else {
                    // if its more effective to increase value
                    // # 100100 => 100101
                    currentMoney--
                    operations.push(Operation.INC)
                }
            }
            currentMoney /= 2
            operations.push(Operation.DBL)
        }
        operations.push(Operation.INC)
    }

    private fun Long.countOneBitsInRow(): Int {
        // we don't count last bit
        var tmp = this / 2
        var amount = 0
        while(tmp % 2 != 0L) {
            tmp /= 2
            amount++
        }
        return amount
    }

    private fun Long.countZeroBitsInRow(): Int {
        // we don't count last bit
        var tmp = this / 2
        var amount = 0
        while(tmp % 2 != 1L) {
            tmp /= 2
            amount++
        }
        return amount
    }

    fun getAnswer() = operations
}

fun main() {
    val scanner = Scanner(System.`in`)
    val money = scanner.nextLong()

    val solver = CrazyRich(money)
    val answer = solver.getAnswer()

    while (answer.size > 0) {
        when(answer.pop()) {
            Operation.INC -> println("inc")
            Operation.DEC -> println("dec")
            Operation.DBL -> println("dbl")
            else -> throw NoSuchMethodException("error")
        }
    }
}
