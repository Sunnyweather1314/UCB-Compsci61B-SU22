import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;



public class TestUnionFind {


    @Test
    public void testInit() {
        UnionFind f = new UnionFind(10);
        for (int i = 0; i < 10; i++) {
            assertEquals(1, f.sizeOf(i));
            assertEquals(i, f.find(i));
            assertEquals(-1, f.parent(i));

        }
        assertFalse(f.connected(0,1));
    }

    @Test
    public void testUnion() {
        UnionFind f = new UnionFind(10);
        f.union(0,1);
        f.union(1, 2);
        assertEquals(1, f.find(1));
        assertEquals(1, f.find(2));
        assertEquals(3, f.sizeOf(0));

        // for (int i = 0; i < 10; i++) {
        //     assertEquals(-1, f.sizeOf(i));
        //     assertEquals(i, f.parent(i));
        // }
    }

    @Test
    public void testUnion2() {
        UnionFind f = new UnionFind(10);
        f.union(0,1);
        f.union(1, 2);
        f.union(1, 3);
        f.union(2, 4);
        f.union(8, 9);
        assertEquals(1, f.find(1));
        assertEquals(1, f.find(2));
        assertEquals(5, f.sizeOf(0));
        assertEquals(9, f.find(9));
        assertEquals(9, f.find(9));

        assertEquals(2, f.sizeOf(8));


    }


}
