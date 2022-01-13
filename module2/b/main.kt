import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.util.*
import kotlin.NoSuchElementException
import kotlin.RuntimeException

class SplayTree() {
    // for join() method
    private constructor(node: Node?) : this() {
        root = node
    }

    private class Node(val key: Long, var value: String) {
        var parent: Node? = null
        var leftChild: Node? = null
        var rightChild: Node? = null
    }

    private class NodeForPrint(
        // is used in print() method
        // amountOfUnderscores allows us to print children of empty node with saving memory
        val node: Node?,
        val amountOfUnderscores: Int = if (node == null) 1 else 0
    )

    private var root: Node? = null

    private fun left_zig(node: Node, parent: Node) {
        /*
                parent                              node
               /     \                             /     \
             node     c         ------>           a     parent
            /    \                                     /      \
           a      b                                   b        c

         */
        val b = node.rightChild
        node.rightChild = parent
        parent.parent = node
        parent.leftChild = b
        b?.parent = parent
        node.parent = null
        root = node
    }

    private fun right_zig(node: Node, parent: Node) {
        /*
               parent                                    node
              /      \                                 /      \
             a       node       ------>             parent     c
                    /    \                         /      \
                   b      c                       a        b
         */
        val b = node.leftChild
        node.leftChild = parent
        parent.parent = node
        parent.rightChild = b
        b?.parent = parent
        node.parent = null
        root = node
    }

    private fun left_zig_zig(node: Node, parent: Node, gParent: Node) {
        /*
                   gParent                         node
                   /      \                       /    \
                parent     d                     a     parent
               /     \                                 /     \
             node     c              ------>          b      gParent
            /    \                                          /      \
           a      b                                        c        d

         */
        val b = node.rightChild
        val c = parent.rightChild
        node.rightChild = parent
        parent.parent = node
        node.parent = gParent.parent
        parent.rightChild = gParent
        if (gParent.parent != null) {
            if (gParent.parent!!.leftChild == gParent) {
                gParent.parent!!.leftChild = node
            } else {
                gParent.parent!!.rightChild = node
            }
        }
        gParent.parent = parent
        parent.leftChild = b
        b?.parent = parent
        gParent.leftChild = c
        c?.parent = gParent
        if (gParent == root) {
            root = node
        }
    }

    private fun right_zig_zig(node: Node, parent: Node, gParent: Node) {
        /*
           gParent                                     node
           /      \                                  /     \
          a      parent                           parent    d
                /      \        ------>          /     \
               b       node                  gParent    c
                      /    \                 /     \
                     c      d               a       b
         */
        val b = parent.leftChild
        val c = node.leftChild
        node.parent = gParent.parent
        parent.leftChild = gParent
        if (gParent.parent != null) {
            if (gParent.parent!!.leftChild == gParent) {
                gParent.parent!!.leftChild = node
            } else {
                gParent.parent!!.rightChild = node
            }
        }
        gParent.parent = parent
        gParent.rightChild = b
        b?.parent = gParent
        parent.rightChild = c
        c?.parent = parent
        node.leftChild = parent
        parent.parent = node
        if (gParent == root) {
            root = node
        }
    }

    private fun left_zig_zag(node: Node, parent: Node, gParent: Node) {
        /*
                  gParent                                    node
                  /      \                                 /      \
               parent     d                            parent     gParent
              /     \               ------->           /    \      /    \
             a      node                              a      b    c      d
                   /    \
                  b      c
        */
        val b = node.leftChild
        val c = node.rightChild
        node.parent = gParent.parent
        if (gParent.parent != null) {
            if (gParent.parent!!.leftChild == gParent) {
                gParent.parent!!.leftChild = node
            } else {
                gParent.parent!!.rightChild = node
            }
        }
        node.leftChild = parent
        parent.parent = node
        node.rightChild = gParent
        gParent.parent = node
        parent.rightChild = b
        b?.parent = parent
        gParent.leftChild = c
        c?.parent = gParent
        if (gParent == root) {
            root = node
        }
    }

    private fun right_zig_zag(node: Node, parent: Node, gParent: Node) {
        /*
            gParent                                  node
            /     \                                /      \
           a     parent                       gParent     parent
                 /    \       ------->        /     \     /    \
               node    d                     a       b   c      d
              /    \
             b      c
         */
        val b = node.leftChild
        val c = node.rightChild
        node.parent = gParent.parent
        node.leftChild = gParent
        if (gParent.parent != null) {
            if (gParent.parent!!.leftChild == gParent) {
                gParent.parent!!.leftChild = node
            } else {
                gParent.parent!!.rightChild = node
            }
        }
        gParent.parent = node
        gParent.rightChild = b
        b?.parent = gParent
        node.rightChild = parent
        parent.parent = node
        parent.leftChild = c
        c?.parent = parent
        if (gParent == root) {
            root = node
        }
    }

