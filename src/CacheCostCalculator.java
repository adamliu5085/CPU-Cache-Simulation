import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class is a cache simulator -- it allows a user to select a cache architecture and
 * input a list of memory addresses.  The simulator will simulate accessing the memory
 * addresses given. The program will keep track of the hits and misses. Results will be
 * output to the user.
 * <p>
 * See Canvas for more details.
 *
 * @author Peter Jensen (starting code)
 * @author Daniel Kopta (updated starting code)
 * @author ADAM LIU (Implementing Functionality for Assignment 9)
 * @version Fall 2023
 */
public class CacheCostCalculator {
    // Students are welcome to add constants, fields, or helper methods to their class.  If you want additional
    // classes, feel free to put additional private classes at the end of this file.  (One .java file only.)

    /**
     * This helper method computes the ceiling of log base 2 of some number n.  (Any fractional
     * log is rounded up.)  For example:  logBase2(8) returns 3, logBase2(9) returns 4.
     * <p>
     * This method is being provided to help students when they need to solve for x in
     * 2^x = n.
     *
     * @param n any positive integer
     * @return the ceiling of log_2(n)
     */
    public static int logBase2(int n) {
        int x = 0;
        long twoToTheXth = 1;
        while (twoToTheXth < n) {
            x++;
            twoToTheXth *= 2;
        }
        return x;
    }

    /**
     * Application entry point.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        // Working in main is not great.  Instead, let's just create an object
        // and use the run method below to do all the work.  This way, we can
        // create fields and helper methods without them having to be static.  :)
        // (The work really begins in 'run', below.)

        new CacheCostCalculator().run();
    }

    /**
     * Empty constructor -- feel free to add code if needed.
     */
    public CacheCostCalculator() {
        // Add code if needed
    }

    /**
     * Gathers input, runs the simulation, and produces output.
     */
    public void run() {
        // Scan keyboard input from the console.

        Scanner userInput = new Scanner(System.in);

        // Determine which cache architecture is to be used.
        // Caution:  Do not change!!!  My autograder will expect these
        // exact prompts / responses.

        System.out.println("Cache simulator CS 3810");
        System.out.println("  (D)irect-mapped");
        System.out.println("  (S)et associative");
        System.out.println("  (F)ully associative");
        System.out.print("Enter a letter to select a cache and press enter: ");

        String choice = userInput.next();    // Get the first 'word' typed by the user.
        choice = choice.toUpperCase();         // Make it uppercase for consistency.

        boolean simulateDirectMapped = choice.startsWith("D");
        boolean simulateSetAssociative = choice.startsWith("S");
        boolean simulateFullyAssociative = choice.startsWith("F");

        // Each cache type has different customizations.  Get these inputs from the user.
        // Note:  All these variables are not needed.  You may rename them, but you
        // MUST NOT CHANGE THE ORDER OF INPUTS.  The autograder will give the inputs
        // in the order coded below.

        int blockDataBytes = 0;
        int sets = 0;
        int setWays = 0;

        // In all caches, we need to know how many data bytes are cached in each block.
        // Note that we are counting on this being a power of two.  (required)

        System.out.println();
        System.out.print("How many data bytes will be in each cache block? ");
        blockDataBytes = userInput.nextInt();  // Must be a power of two

        // Each cache will require different parameters...

        if (simulateDirectMapped || simulateSetAssociative) {
            setWays = 1;
            System.out.print("How many sets will there be? ");
            sets = userInput.nextInt();  // Must be a power of two
        }
        if (simulateSetAssociative) {
            System.out.print("How many 'ways' will there be for each set? ");
            setWays = userInput.nextInt();  // Any positive integer is OK
        }
        if (simulateFullyAssociative) {
            sets = 1;
            System.out.print("How many blocks will be in this fully associative cache? ");
            setWays = userInput.nextInt();  // Any positive integer is OK
        }

        // The last step is to gather the addresses.  We will allow an unlimited number of addresses.
        // Each address represents a memory request (a read from memory).

        List<Integer> addressList = new ArrayList<Integer>();  // Some students may prefer a list.
        int[] addresses;                               // Some students may prefer an array.

        System.out.println("Enter a whitespace-separated list of addresses, type 'done' followed by enter at the end:");
        while (userInput.hasNextInt())
            addressList.add(userInput.nextInt());

        userInput.close();

        // The input was gathered in a list.  Make an array from it.  Students may use the array and/or the list
        // for their own purposes.

        addresses = new int[addressList.size()];
        for (int i = 0; i < addressList.size(); i++)
            addresses[i] = addressList.get(i);

        // Done gathering inputs.  Simulation code should be added below.

        // Step #1 - students should complete a few computations and update the output
        // statements below.  Do not change the text, only add integer answers to the output.  (No floating point results.)

        int blockOffsetBits = logBase2(blockDataBytes);
        int blockIndexBits = logBase2(sets);
        int tagBits = 32 - logBase2(blockDataBytes) - logBase2(sets);

        int dataBitsPerBlock = blockDataBytes * 8;
        int lruBits = logBase2(setWays);
        int storageBitsPerBlock = 1 + tagBits + dataBitsPerBlock + lruBits;

        int totalBlocks = sets * setWays;
        int totalStorageBits = storageBitsPerBlock * totalBlocks;

        // Report the various properties of the cache.  Reminder:  Do not change the messages
        // or printing order.  Replace the "fix_me" with a calculation or variable in each case.

        System.out.println();
        System.out.println("Number of address bits used as offset bits:        " + blockOffsetBits);
        System.out.println("Number of address bits used as set index bits:     " + blockIndexBits);   // Should be 0 for fully associative cache
        System.out.println("Number of address bits used as tag bits:           " + tagBits);
        System.out.println();

        System.out.println("Number of valid bits needed in each cache block:   " + 1);
        System.out.println("Number of tag bits stored in each cache block:     " + tagBits);
        System.out.println("Number of data bits stored in each cache block:    " + dataBitsPerBlock);
        System.out.println("Number of LRU bits needed in each cache block:     " + lruBits);
        System.out.println("Total number of storage bits needed in each block: " + storageBitsPerBlock);
        System.out.println();

        System.out.println("Total number of blocks in the cache:               " + totalBlocks);
        System.out.println("Total number of storage bits needed for the cache: " + totalStorageBits);
        System.out.println();

        // Simulate the cache.  This step is entirely up to students.  Remember:
        // Simulate memory requests using the addresses in the given order.
        // Do not sort or otherwise alter the order or number of the addresses.

        // Cache containing block objects with dimensions setways x sets.
        Block[][] cache = new Block[setWays][sets];
        // Fill the cache with empty blocks
        for (int i = 0; i < setWays; i++) {
            for (int j = 0; j < sets; j++) {
                cache[i][j] = new Block(false, 0);
            }
        }

        int hits = 0;
        int misses = 0;
        // For each address, update the cache
        for (int address : addressList) {

            // Save the index and tag, set updateCache to true by default
            int setIndex = getSetIndex(address, blockIndexBits, blockOffsetBits);
            int tag = getTag(address, blockIndexBits, blockOffsetBits);
            boolean updateCache = true;

            // Find the set, then iterate through the cache
            for (int i = 0; i < setWays; i++) {
                Block currentBlock = cache[i][setIndex];

                // Block is invalid
                if (!currentBlock.isValid()) {

                    // Increase all other LRU values in cache, set current to 0
                    updateLRU(cache, setIndex, currentBlock.getLRU());
                    currentBlock.setLRU(0);
                    currentBlock.validate();
                    currentBlock.setTag(tag);
                    misses++;
                    updateCache = false;
                    break;
                }

                // Block is valid and contains the correct data
                else if (currentBlock.getTag() == tag && currentBlock.isValid()) {

                    // Update all LRU values in the set
                    updateLRU(cache, setIndex, cache[i][setIndex].getLRU());
                    currentBlock.setLRU(0);
                    hits++;
                    updateCache = false;
                    break;
                }
            }

            // The cache needs to be updated.
            if (updateCache) {
                for (int i = 0; i < setWays; i++) {
                    Block block = cache[i][setIndex];
                    if (block.getLRU() == setWays - 1) {

                        // Update all LRU block values in cache
                        updateLRU(cache, setIndex, block.getLRU());
                        block.setLRU(0);

                        // Change the tag to the new cache data
                        block.setTag(tag);
                        block.validate();
                        misses++;
                        break;
                    }
                }
            }
        }

        // Report the results.  Again, Do not change the messages or printing order.
        // Replace the "fix_me" with a calculation or variable in each case.

        System.out.println("Accessing the addresses gives the following results:");
        System.out.println("Total number of hits:   " + hits);
        System.out.println("Total number of misses: " + misses);

        // Done -- end of run method.
    }

