package nuim.cs.crypto.blitz.curve.strategy.affine;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.curve.EllipticCurve;
import nuim.cs.crypto.blitz.field.Field;
import nuim.cs.crypto.blitz.field.FiniteFieldCharTwo;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.field.extension.Fp2;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class PrimeAffineDoubleStrategyTest extends TestCase {
    public void testConstructor() {
        try {
            new PrimeAffineDoubleStrategy(null);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        BigInteger a1 = BigInteger.ONE;
        BigInteger a2 = BigInteger.ONE;
        BigInteger a3 = BigInteger.ONE;
        BigInteger a4 = BigInteger.ONE;
        BigInteger a6 = BigInteger.ONE;
        Field field = new FiniteFieldCharTwo(new BigInteger("11"));
        EllipticCurve curve = new EllipticCurve(a1, a2, a3, a4, a6, field);

        try {
            new PrimeAffineDoubleStrategy(curve);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        field = new FiniteFieldPrime(new BigInteger("17"));
        curve = new EllipticCurve(a1, a2, a3, a4, a6, field);

        try {
            new PrimeAffineDoubleStrategy(curve);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        a1 = BigInteger.ZERO;
        curve = new EllipticCurve(a1, a2, a3, a4, a6, field);

        try {
            new PrimeAffineDoubleStrategy(curve);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        a2 = BigInteger.ZERO;
        curve = new EllipticCurve(a1, a2, a3, a4, a6, field);

        try {
            new PrimeAffineDoubleStrategy(curve);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        a3 = BigInteger.ZERO;
        curve = new EllipticCurve(a1, a2, a3, a4, a6, field);
        PrimeAffineDoubleStrategy strategy = new PrimeAffineDoubleStrategy(
                                                                           curve);

        assertTrue(strategy != null);
    }

    public void testDoubleComplexPoint() {
        BigInteger field = new BigInteger("11");
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        Fp2 fp2 = new Fp2(field);
        EllipticCurve curve = new EllipticCurve(BigInteger.ZERO,
                                                BigInteger.ZERO,
                                                BigInteger.ZERO,
                                                BigInteger.ONE,
                                                BigInteger.ZERO, fp);

        PrimeAffineDoubleStrategy strategy = new PrimeAffineDoubleStrategy(
                                                                           curve);
        curve.setStrategy(strategy);

        AffinePoint p = new AffinePoint(fp2.element(new BigInteger("6"),
                                                    BigInteger.ZERO),
                                        fp2.element(BigInteger.ZERO,
                                                    new BigInteger("3")));

        AffinePoint r = curve.doublePoint(p);
        AffinePoint expected = curve.negate(p);
        assertTrue(expected.equals(r));
    }

    public void testDoublePoint() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("17"));
        EllipticCurve curve = new EllipticCurve(BigInteger.ZERO,
                                                BigInteger.ZERO,
                                                BigInteger.ZERO,
                                                BigInteger.ONE, BigInteger.ONE,
                                                field);

        PrimeAffineDoubleStrategy strategy = new PrimeAffineDoubleStrategy(
                                                                           curve);
        AffinePoint p = new AffinePoint(field.element(BigInteger.ONE),
                                        field.element(new BigInteger("3")));

        try {
            strategy.doublePoint(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        AffinePoint r = strategy.doublePoint(p);

        AffinePoint expected = new AffinePoint(
                                               field.element(new BigInteger("6")),
                                               field.element(new BigInteger("5")));

        assertTrue(expected.equals(r));
    }
}