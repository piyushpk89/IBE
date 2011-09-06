package nuim.cs.crypto.algebra;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.point.AffinePoint;

/**
 * Represents a line of the form <code>ax + by + c = 0</code>.
 */
public class StraightLine extends Function {
    public StraightLine(AffinePoint p, AffinePoint q, AffinePoint infinity) {
        super(infinity);
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        if (q == null) {
            throw new NullPointerException("q cannot be null");
        }
        Element a = new Element(BigInteger.ZERO, p.x());
        Element b = new Element(BigInteger.ZERO, p.x());
        Element c = new Element(BigInteger.ZERO, p.x());
        if (p.equals(infinity) && q.equals(infinity)) {
            c = new Element(BigInteger.ONE, p.x());
        } else {
            Element x = q.x().sub(p.x());
            Element y = q.y().sub(p.y());

            a = y.neg();
            b = (Element) x.clone();
            c = a.mult(p.x()).add(b.mult(p.y())).neg();
        }

        set(a, b, c);
    }
}