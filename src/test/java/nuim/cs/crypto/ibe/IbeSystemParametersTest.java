package nuim.cs.crypto.ibe;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.bilinear.TatePairing;
import nuim.cs.crypto.md.XORMessageDigest;

public class IbeSystemParametersTest extends TestCase {
    public void testConstructor() {
        TatePairing map = new TatePairing();
        map.getCurve().setKappa(BigInteger.ONE);
        XORMessageDigest md = new XORMessageDigest(1);
        BigInteger s = new BigInteger("7");
        IbeSystemParameters params = new IbeSystemParameters(map, md, s);

        assertTrue(params != null);

        try {
            new IbeSystemParameters(null, md, s);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new IbeSystemParameters(map, null, s);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new IbeSystemParameters(map, md, null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new IbeSystemParameters(map, md, BigInteger.ZERO);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            new IbeSystemParameters(map, md, BigInteger.ONE.negate());
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }
    }

    public void testGet() {
        BigInteger s = new BigInteger("7");
        TatePairing map = new TatePairing();
        map.getCurve().setKappa(BigInteger.ONE);
        XORMessageDigest md = new XORMessageDigest(1);
        IbeSystemParameters params = new IbeSystemParameters(map, md, s);

        assertTrue(params != null);

        assertTrue(params.getMap() instanceof TatePairing);

        assertTrue(params.getP() != null);

        assertTrue(params.getPpub().equals(
                                           params.getMap().getCurve().multiply(
                                                                               s,
                                                                               params.getP())));
    }
}