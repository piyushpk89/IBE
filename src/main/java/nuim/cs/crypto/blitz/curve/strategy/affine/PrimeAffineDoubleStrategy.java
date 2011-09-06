package nuim.cs.crypto.blitz.curve.strategy.affine;

import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.curve.EllipticCurve;
import nuim.cs.crypto.blitz.curve.strategy.PrimeDoubleStrategy;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class PrimeAffineDoubleStrategy extends PrimeDoubleStrategy {
    public PrimeAffineDoubleStrategy(EllipticCurve curve) {
        super(curve);
    }

    @Override
    protected AffinePoint doubleP(AffinePoint p) {
        Element lambda = p.x().powMod(Constant.TWO).mult(Constant.THREE).add(curve.a4());
        lambda = lambda.divMod(p.y().mult(Constant.TWO));

        // double the two points
        Element x = lambda.powMod(Constant.TWO).sub(p.x()).subMod(p.x());
        Element y = p.x().sub(x).mult(lambda).subMod(p.y());

        return new AffinePoint(x, y);
    }
}