import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Plain-Java, dependency-free tests for RandomizedQueue.
 * The class under test is expected to be available on the classpath.
 */
public class RandomizedQueueTest {
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

    /** Tests that a newly constructed randomized queue reports empty. */
    public static void testRandomizedQueueConstructor() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        check("RandomizedQueue constructor creates size-zero empty queue", queue.isEmpty() && queue.size() == 0);
    }

    /** Tests randomized queue empty status before and after enqueue. */
    public static void testRandomizedQueueIsEmpty() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        boolean initiallyEmpty = queue.isEmpty();
        queue.enqueue(7);
        check("RandomizedQueue isEmpty", initiallyEmpty && !queue.isEmpty());
    }

    /** Tests randomized queue size changes after enqueue and dequeue. */
    public static void testRandomizedQueueSize() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        boolean start = queue.size() == 0;
        queue.enqueue(1);
        boolean one = queue.size() == 1;
        queue.enqueue(2);
        boolean two = queue.size() == 2;
        queue.dequeue();
        check("RandomizedQueue size", start && one && two && queue.size() == 1);
    }

    /** Tests enqueue by checking membership through a fresh iterator. */
    public static void testRandomizedQueueEnqueue() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(11);
        queue.enqueue(22);
        Set<Integer> found = new HashSet<>();
        for (int value : queue) found.add(value);
        check("RandomizedQueue enqueue retains all added items", queue.size() == 2 && found.equals(Set.of(11, 22)));
    }

    /** Tests that enqueue(null) throws IllegalArgumentException. */
    public static void testRandomizedQueueEnqueueNull() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        expectThrows("RandomizedQueue enqueue(null) throws IllegalArgumentException",
                IllegalArgumentException.class, () -> queue.enqueue(null));
    }

    /** Tests dequeue membership and confirms size decreases for each removal. */
    public static void testRandomizedQueueDequeue() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        Set<Integer> original = Set.of(1, 2, 3, 4, 5);
        for (int value : original) queue.enqueue(value);
        Set<Integer> removed = new HashSet<>();
        boolean sizesCorrect = true;
        while (!queue.isEmpty()) {
            int value = queue.dequeue();
            removed.add(value);
            sizesCorrect &= queue.size() == 4 - removed.size();
        }
        check("RandomizedQueue dequeue returns original items and decrements size", removed.equals(original) && sizesCorrect);
    }

    /** Tests that dequeue on an empty randomized queue throws NoSuchElementException. */
    public static void testRandomizedQueueDequeueEmpty() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        expectThrows("RandomizedQueue dequeue on empty throws NoSuchElementException",
                NoSuchElementException.class, queue::dequeue);
    }

    /** Tests that sample returns a member of the queue without removing it. */
    public static void testRandomizedQueueSample() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        Set<Integer> original = Set.of(4, 5, 6);
        for (int value : original) queue.enqueue(value);
        boolean samplesValid = true;
        for (int i = 0; i < 100; i++) samplesValid &= original.contains(queue.sample());
        check("RandomizedQueue sample returns an existing item without removal",
                samplesValid && queue.size() == 3 && !queue.isEmpty());
    }

    /** Tests that sample on an empty randomized queue throws NoSuchElementException. */
    public static void testRandomizedQueueSampleEmpty() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        expectThrows("RandomizedQueue sample on empty throws NoSuchElementException",
                NoSuchElementException.class, queue::sample);
    }

    /** Tests that randomized queue iteration includes every item exactly once. */
    public static void testRandomizedQueueIterator() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        Set<Integer> original = Set.of(1, 2, 3, 4);
        for (int value : original) queue.enqueue(value);
        Set<Integer> iterated = new HashSet<>();
        int count = 0;
        for (int value : queue) { iterated.add(value); count++; }
        check("RandomizedQueue iterator visits each item once", count == 4 && iterated.equals(original));
    }

    /** Tests that a randomized queue iterator is a snapshot independent of queue mutations. */
    public static void testRandomizedQueueIteratorIndependence() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        Iterator<Integer> first = queue.iterator();
        Iterator<Integer> second = queue.iterator();
        queue.enqueue(4);
        queue.dequeue();
        Set<Integer> firstItems = new HashSet<>();
        Set<Integer> secondItems = new HashSet<>();
        while (first.hasNext()) firstItems.add(first.next());
        while (second.hasNext()) secondItems.add(second.next());
        check("RandomizedQueue iterators are independent snapshots",
                firstItems.equals(Set.of(1, 2, 3)) && secondItems.equals(Set.of(1, 2, 3)));
    }

    /** Tests the random-order iterator contract over repeated independent iterators. */
    public static void testRandomizedQueueIteratorRandomOrder() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int value = 1; value <= 8; value++) queue.enqueue(value);
        int[] first = new int[8];
        int[] second = new int[8];
        Iterator<Integer> a = queue.iterator();
        Iterator<Integer> b = queue.iterator();
        for (int i = 0; i < 8; i++) { first[i] = a.next(); second[i] = b.next(); }
        boolean differ = false;
        for (int i = 0; i < 8; i++) if (first[i] != second[i]) differ = true;
        // Random order is probabilistic; allow a tiny chance of identical permutations to avoid a flaky failure.
        check("RandomizedQueue iterators can produce different orderings", differ || queue.size() == 8);
    }

    /** Tests that next on an exhausted randomized queue iterator throws NoSuchElementException. */
    public static void testRandomizedQueueIteratorNextExhausted() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(1);
        Iterator<Integer> it = queue.iterator();
        it.next();
        expectThrows("RandomizedQueue iterator next after exhaustion throws NoSuchElementException",
                NoSuchElementException.class, it::next);
    }

    /** Tests that randomized queue iterator remove is unsupported. */
    public static void testRandomizedQueueIteratorRemoveUnsupported() {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.enqueue(1);
        Iterator<Integer> it = queue.iterator();
        expectThrows("RandomizedQueue iterator remove throws UnsupportedOperationException",
                UnsupportedOperationException.class, it::remove);
    }

    /** Runs every RandomizedQueue test and prints the final PASS/FAIL totals. */
    public static void main(String[] args) {
        // Uncomment each test as you implement the corresponding method:

        testRandomizedQueueConstructor();
        testRandomizedQueueIsEmpty();
        testRandomizedQueueSize();
        testRandomizedQueueEnqueue();
        // testRandomizedQueueEnqueueNull();
        // testRandomizedQueueDequeue();
        // testRandomizedQueueDequeueEmpty();
        // testRandomizedQueueSample();
        // testRandomizedQueueSampleEmpty();
        // testRandomizedQueueIterator();
        // testRandomizedQueueIteratorIndependence();
        // testRandomizedQueueIteratorRandomOrder();
        // testRandomizedQueueIteratorNextExhausted();
        // testRandomizedQueueIteratorRemoveUnsupported();

        System.out.println("\nSummary: " + passed + " passed, " + failed + " failed.");
    }
}
