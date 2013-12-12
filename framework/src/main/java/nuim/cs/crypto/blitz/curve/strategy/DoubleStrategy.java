package nuim.cs.crypto.blitz.curve.strategy;

import nuim.cs.crypto.blitz.point.AffinePoint;

public interface DoubleStrategy {
    public AffinePoint doublePoint(AffinePoint p);
}