    private fun splay(node: Node) {
        while (node != root) {
            val parent = node.parent
            if (parent == root) {
                if (node == parent!!.leftChild) {
                    left_zig(node, parent)
                } else {
                    right_zig(node, parent)
                }
            } else {
                val gParent = parent!!.parent!!
                if (node == parent.leftChild && parent == gParent.leftChild) {
                    left_zig_zig(node, parent, gParent)
                } else if (node == parent.rightChild && parent == gParent.rightChild) {
                    right_zig_zig(node, parent, gParent)
                } else if (node == parent.rightChild && parent == gParent.leftChild) {
                    left_zig_zag(node, parent, gParent)
                } else if (node == parent.leftChild && parent == gParent.rightChild) {
                    right_zig_zag(node, parent, gParent)
                }
            }
        }
    }

    fun add(key: Long, value: String) {
        if (root == null) {
            root = Node(key, value)
        } else {
            var comparedNode = root!!
            while (true) {
                if (key > comparedNode.key) {
                    if (comparedNode.rightChild != null) {
                        comparedNode = comparedNode.rightChild!!
                        continue
                    } else { // we are at the leaf's child, now we can emplace new node
                        comparedNode.rightChild = Node(key, value)
                        comparedNode.rightChild!!.parent = comparedNode
                        splay(comparedNode.rightChild!!)
                        break
                    }
                } else if (key < comparedNode.key){
                    if (comparedNode.leftChild != null) {
                        comparedNode = comparedNode.leftChild!!
                        continue
                    } else { // we are at the leaf's child, now we can emplace new node
                        comparedNode.leftChild = Node(key, value)
                        comparedNode.leftChild!!.parent = comparedNode
                        splay(comparedNode.leftChild!!)
                        break
                    }
                } else { // if such key is already exists in tree, then throws error
                    splay(comparedNode)
                    throw RuntimeException("error")
                }
            }
        }
    }

    private fun find_node(key: Long): Node? {
        if (root == null) {
            throw NoSuchElementException("error")
        }
        var comparedNode = root!!
        while (true) {
            if (key > comparedNode.key) {
                if (comparedNode.rightChild != null) {
                    comparedNode = comparedNode.rightChild!!
                } else { // if we haven't found a desired node
                    splay(comparedNode)
                    return null
                }
            } else if (key < comparedNode.key) {
                if (comparedNode.leftChild != null) {
                    comparedNode = comparedNode.leftChild!!
                } else { // if we haven't found a desired node
                    splay(comparedNode)
                    return null
                }
            } else { // found a desired node!
                break
            }
        }
        splay(comparedNode)
        return comparedNode
    }

    fun search(key: Long): String? {
        return try {
            val desiredNode = find_node(key)
            desiredNode?.value
        } catch(exception: NoSuchElementException){
            null
        }
    }

    fun set(key: Long, value: String) {
        val desiredNode = find_node(key) ?: throw NoSuchElementException("error")
        desiredNode.value = value
        splay(desiredNode)
    }

    private fun join(leftTree: SplayTree, rightTree: SplayTree) {
        var futureRoot = leftTree.root
        while (futureRoot!!.rightChild != null) {
            futureRoot = futureRoot.rightChild!!
        }
        leftTree.splay(futureRoot)
        futureRoot.rightChild = rightTree.root
        rightTree.root?.parent = futureRoot
        futureRoot.parent = null
        root = futureRoot
    }

    fun delete(key: Long) {
        val node = find_node(key) ?: throw NoSuchElementException("error")
        val leftTree = SplayTree(node.leftChild)
        val rightTree = SplayTree(node.rightChild)
        if (leftTree.root != null) {
            if(rightTree.root == null) {
                root = leftTree.root
                root!!.parent = null
            } else {
                join(leftTree, rightTree)
            }
        } else {
            root = rightTree.root
            root?.parent = null
        }
    }

    fun min(): Pair<Long, String> {
        if (root == null) {
            throw NoSuchElementException("error")
        }
        var minNode = root
        while (minNode!!.leftChild != null) {
            minNode = minNode.leftChild
        }
        splay(minNode)
        return (Pair(minNode.key, minNode.value))
    }

