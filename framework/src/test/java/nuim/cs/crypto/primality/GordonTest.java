package nuim.cs.crypto.primality;

import java.math.BigInteger;
import java.util.Random;

import junit.framework.TestCase;

public class GordonTest extends TestCase {
    public void testBitLength() {
        Gordon gordon = new Gordon(10);
        BigInteger p = gordon.getP();
        assertTrue(p.bitLength() > 50);

        gordon = new Gordon(40);
        p = gordon.getP();
        assertTrue(p.bitLength() > 100);

        gordon = new Gordon(500);
        p = gordon.getP();
        assertTrue(p.bitLength() > 1000);
    }

    public void testConstructor() {
        try {
            new Gordon(0, 16, new Random(), Gordon.RANDOM_INT,
                       Gordon.RANDOM_INT);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            new Gordon(-1, 16, new Random(), Gordon.RANDOM_INT,
                       Gordon.RANDOM_INT);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            new Gordon(10, 0, new Random(), Gordon.RANDOM_INT,
                       Gordon.RANDOM_INT);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            new Gordon(10, -1, new Random(), Gordon.RANDOM_INT,
                       Gordon.RANDOM_INT);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            new Gordon(10, 16, null, Gordon.RANDOM_INT, Gordon.RANDOM_INT);
            assertTrue(false);
        } catch (NullPointerException e) {
            assertTrue(true);
        }

        try {
            new Gordon(10, 16, new Random(), null, Gordon.RANDOM_INT);
            assertTrue(false);
        } catch (NullPointerException e) {
            assertTrue(true);
        }

        try {
            new Gordon(10, 16, new Random(), BigInteger.ZERO, Gordon.RANDOM_INT);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            new Gordon(10, 16, new Random(), BigInteger.ONE.negate(),
                       Gordon.RANDOM_INT);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            new Gordon(10, 16, new Random(), Gordon.RANDOM_INT, BigInteger.ZERO);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            new Gordon(10, 16, new Random(), Gordon.RANDOM_INT,
                       BigInteger.ONE.negate());
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            new Gordon();
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    public void testGet() {
        Gordon gordon = new Gordon();
        BigInteger p = new BigInteger("1879666776044251");
        BigInteger r = new BigInteger("41180801");
        BigInteger s = new BigInteger("839");
        BigInteger t = new BigInteger("757");

        gordon.set(p, r, s, t);

        assertTrue(p.compareTo(gordon.getP()) == 0);

        assertTrue(r.compareTo(gordon.getR()) == 0);

        assertTrue(s.compareTo(gordon.getS()) == 0);

        assertTrue(t.compareTo(gordon.getT()) == 0);
    }

    public void testGordon() {
        Gordon gordon = new Gordon(4, 10, new Random(), Gordon.RANDOM_INT,
                                   Gordon.RANDOM_INT);

        BigInteger p = gordon.getP();
        BigInteger q = gordon.getS();
        BigInteger l = p.add(BigInteger.ONE).divide(q);

        assertTrue(p.compareTo(q.multiply(l).subtract(BigInteger.ONE)) == 0);
    }

    public void testSet() {
        Gordon gordon = new Gordon();
        BigInteger p = new BigInteger("1879666776044251");
        BigInteger r = new BigInteger("41180801");
        BigInteger s = new BigInteger("839");
        BigInteger t = new BigInteger("757");

        BigInteger composite = new BigInteger("8");

        try {
            gordon.set(null, r, s, t);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            gordon.set(composite, r, s, t);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            gordon.set(p, null, s, t);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            gordon.set(p, composite, s, t);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            gordon.set(p, r, null, t);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            gordon.set(p, r, composite, t);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            gordon.set(p, r, s, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            gordon.set(p, r, s, composite);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        gordon.set(p, r, s, t);
    }
}