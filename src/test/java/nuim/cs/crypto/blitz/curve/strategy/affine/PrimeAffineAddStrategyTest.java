package nuim.cs.crypto.blitz.curve.strategy.affine;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.curve.EllipticCurve;
import nuim.cs.crypto.blitz.field.Field;
import nuim.cs.crypto.blitz.field.FiniteField;
import nuim.cs.crypto.blitz.field.FiniteFieldCharTwo;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.field.extension.Fp2;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class PrimeAffineAddStrategyTest extends TestCase {
    public void testAdd() {
        BigInteger a1 = BigInteger.ZERO;
        BigInteger a2 = BigInteger.ZERO;
        BigInteger a3 = BigInteger.ZERO;
        BigInteger a4 = BigInteger.ONE;
        BigInteger a6 = BigInteger.ONE;
        FiniteField field = new FiniteFieldPrime(new BigInteger("17"));
        EllipticCurve curve = new EllipticCurve(a1, a2, a3, a4, a6, field);

        PrimeAffineAddStrategy strategy = new PrimeAffineAddStrategy(curve);
        AffinePoint p = new AffinePoint(field.element(new BigInteger("2")),
                                        field.element(BigInteger.ZERO));
        AffinePoint q = new AffinePoint(field.element(BigInteger.ONE),
                                        field.element(new BigInteger("3")));

        try {
            strategy.add(null, q);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            strategy.add(p, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        AffinePoint r = strategy.add(p, q);
        AffinePoint expected = new AffinePoint(
                                               field.element(new BigInteger("6")),
                                               field.element(new BigInteger(
                                                                            "12")));
        assertTrue(r.equals(expected));

        // a more difficult add test
        a4 = new BigInteger("9");
        field = new FiniteFieldPrime(new BigInteger("23"));
        curve = new EllipticCurve(a1, a2, a3, a4, a6, field);

        strategy = new PrimeAffineAddStrategy(curve);
        p = new AffinePoint(field.element(new BigInteger("14")),
                            field.element(new BigInteger("14")));
        q = new AffinePoint(field.element(new BigInteger("13")),
                            field.element(new BigInteger("10")));
        r = strategy.add(p, q);
        expected = new AffinePoint(field.element(new BigInteger("12")),
                                   field.element(new BigInteger("17")));
        assertTrue(r.equals(expected));
    }

    public void testComplexAdd() {
        BigInteger field = new BigInteger("11");
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        Fp2 fp2 = new Fp2(field);
        EllipticCurve curve = new EllipticCurve(BigInteger.ZERO,
                                                BigInteger.ZERO,
                                                BigInteger.ZERO,
                                                BigInteger.ONE,
                                                BigInteger.ZERO, fp);

        curve.setStrategy(new PrimeAffineAddStrategy(curve));
        curve.setStrategy(new PrimeAffineDoubleStrategy(curve));

        AffinePoint p = new AffinePoint(fp2.element(new BigInteger("6"),
                                                    BigInteger.ZERO),
                                        fp2.element(BigInteger.ZERO,
                                                    new BigInteger("8")));

        AffinePoint r = curve.add(p, p);
        AffinePoint expected = curve.negate(p);
        assertTrue(expected.equals(r));
    }

    public void testConstructor() {
        try {
            new PrimeAffineAddStrategy(null);
            assertTrue(false);
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
            new PrimeAffineAddStrategy(curve);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        field = new FiniteFieldPrime(new BigInteger("17"));
        curve = new EllipticCurve(a1, a2, a3, a4, a6, field);

        try {
            new PrimeAffineAddStrategy(curve);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        a1 = BigInteger.ZERO;
        curve = new EllipticCurve(a1, a2, a3, a4, a6, field);

        try {
            new PrimeAffineAddStrategy(curve);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        a2 = BigInteger.ZERO;
        curve = new EllipticCurve(a1, a2, a3, a4, a6, field);

        try {
            new PrimeAffineAddStrategy(curve);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        a3 = BigInteger.ZERO;
        curve = new EllipticCurve(a1, a2, a3, a4, a6, field);
        PrimeAffineAddStrategy strategy = new PrimeAffineAddStrategy(curve);

        assertTrue(strategy != null);
    }
}