import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Card Dealer: deal k distinct random cards from a deck of strings read from standard input.
 */
public class Permutation {

    /**
     * Deal exactly k cards uniformly at random from the deck supplied on standard input.
     * Read card names with a Scanner and print each dealt card at most once.
     * @param args command-line arguments; args[0] is the number k of cards to deal
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO: Act as the card dealer: read the deck from standard input and deal k random cards.
        // Hint: use a Scanner to read card names: Scanner scanner = new Scanner(System.in);
        //       then use scanner.next() to read each card, and System.out.println() to print dealt cards.
        if(args.length > 1){
            int k = Integer.parseInt(args[0]);
            Scanner scanner = new Scanner(new File(args[1]));
            RandomizedQueue<String> deck = new RandomizedQueue<>();
            while(scanner.hasNext()){
                deck.enqueue(scanner.next());
            }
            for(int i = 0; i < k; i++){
                if(deck.isEmpty()){
                    throw new NoSuchElementException("Deck is empty!");
                }
                System.out.println(deck.dequeue());
            }
        }
    }
}
