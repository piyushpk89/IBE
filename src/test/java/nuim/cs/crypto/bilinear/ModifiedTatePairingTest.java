package nuim.cs.crypto.bilinear;

import java.math.BigInteger;
import java.util.Random;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.field.extension.Fp2;
import nuim.cs.crypto.blitz.field.extension.Fp2Element;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class ModifiedTatePairingTest extends TestCase {
    public void testBilinearity() {
        BigInteger field = new BigInteger("43");
        ModifiedTatePairing pair = new ModifiedTatePairing();
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        new Fp2(field);
        pair.init(field, new BigInteger("11"), new BigInteger("4"));
        PairingEllipticCurve curve = new PairingEllipticCurve(BigInteger.ONE,
                                                              BigInteger.ZERO,
                                                              fp);
        curve.setKappa(BigInteger.ONE);
        pair.curve = curve;

        AffinePoint p1 = new AffinePoint(fp.element(new BigInteger("23")),
                                         fp.element(new BigInteger("8")));
        AffinePoint p2 = new AffinePoint(fp.element(new BigInteger("31")),
                                         fp.element(new BigInteger("18")));
        AffinePoint q = new AffinePoint(fp.element(new BigInteger("14")),
                                        fp.element(new BigInteger("36")));

        Element e = pair.getPair(pair.curve.add(p1, p2), q);

        Element e1 = pair.getPair(p1, q);
        Element e2 = pair.getPair(p2, q);

        assertTrue(e.equals(e1.multMod(e2)));

        AffinePoint p = new AffinePoint(fp.element(new BigInteger("31")),
                                        fp.element(new BigInteger("25")));
        AffinePoint q1 = new AffinePoint(fp.element(new BigInteger("31")),
                                         fp.element(new BigInteger("18")));
        AffinePoint q2 = new AffinePoint(fp.element(new BigInteger("13")),
                                         fp.element(new BigInteger("19")));

        e = pair.getPair(p, pair.curve.add(q1, q2));

        e1 = pair.getPair(p, q1);
        e2 = pair.getPair(p, q2);

        assertTrue(e.equals(e1.multMod(e2)));
    }

    public void testBilinearityMore() {
        BigInteger field = new BigInteger("43");
        ModifiedTatePairing pair = new ModifiedTatePairing();
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        new Fp2(field);
        pair.init(field, new BigInteger("11"), new BigInteger("4"));
        PairingEllipticCurve curve = new PairingEllipticCurve(BigInteger.ONE,
                                                              BigInteger.ZERO,
                                                              fp);
        curve.setKappa(BigInteger.ONE);
        pair.curve = curve;

        // run this very important test repeatedly
        for (int i = 0; i < 100; i++) {
            BigInteger a = new BigInteger(10, 10, new Random());
            // get a random point p
            AffinePoint p = pair.randomPoint();
            AffinePoint q = pair.randomPoint();
            AffinePoint aP = pair.curve.multiply(a, p);
            AffinePoint aQ = pair.curve.multiply(a, q);

            Element e1 = pair.getPair(aP, q);
            Element e2 = pair.getPair(p, aQ);
            Element e3 = pair.getPair(p, q);
            e3 = e3.powMod(a);

            String message = new String();
            message += "a = " + a + "\n";
            message += "P = " + p + "\n";
            message += "Q = " + q + "\n";
            message += "aP = " + aP + "\n";
            message += "aQ = " + aQ + "\n";
            message += "t(aP,Q) = " + e1 + "\n";
            message += "t(P,aQ) = " + e2 + "\n";
            message += "t(P,Q)^a = " + e3 + "\n";

            assertTrue(message, e1.equals(e2) && e1.equals(e3));
        }
    }

    public void testBilinearityYetMore() {
        BigInteger field = new BigInteger("43");
        ModifiedTatePairing pair = new ModifiedTatePairing();
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        new Fp2(field);
        pair.init(field, new BigInteger("11"), new BigInteger("4"));
        PairingEllipticCurve curve = new PairingEllipticCurve(BigInteger.ONE,
                                                              BigInteger.ZERO,
                                                              fp);
        curve.setKappa(BigInteger.ONE);
        pair.curve = curve;

        // run this very important test repeatedly
        for (int i = 0; i < 100; i++) {
            BigInteger x = new BigInteger(5, 10, new Random());
            BigInteger y = new BigInteger(6, 10, new Random());
            AffinePoint p = pair.randomPoint();
            AffinePoint q = pair.randomPoint();

            AffinePoint xP = pair.curve.multiply(x, p);
            AffinePoint yQ = pair.curve.multiply(y, q);
            Element e1 = pair.getPair(xP, yQ);
            AffinePoint xQ = pair.curve.multiply(x, q);
            AffinePoint yP = pair.curve.multiply(y, p);
            Element e2 = pair.getPair(yP, xQ);

            String message = new String();
            message += "x = " + x + "\n";
            message += "y = " + y + "\n";
            message += "P = " + p + "\n";
            message += "Q = " + q + "\n";
            message += "xP = " + xP + "\n";
            message += "xQ = " + xQ + "\n";
            message += "yP = " + yP + "\n";
            message += "yQ = " + yQ + "\n";
            message += "t(xP,yQ) = " + e1 + "\n";
            message += "t(yP,xQ) = " + e2 + "\n";

            assertTrue(message, e1.equals(e2));

            AffinePoint xPyP = pair.curve.add(xP, yP);
            Element e3 = pair.getPair(xPyP, q);
            Element e4 = pair.getPair(xP, q);
            Element e5 = pair.getPair(yP, q);
            Element e6 = e4.multMod(e5);

            message += "xP + yP = " + xPyP + "\n";
            message += "t(xP + yP,Q) = " + e3 + "\n";
            message += "t(xP,Q) = " + e4 + "\n";
            message += "t(yP,Q) = " + e5 + "\n";
            message += "t(xP,Q)t(yP,Q) = " + e6 + "\n";

            assertTrue(message, e3.equals(e6));
        }
    }

    public void testGetPair() {
        BigInteger field = new BigInteger("43");
        ModifiedTatePairing pair = new ModifiedTatePairing();
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        Fp2 fp2 = new Fp2(field);
        pair.init(field, new BigInteger("11"), new BigInteger("4"));
        PairingEllipticCurve curve = new PairingEllipticCurve(BigInteger.ONE,
                                                              BigInteger.ZERO,
                                                              fp);
        curve.setKappa(BigInteger.ONE);
        pair.curve = curve;

        AffinePoint p = new AffinePoint(fp.element(new BigInteger("23")),
                                        fp.element(new BigInteger("8")));
        AffinePoint q = (AffinePoint) p.clone();

        Fp2Element expected = fp2.element(new BigInteger("11"),
                                          new BigInteger("3"));

        Element e = pair.getPair(p, q);
        assertTrue(e.equals(expected));
    }

    public void testNonDegeneracy() {
        BigInteger field = new BigInteger("43");
        ModifiedTatePairing pair = new ModifiedTatePairing();
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        new Fp2(field);
        pair.init(field, new BigInteger("11"), new BigInteger("4"));
        PairingEllipticCurve curve = new PairingEllipticCurve(BigInteger.ONE,
                                                              BigInteger.ZERO,
                                                              fp);
        curve.setKappa(BigInteger.ONE);
        pair.curve = curve;

        AffinePoint q = new AffinePoint(fp.element(new BigInteger("14")),
                                        fp.element(new BigInteger("36")));

        Element e = pair.getPair(pair.curve.infinity(), q);
        assertTrue(e.equals(BigInteger.ONE));
    }

    public void testStoegbauerAppendixB() {
        BigInteger field = new BigInteger("43");
        ModifiedTatePairing pair = new ModifiedTatePairing();
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        new Fp2(field);
        pair.init(field, new BigInteger("11"), new BigInteger("4"));
        PairingEllipticCurve curve = new PairingEllipticCurve(BigInteger.ONE,
                                                              BigInteger.ZERO,
                                                              fp);
        curve.setKappa(BigInteger.ONE);
        pair.curve = curve;

        AffinePoint p = new AffinePoint(fp.element(new BigInteger("23")),
                                        fp.element(new BigInteger("8")));
        AffinePoint q = (AffinePoint) p.clone();

        // compute t([2]P,Q)
        AffinePoint twoP = pair.curve.doublePoint(p);
        Element first = pair.getPair(twoP, q);

        // compute t(P,Q)^2
        Element second = pair.getPair(p, q);
        second = second.powMod(Constant.TWO);

        // should both be equal to 26 + 23i
        assertTrue(first.equals(second));
    }
}