import java.io.File
import java.lang.Integer.min
import java.util.*

/*
Пусть n - длина слова, m - мощность алфавита

Автокоррекция будет выполнена на основе префиксного дерева.

В качестве способа организации слов в каждом узле будем хранить хеш-таблицу.
Она позволит получить доступ к следующему узлу за ожидаемое время O(1)
 */
class Autocorrector {
    private class Node() {
        var kids = hashMapOf<Char, Node>()
        var isLeaf: Boolean = false
    }

    private val root = Node()
    private var matched = mutableListOf<String>()

    /*
    Благодаря использованию хеш-таблицы, вставка слова происходит за O(n)

    На каждом символе мы проверяем, есть ли в нашей хеш-таблице этот символ
    Обращение к хеш-таблице (если нет коллизий) за O(1)
    Кол-во обращений - длина слова, то есть O(n)
     */
    fun addWordToDictionary(wordToAdd: String) {
        val word = wordToAdd.lowercase(Locale.getDefault())
        var currentNode = root
        // currentWordIndex позволяет работать с суффиксом слова, избегая рассмотрения префикса
        var currentWordIndex = 0
        for (i in 0 until word.length) {
            val transitSymbol = word[i]
            if (currentNode.kids.contains(transitSymbol)) {
                currentNode = currentNode.kids[transitSymbol]!!
            } else {
                currentNode.kids[transitSymbol] = Node()
                currentNode = currentNode.kids[transitSymbol]!!
            }
        }
        currentNode.isLeaf = true
    }

    /*
    Функция search работает за O(n)

    Аналогично работе add
     */

    private fun search(wordToSearch: String): Boolean {
        var currentNode = root
        val word = wordToSearch.lowercase(Locale.getDefault())
        for (transitSymbol in word) {
            if (currentNode.kids.contains(transitSymbol)) {
                currentNode = currentNode.kids[transitSymbol]!!
            } else {
                return false
            }
        }
        if (currentNode.isLeaf) return true
        return false
    }

    /* Проверка слова

        Первым делам мы проверяем, есть ли вообще это слово в словаре делаем это за O(n)
        Затем мы рекурсивно проверяем слово за O(m*n)
     */
    fun check(wordToCheck: String): MutableList<String>? {
        matched.clear()
        var currentNode = root
        val word = wordToCheck.lowercase(Locale.getDefault())
        if (search(wordToCheck)) return null
        checkRecursive(root, word, 0, false, "")
        matched.sort()
        return matched
    }

    /*
    Пока у нас нет ошибок, для каждого символа слова мы перебираем всех детей узла.
    Количество детей зависит от мощности алфавита, => сложность O(m*n)
    Большинство этих "ветвей" обрезается благодаря установленному максимуму ошибок равному одному
     */
    private fun checkRecursive(
        currentNode: Node,
        word: String,
        index: Int,
        hasMistakeInParent: Boolean,
        code: String
    ) {
        if (index == word.length && currentNode.isLeaf) {
            matched.add(code)
            return
        }
        if (index == word.lastIndex && !hasMistakeInParent && currentNode.isLeaf) {
            matched.add(code)
        }
        if (index == word.length && !hasMistakeInParent && currentNode.kids.isNotEmpty()) {
            for ((key, node) in currentNode.kids) {
                if (node.isLeaf) {
                    matched.add(code + key)
                }
            }
            return
        }
        for ((key, node) in currentNode.kids) {
            if (index == word.length) {
                return
            }
            if (key != word[index]) {
                if (index < word.lastIndex && key == word[index + 1]) {
                    // ошибка вставки лишнего символа
                    if (hasMistakeInParent) continue
                    checkRecursive(currentNode, word, index + 1, true, code)
                }
                if (index < word.lastIndex && key == word[index + 1] && node.kids.contains(word[index])) {
                    // ошибка перестановки
                    if (hasMistakeInParent) continue
                    checkRecursive(node.kids[word[index]]!!, word, index + 2, true, code + key + word[index])
                }
                if (node.kids.contains(word[index])) {
                    // ошибка пропуск символа
                    if (hasMistakeInParent) continue
                    checkRecursive(node, word, index, true, code + key)
                }
                if (key != word[index]) {
                    // ошибка неправильного символа
                    if (hasMistakeInParent) continue
                    checkRecursive(node, word, index + 1, true, code + key)
                }
            } else {
                checkRecursive(node, word, index + 1, hasMistakeInParent, code + key)
            }
        }
    }
}


fun main() {

//TODO main function is not working properly. REWORK!!!

    val autocorrector = Autocorrector()
    val scanner = Scanner(System.`in`)
    val amount = scanner.nextInt()
    for (i in 1..amount) {
        autocorrector.addWordToDictionary(scanner.next())
    }
    while (true) {
        println("BEGINS SEARCH")
        val word: String = scanner.nextLine()
        print(word)
        print(" -")
        try {
            val matched = autocorrector.check(word)
            if (matched != null) {
                if (matched.isEmpty()) {
                    print("?\n")
                    continue
                }
                print("> ")
                print(matched[0])
                for (i in 1 until matched.size) {
                    print(", ")
                    print(matched[i])
                }
                print("\n")
            } else {
                print(" ok\n".toByteArray())
            }
        } catch (exception: Exception) {
            println(exception.message)
        }
    }
}
