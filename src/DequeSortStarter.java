import java.util.Random;

/**
 * Deque Sort Starter
 *
 * Rules: sort using ONLY these Deque methods:
 *   addFirst, addLast, removeFirst, removeLast, isEmpty, size
 * No index access, no inserting in the middle, no java.util sorting.
 * You may create extra Deques.
 *
 * Uses YOUR Deque class (must be generic: Deque<Integer>).
 * If your method names differ, change them in sort() only.
 */
public class DequeSortStarter {

    // Counters: call lessThan(a, b) each time you compare two items,
    // and countMove() each time you take an item out of one Deque and put it in another.
    static long compares = 0;
    static long moves = 0;

    static boolean lessThan(int a, int b) {
        compares++;
        return a < b;
    }

    static void countMove() {
        moves++;
    }

    /**
     * TODO: Implement your algorithm from your pseudocode.
     * When this returns, d must hold the same items in ascending order.
     */
    public static void sort(Deque<Integer> d) {
        // Your code here.
        // Example of counting a compare:   if (lessThan(a, b)) { ... }
        // Example of counting a move:      other.addLast(d.removeFirst()); countMove();
        Deque<Integer> d1 = new Deque<>();
        Deque<Integer> d2 = new Deque<>();
        if(d.size() > 2){
            int s = d.size() / 2;
            for(int i = 0; i < d.size(); i++){
                if(i < s){
                    d1.addFirst(d.removeFirst());
                }
                else{
                    d2.addFirst(d.removeFirst());
                }
            }
            sort(d1);
            sort(d2);
        }
        while(d1.size() > 0 && d2.size() > 0){
            int a = d1.removeFirst();
            int b = d2.removeFirst();
        }
    }

    // ---------- Datasets ----------

    static int[] sortedData(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = i;
        return a;
    }

    static int[] reversedData(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = n - i;
        return a;
    }

    static int[] randomData(int n, long seed) {
        Random r = new Random(seed);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt(1000);
        return a;
    }

    static int[] duplicatesData(int n, long seed) {
        Random r = new Random(seed);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt(5); // only values 0-4
        return a;
    }

    // ---------- Test harness (no need to edit below) ----------

    static Deque<Integer> toDeque(int[] data) {
        Deque<Integer> d = new Deque<Integer>();
        for (int x : data) d.addLast(x);
        return d;
    }

    // Checks that d is sorted and has the expected size.
    // Empties and rebuilds d using only end operations.
    static boolean isSortedAndSize(Deque<Integer> d, int expectedSize) {
        if (d.size() != expectedSize) return false;
        boolean ok = true;
        Deque<Integer> temp = new Deque<Integer>();
        Integer prev = null;
        while (!d.isEmpty()) {
            int x = d.removeFirst();
            if (prev != null && x < prev) ok = false;
            prev = x;
            temp.addLast(x);
        }
        while (!temp.isEmpty()) d.addLast(temp.removeFirst());
        return ok;
    }

    static void run(String name, int[] data) {
        compares = 0;
        moves = 0;
        Deque<Integer> d = toDeque(data);
        sort(d);
        boolean ok = isSortedAndSize(d, data.length);
        System.out.printf("%-22s n=%-6d %-6s compares=%-10d moves=%d%n",
                name, data.length, ok ? "PASS" : "FAIL", compares, moves);
    }

    public static void main(String[] args) {
        // Edge cases
        run("empty", new int[0]);
        run("one item", new int[]{42});
        run("two items (reversed)", new int[]{9, 3});

        // Small sets: trace these by hand first
        run("small random", new int[]{5, 2, 8, 1, 9, 3, 7, 4});
        run("small duplicates", new int[]{3, 1, 3, 2, 1, 2, 3, 1});

        // Bigger sets: watch how the counts grow
        int[] sizes = {100, 200, 400, 800};
        for (int n : sizes) {
            run("sorted", sortedData(n));
            run("reversed", reversedData(n));
            run("random", randomData(n, 1234));
            run("many duplicates", duplicatesData(n, 1234));
            System.out.println();
        }

        // Reflection questions (answer in your write-up):
        // 1. When n doubled, about how much did compares grow? What Big-O does that suggest?
        // 2. Which dataset was best case? Worst case? Why?
        // 3. Which known sort does your algorithm most resemble?
    }
}
