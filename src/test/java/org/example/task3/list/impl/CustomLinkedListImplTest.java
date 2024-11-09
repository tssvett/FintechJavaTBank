package org.example.task3.list.impl;



import org.example.task3.iterator.CustomLinkedIterator;
import org.example.task3.list.CustomLinkedList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomLinkedListImplTest {
    private CustomLinkedListImpl<String> list;

    @BeforeEach
    void setUp() {
        list = new CustomLinkedListImpl<>();
    }

    @Test
    void testAddAndGet() {
        list.add("test");
        list.add("test2");
        list.add("test3");
        assertEquals(3, list.size());
        assertEquals("test", list.get(0));
        assertEquals("test2", list.get(1));
        assertEquals("test3", list.get(2));
    }

    @Test
    void testAddNull(){
        list.add(null);
        assertEquals(null, list.get(0));
    }

    @Test
    void testGetOutOfBounds() {
        list.add("test");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void testRemove() {
        list.add("test");
        list.add("test2");
        list.add("test3");
        assertEquals(3, list.size());
        list.remove(1);
        assertEquals(2, list.size());
        assertEquals("test", list.get(0));
        assertEquals("test3", list.get(1));
    }

    @Test
    void testRemoveOutOfBounds() {
        list.add("test");
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    void testContains() {
        list.add("test");
        list.add("test2");
        list.add("test3");
        assertTrue(list.contains("test"));
        assertFalse(list.contains("test4"));
    }

    @Test
    void testAddAll() {
        CustomLinkedList<String> expectedList = new CustomLinkedListImpl<>();
        expectedList.add("test");
        expectedList.add("test2");
        expectedList.add("test3");
        list.addAll(expectedList);
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i), list.get(i));
        }
    }

    @Test
    void testAddAllIterator(){
        CustomLinkedList<String> expectedList = new CustomLinkedListImpl<>();
        expectedList.add("test");
        expectedList.add("test2");
        expectedList.add("test3");
        expectedList.iterator().forEachRemaining(list::add);
        assertEquals(3, list.size());
        for (int i = 0; i < expectedList.size(); i++) {
            assertEquals(expectedList.get(i), list.get(i));
        }
    }

    @Test
    void testIterator() {
        list.add("test");
        list.add("test2");
        list.add("test3");
        CustomLinkedIterator<String> iterator = list.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("test", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("test2", iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals("test3", iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testSize() {
        list.add("test");
        list.add("test2");
        list.add("test3");
        assertEquals(3, list.size());
    }

    //Третья часть домашки
    @Test
    void testStreamConvertToCustomList() {
        Stream<String> stream = Stream.of("test", "test2", "test3");
        CustomLinkedList<String> customListCollectedFromStream = stream.reduce(
                new CustomLinkedListImpl<>(),
                (accumulatorList, element) -> {
                    accumulatorList.add(element);
                    return accumulatorList;
                },
                (leftList, rightList) -> {
                    leftList.addAll(rightList);
                    return leftList;
                }
        );
        assertEquals("test", customListCollectedFromStream.get(0));
        assertEquals("test2", customListCollectedFromStream.get(1));
        assertEquals("test3", customListCollectedFromStream.get(2));
    }
}

