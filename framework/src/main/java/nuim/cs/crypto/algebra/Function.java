package nuim.cs.crypto.algebra;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class Function {
    protected Element a;
    protected Element b;
    protected Element c;
    protected AffinePoint infinity;

    public Function(AffinePoint infinity) {
        this.infinity = (AffinePoint) infinity.clone();
        a = new Element(BigInteger.ZERO, infinity.x());
        b = new Element(BigInteger.ZERO, infinity.x());
        c = new Element(BigInteger.ZERO, infinity.x());
    }

    public Element evaluate(AffinePoint p) {
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        if (p.equals(infinity)) {
            return new Element(BigInteger.ONE, p.x());
        } else {
            Element r = a.mult(p.x());
            r = r.add(b.mult(p.y()));
            r = r.addMod(c);

            return r;
        }
    }

    protected void set(Element a, Element b, Element c) {
        if (a == null) {
            throw new NullPointerException("a cannot be null");
        }
        if (b == null) {
            throw new NullPointerException("b cannot be null");
        }
        if (c == null) {
            throw new NullPointerException("c cannot be null");
        }
        this.a = (Element) a.clone();
        this.b = (Element) b.clone();
        this.c = (Element) c.clone();
    }

    @Override
    public String toString() {
        return new String(a + "x + " + b + "y + " + c);
    }
}