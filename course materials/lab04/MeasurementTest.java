import org.junit.Test;

import static org.junit.Assert.*;

public class MeasurementTest {
    @Test
    public void test1() {
        // TODO: stub for first test
        Measurement m1 = new Measurement();
        assertEquals(0, m1.getFeet());
        assertEquals(0, m1.getInches());
    }

    @Test
    // TODO: Add additional JUnit tests for Measurement.java here.
    public void test2() {
        Measurement m = new Measurement(2,3);
        assertEquals(2, m.getFeet());
        assertEquals(3, m.getInches());
    }
    @Test
    public void test3() {
        Measurement m = new Measurement(3);
        assertEquals(3, m.getFeet());
        assertEquals(0, m.getInches());
    }

    @Test
    public void testPlus1() {
        Measurement m1 = new Measurement(2,4);
        Measurement m2 = new Measurement(2,5);
        Measurement m3 = m1.plus(m2);
        assertEquals(4, m3.getFeet());
        assertEquals(9, m3.getInches());
    }

    @Test
    public void testPlus2() {
        Measurement m1 = new Measurement(2,4);
        Measurement m2 = new Measurement(2,10);
        Measurement m3 = m1.plus(m2);
        assertEquals(5, m3.getFeet());
        assertEquals(2, m3.getInches());
    }

    @Test
    public void testMinus1() {
        Measurement m1 = new Measurement(2,6);
        Measurement m2 = new Measurement(2,5);
        Measurement m3 = m1.minus(m2);
        assertEquals(0, m3.getFeet());
        assertEquals(1, m3.getInches());
    }

    @Test
    public void testMinus2() {
        Measurement m1 = new Measurement(2,4);
        Measurement m2 = new Measurement(1,8);
        Measurement m3 = m1.minus(m2);
        assertEquals(0, m3.getFeet());
        assertEquals(8, m3.getInches());
    }

    @Test
    public void testMul1() {
        Measurement m1 = new Measurement(2,6);
        Measurement m2 = m1.multiple(2);
        assertEquals(5, m2.getFeet());
        assertEquals(0, m2.getInches());
        assertEquals("5\'0\"", m2.toString());
    }

    @Test
    public void testMul2() {
        Measurement m1 = new Measurement(2,1);
        Measurement m2 = m1.multiple(2);
        assertEquals(4, m2.getFeet());
        assertEquals(2, m2.getInches());
        assertEquals("4\'2\"", m2.toString());
    }

}