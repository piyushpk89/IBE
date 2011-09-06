package nuim.cs.crypto.ibe;

import java.math.BigInteger;
import java.security.PublicKey;
import java.util.Arrays;

import junit.framework.TestCase;
import nuim.cs.crypto.md.XORMessageDigest;

public class IbePublicKeyTest extends TestCase {
    public void testConstructor() {
        BigInteger field = new BigInteger("43");
        XORMessageDigest md = new XORMessageDigest(field.bitLength() - 1);
        byte b[] = new byte[1];
        b[0] = 14;

        try {
            new IbePublicKey((byte[]) null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        try {
            new IbePublicKey(new byte[0]);
            assertTrue(false);
        } catch (IllegalArgumentException iae) {
            assertTrue(true);
        }

        PublicKey key = new IbePublicKey(md.digest(b));
        assertTrue(key != null);
    }

    public void testGet() {
        BigInteger field = new BigInteger("43");
        XORMessageDigest md = new XORMessageDigest(field.bitLength() - 1);
        byte b[] = new byte[1];
        b[0] = 14;
        IbePublicKey key = new IbePublicKey(md.digest(b));

        assertTrue(Arrays.equals(b, key.getIdentity()));
    }
}