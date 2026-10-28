import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * A double-ended queue that supports adding and removing items at either end.
 * @param <Item> the type of items stored in this deque
 */
public class Deque<Item> implements Iterable<Item> {

    /** A node in the doubly linked list. */
    private class Node {
        Item item;
        Node next;
        Node prev;
    }

    private Node head;
    private Node tail;
    private int size;

    /** Construct an empty deque. */
    public Deque() {
        // TODO: Initialize an empty linked list.
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    /** Return whether the deque is empty. */
    public boolean isEmpty() {
        // TODO: Report whether this linked list contains no items.
        return size == 0;
    }

    /** Return the number of items in the deque. */
    public int size() {
        // TODO: Return this linked list's item count.
        return size;
    }

    /** Add an item to the front; reject null items with IllegalArgumentException. */
    public void addFirst(Item item) {
        // TODO: Create a node and link it at the front; update head/tail pointers.
        if(item == null){
            throw new IllegalArgumentException("item cannot be null!");
        }
        size++;
        Node old = head.next;
        head.next = new Node();
        head.next.item = item;
        head.next.next = old;
        head.next.prev = head;
        head.next.next.prev = head.next;
    }

    /** Add an item to the back; reject null items with IllegalArgumentException. */
    public void addLast(Item item) {
        // TODO: Create a node and link it at the back; update head/tail pointers.
        if(item == null){
            throw new IllegalArgumentException("item cannot be null!");
        }
        size++;
        Node old = tail.prev;
        tail.prev = new Node();
        tail.prev.item = item;
        tail.prev.prev = old;
        tail.prev.next = tail;
        tail.prev.prev.next = tail.prev;
    }

    /** Remove and return the front item; throw NoSuchElementException if empty. */
    public Item removeFirst() {
        // TODO: Remove the front node and update head/tail pointers.
        if(isEmpty()){
            throw new NoSuchElementException("List is empty!");
        }
        size--;
        Node old = head.next;
        head.next = head.next.next;
        head.next.prev = head;
        return old.item;
    }

    /** Remove and return the back item; throw NoSuchElementException if empty. */
    public Item removeLast() {
        // TODO: Remove the back node and update head/tail pointers.
        if(isEmpty()){
            throw new NoSuchElementException("List is empty!");
        }
        size--;
        Node old = tail.prev;
        tail.prev = tail.prev.prev;
        tail.prev.next = tail;
        return old.item;
    }

    /** Return an iterator from front to back; next() and remove() follow the specified exceptions. */
    @Override
    public Iterator<Item> iterator() {
        // TODO: Return an iterator that follows next links from head to tail.
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item>{
        private Node current = head.next;
        @Override
        public boolean hasNext() {
            return current != null && !current.equals(tail);
        }

        @Override
        public Item next() {
            if(!hasNext()){
                throw new NoSuchElementException("You've reached the end of this list");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    /** Run unit tests for the deque. */
    public static void main(String[] args) {
        // TODO: Add unit tests.
    }
}
