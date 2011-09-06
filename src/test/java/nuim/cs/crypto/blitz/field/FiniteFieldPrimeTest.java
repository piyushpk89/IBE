package nuim.cs.crypto.blitz.field;

import java.math.BigInteger;

import junit.framework.TestCase;

public class FiniteFieldPrimeTest extends TestCase {
    public void testClone() {
        BigInteger c = new BigInteger("329");
        FiniteFieldPrime field = new FiniteFieldPrime(c);

        FiniteFieldPrime f2 = (FiniteFieldPrime) field.clone();

        assertTrue(field != f2);

        assertTrue(field.equals(f2));
    }

    public void testConstructor() {
        BigInteger c = new BigInteger("329");

        try {
            new FiniteFieldPrime((BigInteger) null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        FiniteFieldPrime field = new FiniteFieldPrime(c);

        assertTrue(field != null);
    }
}