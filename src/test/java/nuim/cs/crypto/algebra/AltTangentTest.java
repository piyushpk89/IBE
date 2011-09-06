package nuim.cs.crypto.algebra;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class AltTangentTest extends TestCase {
    public void testConstructor() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("97"));
        AffinePoint p = new AffinePoint(field.element(BigInteger.ONE),
                                        field.element(BigInteger.ONE));
        AffinePoint infinity = new AffinePoint(field.element(BigInteger.ZERO),
                                               field.element(BigInteger.ZERO));

        try {
            new AltTangent(null, BigInteger.ZERO, infinity);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new AltTangent(p, null, infinity);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new AltTangent(p, BigInteger.ZERO, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        AltTangent line = new AltTangent(p, BigInteger.ZERO, infinity);

        assertTrue(line.a.toBigInteger().compareTo(new BigInteger("-50")) == 0);
        assertTrue(line.b.toBigInteger().compareTo(BigInteger.ONE) == 0);
        assertTrue(line.c.toBigInteger().compareTo(new BigInteger("49")) == 0);
    }

    public void testEvaluate() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("97"));
        AffinePoint p = new AffinePoint(field.element(BigInteger.ONE),
                                        field.element(BigInteger.ONE));
        AffinePoint infinity = new AffinePoint(field.element(BigInteger.ZERO),
                                               field.element(BigInteger.ZERO));

        AltTangent line = new AltTangent(p, BigInteger.ZERO, infinity);

        try {
            line.evaluate(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        AffinePoint r = new AffinePoint(field.element(new BigInteger("3")),
                                        field.element(new BigInteger("5")));

        assertTrue(line.evaluate(r).equals(BigInteger.ONE));
    }
}