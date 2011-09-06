package nuim.cs.crypto.blitz.curve.strategy;

import nuim.cs.crypto.blitz.point.AffinePoint;

public interface AddStrategy {
    /**
     * Adds two points on an elliptic curve. The following assumptions are made
     * about the inputs;
     * <ol>
     * <li>p != oo</li>
     * <li>q != oo</li>
     * <li>p != q</li>
     * <li>p != -q</li>
     * <li>p<sub>x</sub> != q<sub>x</sub></li>
     * </ol>
     * Note: oo is used to denote infinity.
     */
    public AffinePoint add(AffinePoint p, AffinePoint q);
}