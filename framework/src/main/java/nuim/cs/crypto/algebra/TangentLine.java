package nuim.cs.crypto.algebra;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class TangentLine extends Function {
    public TangentLine(AffinePoint p, BigInteger a4, AffinePoint infinity) {
        super(infinity);
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        if (a4 == null) {
            throw new NullPointerException("a4 cannot be null");
        }
        Element a = new Element(BigInteger.ZERO, p.x());
        Element b = new Element(BigInteger.ZERO, p.x());
        Element c = new Element(BigInteger.ZERO, p.x());

        if (p.equals(infinity)) {
            c = new Element(BigInteger.ONE, p.x());
        } else if (p.y().equals(BigInteger.ZERO)) {
            a = new Element(BigInteger.ONE, p.x());
            c = p.x().neg();
        } else {
            Element m = p.x().mult(Constant.THREE);
            m = m.mult(p.x());
            m = m.addMod(a4);

            a = m.neg();

            b = p.y().add(p.y());

            c = p.y().mult(b).neg();
            c = c.sub(p.x().mult(a));
        }
        set(a, b, c);
    }
}