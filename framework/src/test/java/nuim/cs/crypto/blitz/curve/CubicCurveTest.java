package nuim.cs.crypto.blitz.curve;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.Field;
import nuim.cs.crypto.blitz.field.FiniteFieldCharTwo;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

public class CubicCurveTest extends TestCase {
    public void testConstructor() {
        BigInteger a1 = new BigInteger("7");
        BigInteger a2 = new BigInteger("2");
        BigInteger a3 = new BigInteger("91");
        BigInteger a4 = new BigInteger("12");
        BigInteger a6 = new BigInteger("54");
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("93"));

        try {
            new CubicCurve(null, a2, a3, a4, a6, field);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new CubicCurve(a1, null, a3, a4, a6, field);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new CubicCurve(a1, a2, null, a4, a6, field);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new CubicCurve(a1, a2, a3, null, a6, field);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new CubicCurve(a1, a2, a3, a4, null, field);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new CubicCurve(a1, a2, a3, a4, a6, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        CubicCurve curve = new CubicCurve(a1, a2, a3, a4, a6, field);

        assertTrue(curve != null);
    }

    public void testGet() {
        BigInteger a1 = new BigInteger("7");
        BigInteger a2 = new BigInteger("2");
        BigInteger a3 = new BigInteger("91");
        BigInteger a4 = new BigInteger("12");
        BigInteger a6 = new BigInteger("54");
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("93"));

        CubicCurve curve = new CubicCurve(a1, a2, a3, a4, a6, field);

        assertTrue(curve != null);

        assertTrue(a1.compareTo(curve.a1()) == 0 && a1 != curve.a1());

        assertTrue(a2.compareTo(curve.a2()) == 0 && a2 != curve.a2());

        assertTrue(a3.compareTo(curve.a3()) == 0 && a3 != curve.a3());

        assertTrue(a4.compareTo(curve.a4()) == 0 && a4 != curve.a4());

        assertTrue(a6.compareTo(curve.a6()) == 0 && a6 != curve.a6());

        assertTrue(field.equals(curve.getField()) && field != curve.getField());
    }

    public void testInvariants() {
        BigInteger a1 = BigInteger.ZERO;
        BigInteger a2 = BigInteger.ZERO;
        BigInteger a3 = BigInteger.ONE;
        BigInteger a4 = BigInteger.ONE.negate();
        BigInteger a6 = BigInteger.ZERO;
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("751"));
        CubicCurve curve = new CubicCurve(a1, a2, a3, a4, a6, field);

        assertTrue(curve.b2().compareTo(BigInteger.ZERO) == 0);

        assertTrue(curve.b4().compareTo(new BigInteger("749")) == 0);

        assertTrue(curve.b6().compareTo(new BigInteger("1")) == 0);

        assertTrue(curve.b8().compareTo(new BigInteger("750")) == 0);

        assertTrue(curve.c4().compareTo(new BigInteger("48")) == 0);

        assertTrue(curve.c6().compareTo(new BigInteger("535")) == 0);

        assertTrue(curve.discriminant().compareTo(new BigInteger("37")) == 0);

