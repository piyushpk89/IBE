package nuim.cs.crypto.bilinear;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.field.extension.Fp2;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class PairingEllipticCurveTest extends TestCase {
    public void testConstructor() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("929"));

        try {
            new PairingEllipticCurve(null, BigInteger.ONE, field);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new PairingEllipticCurve(BigInteger.ZERO, null, field);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new PairingEllipticCurve(BigInteger.ONE, BigInteger.ZERO, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        PairingEllipticCurve curve = new PairingEllipticCurve(BigInteger.ONE,
                                                              BigInteger.ZERO,
                                                              field);

        assertTrue(curve != null);
    }

    public void testGetPoint() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("929"));
        PairingEllipticCurve curve = new PairingEllipticCurve(BigInteger.ZERO,
                                                              BigInteger.ONE,
                                                              field);

        AffinePoint pt = curve.getPoint(new BigInteger("4"));
        AffinePoint e = new AffinePoint(field.element(new BigInteger("395")),
                                        field.element(new BigInteger("4")));

        assertTrue(e.equals(pt));

        BigInteger f = new BigInteger("43");
        field = new FiniteFieldPrime(f);
        curve = new PairingEllipticCurve(BigInteger.ONE, BigInteger.ZERO, field);
        BigInteger k = new BigInteger("4");
        curve.setKappa(k);

        int count = 0;
        for (BigInteger i = BigInteger.ZERO; i.compareTo(f.divide(k)) < 0; i = i.add(BigInteger.ONE)) {
            curve.getPoint(i, null);
            count++;
        }

        assertTrue(f.divide(k).intValue() == count);
    }

    public void testPointPhi() {
        BigInteger p = new BigInteger("929");
        FiniteFieldPrime fp = new FiniteFieldPrime(p);
        Fp2 fp2 = new Fp2(p);
        PairingEllipticCurve curve = new PairingEllipticCurve(BigInteger.ZERO,
                                                              BigInteger.ONE,
                                                              fp);

        AffinePoint pt = new AffinePoint(fp.element(new BigInteger("395")),
                                         fp.element(new BigInteger("4")));

        AffinePoint e = new AffinePoint(fp2.element(new BigInteger("267"),
                                                    new BigInteger("818")),
                                        fp.element(new BigInteger("4")));

        assertTrue(e.equals(curve.pointPhi(fp2.cubeRootOfUnity(), pt)));
    }

    public void testSetKappa() {
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("929"));
        PairingEllipticCurve curve = new PairingEllipticCurve(BigInteger.ZERO,
                                                              BigInteger.ONE,
                                                              field);

        assertTrue(curve.kappa.compareTo(new BigInteger("20")) == 0);

        curve.setKappa(Constant.FOUR);

        assertTrue(curve.kappa.compareTo(Constant.FOUR) == 0);
    }
}