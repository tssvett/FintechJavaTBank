package org.example.task3.list.impl;


import org.example.task3.list.CustomLinkedList;

public class CustomLinkedListImpl<T> implements CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public static class Node<E> {
        private final E data;
        private Node<E> next;
        private Node<E> prev;

        public Node(E data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    public CustomLinkedListImpl() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        //Обновляем ссылки
        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
        }
        //Элемент всегда становится хвостом
        tail = newNode;
        size++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current.data;
        }
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T removedData;
        if (index == 0) {
            // Удаление головы
            removedData = head.data;
            head = head.next;
            if (head != null) {
                head.prev = null;
            } else {
                tail = null;
            }
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            removedData = current.next.data;
            Node<T> nodeToRemove = current.next;

            current.next = nodeToRemove.next;
            if (nodeToRemove.next != null) {
                nodeToRemove.next.prev = current;
            } else {
                tail = current;
            }
        }
        size--;
        return removedData;
    }

    @Override
    public boolean contains(Object element) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void addAll(CustomLinkedList<T> list) {
        for (int i = 0; i < list.size(); i++) {
            add(list.get(i));
        }
    }

    @Override
    public int size() {
        return size;
    }
}
