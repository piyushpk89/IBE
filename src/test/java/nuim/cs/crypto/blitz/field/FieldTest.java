package nuim.cs.crypto.blitz.field;

import java.math.BigInteger;

import junit.framework.TestCase;

public class FieldTest extends TestCase {
    public void testClone() {
        BigInteger c = new BigInteger("9");
        Field field = new Field(c);

        Field f2 = (Field) field.clone();

        assertTrue(field.equals(f2));
    }

    public void testConstructor() {
        try {
            new Field((BigInteger) null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        BigInteger c = new BigInteger("73");
        Field field = new Field(c);

        assertTrue(field != null);

        assertTrue(c.compareTo(field.characteristic) == 0);

        assertTrue(c != field.characteristic);
    }

    public void testEquals() {
        BigInteger c1 = new BigInteger("12");
        Field f1 = new Field(c1);

        try {
            f1.equals(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        Field f2 = new Field(c1);

        assertTrue(f1.equals(f2));

        BigInteger c3 = new BigInteger("99");
        Field f3 = new Field(c3);

        assertTrue(!f1.equals(f3));
    }

    public void testGet() {
        BigInteger c = new BigInteger("37");
        Field field = new Field(c);

        assertTrue(field.getChar() != field.characteristic);

        assertTrue(c.compareTo(field.getChar()) == 0);
    }
}