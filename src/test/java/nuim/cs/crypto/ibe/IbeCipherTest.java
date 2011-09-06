package nuim.cs.crypto.ibe;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import junit.framework.TestCase;
import nuim.cs.crypto.bilinear.ModifiedTatePairing;
import nuim.cs.crypto.bilinear.PairingEllipticCurve;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.point.AffinePoint;
import nuim.cs.crypto.md.XORMessageDigest;

public class IbeCipherTest extends TestCase {
    public void testBigMessage() {
        // initialise the engine
        ModifiedTatePairing pair = new ModifiedTatePairing(10);
        BigInteger field = new BigInteger("43");
        new FiniteFieldPrime(field);
        pair.init(field, new BigInteger("11"), new BigInteger("4"));
        pair.getCurve().setKappa(BigInteger.ONE);

        // master key
        BigInteger s = new BigInteger("7");

        // identity
        BigInteger id = new BigInteger("14");

        // hash of identity (h1)
        AffinePoint Qid = pair.mapToPoint(id);

        // hash
        XORMessageDigest md = new XORMessageDigest(1);

        IbeSystemParameters parameters = new IbeSystemParameters(pair, md, s);

        IbeCipher cipher = new IbeCipher();
        IbePublicKey key = new IbePublicKey(id.toByteArray());
        try {
            cipher.engineInit(Cipher.ENCRYPT_MODE, key, parameters,
                              new SecureRandom());
        } catch (InvalidKeyException iae) {
            assertTrue(false);
        } catch (InvalidAlgorithmParameterException iape) {
            assertTrue(false);
        }

        // get the encrypted data
        BigInteger m = new BigInteger(999, new SecureRandom());
        byte input[] = m.toByteArray();
        byte ciphertext[] = new byte[0];
        try {
            ciphertext = cipher.engineDoFinal(input, 0, input.length);
        } catch (IllegalBlockSizeException ibse) {
            assertTrue(ibse.toString(), false);
        } catch (BadPaddingException bpe) {
            assertTrue(bpe.toString(), false);
        }

        // re-initialise the cipher for decryption
        AffinePoint Did = pair.getCurve().multiply(s, Qid);
        IbePrivateKey privateKey = new IbePrivateKey(Did);
        try {
            cipher.engineInit(Cipher.DECRYPT_MODE, privateKey, parameters,
                              new SecureRandom());
        } catch (InvalidKeyException iae) {
            assertTrue(false);
        } catch (InvalidAlgorithmParameterException iape) {
            assertTrue(false);
        }

        // get the decrypted data
        byte plaintext[] = new byte[0];
        try {
            plaintext = cipher.engineDoFinal(ciphertext, 0, ciphertext.length);
        } catch (IllegalBlockSizeException ibse) {
            assertTrue(ibse.toString(), false);
        } catch (BadPaddingException bpe) {
            assertTrue(bpe.toString(), false);
        }

        assertTrue(m.compareTo(new BigInteger(1, plaintext)) == 0);
    }

    public void testEncryptDecryptBlock() {
        // initialise the engine
        ModifiedTatePairing pair = new ModifiedTatePairing(10);
        BigInteger field = new BigInteger("43");
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        pair.init(field, new BigInteger("11"), new BigInteger("4"));
        pair.getCurve().setKappa(BigInteger.ONE);

        // master key
        BigInteger s = new BigInteger("7");

        // identity
        BigInteger id = new BigInteger("14");

        // hash of identity (h1)
        AffinePoint Qid = pair.mapToPoint(id);

        // arbitrary generator
        AffinePoint P = new AffinePoint(fp.element(new BigInteger("23")),
                                        fp.element(new BigInteger("8")));

        // public key
        AffinePoint Ppub = pair.getCurve().multiply(s, P);

        // hash
        XORMessageDigest md = new XORMessageDigest(1);

        IbeSystemParameters parameters = new IbeSystemParameters(pair, P, Ppub,
                                                                 md);

        IbeCipher cipher = new IbeCipher();
        IbePublicKey key = new IbePublicKey(id.toByteArray());
        try {
            cipher.engineInit(Cipher.ENCRYPT_MODE, key, parameters,
                              new SecureRandom());
        } catch (InvalidKeyException iae) {
            assertTrue(false);
        } catch (InvalidAlgorithmParameterException iape) {
            assertTrue(false);
        }

        // get the encrypted data
        BigInteger m = new BigInteger("32");
        byte input[] = m.toByteArray();
        byte ciphertext[] = cipher.encryptBlock(input);

        // re-initialise the cipher for decryption
        AffinePoint Did = pair.getCurve().multiply(s, Qid);
        IbePrivateKey privateKey = new IbePrivateKey(Did);
        try {
            cipher.engineInit(Cipher.DECRYPT_MODE, privateKey, parameters,
                              new SecureRandom());
        } catch (InvalidKeyException iae) {
            assertTrue(false);
        } catch (InvalidAlgorithmParameterException iape) {
            assertTrue(false);
        }

        // get the decrypted data
        byte plaintext[] = cipher.decryptBlock(ciphertext);

        assertTrue(m.compareTo(new BigInteger(1, plaintext)) == 0);
    }

