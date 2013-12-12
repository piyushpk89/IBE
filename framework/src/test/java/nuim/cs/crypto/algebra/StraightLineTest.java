package nuim.cs.crypto.algebra;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class StraightLineTest extends TestCase {
    public void testConstructor() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("79"));
        AffinePoint p = new AffinePoint(field.element(BigInteger.ONE),
                                        field.element(BigInteger.ONE));
        AffinePoint q = new AffinePoint(field.element(new BigInteger("2")),
                                        field.element(new BigInteger("2")));
        AffinePoint infinity = new AffinePoint(field.element(BigInteger.ZERO),
                                               field.element(BigInteger.ZERO));

        try {
            new StraightLine(null, q, infinity);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new StraightLine(p, null, infinity);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new StraightLine(p, q, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        StraightLine line = new StraightLine(p, q, infinity);

        assertTrue(line.a.toBigInteger().compareTo(BigInteger.ONE.negate()) == 0);

        assertTrue(line.b.toBigInteger().compareTo(BigInteger.ONE) == 0);

        assertTrue(line.c.toBigInteger().compareTo(BigInteger.ZERO) == 0);
    }

    public void testEvaluate() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("79"));
        AffinePoint p = new AffinePoint(field.element(BigInteger.ONE),
                                        field.element(BigInteger.ONE));
        AffinePoint q = new AffinePoint(field.element(new BigInteger("2")),
                                        field.element(new BigInteger("2")));
        AffinePoint infinity = new AffinePoint(field.element(BigInteger.ZERO),
                                               field.element(BigInteger.ZERO));
        StraightLine line = new StraightLine(p, q, infinity);

        try {
            line.evaluate(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        AffinePoint r = new AffinePoint(field.element(new BigInteger("3")),
                                        field.element(new BigInteger("5")));

        assertTrue(line.evaluate(r).toBigInteger().compareTo(
                                                             new BigInteger("2")) == 0);
    }
}