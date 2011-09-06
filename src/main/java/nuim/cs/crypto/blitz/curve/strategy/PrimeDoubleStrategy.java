package nuim.cs.crypto.blitz.curve.strategy;

import nuim.cs.crypto.blitz.curve.EllipticCurve;
import nuim.cs.crypto.blitz.point.AffinePoint;

public abstract class PrimeDoubleStrategy extends PrimeStrategy implements
        DoubleStrategy {
    protected PrimeDoubleStrategy(EllipticCurve curve) {
        super(curve);
    }

    protected abstract AffinePoint doubleP(AffinePoint p);

    @Override
    public AffinePoint doublePoint(AffinePoint p) {
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        return doubleP(p);
    }
}