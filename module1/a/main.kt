fun main(args: Array<String>) {
    var sum : Long = 0
    while (true) {
        val input: String = readLine() ?: break
        var isNegative = false
        var indexIsRemembered = false
        var firstIndex = 0
        for ((index, symb) in input.withIndex()) {
            if (symb == '-') {
                if (indexIsRemembered) {
                    sum += input.substring(firstIndex, index).toInt()
                    indexIsRemembered = false
                }
                isNegative = true
            } else if (symb.isDigit()) {
                if (!indexIsRemembered) { // remembers first index of a number
                    if (isNegative) { // if number is negative, shifts first index of a number
                        firstIndex = index - 1
                    } else {
                        firstIndex = index
                    }
                    indexIsRemembered = true
                }
            } else if (indexIsRemembered) {
                sum += input.substring(firstIndex, index).toInt()
                indexIsRemembered = false
                isNegative = false
            } else {
                indexIsRemembered = false
                isNegative = false
            }
        }
        if (indexIsRemembered) {
            sum += input.substring(firstIndex, input.length).toInt()
        }
    }
    println(sum)
}
