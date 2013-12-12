package nuim.cs.crypto.ibe;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import nuim.cs.crypto.bilinear.BilinearMap;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.point.AffinePoint;
import nuim.cs.crypto.util.BitUtility;

public class IbeCipher extends CipherSpi {
    protected Key                 key;
    protected IbeSystemParameters parameters;
    protected SecureRandom        secureRandom;
    protected int                 state;

    protected byte[] decryptBlock(byte input[]) {
        ArrayList<?> arrayList = (ArrayList<?>) BitUtility.fromBytes(input);
        AffinePoint U = (AffinePoint) arrayList.get(0);
        byte V[] = (byte[]) arrayList.get(1);
        // get the system parameters
        BilinearMap pair = parameters.getMap();
        // get the private key
        IbePrivateKey privateKey = (IbePrivateKey) key;
        AffinePoint Did = privateKey.getPrivateKey();
        // W = t(Did,U)
        Element W = pair.getPair(Did, U);
        byte P[] = hashBlock(V, W.pAdic().toByteArray());

        return P;
    }

    protected byte[] encryptBlock(byte input[]) {
        // get the system parameters
        BilinearMap pair = parameters.getMap();
        // get the public key
        IbePublicKey publicKey = (IbePublicKey) key;
        // hash of identity (h1)
        byte id[] = publicKey.getIdentity();
        AffinePoint Qid = pair.mapToPoint(new BigInteger(1, id));
        // Gid = t(Qid,Ppub)
        AffinePoint Ppub = parameters.getPpub();
        Element Gid = pair.getPair(Qid, Ppub);

        // get the random r
        BigInteger r = null;
        do {
            r = new BigInteger(pair.getQ().bitLength() - 1, new SecureRandom());
        } while (r.compareTo(BigInteger.ZERO) <= 0);
        Gid = Gid.powMod(r);
        // hash of Gid^r (h2)
        BigInteger h2 = Gid.pAdic();
        AffinePoint P = parameters.getP();
        AffinePoint U = pair.getCurve().multiply(r, P);
        byte V[] = hashBlock(input, h2.toByteArray());

        ArrayList<Object> list = new ArrayList<Object>();
        list.add(U);
        list.add(V);

        return BitUtility.toBytes(list);
    }

    @Override
    protected byte[] engineDoFinal(byte[] input, int inputOffset, int inputLen)
                                                                               throws IllegalBlockSizeException,
                                                                               BadPaddingException {
        byte i[] = new byte[inputLen];
        System.arraycopy(input, inputOffset, i, 0, i.length);
        if (state == Cipher.ENCRYPT_MODE) {
            return encryptBlock(i);
        } else if (state == Cipher.DECRYPT_MODE) {
            return decryptBlock(i);
        }
        return new byte[0];
    }

    @Override
    protected int engineDoFinal(byte[] input, int inputOffset, int inputLen,
                                byte[] output, int outputOffset)
                                                                throws ShortBufferException,
                                                                IllegalBlockSizeException,
                                                                BadPaddingException {
        byte o[] = engineDoFinal(input, inputOffset, inputLen);
        System.arraycopy(o, 0, output, outputOffset, output.length);

        return output.length;
    }

    @Override
    protected int engineGetBlockSize() {
        return -1;
    }

    @Override
    protected byte[] engineGetIV() {
        return null;
    }

    @Override
    protected int engineGetOutputSize(int param) {
        return -1;
    }

    @Override
    protected AlgorithmParameters engineGetParameters() {
        return null;
    }

    @Override
    protected void engineInit(int opmode, Key key,
                              AlgorithmParameters algorithmParameters,
                              SecureRandom secureRandom)
                                                        throws InvalidKeyException,
                                                        InvalidAlgorithmParameterException {
        if (algorithmParameters != null) {
            throw new IllegalArgumentException(
                                               "algorithmParameters should be null as there are no known "
                                                       + "AlgorithmParameters for this cipher");
        }
    }

    @Override
    protected void engineInit(int opmode, Key key,
                              AlgorithmParameterSpec algorithmParameterSpec,
                              SecureRandom secureRandom)
                                                        throws InvalidKeyException,
                                                        InvalidAlgorithmParameterException {
        if (key == null) {
            throw new NullPointerException("key cannot be null");
        }
        if (opmode == Cipher.ENCRYPT_MODE) {
            if (!(key instanceof IbePublicKey)) {
                throw new IllegalArgumentException(
                                                   "key must be instance of "
                                                           + IbePublicKey.class.getName());
            }
        } else if (opmode == Cipher.DECRYPT_MODE) {
            if (!(key instanceof IbePrivateKey)) {
                throw new IllegalArgumentException(
                                                   "key must be instance of "
                                                           + IbePrivateKey.class.getName());
            }
        }
        state = opmode;
        this.key = key;

        if (algorithmParameterSpec == null) {
            throw new NullPointerException(
                                           "algorithmParameterSpec cannot be null");
        }
        if (algorithmParameterSpec instanceof IbeSystemParameters) {
            parameters = (IbeSystemParameters) algorithmParameterSpec;
        } else {
            throw new IllegalArgumentException(
                                               "algorithmParameterSpec must be an instance of "
                                                       + IbeSystemParameters.class.getName());
        }
        if (secureRandom == null) {
            this.secureRandom = new SecureRandom();
        } else {
            this.secureRandom = secureRandom;
        }
    }

    @Override
    protected void engineInit(int opmode, Key key, SecureRandom secureRandom)
                                                                             throws InvalidKeyException {
        /*
        try {
            // need the master key for the ibe system parameters....
            //engineInit( opmode, key, new IbeSystemParameters(), secureRandom );
        }
        catch( InvalidAlgorithmParameterException iape ) {
            throw new RuntimeException( iape );
        }
         */
    }

    @Override
    protected void engineSetMode(String str) throws NoSuchAlgorithmException {
    }

    @Override
    protected void engineSetPadding(String str) throws NoSuchPaddingException {
    }

    @Override
    protected byte[] engineUpdate(byte input[], int inputOffset, int inputLen) {
        try {
            return engineDoFinal(input, inputOffset, inputLen);
        } catch (IllegalBlockSizeException ibse) {
            throw new RuntimeException(ibse);
        } catch (BadPaddingException bpe) {
            throw new RuntimeException(bpe);
        }
    }

    @Override
    protected int engineUpdate(byte[] input, int inputOffset, int inputLen,
                               byte[] output, int outputOffset)
                                                               throws ShortBufferException {
        try {
            return engineDoFinal(input, inputOffset, inputLen, output,
                                 outputOffset);
        } catch (IllegalBlockSizeException ibse) {
            throw new RuntimeException(ibse);
        } catch (BadPaddingException bpe) {
            throw new RuntimeException(bpe);
        }
    }

    protected byte[] hashBlock(byte input[], byte hash[]) {
        byte output[] = new byte[input.length];
        // the block size depends on the hash
        int blockSize = hash.length;

        for (int i = 0; i < output.length; i += blockSize) {
            for (int j = 0; j < hash.length && i + j < output.length; j++) {
                output[i + j] = (byte) (input[i + j] ^ hash[j]);
            }
        }

        return output;
    }

    /* (non-Javadoc)
     * @see javax.crypto.CipherSpi#engineGetKeySize(java.security.Key)
     */
    @Override
    protected int engineGetKeySize(Key paramKey) throws InvalidKeyException {
        return 128;
    }
}