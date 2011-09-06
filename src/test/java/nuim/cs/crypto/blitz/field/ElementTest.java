package nuim.cs.crypto.blitz.field;

import java.math.BigInteger;

import junit.framework.TestCase;

public class ElementTest extends TestCase {
    public void testCompareTo() {
        FiniteFieldPrime f = new FiniteFieldPrime(new BigInteger("7"));
        BigInteger r1 = new BigInteger("3");
        BigInteger r2 = new BigInteger("-2");

        Element e1 = new Element(r1, f);
        Element e2 = new Element(r2, f);

        try {
            e1.compareTo(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            e1.compareTo(new String());
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        assertTrue(e1.compareTo(e2) > 0);

        assertTrue(e1.compareTo(e1) == 0);

        assertTrue(e2.compareTo(e1) < 0);

        assertTrue(e1.compareTo(r2) > 0);

        assertTrue(e1.compareTo(r1) == 0);

        assertTrue(e2.compareTo(r1) < 0);
    }

    public void testConstructor() {
        FiniteFieldPrime f = new FiniteFieldPrime(new BigInteger("7"));
        try {
            new Element(null, f);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        f = null;

        try {
            new Element(new BigInteger("3"), f);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        f = new FiniteFieldPrime(new BigInteger("7"));

        Element e = new Element(new BigInteger("3"), f);
        assertTrue(e != null);
    }

    public void testPAdic() {
        FiniteFieldPrime f = new FiniteFieldPrime(new BigInteger("7"));
        BigInteger r = new BigInteger("3");
        Element e = new Element(r, f);

        BigInteger pAdic = e.pAdic();

        assertTrue(pAdic.compareTo(r) == 0);

        try {
            Element.element(null, f);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            Element.element(pAdic, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        Element x = Element.element(pAdic, f);

        assertTrue(x.equals(e));
    }
}