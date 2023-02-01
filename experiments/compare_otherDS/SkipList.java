import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SkipList<T extends Comparable<? super T>> implements Iterable<T> {

Node<T> _head = new Node<>(null, 33);
private final Random rand = new Random();
private int _levels = 1;
private AtomicInteger size = new AtomicInteger(0);

/// <summary>
/// Inserts a value into the skip list.
/// </summary>
public void insert(T value) {
    // Determine the level of the new node. Generate a random number R. The
    // number of
    // 1-bits before we encounter the first 0-bit is the level of the node.
    // Since R is
    // 32-bit, the level can be at most 32.
    int level = 0;
    size.incrementAndGet();
    for (int R = rand.nextInt(); (R & 1) == 1; R >>= 1) {
        level++;
        if (level == _levels) {
            _levels++;
            break;
        }
    }

    // Insert this node into the skip list
    Node<T> newNode = new Node<>(value, level + 1);
    Node<T> cur = _head;
    for (int i = _levels - 1; i >= 0; i--) {
        for (; cur.next[i] != null; cur = cur.next[i]) {
            if (cur.next[i].getValue().compareTo(value) > 0)
                break;
        }

        if (i <= level) {
            newNode.next[i] = cur.next[i];
            cur.next[i] = newNode;
        }
    }
}

/// <summary>
/// Returns whether a particular value already exists in the skip list
/// </summary>
public boolean contains(T value) {
    Node<T> cur = _head;
    for (int i = _levels - 1; i >= 0; i--) {
        for (; cur.next[i] != null; cur = cur.next[i]) {
            if (cur.next[i].getValue().compareTo(value) > 0)
                break;
            if (cur.next[i].getValue().compareTo(value) == 0)
                return true;
        }
    }
    return false;
}


@SuppressWarnings({ "rawtypes", "unchecked" })
@Override
public Iterator<T> iterator() {
    return new SkipListIterator(this, 0);
}

public int size() {
    return size.get();
}

public Double[] toArray() {
    Double[] a = new Double[size.get()];
    int i = 0;
    for (T t : this) {
        a[i] = (Double) t;
        i++;
    }
    return a;
  }

 }

 class Node<N extends Comparable<? super N>> {
public Node<N>[] next;
public N value;

@SuppressWarnings("unchecked")
public Node(N value, int level) {
    this.value = value;
    next = new Node[level];
}

public N getValue() {
    return value;
}

public Node<N>[] getNext() {
    return next;
}

public Node<N> getNext(int level) {
    return next[level];
}

public void setNext(Node<N>[] next) {
    this.next = next;
}
}

class SkipListIterator<E extends Comparable<E>> implements Iterator<E> {
   SkipList<E> list;
   Node<E> current;
   int level;

public SkipListIterator(SkipList<E> list, int level) {
    this.list = list;
    this.current = list._head;
    this.level = level;
}

public boolean hasNext() {
    return current.getNext(level) != null;
}

public E next() {
    current = current.getNext(level);
    return current.getValue();
}

public void remove() throws UnsupportedOperationException {
    throw new UnsupportedOperationException();
}
}