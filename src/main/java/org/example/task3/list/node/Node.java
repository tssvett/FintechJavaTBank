package org.example.task3.list.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class Node <T> {
    private T data;
    private Node<T> next;
    private Node<T> prev;

    public Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
