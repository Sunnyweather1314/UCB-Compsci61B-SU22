import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanSetTest {

    @Test
    public void testBasics() {
        BooleanSet aSet = new BooleanSet(100);
        assertEquals(0, aSet.size());
        for (int i = 0; i < 100; i += 2) {
            aSet.add(i);
            assertTrue(aSet.contains(i));
        }
        assertEquals(50, aSet.size());

        for (int i = 0; i < 100; i += 2) {
            aSet.remove(i);
            assertFalse(aSet.contains(i));
        }
        assertTrue(aSet.isEmpty());
        assertEquals(0, aSet.size());
    }

    @Test
    public void InttoArrayTest() {
        BooleanSet aSet = new BooleanSet(49);
        for (int i = 0; i < 50; i += 1) {
            aSet.add(i);
            assertTrue(aSet.contains(i));
        }

        assertEquals(50, aSet.size());

        for (int i = 1; i < 50; i += 2) {
            aSet.remove(i);
            assertFalse(aSet.contains(i));
        }
        assertEquals(25, aSet.size());
        int[] arr = aSet.toIntArray();
        assertEquals(25, arr.length);
        assertEquals(2, arr[1]);
        for(int k:arr){
            System.out.print(k);
        }

        aSet = new BooleanSet(49);
        arr = aSet.toIntArray();
        int[] k = new int[0];
//        assertEquals(k, arr);

    }
}