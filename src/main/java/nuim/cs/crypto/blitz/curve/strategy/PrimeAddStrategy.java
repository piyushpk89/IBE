package nuim.cs.crypto.blitz.curve.strategy;

import nuim.cs.crypto.blitz.curve.EllipticCurve;
import nuim.cs.crypto.blitz.point.AffinePoint;

public abstract class PrimeAddStrategy extends PrimeStrategy implements
        AddStrategy {
    protected PrimeAddStrategy(EllipticCurve curve) {
        super(curve);
    }

    @Override
    public AffinePoint add(AffinePoint p, AffinePoint q) {
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        if (q == null) {
            throw new NullPointerException("q cannot be null");
        }
        return addP(p, q);
    }

    protected abstract AffinePoint addP(AffinePoint p, AffinePoint q);
}