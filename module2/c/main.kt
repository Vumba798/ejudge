import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.lang.Math.ceil
import java.util.*
import java.util.Collections.swap
import kotlin.NoSuchElementException
import kotlin.math.ceil
import kotlin.math.log2

class Heap {
    private class Node(
        val key: Long,
        var value: String,
    ) {
        operator fun compareTo(oth: Node): Int = key.compareTo(oth.key)
    }

    private var heap: MutableList<Node> = mutableListOf()
    //hashMap allows us to get element for O(1)
    private var keysToIndexes = hashMapOf<Long, Int>()

    private fun height(): Int = ceil(log2((heap.size + 1).toFloat())).toInt()
    private fun parentOf(index: Int): Int = (index - 1) / 2
    private fun leftChild(index: Int) = 2 * index + 1
    private fun rightChild(index: Int) = 2 * index + 2
    private fun heapify(index: Int) {
        var i = index
        while (true) {
            val leftChildIndex = leftChild(i)
            val rightChildIndex = rightChild(i)
            var smallest = i
            if (leftChildIndex < heap.size && heap[leftChildIndex] < heap[i]) {
                smallest = leftChildIndex
            }
            if (rightChildIndex < heap.size && heap[rightChildIndex] < heap[smallest]) {
                smallest = rightChildIndex
            }
            if (smallest != i) {
                // swap heap[i] and heap[smallest]
                keysToIndexes[heap[i].key] = smallest
                keysToIndexes[heap[smallest].key] = i
                swap(heap, i, smallest)
                i = smallest
            } else if (i != 0) {
                i = parentOf(i)
            } else {
                break
            }
        }
    }

    fun add(key: Long, value: String) {
        if (keysToIndexes.contains(key)) {
            throw RuntimeException("error")
        }
        heap.add(Node(key, value))
        var i = heap.lastIndex
        while (i > 0 && heap[parentOf(i)].key > key) {
            swap(heap, i, parentOf(i))
            keysToIndexes[heap[i].key] = i
            i = parentOf(i)
        }
        keysToIndexes[key] = i
    }

    fun set(key: Long, newValue: String) {
        // if we there is no any element in heap with such key, throws an exception
        heap[keysToIndexes[key]?: throw NoSuchElementException("error")].value = newValue
    }

    fun delete(key: Long) {
        val index = keysToIndexes[key] ?: throw NoSuchElementException("error")
        keysToIndexes.remove(key)
        if (index == heap.lastIndex) {
            heap.removeLast()
            return
        }
        swap(heap, index, heap.lastIndex)
        heap.removeLast()
        keysToIndexes[heap[index].key] = index
        heapify(index)
    }

    fun search(key: Long): Pair<Int, String>? {
        // if there is no any element with such key, we will return null
        val index = keysToIndexes[key] ?: return null
        return Pair(index, heap[index].value)
    }

    fun min(): Triple<Long, Int, String> {
        if (heap.size == 0) throw NoSuchElementException("error")
        return Triple(heap[0].key, 0, heap[0].value)
    }

    fun max(): Triple<Long, Int, String> {
        if (heap.size == 0) throw NoSuchElementException("error")
        // max element is in the last tier of tree
        val height: Int = height()
        val firstLeaf =
            if (heap.lastIndex.rem(2) == 0) {
                parentOf(heap.lastIndex + 1)
            } else {
                parentOf(heap.lastIndex + 2)
            }
        var maxNodeIndex = heap.lastIndex
        for (i in firstLeaf..heap.lastIndex) {
            if (heap[i] > heap[maxNodeIndex]) {
                maxNodeIndex = i
            }
        }
        return Triple(heap[maxNodeIndex].key, maxNodeIndex, heap[maxNodeIndex].value)
    }

    fun extract(): Pair<Long, String> {
        if (heap.size == 0) throw NoSuchElementException("error")
        val pairToReturn = Pair<Long, String>(heap[0].key, heap[0].value)
        keysToIndexes.remove(heap[0].key)
        swap(heap, 0, heap.lastIndex)
        heap.removeLast()
        keysToIndexes[heap[0].key] = 0
        heapify(0)
        return pairToReturn
    }

    fun print(out: OutputStream) {
        val writer = BufferedWriter(OutputStreamWriter(out))
        if (heap.size == 0) {
            writer.write("_\n")
            writer.flush()
            return
        }
        val elementsToPrint = (1 shl(height() - 1)) - 1

        // prints first element
        writer.write("[${heap[0].key} ${heap[0].value}]\n")
        var tierNumber = 1
        var i = 1
        while (i < elementsToPrint){
            // this loop prints whole tier
            writer.write("[${heap[i].key} ${heap[i].value} ${heap[parentOf(i)].key}]")
            ++i
            for (j in 1 until (1 shl(tierNumber))) {
                // prints rest elements in current tier
                writer.write(" [${heap[i].key} ${heap[i].value} ${heap[parentOf(i)].key}]")
                ++i
            }
            writer.write("\n")
            ++tierNumber
        }
        // now we will print last tier
        if (i < heap.size) {
            writer.write("[${heap[i].key} ${heap[i].value} ${heap[parentOf(i)].key}]")
            ++i
            while (i < heap.size) {
                writer.write(" [${heap[i].key} ${heap[i].value} ${heap[parentOf(i)].key}]")
                ++i
            }
            val amountOfUnderscores = (1 shl(height())) - 1 - heap.size
            for (k in 1..amountOfUnderscores) {
                writer.write(" _")
            }
            writer.write("\n")
        }
        writer.flush()
    }

}
fun main() {
    val heap = Heap()
    val scanner = Scanner(System.`in`)
    while (scanner.hasNext()) {
        try {
            when (scanner.next()) {
                "add" -> {
                    val key = scanner.nextLong()
                    val value = scanner.next()
                    heap.add(key, value)
                }
                "set" -> {
                    val key = scanner.nextLong()
                    val newValue = scanner.next()
                    heap.set(key, newValue)
                }
                "delete" -> {
                    val key = scanner.nextLong()
                    heap.delete(key)
                }
                "search" -> {
                    val key = scanner.nextLong()
                    val pair = heap.search(key)
                    if (pair == null) {
                        println("0")
                    } else {
                        println("1 ${pair.first} ${pair.second}")
                    }
                }
                "min" -> {
                    val triple = heap.min()
                    println("${triple.first} ${triple.second} ${triple.third}")
                }
                "max" -> {
                    val triple = heap.max()
                    println("${triple.first} ${triple.second} ${triple.third}")
                }
                "extract" -> {
                    val pair = heap.extract()
                    println("${pair.first} ${pair.second}")
                }
                "print" -> {
                    heap.print(System.out)
                }
                else -> {
                    println("error")
                }
            }
        } catch(exception: Exception) {
            println(exception.message)
        }
    }
}

