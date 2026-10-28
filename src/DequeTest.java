import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Plain-Java, dependency-free tests for Deque.
 * The class under test is expected to be available on the classpath.
 */
public class DequeTest {
    private static int passed = 0;
    private static int failed = 0;

    private static void check(String testName, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + testName);
        } else {
            failed++;
            System.out.println("FAIL: " + testName);
        }
    }

    private static void expectThrows(String testName, Class<? extends Throwable> expected,
                                     Runnable action) {
        try {
            action.run();
            check(testName, false);
        } catch (Throwable actual) {
            check(testName, expected.isInstance(actual));
        }
    }

    /** Tests that a newly constructed deque reports empty. */
    public static void testDequeConstructor() {
        Deque<Integer> deque = new Deque<>();
        check("Deque constructor creates size-zero empty deque", deque.isEmpty() && deque.size() == 0);
    }

    /** Tests empty status before and after adding an item. */
    public static void testDequeIsEmpty() {
        Deque<Integer> deque = new Deque<>();
        boolean initiallyEmpty = deque.isEmpty();
        deque.addFirst(7);
        check("Deque isEmpty", initiallyEmpty && !deque.isEmpty());
    }

    /** Tests size changes for front and back additions. */
    public static void testDequeSize() {
        Deque<Integer> deque = new Deque<>();
        boolean start = deque.size() == 0;
        deque.addFirst(1);
        boolean one = deque.size() == 1;
        deque.addLast(2);
        check("Deque size", start && one && deque.size() == 2);
    }

    /** Tests adding at the front and verifies front-to-back iterator order. */
    public static void testDequeAddFirst() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        Iterator<Integer> it = deque.iterator();
        check("Deque addFirst places items at front", it.next() == 2 && it.next() == 1 && !it.hasNext());
    }

    /** Tests adding at the back and verifies front-to-back iterator order. */
    public static void testDequeAddLast() {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        deque.addLast(2);
        Iterator<Integer> it = deque.iterator();
        check("Deque addLast places items at back", it.next() == 1 && it.next() == 2 && !it.hasNext());
    }

    /** Tests that adding null at the front throws IllegalArgumentException. */
    public static void testDequeAddFirstNull() {
        Deque<Integer> deque = new Deque<>();
        expectThrows("Deque addFirst(null) throws IllegalArgumentException",
                IllegalArgumentException.class, () -> deque.addFirst(null));
    }

    /** Tests that adding null at the back throws IllegalArgumentException. */
    public static void testDequeAddLastNull() {
        Deque<Integer> deque = new Deque<>();
        expectThrows("Deque addLast(null) throws IllegalArgumentException",
                IllegalArgumentException.class, () -> deque.addLast(null));
    }

    /** Tests removing the front item and updating size. */
    public static void testDequeRemoveFirst() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(10);
        deque.addLast(20);
        boolean result = deque.removeFirst() == 10 && deque.size() == 1 && deque.removeFirst() == 20;
        check("Deque removeFirst", result && deque.isEmpty());
    }

    /** Tests removing the back item and updating size. */
    public static void testDequeRemoveLast() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(10);
        deque.addLast(20);
        boolean result = deque.removeLast() == 20 && deque.size() == 1 && deque.removeLast() == 10;
        check("Deque removeLast", result && deque.isEmpty());
    }

    /** Tests removing the only item through the front, then reusing the deque. */
    public static void testDequeRemoveFirstFromSingleton() {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(3);
        boolean result = deque.removeFirst() == 3 && deque.isEmpty() && deque.size() == 0;
        deque.addFirst(4);
        check("Deque removeFirst from singleton and reuse", result && deque.removeLast() == 4 && deque.isEmpty());
    }

    /** Tests removing the only item through the back, then reusing the deque. */
    public static void testDequeRemoveLastFromSingleton() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(3);
        boolean result = deque.removeLast() == 3 && deque.isEmpty() && deque.size() == 0;
        deque.addLast(4);
        check("Deque removeLast from singleton and reuse", result && deque.removeFirst() == 4 && deque.isEmpty());
    }

    /** Tests that removeFirst on an empty deque throws NoSuchElementException. */
    public static void testDequeRemoveFirstEmpty() {
        Deque<Integer> deque = new Deque<>();
        expectThrows("Deque removeFirst on empty throws NoSuchElementException",
                NoSuchElementException.class, deque::removeFirst);
    }

    /** Tests that removeLast on an empty deque throws NoSuchElementException. */
    public static void testDequeRemoveLastEmpty() {
        Deque<Integer> deque = new Deque<>();
        expectThrows("Deque removeLast on empty throws NoSuchElementException",
                NoSuchElementException.class, deque::removeLast);
    }

    /** Tests iterator traversal from front to back and hasNext at exhaustion. */
    public static void testDequeIterator() {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);
        Iterator<Integer> it = deque.iterator();
        boolean ordered = it.hasNext() && it.next() == 1 && it.next() == 2 && it.next() == 3;
        check("Deque iterator traverses front to back", ordered && !it.hasNext());
    }

    /** Tests that next on an exhausted deque iterator throws NoSuchElementException. */
    public static void testDequeIteratorNextExhausted() {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        Iterator<Integer> it = deque.iterator();
        it.next();
        expectThrows("Deque iterator next after exhaustion throws NoSuchElementException",
                NoSuchElementException.class, it::next);
    }

    /** Tests that deque iterator remove is unsupported. */
    public static void testDequeIteratorRemoveUnsupported() {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        Iterator<Integer> it = deque.iterator();
        expectThrows("Deque iterator remove throws UnsupportedOperationException",
                UnsupportedOperationException.class, it::remove);
    }

    /** Runs every Deque test and prints the final PASS/FAIL totals. */
    public static void main(String[] args) {
        // Uncomment each test as you implement the corresponding method:

        testDequeConstructor();
        testDequeIsEmpty();
        testDequeSize();
        testDequeAddFirst();
        testDequeAddLast();
        testDequeAddFirstNull();
        testDequeAddLastNull();
        testDequeRemoveFirst();
        testDequeRemoveLast();
        testDequeRemoveFirstFromSingleton();
        testDequeRemoveLastFromSingleton();
        testDequeRemoveFirstEmpty();
        testDequeRemoveLastEmpty();
        testDequeIterator();
        testDequeIteratorNextExhausted();
        testDequeIteratorRemoveUnsupported();

        System.out.println("\nSummary: " + passed + " passed, " + failed + " failed.");
    }
}