    public void testEngineDoFinal() {
        // initialise the engine
        ModifiedTatePairing pair = new ModifiedTatePairing(10);
        BigInteger field = new BigInteger("43");
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        pair.init(field, new BigInteger("11"), new BigInteger("4"));
        pair.getCurve().setKappa(BigInteger.ONE);

        // master key
        BigInteger s = new BigInteger("7");

        // identity
        BigInteger id = new BigInteger("14");

        // hash of identity (h1)
        AffinePoint Qid = pair.mapToPoint(id);

        // arbitrary generator
        AffinePoint P = new AffinePoint(fp.element(new BigInteger("23")),
                                        fp.element(new BigInteger("8")));

        // public key
        AffinePoint Ppub = pair.getCurve().multiply(s, P);

        // hash
        XORMessageDigest md = new XORMessageDigest(1);

        IbeSystemParameters parameters = new IbeSystemParameters(pair, P, Ppub,
                                                                 md);

        IbeCipher cipher = new IbeCipher();
        IbePublicKey key = new IbePublicKey(id.toByteArray());
        try {
            cipher.engineInit(Cipher.ENCRYPT_MODE, key, parameters,
                              new SecureRandom());
        } catch (InvalidKeyException iae) {
            assertTrue(false);
        } catch (InvalidAlgorithmParameterException iape) {
            assertTrue(false);
        }

        // get the encrypted data
        BigInteger m = new BigInteger("32");
        byte input[] = m.toByteArray();
        byte ciphertext[] = new byte[0];
        try {
            ciphertext = cipher.engineDoFinal(input, 0, input.length);
        } catch (IllegalBlockSizeException ibse) {
            assertTrue(ibse.toString(), false);
        } catch (BadPaddingException bpe) {
            assertTrue(bpe.toString(), false);
        }

        // re-initialise the cipher for decryption
        AffinePoint Did = pair.getCurve().multiply(s, Qid);
        IbePrivateKey privateKey = new IbePrivateKey(Did);
        try {
            cipher.engineInit(Cipher.DECRYPT_MODE, privateKey, parameters,
                              new SecureRandom());
        } catch (InvalidKeyException iae) {
            assertTrue(false);
        } catch (InvalidAlgorithmParameterException iape) {
            assertTrue(false);
        }

        // get the decrypted data
        byte plaintext[] = new byte[0];
        try {
            plaintext = cipher.engineDoFinal(ciphertext, 0, ciphertext.length);
        } catch (IllegalBlockSizeException ibse) {
            assertTrue(ibse.toString(), false);
        } catch (BadPaddingException bpe) {
            assertTrue(bpe.toString(), false);
        }

        assertTrue(m.compareTo(new BigInteger(1, plaintext)) == 0);
    }

    public void testGordon() {
        // initialise the engine
        ModifiedTatePairing pair = new ModifiedTatePairing();

        // master key
        BigInteger s = new BigInteger("7");

        // identity
        BigInteger field = pair.getCurve().getField().getChar();
        BigInteger kappa = pair.getCurve().getKappa();
        MessageDigest md = new XORMessageDigest(field.bitLength());
        md.update(new String("cian@company.ie").getBytes());
        BigInteger id = new BigInteger(1, md.digest());
        id = id.mod(field.divide(kappa));

        // hash of identity (h1)
        AffinePoint Qid = pair.mapToPoint(id);

        IbeSystemParameters parameters = new IbeSystemParameters(pair, md, s);

        IbeCipher cipher = new IbeCipher();
        IbePublicKey key = new IbePublicKey(id.toByteArray());
        try {
            cipher.engineInit(Cipher.ENCRYPT_MODE, key, parameters,
                              new SecureRandom());
        } catch (InvalidKeyException iae) {
            assertTrue(false);
        } catch (InvalidAlgorithmParameterException iape) {
            assertTrue(false);
        }

        // get the encrypted data
        BigInteger m = new BigInteger(999, new SecureRandom());
        byte input[] = m.toByteArray();
        byte ciphertext[] = new byte[0];
        try {
            ciphertext = cipher.engineDoFinal(input, 0, input.length);
        } catch (IllegalBlockSizeException ibse) {
            assertTrue(ibse.toString(), false);
        } catch (BadPaddingException bpe) {
            assertTrue(bpe.toString(), false);
        }

        // re-initialise the cipher for decryption
        AffinePoint Did = pair.getCurve().multiply(s, Qid);
        IbePrivateKey privateKey = new IbePrivateKey(Did);
        try {
            cipher.engineInit(Cipher.DECRYPT_MODE, privateKey, parameters,
                              new SecureRandom());
        } catch (InvalidKeyException iae) {
            assertTrue(false);
        } catch (InvalidAlgorithmParameterException iape) {
            assertTrue(false);
        }

        // get the decrypted data
        byte plaintext[] = new byte[0];
        try {
            plaintext = cipher.engineDoFinal(ciphertext, 0, ciphertext.length);
        } catch (IllegalBlockSizeException ibse) {
            assertTrue(ibse.toString(), false);
        } catch (BadPaddingException bpe) {
            assertTrue(bpe.toString(), false);
        }

        assertTrue(m.compareTo(new BigInteger(1, plaintext)) == 0);
    }

