package nuim.cs.crypto.blitz.curve;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.Field;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

public class EllipticCurveTest extends TestCase {
    public void testConstructor() {
        BigInteger a1 = new BigInteger("213");
        BigInteger a2 = new BigInteger("39");
        BigInteger a3 = new BigInteger("22");
        BigInteger a4 = new BigInteger("991");
        BigInteger a6 = new BigInteger("293");
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("93"));

        EllipticCurve curve = new EllipticCurve(a1, a2, a3, a4, a6, field);

        assertTrue(curve != null);

        assertTrue(a1.compareTo(curve.a1()) == 0);

        assertTrue(a2.compareTo(curve.a2()) == 0);

        assertTrue(a3.compareTo(curve.a3()) == 0);

        assertTrue(a4.compareTo(curve.a4()) == 0);

        assertTrue(a6.compareTo(curve.a6()) == 0);

        assertTrue(field.equals(curve.getField()));
    }

    public void testIsomorphism() {
        try {
            EllipticCurve.isomorphism(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        // char != 2,3
        BigInteger a1 = BigInteger.ZERO;
        BigInteger a2 = BigInteger.ZERO;
        BigInteger a3 = BigInteger.ONE;
        BigInteger a4 = BigInteger.ONE.negate();
        BigInteger a6 = BigInteger.ZERO;
        Field field = new FiniteFieldPrime(new BigInteger("751"));
        EllipticCurve curve = new EllipticCurve(a1, a2, a3, a4, a6, field);

        EllipticCurve c2 = EllipticCurve.isomorphism(curve);

        assertTrue(curve != c2);

        assertTrue(c2.a1().compareTo(BigInteger.ZERO) == 0);

        assertTrue(c2.a2().compareTo(BigInteger.ZERO) == 0);

        assertTrue(c2.a3().compareTo(BigInteger.ZERO) == 0);

        assertTrue(c2.a4().compareTo(new BigInteger("750")) == 0);

        assertTrue(c2.a6().compareTo(new BigInteger("188")) == 0);
    }
}