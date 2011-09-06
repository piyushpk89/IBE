package nuim.cs.crypto.blitz.curve.strategy.affine;

import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.curve.EllipticCurve;
import nuim.cs.crypto.blitz.curve.strategy.PrimeAddStrategy;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class PrimeAffineAddStrategy extends PrimeAddStrategy {
    public PrimeAffineAddStrategy(EllipticCurve curve) {
        super(curve);
    }

    @Override
    public AffinePoint addP(AffinePoint p, AffinePoint q) {
        curve.getField();
        Element lambda = p.y().subMod(q.y()).divMod(p.x().subMod(q.x()));

        // add the two points
        Element x = lambda.powMod(Constant.TWO).sub(p.x()).subMod(q.x());
        Element y = q.x().sub(x).mult(lambda).subMod(q.y());
        return new AffinePoint(x, y);
    }
}