    fun max(): Pair<Long, String> {
        if (root == null) {
            throw NoSuchElementException("error")
        }
        var maxNode = root
        while (maxNode!!.rightChild != null) {
            maxNode = maxNode.rightChild
        }
        splay(maxNode)
        return (Pair(maxNode.key, maxNode.value))
    }

    fun print(out: OutputStream) {
        val writer = BufferedWriter(OutputStreamWriter(out))
        if (root == null) {
            writer.write("_\n")
            writer.flush()
            return
        }
        val queue: Queue<NodeForPrint> = LinkedList<NodeForPrint>()
        var nextLevelIsNotEmpty = true
        queue.add(NodeForPrint(root))
        while (nextLevelIsNotEmpty) {
            nextLevelIsNotEmpty = false
            val treeLevelSize = queue.size
            var currentNode = queue.poll()
            // this part is for printing first element of current tree level
            // # "[k v p]" is for first element
            // # " [k v p]" is for remaining
            if (currentNode.node != null) {
                // if one of currentNode's children is not null, then we will continue printing next level of tree
                if (currentNode.node!!.leftChild != null || currentNode.node!!.rightChild != null) {
                    nextLevelIsNotEmpty = true
                }
                val k: String = currentNode.node!!.key.toString()
                val v: String = currentNode.node!!.value
                // we remember, that root has no parent
                if (currentNode.node != root) {
                    val p: String = currentNode.node!!.parent!!.key.toString()
                    writer.write("[$k $v $p]")
                } else {
                    writer.write("[$k $v]")
                }
                queue.add(NodeForPrint(currentNode.node?.leftChild))
                queue.add(NodeForPrint(currentNode.node?.rightChild))
            } else { // if node is null
                writer.write("_")
                for (j in 1 until currentNode.amountOfUnderscores) {
                    writer.write(" _")
                }
                // if currentNode is null, we must double amount of underscores for its children
                queue.add(NodeForPrint(null,  currentNode.amountOfUnderscores * 2))
            }
            // this part is for printing remaining nodes in current tree level
            for (i in 1 until treeLevelSize) {
                currentNode = queue.poll()
                if (currentNode.node != null) {
                    val k: String = currentNode.node!!.key.toString()
                    val v: String = currentNode.node!!.value
                    // root has no parent
                    if (currentNode.node != root) {
                        val p: String = currentNode.node!!.parent!!.key.toString()
                        writer.write(" [$k $v $p]")
                    } else {
                        writer.write(" [$k $v]")
                    }
                    queue.add(NodeForPrint(currentNode.node?.leftChild))
                    queue.add(NodeForPrint(currentNode.node?.rightChild))
                    // if one of currentNode's children is not null, then we will continue printing next level of tree
                    if (currentNode.node!!.leftChild != null || currentNode.node!!.rightChild != null) {
                        nextLevelIsNotEmpty = true
                    }
                } else {
                    for (j in 0 until currentNode.amountOfUnderscores) {
                        writer.write(" _")
                    }
                    // if currentNode is null, we must double amount of underscores for its children
                    queue.add(NodeForPrint(null,  currentNode.amountOfUnderscores * 2))
                }
            }
            writer.write("\n")
            if (!nextLevelIsNotEmpty) {
                break
            }
        }
        writer.flush()
    }
}

fun main() {
    val splayTree = SplayTree()
    val scanner = Scanner(System.`in`)
    while (scanner.hasNext()) {
        try {
            // reads command
            when (scanner.next()) {
                "add" -> {
                    val key = scanner.nextLong()
                    val value = scanner.next()
                    splayTree.add(key, value)
                } "set" -> {
                    val key = scanner.nextLong()
                    val newValue = scanner.next()
                    splayTree.set(key, newValue)
                } "delete" -> {
                    val key = scanner.nextLong()
                    splayTree.delete(key)
                } "search" -> {
                    val key = scanner.nextLong()
                    val value: String? = splayTree.search(key)
                    if (value == null) {
                        println("0")
                    } else {
                        println("1 $value")
                    }
                } "min" -> {
                    val pair = splayTree.min()
                    println("${pair.first} ${pair.second}")
                } "max" -> {
                    val pair = splayTree.max()
                    println("${pair.first} ${pair.second}")
                }"print" -> {
                    splayTree.print(System.out)
                } else -> {
                    throw NoSuchMethodException("error")
                }
            }
        } catch (exception: Exception) {
            println(exception.message)
        }
    }
}
