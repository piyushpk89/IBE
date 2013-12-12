package nuim.cs.crypto.blitz.point;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

public class AffinePointTest extends TestCase {
    public void testClone() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("17"));
        Element x = field.element(new BigInteger("6"));
        Element y = field.element(new BigInteger("5"));
        AffinePoint p = new AffinePoint(x, y);

        AffinePoint q = (AffinePoint) p.clone();

        assertTrue(p.equals(q));
    }

    public void testCompareTo() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("17"));
        Element x = field.element(new BigInteger("6"));
        Element y = field.element(new BigInteger("5"));
        AffinePoint p = new AffinePoint(x, y);

        x = field.element(new BigInteger("1"));
        y = field.element(new BigInteger("7"));
        AffinePoint q = new AffinePoint(x, y);

        assertTrue(p.compareTo(q) > 0);
        assertTrue(q.compareTo(p) < 0);
        assertTrue(p.compareTo(p) == 0);

        x = field.element(new BigInteger("6"));
        q = new AffinePoint(x, y);

        assertTrue(p.compareTo(q) < 0);
    }

    public void testConstructor() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("17"));
        Element x = field.element(new BigInteger("6"));
        Element y = field.element(new BigInteger("5"));

        try {
            new AffinePoint(null, y);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new AffinePoint(x, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        AffinePoint p = new AffinePoint(x, y);

        assertTrue(x != p.x && x.equals(p.x) && y != p.y && y.equals(p.y));

        AffinePoint q = new AffinePoint(p);

        assertTrue(p.x != q.x && p.x.equals(q.x) && p.y != q.y
                   && p.y.equals(q.y));
    }

    public void testEquals() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("17"));
        Element x = field.element(new BigInteger("6"));
        Element y = field.element(new BigInteger("5"));
        AffinePoint p = new AffinePoint(x, y);

        try {
            p.equals(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        assertTrue(p.equals(p));

        AffinePoint q = new AffinePoint(x, y);

        assertTrue(p.equals(q));

        x = field.element(new BigInteger("1"));
        y = field.element(new BigInteger("7"));
        q = new AffinePoint(x, y);

        assertTrue(!p.equals(q));
    }

    public void testGet() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("17"));
        Element x = field.element(new BigInteger("6"));
        Element y = field.element(new BigInteger("5"));
        AffinePoint p = new AffinePoint(x, y);

        assertTrue(p.x() != null && p.x() != p.x && p.x().equals(x)
                   && p.y() != null && p.y() != p.y && p.y().equals(y));
    }

    public void testToString() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("17"));
        Element x = field.element(new BigInteger("6"));
        Element y = field.element(new BigInteger("5"));
        AffinePoint p = new AffinePoint(x, y);

        String r = p.toString();

        assertTrue(r.equals(new String("(6,5)")));
    }
}