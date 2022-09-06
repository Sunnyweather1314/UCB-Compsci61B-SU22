import org.junit.Test;

import static org.junit.Assert.*;

public class ModNCounterTest {

    @Test
    public void testConstructor() {

        ModNCounter c = new ModNCounter(3);
        assertEquals(0, c.value());
        assertEquals(3, c.myN);
    }


    @Test
    public void increment() {
        ModNCounter c = new ModNCounter(3);
        for(int i = 0; i<4; i ++){
            c.increment();
        }
        assertEquals(0, c.value());

    }

    @Test
    public void reset() {
        ModNCounter c = new ModNCounter(3);
        c.increment();
        c.reset();
        assertEquals(0, c.value());
    }
}