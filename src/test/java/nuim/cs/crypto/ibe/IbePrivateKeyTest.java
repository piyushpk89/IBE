package nuim.cs.crypto.ibe;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class IbePrivateKeyTest extends TestCase {
    public void testConstructor() {
        try {
            new IbePrivateKey(null);
            assertTrue(false);
        } catch (NullPointerException npe) {
            assertTrue(true);
        }

        BigInteger field = new BigInteger("43");
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        AffinePoint Did = new AffinePoint(fp.element(new BigInteger("23")),
                                          fp.element(new BigInteger("8")));

        IbePrivateKey key = new IbePrivateKey(Did);
        assertTrue(key != null);
    }

    public void testGet() {
        BigInteger field = new BigInteger("43");
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        AffinePoint Did = new AffinePoint(fp.element(new BigInteger("23")),
                                          fp.element(new BigInteger("8")));

        IbePrivateKey key = new IbePrivateKey(Did);
        assertTrue(Did.equals(key.getPrivateKey()));
    }
}