package nuim.cs.crypto.bilinear;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.field.extension.Fp2;
import nuim.cs.crypto.blitz.field.extension.Fp2Element;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class TatePairingTest extends TestCase {
    public void testConstructor() {
        try {
            new TatePairing(0);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            new TatePairing(-3);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        TatePairing pair = new TatePairing();
        BigInteger p = pair.getCurve().getField().getChar();

        assertTrue(p.mod(Constant.FOUR).compareTo(Constant.THREE) == 0);

        BigInteger q = pair.q;
        BigInteger l = pair.l;

        assertTrue(p.add(BigInteger.ONE).compareTo(q.multiply(l)) == 0);
    }

    public void testDifferentGroup() {
        BigInteger field = new BigInteger("43");
        TatePairing pair = new TatePairing();
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        Fp2 fp2 = new Fp2(field);
        BigInteger qParam = new BigInteger("11");
        BigInteger lParam = new BigInteger("4");
        pair.init(field, qParam, lParam);

        pair.curve = new PairingEllipticCurve(BigInteger.ONE, BigInteger.ZERO,
                                              fp);

        AffinePoint p = new AffinePoint(fp.element(new BigInteger("23")),
                                        fp.element(new BigInteger("8")));
        AffinePoint q = new AffinePoint(fp2.element(new BigInteger("20"),
                                                    BigInteger.ZERO),
                                        fp2.element(BigInteger.ZERO,
                                                    new BigInteger("8")));
        AffinePoint r = new AffinePoint(fp2.element(new BigInteger("9"),
                                                    BigInteger.ZERO),
                                        fp2.element(new BigInteger("9"),
                                                    BigInteger.ZERO));

        AffinePoint R1 = pair.curve.infinity();
        AffinePoint Qhat = pair.curve.add(q, r);

        // calculate the exponent
        BigInteger exp = field.pow(2).add(BigInteger.ONE);
        exp = exp.divide(qParam);

        Element e = pair.millersAlgorithm(p, p, Qhat, R1, r);
        e = e.powMod(exp);
        Fp2Element expected = fp2.element(new BigInteger("11"),
                                          new BigInteger("3"));
        assertTrue(e.equals(expected));

        r = new AffinePoint(
                            fp2.element(new BigInteger("-14"), BigInteger.ZERO),
                            fp2.element(BigInteger.ZERO, new BigInteger("36")));

        R1 = pair.curve.infinity();
        Qhat = pair.curve.add(q, r);

        e = pair.millersAlgorithm(p, p, Qhat, R1, r);
        e = e.powMod(exp);
        assertTrue(e.equals(expected));

        R1 = pair.curve.infinity();
        Qhat = pair.curve.add(p, r);

        e = pair.millersAlgorithm(p, p, Qhat, R1, r);
        e = e.powMod(exp);
        assertTrue(e.equals(BigInteger.ONE));
    }

    public void testGetPair() {
        TatePairing pair = new TatePairing();
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("11"));
        pair.init(new BigInteger("11"), new BigInteger("5"),
                  new BigInteger("5"));
        pair.curve = new PairingEllipticCurve(BigInteger.ONE.negate(),
                                              BigInteger.ONE, field);

        AffinePoint p = new AffinePoint(field.element(new BigInteger("3")),
                                        field.element(new BigInteger("6")));
        AffinePoint q = (AffinePoint) p.clone();

        AffinePoint r = new AffinePoint(field.element(BigInteger.ZERO),
                                        field.element(BigInteger.ONE));

        Element v = pair.getPair(p, q, r);
        assertTrue(v.equals(new BigInteger("5")));
    }

    public void testIsOrder() {
        BigInteger f = new BigInteger("103");
        FiniteFieldPrime field = new FiniteFieldPrime(f);
        BigInteger xCoeff = new BigInteger("7");
        BigInteger constCoeff = new BigInteger("12");
        PairingEllipticCurve curve = new PairingEllipticCurve(xCoeff,
                                                              constCoeff, field);
        AffinePoint pt = new AffinePoint(field.element(new BigInteger("19")),
                                         field.element(new BigInteger("0")));
        BigInteger q = new BigInteger("2");
        assertTrue(curve.isOrderMultiple(q, pt));

        while (q.compareTo(f.add(BigInteger.ONE)) < 0) {
            q = q.add(new BigInteger("2"));
            assertTrue(curve.isOrderMultiple(q, pt));
        }
    }

    public void testIsPoint() {
        BigInteger f = new BigInteger("929");
        FiniteFieldPrime field = new FiniteFieldPrime(f);
        BigInteger xCoeff = BigInteger.ZERO;
        BigInteger constCoeff = BigInteger.ONE;
        PairingEllipticCurve curve = new PairingEllipticCurve(xCoeff,
                                                              constCoeff, field);

        AffinePoint p = new AffinePoint(field.element(new BigInteger("395")),
                                        field.element(new BigInteger("4")));
        assertTrue(curve.isPoint(p));
    }

    public void testMiller() {
        TatePairing pair = new TatePairing();
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("11"));
        pair.init(new BigInteger("11"), new BigInteger("5"),
                  new BigInteger("5"));
        pair.curve = new PairingEllipticCurve(BigInteger.ONE.negate(),
                                              BigInteger.ONE, field);

        AffinePoint p = new AffinePoint(field.element(new BigInteger("3")),
                                        field.element(new BigInteger("6")));
        AffinePoint q = (AffinePoint) p.clone();

        AffinePoint r1 = pair.curve.infinity();
        AffinePoint r2 = new AffinePoint(field.element(BigInteger.ZERO),
                                         field.element(BigInteger.ONE));

        AffinePoint Phat = pair.curve.add(p, r1);
        AffinePoint Qhat = pair.curve.add(q, r2);

        Element v = pair.millersAlgorithm(p, Phat, Qhat, r1, r2);
        assertTrue(v.equals(new BigInteger("5")));
    }

    public void testMultiply() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("17"));
        PairingEllipticCurve curve = new PairingEllipticCurve(BigInteger.ONE,
                                                              BigInteger.ONE,
                                                              field);

        // the number of times to multiply the point
        BigInteger n = new BigInteger("5");
        // the point to multiply
        AffinePoint p = new AffinePoint(field.element(BigInteger.ONE),
                                        field.element(new BigInteger("3")));
        // the expected result
        AffinePoint expected = new AffinePoint(
                                               field.element(BigInteger.ONE),
                                               field.element(new BigInteger(
                                                                            "14")));

        AffinePoint r = curve.multiply(n, p);

        assertTrue(expected.equals(r));

        // check to see what happens if bit length of 3n is greater than n
        BigInteger bigN = new BigInteger(String.valueOf(Integer.MAX_VALUE));
        r = curve.multiply(bigN, p);
        expected = new AffinePoint(field.element(BigInteger.ONE),
                                   field.element(new BigInteger("3")));

        assertTrue(r.equals(expected));
    }

    public void testMultiplyOrder() {
        BigInteger f = new BigInteger("929");
        FiniteFieldPrime field = new FiniteFieldPrime(f);
        BigInteger xCoeff = BigInteger.ZERO;
        BigInteger constCoeff = BigInteger.ONE;
        PairingEllipticCurve curve = new PairingEllipticCurve(xCoeff,
                                                              constCoeff, field);

        BigInteger q = new BigInteger("5");
        BigInteger l = new BigInteger("186");
        AffinePoint p = new AffinePoint(field.element(new BigInteger("395")),
                                        field.element(new BigInteger("4")));

        // lP != oo
        assertTrue(!curve.multiply(l, p).equals(curve.infinity()));

        // qP != oo
        assertTrue(!curve.multiply(q, p).equals(curve.infinity()));

        // lqP == oo
        assertTrue(curve.multiply(l.multiply(q), p).equals(curve.infinity()));

        // (p+1)P == oo
        assertTrue(curve.multiply(f.add(BigInteger.ONE), p).equals(
                                                                   curve.infinity()));
    }

    public void testPointOrder() {
        BigInteger f = new BigInteger("103");
        FiniteFieldPrime field = new FiniteFieldPrime(f);

        BigInteger a = new BigInteger("7");
        BigInteger b = new BigInteger("12");
        PairingEllipticCurve curve = new PairingEllipticCurve(a, b, field);

        AffinePoint p = new AffinePoint(field.element(new BigInteger("19")),
                                        field.element(BigInteger.ZERO));
        BigInteger n = new BigInteger("44");

        assertTrue(curve.multiply(n, p).equals(curve.infinity()));
    }
}