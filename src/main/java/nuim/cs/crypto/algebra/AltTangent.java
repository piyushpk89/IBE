package nuim.cs.crypto.algebra;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class AltTangent extends Function {
    public AltTangent(AffinePoint p, BigInteger a4, AffinePoint infinity) {
        super(infinity);
        if (p == null) {
            throw new NullPointerException("p cannot be null");
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
            Element m = p.x().add(p.x()).add(p.x());
            m = m.mult(p.x());
            m = m.add(a4);
            m = m.divMod(p.y().mult(Constant.TWO));

            a = m.neg();
            /*
            b = p.y().add( p.y() );
            c = b.mult( p.y() );
            c = c.add( a.mult( p.x() ) );
            c = c.neg();
             */

            // the alternative bit
            b = new Element(BigInteger.ONE, p.x());
            //c = p.y().neg().add( m.mult( p.x() ) );
            //c = p.y().add( a.mult( p.x() ) ).negMod();
            c = m.mult(p.x()).subMod(p.y());
        }
        set(a, b, c);
    }

    public AltTangent(AffinePoint p, BigInteger a4, BigInteger a6,
                      AffinePoint infinity) {
        super(infinity);
        if (p == null) {
            throw new NullPointerException("p cannot be null");
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
            Element m = p.x().add(p.x()).add(p.x());
            m = m.mult(p.x());
            m = m.add(a4);
            m = m.divMod(p.y().mult(Constant.TWO));

            a = m.neg();
            b = new Element(BigInteger.ONE, p.x());
            c = p.x().powMod(Constant.THREE).neg();
            c = c.add(p.x().mult(a4));
            c = c.add(Constant.TWO.multiply(a6));
            c = c.div(p.y().multMod(Constant.TWO)).negMod();
        }
        set(a, b, c);
    }
}