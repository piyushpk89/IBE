package nuim.cs.crypto.blitz.curve.strategy;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.curve.EllipticCurve;
import nuim.cs.crypto.blitz.point.AffinePoint;

public abstract class MultiplyStrategy extends Strategy {
    protected MultiplyStrategy(EllipticCurve curve) {
        super(curve);
    }

    public AffinePoint multiply(BigInteger n, AffinePoint p) {
        if (n == null) {
            throw new NullPointerException("n cannot be null");
        }
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        return multiplyP(n, p);
    }

    protected abstract AffinePoint multiplyP(BigInteger n, AffinePoint p);
}