    public void testHashBlock() {
        IbeCipher cipher = new IbeCipher();

        BigInteger m = new BigInteger("32");
        BigInteger h2 = new BigInteger("1015");

        byte V[] = cipher.hashBlock(m.toByteArray(), h2.toByteArray());
        assertTrue(new BigInteger(1, V).compareTo(new BigInteger("35")) == 0);

        BigInteger W = new BigInteger("1015");
        byte P[] = cipher.hashBlock(V, W.toByteArray());
        assertTrue(new BigInteger(1, P).compareTo(new BigInteger("32")) == 0);
    }

    public void testIbeAlgorithm() {
        IbeCipher cipher = new IbeCipher();

        BigInteger field = new BigInteger("43");
        ModifiedTatePairing pair = new ModifiedTatePairing(10);
        FiniteFieldPrime fp = new FiniteFieldPrime(field);
        pair.init(field, new BigInteger("11"), new BigInteger("4"));
        pair.getCurve().setKappa(BigInteger.ONE);
        PairingEllipticCurve curve = new PairingEllipticCurve(BigInteger.ONE,
                                                              BigInteger.ZERO,
                                                              fp);

        // master key
        BigInteger s = new BigInteger("7");

        // arbitrary generator
        AffinePoint P = new AffinePoint(fp.element(new BigInteger("23")),
                                        fp.element(new BigInteger("8")));

        // public key
        AffinePoint Ppub = curve.multiply(s, P);

        // identity
        BigInteger id = new BigInteger("14");

        // hash of identity (h1)
        AffinePoint Qid = pair.mapToPoint(id);

        // Gid = t(Qid,Ppub)
        Element Gid = pair.getPair(Qid, Ppub);

        // random r
        BigInteger r = new BigInteger("13");
        Gid = Gid.powMod(r);

        // hash of Gid^r (h2)
        BigInteger h2 = Gid.pAdic();

        // message
        BigInteger m = new BigInteger("32");

        // ciphertext
        AffinePoint U = curve.multiply(r, P);
        BigInteger V = m.xor(h2);

        // private key
        AffinePoint Did = curve.multiply(s, Qid);

        // W = t(Did,U)
        Element W = pair.getPair(Did, U);
        BigInteger plaintext = V.xor(W.pAdic());

        assertTrue(plaintext.compareTo(m) == 0);

        // try other runs through the algorithm...
        m = new BigInteger("132");
        byte v[] = cipher.hashBlock(m.toByteArray(), h2.toByteArray());
        byte p[] = cipher.hashBlock(v, W.pAdic().toByteArray());

        assertTrue(m.compareTo(new BigInteger(1, p)) == 0);

        m = new BigInteger(String.valueOf(Integer.MAX_VALUE));
        v = cipher.hashBlock(m.toByteArray(), h2.toByteArray());
        p = cipher.hashBlock(v, W.pAdic().toByteArray());

        assertTrue(m.compareTo(new BigInteger(1, p)) == 0);

        m = new BigInteger(String.valueOf(Integer.MAX_VALUE));
        m = m.add(BigInteger.ONE);
        v = cipher.hashBlock(m.toByteArray(), h2.toByteArray());
        p = cipher.hashBlock(v, W.pAdic().toByteArray());

        assertTrue(m.compareTo(new BigInteger(1, p)) == 0);

        m = new BigInteger("4757843987432974790435798345987983479083");
        v = cipher.hashBlock(m.toByteArray(), h2.toByteArray());
        p = cipher.hashBlock(v, W.pAdic().toByteArray());

        assertTrue(m.compareTo(new BigInteger(1, p)) == 0);
    }

