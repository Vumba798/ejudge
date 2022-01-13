fun main(args: Array<String>) {
    var size = 0 // size of array
    var currentSize = 0
    while (true) { // set_size check
        var stringIsOkay = true
        val input : String = readLine() ?: break // if reads null-string then break
        if (input.startsWith("set_size ") && input.length > 9) {
            for (index in 9..input.lastIndex) { // 9 is length of "set_size "
                if (!input[index].isDigit()) {
                    stringIsOkay = false
                    println("error")
                    break
                }
            }
            if (stringIsOkay) {
                size = input.substring(9, input.length).toInt()
                break
            }
        } else if (input != "") {
            println("error")
        }
    }

    val data = arrayOfNulls<String>(size) // reserves array of size length
    while (true) {
        val input : String = readLine() ?: break // if reads null-string then break

        if (input == "pop") {
            if (currentSize == 0) {
                println("underflow")
                continue
            }
            --currentSize
            println(data[currentSize])
            data[currentSize] = null
        } else if (input == "print") {
            if (currentSize == 0) {
                println("empty")
            } else {
                for (index in 0 until currentSize - 1) {
                    print("${data[index]} ")
                }
                println(data[currentSize - 1])
            }
        } else if (input.startsWith("push ") && input.length > 5) {
            var stringIsOkay = true
            for (index in 5..input.lastIndex) { // 5 is length of "push "
                if (input[index].isWhitespace()) {
                    stringIsOkay = false
                    println("error")
                    break
                }
            }
            if (currentSize == size && stringIsOkay) {
                println("overflow")
                continue
            } else if (stringIsOkay) {
                data[currentSize] = input.substring(5, input.length)
                currentSize++
            }
        } else {
            if (input != "") {
                println("error")
            }
        }
    }
}
