package nuim.cs.crypto.blitz.field.extension;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

public class Fp2ElementTest extends TestCase {
    public void testCompareTo() {
        BigInteger p = new BigInteger("43");
        BigInteger r1 = new BigInteger("11");
        BigInteger i1 = new BigInteger("3");
        BigInteger r2 = new BigInteger("9");
        BigInteger i2 = new BigInteger("6");

        Fp2 fp2 = new Fp2(p);
        Element e1 = new Fp2Element(r1, i1, fp2);
        Element e2 = new Fp2Element(r2, i2, fp2);

        try {
            e1.compareTo(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        assertTrue(e1.compareTo(e2) > 0);

        assertTrue(e1.compareTo(e1) == 0);

        assertTrue(e2.compareTo(e1) < 0);

        e2 = new Fp2Element(r1, i2, fp2);

        assertTrue(e1.compareTo(e2) < 0);

        assertTrue(e1.compareTo(r1) > 0);

        e2 = new Fp2Element(r1, BigInteger.ZERO, fp2);

        assertTrue(e2.compareTo(r1) == 0);

        FiniteFieldPrime fp = new FiniteFieldPrime(p);
        Element e3 = fp.element(r1);

        assertTrue(e2.compareTo(e3) == 0);

        e3 = fp.element(r2);

        assertTrue(e2.compareTo(e3) > 0);
    }

    public void testConstructor() {
        BigInteger p = new BigInteger("43");
        Fp2 fp2 = new Fp2(p);

        BigInteger r = new BigInteger("11");
        BigInteger i = new BigInteger("3");

        try {
            new Fp2Element(null, i, fp2);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new Fp2Element(r, null, fp2);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new Fp2Element(r, i, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        new Fp2Element(r, i, fp2);
    }

    public void testEquals() {
        BigInteger p = new BigInteger("43");
        BigInteger r = new BigInteger("11");
        BigInteger i = new BigInteger("3");

        FiniteFieldPrime fp = new FiniteFieldPrime(p);
        Fp2 fp2 = new Fp2(p);
        Element e = new Fp2Element(r, i, fp2);

        try {
            e.equals(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        assertTrue(!e.equals(new String()));

        Element other = new Fp2Element(r, i, fp2);

        assertTrue(e.equals(other));

        other = new Fp2Element(r, BigInteger.ZERO, fp2);

        assertTrue(!e.equals(other));

        other = fp.element(r);

        assertTrue(!e.equals(other));

        other = fp2.element(BigInteger.ZERO, i);
        e = fp.element(BigInteger.ZERO);

        assertTrue(!e.equals(other));

        assertTrue(!other.equals(BigInteger.ZERO));
    }

    public void testPAdic() {
        BigInteger p = new BigInteger("43");
        BigInteger r = new BigInteger("11");
        BigInteger i = new BigInteger("3");

        Fp2 fp2 = new Fp2(p);
        Element e = new Fp2Element(r, i, fp2);

        BigInteger pAdic = e.pAdic();

        assertTrue(pAdic != null);

        assertTrue(pAdic.compareTo(new BigInteger("140")) == 0);

        try {
            Fp2Element.element(null, fp2);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            Fp2Element.element(pAdic, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        Element x = Fp2Element.element(pAdic, fp2);

        assertTrue(x.equals(e));
    }
}