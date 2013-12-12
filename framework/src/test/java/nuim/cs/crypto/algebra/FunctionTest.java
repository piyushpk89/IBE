package nuim.cs.crypto.algebra;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class FunctionTest extends TestCase {
    public void testEvaluate() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("79"));
        Element a = field.element(new BigInteger("4"));
        Element b = field.element(new BigInteger("87"));
        Element c = field.element(new BigInteger("-23"));
        AffinePoint infinity = new AffinePoint(field.element(BigInteger.ZERO),
                                               field.element(BigInteger.ZERO));
        Function f = new Function(infinity);
        f.set(a, b, c);

        AffinePoint p = new AffinePoint(field.element(new BigInteger("2")),
                                        field.element(new BigInteger("3")));

        Element one = field.element(BigInteger.ONE);

        assertTrue(f.evaluate(infinity).equals(one));

        Element r = field.element(new BigInteger("9"));

        assertTrue(f.evaluate(p).equals(r));
    }

    public void testSet() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("79"));
        Element a = field.element(new BigInteger("4"));
        Element b = field.element(new BigInteger("87"));
        Element c = field.element(new BigInteger("-23"));
        AffinePoint infinity = new AffinePoint(field.element(BigInteger.ZERO),
                                               field.element(BigInteger.ZERO));
        Function f = new Function(infinity);

        try {
            f.set(null, b, c);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            f.set(a, null, c);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            f.set(a, b, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        f.set(a, b, c);
    }
}