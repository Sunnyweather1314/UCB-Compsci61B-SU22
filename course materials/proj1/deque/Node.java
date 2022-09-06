package deque;

public class Node<T> {
    T data;
    Node<T> prev;
    Node<T> next;

    Node(T source) {
        data = source;
        prev = null;
        next = null;
    }
}