    public void testMessageDigest() {
        // initialise the engine
        ModifiedTatePairing pair = new ModifiedTatePairing(10);
        BigInteger field = new BigInteger("43");
        new FiniteFieldPrime(field);
        pair.init(field, new BigInteger("11"), new BigInteger("4"));
        BigInteger kappa = new BigInteger("4");
        pair.getCurve().setKappa(kappa);

        // master key
        BigInteger s = new BigInteger("7");

        // identity
        MessageDigest md = new XORMessageDigest(field.bitLength());
        md.update(new String("cian@company.com").getBytes());
        BigInteger id = new BigInteger(1, md.digest());
        id = id.mod(field.divide(kappa));

        // hash of identity (h1)
        AffinePoint Qid = pair.mapToPoint(id);

        IbeSystemParameters parameters = new IbeSystemParameters(pair, md, s);

        IbeCipher cipher = new IbeCipher();
        IbePublicKey key = new IbePublicKey(id.toByteArray());
        try {
            cipher.engineInit(Cipher.ENCRYPT_MODE, key, parameters,
                              new SecureRandom());
        } catch (InvalidKeyException iae) {
            assertTrue(false);
        } catch (InvalidAlgorithmParameterException iape) {
            assertTrue(false);
        }

        // get the encrypted data
        BigInteger m = new BigInteger(999, new SecureRandom());
        byte input[] = m.toByteArray();
        byte ciphertext[] = new byte[0];
        try {
            ciphertext = cipher.engineDoFinal(input, 0, input.length);
        } catch (IllegalBlockSizeException ibse) {
            assertTrue(ibse.toString(), false);
        } catch (BadPaddingException bpe) {
            assertTrue(bpe.toString(), false);
        }

        // re-initialise the cipher for decryption
        AffinePoint Did = pair.getCurve().multiply(s, Qid);
        IbePrivateKey privateKey = new IbePrivateKey(Did);
        try {
            cipher.engineInit(Cipher.DECRYPT_MODE, privateKey, parameters,
                              new SecureRandom());
        } catch (InvalidKeyException iae) {
            assertTrue(false);
        } catch (InvalidAlgorithmParameterException iape) {
            assertTrue(false);
        }

        // get the decrypted data
        byte plaintext[] = new byte[0];
        try {
            plaintext = cipher.engineDoFinal(ciphertext, 0, ciphertext.length);
        } catch (IllegalBlockSizeException ibse) {
            assertTrue(ibse.toString(), false);
        } catch (BadPaddingException bpe) {
            assertTrue(bpe.toString(), false);
        }

        assertTrue(m.compareTo(new BigInteger(1, plaintext)) == 0);
    }

    public void testSystemParameters() {
        // initialise the engine
        ModifiedTatePairing pair = new ModifiedTatePairing(10);
        BigInteger field = new BigInteger("43");
        new FiniteFieldPrime(field);
        pair.init(field, new BigInteger("11"), new BigInteger("4"));
        pair.getCurve().setKappa(BigInteger.ONE);

        // master key
        BigInteger s = new BigInteger("7");

        // identity
        BigInteger id = new BigInteger("14");

        // hash of identity (h1)
        AffinePoint Qid = pair.mapToPoint(id);

        // hash
        XORMessageDigest md = new XORMessageDigest(1);

        IbeSystemParameters parameters = new IbeSystemParameters(pair, md, s);

        IbeCipher cipher = new IbeCipher();
        IbePublicKey key = new IbePublicKey(id.toByteArray());
        try {
            cipher.engineInit(Cipher.ENCRYPT_MODE, key, parameters,
                              new SecureRandom());
        } catch (InvalidKeyException iae) {
            assertTrue(false);
        } catch (InvalidAlgorithmParameterException iape) {
            assertTrue(false);
        }

        // get the encrypted data
        BigInteger m = new BigInteger("32");
        byte input[] = m.toByteArray();
        byte ciphertext[] = new byte[0];
        try {
            ciphertext = cipher.engineDoFinal(input, 0, input.length);
        } catch (IllegalBlockSizeException ibse) {
            assertTrue(ibse.toString(), false);
        } catch (BadPaddingException bpe) {
            assertTrue(bpe.toString(), false);
        }

        // re-initialise the cipher for decryption
        AffinePoint Did = pair.getCurve().multiply(s, Qid);
        IbePrivateKey privateKey = new IbePrivateKey(Did);
        try {
            cipher.engineInit(Cipher.DECRYPT_MODE, privateKey, parameters,
                              new SecureRandom());
        } catch (InvalidKeyException iae) {
            assertTrue(false);
        } catch (InvalidAlgorithmParameterException iape) {
            assertTrue(false);
        }

        // get the decrypted data
        byte plaintext[] = new byte[0];
        try {
            plaintext = cipher.engineDoFinal(ciphertext, 0, ciphertext.length);
        } catch (IllegalBlockSizeException ibse) {
            assertTrue(ibse.toString(), false);
        } catch (BadPaddingException bpe) {
            assertTrue(bpe.toString(), false);
        }

        assertTrue(m.compareTo(new BigInteger(1, plaintext)) == 0);
    }
}