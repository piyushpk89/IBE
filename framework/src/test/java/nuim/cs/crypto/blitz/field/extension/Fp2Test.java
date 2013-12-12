package nuim.cs.crypto.blitz.field.extension;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

public class Fp2Test extends TestCase {
    public void testAdd() {
        BigInteger p = new BigInteger("63");
        Fp2 f = new Fp2(p);

        Element a = f.element(new BigInteger("76"), new BigInteger("-123"));
        Element b = f.element(new BigInteger("37"), new BigInteger("99"));

        try {
            f.add(null, b);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            f.add(a, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        Element r = f.add(a, b);

        assertTrue(r instanceof Fp2Element);

        Fp2Element r2 = (Fp2Element) r;
        assertTrue(r2.real().compareTo(new BigInteger("113")) == 0);
        assertTrue(r2.imag().compareTo(new BigInteger("-24")) == 0);
    }

    public void testClone() {
        BigInteger p = new BigInteger("63");
        Fp2 f = new Fp2(p);
        Element a = f.element(new BigInteger("37"), new BigInteger("99"));

        Element b = (Element) a.clone();

        assertTrue(a.equals(b));
    }

    public void testConstructor() {
        try {
            new Fp2(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        Fp2 fp2 = new Fp2(new BigInteger("7"));

        assertTrue(fp2 != null);
    }

    public void testCubeRootOfUnity() {
        BigInteger p = new BigInteger("63");
        Fp2 f = new Fp2(p);
        Fp2Element r = f.cubeRootOfUnity();

        Fp2Element e = f.element(new BigInteger("31"), new BigInteger("54"));

        assertTrue(e.equals(r));
    }

    public void testDivide() {
        BigInteger p = new BigInteger("63");
        Fp2 f = new Fp2(p);

        Element a = f.element(new BigInteger("76"), new BigInteger("-123"));
        Element b = f.element(new BigInteger("37"), new BigInteger("99"));

        try {
            f.div(null, b);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            f.div(a, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        Element r = f.div(a, b);

        assertTrue(r instanceof Fp2Element);

        Fp2Element r2 = (Fp2Element) r;
        assertTrue(r2.real().compareTo(new BigInteger("-93650")) == 0);
        assertTrue(r2.imag().compareTo(new BigInteger("-120750")) == 0);
    }

    public void testElement() {
        Fp2 f = new Fp2(new BigInteger("7"));

        try {
            f.element(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        BigInteger r = new BigInteger("3");
        BigInteger i = new BigInteger("5");

        Element e = f.element(r);
        assertTrue(e instanceof Fp2Element);

        try {
            f.element(null, i);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            f.element(r, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        e = f.element(r, i);
        assertTrue(e instanceof Fp2Element);
    }

    public void testMixedOperation() {
        BigInteger p = new BigInteger("63");
        FiniteFieldPrime fp = new FiniteFieldPrime(p);
        Fp2 fp2 = new Fp2(p);

        Element a = fp.element(new BigInteger("76"));
        Element b = fp2.element(new BigInteger("37"), new BigInteger("99"));

        Element r = a.add(b);

        Element e = fp2.element(new BigInteger("113"), new BigInteger("99"));

        assertTrue(r.equals(e));

        r = b.add(a);

        assertTrue(r.equals(e));

        r = a.sub(b);

        e = fp2.element(new BigInteger("39"), new BigInteger("-99"));

        assertTrue(r.equals(e));

        r = b.sub(a);

        e = fp2.element(new BigInteger("-39"), new BigInteger("99"));

        assertTrue(r.equals(e));

        r = a.mult(b);

        e = fp2.element(new BigInteger("2812"), new BigInteger("7524"));

        assertTrue(r.equals(e));

        r = b.mult(a);

        assertTrue(r.equals(e));
    }

    public void testMultiply() {
        BigInteger p = new BigInteger("63");
        Fp2 f = new Fp2(p);

        Element a = f.element(new BigInteger("76"), new BigInteger("-123"));
        Element b = f.element(new BigInteger("37"), new BigInteger("99"));

        try {
            f.mult(null, b);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            f.mult(a, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        Element r = f.mult(a, b);

        assertTrue(r instanceof Fp2Element);

        Fp2Element r2 = (Fp2Element) r;
        assertTrue(r2.real().compareTo(new BigInteger("14989")) == 0);
        assertTrue(r2.imag().compareTo(new BigInteger("2973")) == 0);
    }

    public void testPow() {
        BigInteger p = new BigInteger("63");
        Fp2 f = new Fp2(p);

        Element a = f.element(new BigInteger("76"), new BigInteger("-123"));
        BigInteger exp = new BigInteger("3");

        try {
            f.powMod(null, exp);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            f.powMod(a, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            f.powMod(a, BigInteger.ONE.negate());
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        Element r = f.powMod(a, BigInteger.ZERO);
        assertTrue(r.equals(BigInteger.ONE));

        r = f.powMod(a, BigInteger.ONE);
        Element e = f.element(new BigInteger("13"), new BigInteger("3"));
        assertTrue(r.equals(e));

        r = f.powMod(a, new BigInteger("2"));
        e = a.multMod(a);
        assertTrue(r.equals(e));

        r = f.powMod(a, new BigInteger("5"));
        e = a.multMod(a).multMod(a).multMod(a).multMod(a);
        assertTrue(r.equals(e));

        // try a very big number
        r = f.powMod(a, new BigInteger("262"));
        e = a.mod();
        for (int i = 1; i < 262; i++) {
            e = e.multMod(a);
        }
        assertTrue(r.equals(e));
    }

    public void testSquare() {
        BigInteger p = new BigInteger("63");
        Fp2 f = new Fp2(p);
        Element a = f.element(new BigInteger("2"), new BigInteger("3"));
        Element b = f.square((Fp2Element) a);
        Fp2Element e = f.element(new BigInteger("58"), new BigInteger("12"));
        assertTrue(b.equals(e));

        a = f.element(new BigInteger("2"), new BigInteger("-3"));
        b = f.square((Fp2Element) a);
        e = f.element(new BigInteger("58"), new BigInteger("51"));
        assertTrue(b.equals(e));

        a = f.element(new BigInteger("-2"), new BigInteger("3"));
        b = f.square((Fp2Element) a);
        e = f.element(new BigInteger("58"), new BigInteger("51"));
        assertTrue(b.equals(e));

        a = f.element(new BigInteger("-2"), new BigInteger("-3"));
        b = f.square((Fp2Element) a);
        e = f.element(new BigInteger("58"), new BigInteger("12"));
        assertTrue(b.equals(e));
    }

    public void testSub() {
        BigInteger p = new BigInteger("63");
        Fp2 f = new Fp2(p);

        Element a = f.element(new BigInteger("76"), new BigInteger("-123"));
        Element b = f.element(new BigInteger("37"), new BigInteger("99"));

        try {
            f.sub(null, b);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            f.sub(a, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        Element r = f.sub(a, b);

        assertTrue(r instanceof Fp2Element);

        Fp2Element r2 = (Fp2Element) r;
        assertTrue(r2.real().compareTo(new BigInteger("39")) == 0);
        assertTrue(r2.imag().compareTo(new BigInteger("-222")) == 0);
    }
}