        assertTrue(curve.j().compareTo(new BigInteger("533")) == 0);
    }

    public void testIsomorphism() {
        try {
            CubicCurve.isomorphism(null);
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
        CubicCurve curve = new CubicCurve(a1, a2, a3, a4, a6, field);

        CubicCurve c2 = CubicCurve.isomorphism(curve);

        assertTrue(curve != c2);

        assertTrue(c2.a1().compareTo(BigInteger.ZERO) == 0);

        assertTrue(c2.a2().compareTo(BigInteger.ZERO) == 0);

        assertTrue(c2.a3().compareTo(BigInteger.ZERO) == 0);

        assertTrue(c2.a4().compareTo(new BigInteger("750")) == 0);

        assertTrue(c2.a6().compareTo(new BigInteger("188")) == 0);

        // char = 2, a1 = 0
        a1 = BigInteger.ZERO;
        a2 = BigInteger.ONE;
        a3 = BigInteger.ONE;
        a4 = BigInteger.ZERO;
        a6 = BigInteger.ONE;
        field = new FiniteFieldCharTwo(new BigInteger("11"));
        curve = new CubicCurve(a1, a2, a3, a4, a6, field);

        c2 = CubicCurve.isomorphism(curve);

        assertTrue(curve != c2);

        assertTrue(c2.a1().compareTo(BigInteger.ZERO) == 0);

        assertTrue(c2.a2().compareTo(BigInteger.ZERO) == 0);

        assertTrue(c2.a3().compareTo(BigInteger.ONE) == 0);

        assertTrue(c2.a4().compareTo(BigInteger.ONE) == 0);

        assertTrue(c2.a6().compareTo(BigInteger.ONE) == 0);

        // char = 2, a1 != 0
        a1 = BigInteger.ONE;
        a2 = BigInteger.ONE;
        a3 = BigInteger.ZERO;
        a4 = BigInteger.ONE;
        a6 = BigInteger.ONE;
        field = new FiniteFieldCharTwo(new BigInteger("11"));
        curve = new CubicCurve(a1, a2, a3, a4, a6, field);

        c2 = CubicCurve.isomorphism(curve);

        assertTrue(curve != c2);

        assertTrue(c2.a1().compareTo(BigInteger.ONE) == 0);

        assertTrue(c2.a2().compareTo(BigInteger.ONE) == 0);

        assertTrue(c2.a3().compareTo(BigInteger.ZERO) == 0);

        assertTrue(c2.a4().compareTo(BigInteger.ZERO) == 0);

        assertTrue(c2.a6().compareTo(BigInteger.ZERO) == 0);

        /* test removed until field char 3 implemented
        // char = 3, d2 != 0
        a1 = new BigInteger( "2" );
        a2 = new BigInteger( "0" );
        a3 = new BigInteger( "0" );
        a4 = new BigInteger( "2" );
        a6 = new BigInteger( "1" );
        field = new FiniteFieldCharThree( new BigInteger( "11" ) );
        curve = new CubicCurve( a1, a2, a3, a4, a6, field );

        c2 = CubicCurve.isomorphism( curve );

        assertTrue( curve != c2 );

        assertTrue( c2.a1().compareTo( BigInteger.ZERO ) == 0 );

        assertTrue( c2.a2().compareTo( BigInteger.ONE ) == 0 );

        assertTrue( c2.a3().compareTo( BigInteger.ZERO ) == 0 );

        assertTrue( c2.a4().compareTo( BigInteger.ZERO ) == 0 );

        assertTrue( c2.a6().compareTo( new BigInteger( "2" ) ) == 0 );

        // char = 3, d2 = 0
        a1 = new BigInteger( "0" );
        a2 = new BigInteger( "0" );
        a3 = new BigInteger( "2" );
        a4 = new BigInteger( "2" );
        a6 = new BigInteger( "1" );
        field = new FiniteFieldCharThree( new BigInteger( "11" ) );
        curve = new CubicCurve( a1, a2, a3, a4, a6, field );

        c2 = CubicCurve.isomorphism( curve );

        assertTrue( curve != c2 );

        assertTrue( c2.a1().compareTo( BigInteger.ZERO ) == 0 );

        assertTrue( c2.a2().compareTo( BigInteger.ZERO ) == 0 );

        assertTrue( c2.a3().compareTo( BigInteger.ZERO ) == 0 );

        assertTrue( c2.a4().compareTo( new BigInteger( "2" ) ) == 0 );

        assertTrue( c2.a6().compareTo( new BigInteger( "2" ) ) == 0 );
         */
    }

    public void testToString() {
        BigInteger a1 = new BigInteger("7");
        BigInteger a2 = new BigInteger("2");
        BigInteger a3 = new BigInteger("91");
        BigInteger a4 = new BigInteger("12");
        BigInteger a6 = new BigInteger("54");
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("93"));

        CubicCurve curve = new CubicCurve(a1, a2, a3, a4, a6, field);

        String str = curve.toString();

        assertTrue(str != null && str.length() > 0);

        assertTrue(str.equals("y2 + 7xy + 91y = x3 + 2x2 + 12x + 54"));
    }
}