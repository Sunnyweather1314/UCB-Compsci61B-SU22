package gh2;

 import deque.Deque;
 import deque.LinkedListDeque;
 import deque.ArrayDeque;

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
     private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int capacity = (int) Math.round(SR / frequency);
        this.buffer = new LinkedListDeque<Double>();
        for (int i = 0; i < capacity; i++){
            buffer.addLast((double)0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        double r = Math.random() - 0.5;
        int size = buffer.size();
        buffer = new LinkedListDeque<Double>();
        for (int i = 0; i < size; i++){
            buffer.addLast(r);
            r = Math.random() - 0.5;
        }
        //       Make sure that your random numbers are different from each
        //       other. This does not mean that you need to check that the numbers
        //       are different from each other. It means you should repeatedly call
        //       Math.random() - 0.5 to generate new random numbers for each array index.
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double k1 = buffer.removeFirst();
        double k2 = buffer.removeFirst();
        buffer.addFirst(k2);
        buffer.addLast((k1+k2)/2*DECAY);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        double k = buffer.removeFirst();
        buffer.addFirst(k);
        return k;
    }
}