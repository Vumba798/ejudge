import java.util.Scanner
import java.util.Stack
import java.util.LinkedList
import java.util.Queue
import kotlin.collections.HashMap

fun main() {
    val scanner = Scanner(System.`in`) // std::cin like
    val graph = hashMapOf<String, MutableList<String>>()
    val graphType : String = scanner.next()
    val startVertex : String = scanner.next()
    val searchType : String = scanner.next()

    if (graphType == "d") {
        directed_graph_insert(graph, scanner)
    } else {
        undirected_graph_insert(graph, scanner)
    }
    if (searchType == "b") {
        for (list in graph) {
            list.value.sort()
        }
        breadth_first_search(graph, startVertex)
    } else {
       for (list in graph) {
           list.value.sortDescending()
       }
        depth_first_search(graph, startVertex)
    }

}

fun directed_graph_insert(
    graph: HashMap<String, MutableList<String>>,
    scanner: Scanner
) {
    while (scanner.hasNext()) {
        val thisVertex = scanner.next()
        val otherVertex = scanner.next()
        if (graph[thisVertex] == null) {
            graph[thisVertex] = mutableListOf(otherVertex)
        } else {
            graph[thisVertex]!!.add(otherVertex)
        }
    }
}

fun undirected_graph_insert(
    graph: HashMap<String, MutableList<String>>,
    scanner: Scanner
) {
    while (scanner.hasNext()) {
        val thisVertex: String = scanner.next()
        val otherVertex: String = scanner.next()
        if (graph[thisVertex] == null) {
            graph[thisVertex] = mutableListOf(otherVertex)
        } else {
            graph[thisVertex]!!.add(otherVertex)
        }
        if (graph[otherVertex] == null) {
            graph[otherVertex] = mutableListOf(thisVertex)
        } else {
            graph[otherVertex]!!.add(thisVertex)
        }
    }
}

fun depth_first_search(graph: HashMap<String, MutableList<String>>, startVertex: String) {
    val stack = Stack<String>()
    val visited = hashSetOf<String>()
    stack.add(startVertex)
    while (stack.isNotEmpty()) {
        val head = stack.pop()
        if (visited.add(head)) {
            println(head)
        }
        if (graph[head] == null) {
            continue;
        }
        for (neighbor in graph[head]!!) {
            if (neighbor !in visited) {
                stack.add(neighbor)
            }
        }
    }
}
fun breadth_first_search(graph: HashMap<String, MutableList<String>>, startVertex: String) {
    val queue : Queue<String> = LinkedList<String>()
    val visited = hashSetOf<String>()
    queue.add(startVertex)
    visited.add(queue.peek())
    while (queue.isNotEmpty()) {
        val head = queue.peek()
        println(head)
        if (graph[head] == null) {
            queue.remove()
            continue
        }
        for (neighbor in graph[head]!!) {
            if (neighbor !in visited) {
                queue.add(neighbor)
                visited.add(neighbor)
            }
        }
        queue.remove()
    }
}import java.util.Scanner
import java.util.Stack
import java.util.LinkedList
import java.util.Queue
import kotlin.collections.HashMap

fun main() {
    val scanner = Scanner(System.`in`) // std::cin like
    val graph = hashMapOf<String, MutableList<String>>()
    val graphType : String = scanner.next()
    val startVertex : String = scanner.next()
    val searchType : String = scanner.next()

    if (graphType == "d") {
        directed_graph_insert(graph, scanner)
    } else {
        undirected_graph_insert(graph, scanner)
    }
    if (searchType == "b") {
        for (list in graph) {
            list.value.sort()
        }
        breadth_first_search(graph, startVertex)
    } else {
       for (list in graph) {
           list.value.sortDescending()
       }
        depth_first_search(graph, startVertex)
    }

}

fun directed_graph_insert(
    graph: HashMap<String, MutableList<String>>,
    scanner: Scanner
) {
    while (scanner.hasNext()) {
        val thisVertex = scanner.next()
        val otherVertex = scanner.next()
        if (graph[thisVertex] == null) {
            graph[thisVertex] = mutableListOf(otherVertex)
        } else {
            graph[thisVertex]!!.add(otherVertex)
        }
    }
}

