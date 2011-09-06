package nuim.cs.crypto.ibe;

import java.math.BigInteger;
import java.util.Arrays;

import junit.framework.TestCase;
import nuim.cs.crypto.bilinear.ModifiedTatePairing;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.point.AffinePoint;
import nuim.cs.crypto.md.XORMessageDigest;

public class IbeKeyParametersTest extends TestCase {
    public void testConstructor() {
        XORMessageDigest md = new XORMessageDigest(50);
        String id = new String("cian@company.ie");

        try {
            new IbeKeyParameters(null, id);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new IbeKeyParameters(md, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new IbeKeyParameters(md, new String());
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        IbeKeyParameters params = new IbeKeyParameters(md, id);
        assertTrue(params != null);

        BigInteger field = new BigInteger("43");
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        AffinePoint Did = new AffinePoint(fp.element(new BigInteger("23")),
                                          fp.element(new BigInteger("8")));

        try {
            new IbeKeyParameters(md, id, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        params = new IbeKeyParameters(md, id, Did);
        assertTrue(params != null);

        BigInteger masterKey = new BigInteger("7");
        ModifiedTatePairing map = new ModifiedTatePairing(10);
        map.init(field, new BigInteger("11"), new BigInteger("4"));
        map.getCurve().setKappa(BigInteger.ONE);

        try {
            new IbeKeyParameters(md, id, null, map);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new IbeKeyParameters(md, id, masterKey, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }
    }

    public void testGet() {
        BigInteger field = new BigInteger("43");
        XORMessageDigest md = new XORMessageDigest(field.bitLength() - 1);
        byte b[] = new byte[1];
        b[0] = 14;
        String id = new String(b);
        IbeKeyParameters params = new IbeKeyParameters(md, id);

        assertTrue(Arrays.equals(b, params.getPublicKey()));

        try {
            params.getPrivateKey();
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        BigInteger masterKey = new BigInteger("7");
        ModifiedTatePairing map = new ModifiedTatePairing(10);
        map.init(field, new BigInteger("11"), new BigInteger("4"));
        map.getCurve().setKappa(BigInteger.ONE);
        params = new IbeKeyParameters(md, id, masterKey, map);

        assertTrue(id.equals(new String(params.getPublicKey())));

        AffinePoint Did = new AffinePoint(fp.element(new BigInteger("23")),
                                          fp.element(new BigInteger("8")));
        assertTrue(Did.equals(params.getPrivateKey()));
    }
}