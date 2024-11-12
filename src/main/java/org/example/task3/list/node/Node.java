package org.example.task3.list.node;

public class Node<T> {
    private final T data;
    private final Node<T> next;
    private final Node<T> prev;

    public Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
