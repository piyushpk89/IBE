package nuim.cs.crypto.ibe;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;

import nuim.cs.crypto.bilinear.BilinearMap;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class IbeKeyParameters implements AlgorithmParameterSpec {
    protected AffinePoint Did;
    protected byte id[];

    public IbeKeyParameters(MessageDigest hash, String identity) {
        if (hash == null) {
            throw new NullPointerException("hash cannot be null");
        }
        if (identity == null) {
            throw new NullPointerException("identity cannot be null");
        }
        if (identity.length() <= 0) {
            throw new IllegalArgumentException(
                                               "identity must be a string with length greater than zero");
        }
        hash.reset();
        id = hash.digest(identity.getBytes());
    }

    public IbeKeyParameters(MessageDigest hash, String identity, AffinePoint Did) {
        this(hash, identity);
        if (Did == null) {
            throw new NullPointerException("Did cannot be null");
        }
        this.Did = Did;
    }

    public IbeKeyParameters(MessageDigest hash, String identity,
                            BigInteger masterKey, BilinearMap map) {
        this(hash, identity);
        if (masterKey == null) {
            throw new NullPointerException("masterKey cannot be null");
        }
        if (map == null) {
            throw new NullPointerException("map cannot be null");
        }
        AffinePoint Qid = map.mapToPoint(new BigInteger(1, id));
        Did = map.getCurve().multiply(masterKey, Qid);
    }

    public AffinePoint getPrivateKey() {
        if (Did == null) {
            throw new NullPointerException("private key not initialised");
        }
        return Did;
    }

    public byte[] getPublicKey() {
        return id;
    }
}