    // Gets the value for the set index bits for the cache
    int getSetIndex(int address, int setBits, int blockBits) {
        int maskedBits = ~(0xffffffff << setBits);
        return (address >> blockBits) & maskedBits;
    }

    // Gets the tag value of the block in the cache
    int getTag(int address, int setBits, int blockBits) {
        int maskedBits = ~(0xffffffff << setBits + blockBits);
        return (address >> (blockBits + setBits)) & maskedBits;
    }

    // Updates all the LRU values in a specific block and set in the cache
    void updateLRU(Block[][] cache, int setIndex, int accessedLRUValue) {
        // Increment anything less than the accessed block if its valid
        for (int i = 0; i < cache.length; i++) {
            if (cache[i][setIndex].getLRU() <= accessedLRUValue) {
                cache[i][setIndex].incLRU();
            }

        }
    }

}

// This Class creates a block holding a valid boolean, tag, and LRU representing a simulated cache for Set Associative and directed mapping cache.
class Block {

    // Holds the validity of the block
    private boolean valid;

    // Holds the value of the tag
    private int tag;

    // Holds the LRU value
    private int LRU;

    // Constructs a block for the cache
    Block(boolean valid, int tag) {
        this.valid = valid;
        this.tag = tag;
        this.LRU = 0;
    }

    // Gets the LRU value
    public int getLRU() {
        return LRU;
    }

    // Sets the LRU value to the input parameter
    public void setLRU(int newValue) {
        LRU = newValue;
    }

    // Increases the LRU value by 1
    public void incLRU() {
        LRU++;
    }

    // Gets the tag value
    public int getTag() {
        return tag;
    }

    // Sets the tag to the input parameter
    public void setTag(int newTag) {
        tag = newTag;
    }

    // Returns whether the block is valid
    public boolean isValid() {
        return valid;
    }

    // Sets the valid variable to true.
    public void validate() {
        valid = true;
    }
}
