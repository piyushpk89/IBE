package nuim.cs.crypto.util;

import java.math.BigInteger;

import junit.framework.TestCase;

public class BigSquareRootTest extends TestCase {
    public void testCeiling() {
        BigInteger n = new BigInteger("30");
        BigSquareRoot root = new BigSquareRoot();

        BigInteger r = root.ceiling(n);
        BigInteger expected = new BigInteger("6");

        assertTrue(r.compareTo(expected) == 0);

        n = new BigInteger("9");
        r = root.ceiling(n);
        expected = new BigInteger("3");

        assertTrue(r.compareTo(expected) == 0);
    }

    public void testConstructor() {
        BigSquareRoot root = new BigSquareRoot();

        assertTrue(root.getScale() == BigSquareRoot.DEFAULT_SCALE
                   && root.getIterations() == BigSquareRoot.DEFAULT_MAX_ITERATIONS);

        root = new BigSquareRoot(13);

        assertTrue(root.getScale() == 13
                   && root.getIterations() == BigSquareRoot.DEFAULT_MAX_ITERATIONS);

        root = new BigSquareRoot(45, 143);

        assertTrue(root.getScale() == 45 && root.getIterations() == 143);
    }

    public void testFloor() {
        BigInteger n = new BigInteger("30");
        BigSquareRoot root = new BigSquareRoot();

        BigInteger r = root.floor(n);
        BigInteger expected = new BigInteger("5");

        assertTrue(r.compareTo(expected) == 0);
    }

    public void testSquareRoot() {
        BigInteger n = new BigInteger("9");
        BigSquareRoot root = new BigSquareRoot();

        BigInteger r = root.squareRoot(n).toBigInteger();
        BigInteger expected = new BigInteger("3");

        assertTrue(r.compareTo(expected) == 0);

        n = new BigInteger("12");
        r = root.squareRoot(n).toBigInteger();

        assertTrue(r.compareTo(expected) == 0);
    }
}