fun undirected_graph_insert(
    graph: HashMap<String, MutableList<String>>,
    scanner: Scanner
) {
    while (scanner.hasNext()) {
        val thisVertex: String = scanner.next()
        val otherVertex: String = scanner.next()
        if (graph[thisVertex] == null) {
            graph[thisVertex] = mutableListOf(otherVertex)
        } else {
            graph[thisVertex]!!.add(otherVertex)
        }
        if (graph[otherVertex] == null) {
            graph[otherVertex] = mutableListOf(thisVertex)
        } else {
            graph[otherVertex]!!.add(thisVertex)
        }
    }
}

fun depth_first_search(graph: HashMap<String, MutableList<String>>, startVertex: String) {
    val stack = Stack<String>()
    val visited = hashSetOf<String>()
    stack.add(startVertex)
    while (stack.isNotEmpty()) {
        val head = stack.pop()
        if (visited.add(head)) {
            println(head)
        }
        if (graph[head] == null) {
            continue;
        }
        for (neighbor in graph[head]!!) {
            if (neighbor !in visited) {
                stack.add(neighbor)
            }
        }
    }
}
fun breadth_first_search(graph: HashMap<String, MutableList<String>>, startVertex: String) {
    val queue : Queue<String> = LinkedList<String>()
    val visited = hashSetOf<String>()
    queue.add(startVertex)
    visited.add(queue.peek())
    while (queue.isNotEmpty()) {
        val head = queue.peek()
        println(head)
        if (graph[head] == null) {
            queue.remove()
            continue
        }
        for (neighbor in graph[head]!!) {
            if (neighbor !in visited) {
                queue.add(neighbor)
                visited.add(neighbor)
            }
        }
        queue.remove()
    }
}import java.util.Scanner
import java.util.Stack
import java.util.LinkedList
import java.util.Queue
import kotlin.collections.HashMap

fun main() {
    val scanner = Scanner(System.`in`) // std::cin like
    val graph = hashMapOf<String, MutableList<String>>()
    val graphType : String = scanner.next()
    val startVertex : String = scanner.next()
    val searchType : String = scanner.next()

    if (graphType == "d") {
        directed_graph_insert(graph, scanner)
    } else {
        undirected_graph_insert(graph, scanner)
    }
    if (searchType == "b") {
        for (list in graph) {
            list.value.sort()
        }
        breadth_first_search(graph, startVertex)
    } else {
       for (list in graph) {
           list.value.sortDescending()
       }
        depth_first_search(graph, startVertex)
    }

}

fun directed_graph_insert(
    graph: HashMap<String, MutableList<String>>,
    scanner: Scanner
) {
    while (scanner.hasNext()) {
        val thisVertex = scanner.next()
        val otherVertex = scanner.next()
        if (graph[thisVertex] == null) {
            graph[thisVertex] = mutableListOf(otherVertex)
        } else {
            graph[thisVertex]!!.add(otherVertex)
        }
    }
}

fun undirected_graph_insert(
    graph: HashMap<String, MutableList<String>>,
    scanner: Scanner
) {
    while (scanner.hasNext()) {
        val thisVertex: String = scanner.next()
        val otherVertex: String = scanner.next()
        if (graph[thisVertex] == null) {
            graph[thisVertex] = mutableListOf(otherVertex)
        } else {
            graph[thisVertex]!!.add(otherVertex)
        }
        if (graph[otherVertex] == null) {
            graph[otherVertex] = mutableListOf(thisVertex)
        } else {
            graph[otherVertex]!!.add(thisVertex)
        }
    }
}

fun depth_first_search(graph: HashMap<String, MutableList<String>>, startVertex: String) {
    val stack = Stack<String>()
    val visited = hashSetOf<String>()
    stack.add(startVertex)
    while (stack.isNotEmpty()) {
        val head = stack.pop()
        if (visited.add(head)) {
            println(head)
        }
        if (graph[head] == null) {
            continue;
        }
        for (neighbor in graph[head]!!) {
            if (neighbor !in visited) {
                stack.add(neighbor)
            }
        }
    }
}
fun breadth_first_search(graph: HashMap<String, MutableList<String>>, startVertex: String) {
    val queue : Queue<String> = LinkedList<String>()
    val visited = hashSetOf<String>()
    queue.add(startVertex)
    visited.add(queue.peek())
    while (queue.isNotEmpty()) {
        val head = queue.peek()
        println(head)
        if (graph[head] == null) {
            queue.remove()
            continue
        }
        for (neighbor in graph[head]!!) {
            if (neighbor !in visited) {
                queue.add(neighbor)
                visited.add(neighbor)
            }
        }
        queue.remove()
    }
}
