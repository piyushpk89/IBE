package nuim.cs.crypto.ibe;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Arrays;

import junit.framework.TestCase;
import nuim.cs.crypto.bilinear.BilinearMap;
import nuim.cs.crypto.bilinear.ModifiedTatePairing;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.point.AffinePoint;
import nuim.cs.crypto.md.XORMessageDigest;

public class IbeKeyPairGeneratorTest extends TestCase {
    public void testGenerateKeyPair() {
        BigInteger field = new BigInteger("43");
        ModifiedTatePairing map = new ModifiedTatePairing(10);
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        map.init(field, new BigInteger("11"), new BigInteger("4"));
        map.getCurve().setKappa(BigInteger.ONE);

        // master key
        BigInteger s = new BigInteger("7");

        // identity
        byte b[] = new byte[1];
        b[0] = 14;
        String id = new String(b);

        XORMessageDigest md = new XORMessageDigest(field.bitLength() - 1);
        IbeKeyParameters params = new IbeKeyParameters(md, id, s, map);

        IbeKeyPairGenerator generator = new IbeKeyPairGenerator();
        generator.initialize(params);
        KeyPair keyPair = generator.generateKeyPair();
        IbePublicKey publicKey = (IbePublicKey) keyPair.getPublic();

        assertTrue(Arrays.equals(b, publicKey.getIdentity()));
        IbePrivateKey privateKey = (IbePrivateKey) keyPair.getPrivate();

        AffinePoint Did = new AffinePoint(fp.element(new BigInteger("23")),
                                          fp.element(new BigInteger("8")));
        assertTrue(Did.equals(privateKey.getPrivateKey()));
    }

    public void testInitialize() {
        IbeKeyPairGenerator generator = new IbeKeyPairGenerator();

        try {
            generator.initialize(100, new SecureRandom());
            assertTrue(false);
        } catch (RuntimeException re) {
            assertTrue(true);
        }

        BilinearMap map = new ModifiedTatePairing();
        XORMessageDigest md = new XORMessageDigest(16);
        BigInteger masterKey = new BigInteger(map.getQ().bitLength() - 1,
                                              new SecureRandom());
        String id = new String("cian@company.ie");
        IbeKeyParameters params = new IbeKeyParameters(md, id, masterKey, map);
        IbeSystemParameters wrongParams = new IbeSystemParameters(map, md,
                                                                  masterKey);

        try {
            generator.initialize(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            generator.initialize(wrongParams);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        try {
            generator.initialize(params, null);
        } catch (Exception exc) {
            assertTrue(exc.toString(), false);
        }

        generator.initialize(params);
    }
}