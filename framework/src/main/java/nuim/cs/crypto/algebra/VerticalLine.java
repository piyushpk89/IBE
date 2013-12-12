package nuim.cs.crypto.algebra;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class VerticalLine extends Function {
    public VerticalLine(AffinePoint p, AffinePoint infinity) {
        super(infinity);
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        Element a = new Element(BigInteger.ZERO, p.x());
        Element b = new Element(BigInteger.ZERO, p.x());
        Element c = new Element(BigInteger.ZERO, p.x());
        if (p.equals(infinity)) {
            c = new Element(BigInteger.ONE, p.x());
        } else {
            a = new Element(BigInteger.ONE, p.x());
            c = p.x().neg();
        }

        set(a, b, c);
    }
}