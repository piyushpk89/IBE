package nuim.cs.crypto.blitz.curve;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.curve.strategy.AddStrategy;
import nuim.cs.crypto.blitz.curve.strategy.DoubleStrategy;
import nuim.cs.crypto.blitz.curve.strategy.MultiplyStrategy;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.Field;
import nuim.cs.crypto.blitz.field.FiniteField;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class EllipticCurve extends CubicCurve {
    public static EllipticCurve isomorphism(EllipticCurve curve) {
        if (curve == null) {
            throw new NullPointerException("curve cannot be null");
        }
        return new EllipticCurve(CubicCurve.isomorphism(curve));
    }

    protected AddStrategy add;
    protected DoubleStrategy doubles;

    protected MultiplyStrategy multiply;

    public EllipticCurve(BigInteger a1, BigInteger a2, BigInteger a3,
                         BigInteger a4, BigInteger a6, Field field) {
        super(a1, a2, a3, a4, a6, field);
    }

    public EllipticCurve(CubicCurve curve) {
        super(curve);
    }

    public EllipticCurve(EllipticCurve curve) {
        super(curve);
        if (curve.add != null) {
            add = curve.add;
        }
        if (curve.doubles != null) {
            doubles = curve.doubles;
        }
        if (curve.multiply != null) {
            multiply = curve.multiply;
        }
    }

    public AffinePoint add(AffinePoint p, AffinePoint q) {
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        if (q == null) {
            throw new NullPointerException("q cannot be null");
        }
        AffinePoint infinity = infinity();
        AffinePoint infinityNegate = negate(infinity);
        if (p.equals(infinity) || p.equals(infinityNegate)) {
            return (AffinePoint) q.clone();
        }
        if (q.equals(infinity) || p.equals(infinityNegate)) {
            return (AffinePoint) p.clone();
        }
        if (p.equals(negate(q))) {
            return infinity;
        }
        if (p.x().equals(q.x()) && p.y().equals(BigInteger.ZERO)) {
            return infinity;
        }
        if (p.equals(q)) {
            // do some doubling
            return doublePoint(p);
        }
        // perform the addition
        return add.add(p, q);
    }

    @Override
    public Object clone() {
        return new EllipticCurve(this);
    }

    public AffinePoint doublePoint(AffinePoint p) {
        if (!(field instanceof FiniteField)) {
            throw new IllegalArgumentException(
                                               "curve must be over finite field");
        }
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        AffinePoint infinity = infinity();
        AffinePoint infinityNegate = negate(infinity);
        if (p.equals(infinity) || p.equals(infinityNegate)) {
            return infinity;
        } else if (p.y().equals(BigInteger.ZERO)) {
            return infinity;
        }
        // perform the doubling
        return doubles.doublePoint(p);
    }

    /**
     * TODO : Need to cater for projective points as well as affine points.
     */
    public AffinePoint infinity() {
        if (!(field instanceof FiniteField)) {
            throw new IllegalArgumentException(
                                               "curve must be over finite field");
        }
        FiniteField f = (FiniteField) field;
        Element zero = f.element(BigInteger.ZERO);
        Element one = f.element(BigInteger.ONE);

        if (a6.compareTo(BigInteger.ZERO) == 0) {
            return new AffinePoint(zero, one);
        } else {
            return new AffinePoint(zero, zero);
        }
    }

    public AffinePoint multiply(BigInteger n, AffinePoint p) {
        if (n == null) {
            throw new NullPointerException("n cannot be null");
        }
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        AffinePoint infinity = infinity();
        if (n.compareTo(BigInteger.ZERO) == 0) {
            return infinity;
        } else if (n.compareTo(BigInteger.ONE) == 0) {
            return (AffinePoint) p.clone();
        }
        // perform the multiplication
        return multiply.multiply(n, p);
    }

    public AffinePoint negate(AffinePoint p) {
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        if (!(field instanceof FiniteField)) {
            throw new IllegalArgumentException(
                                               "curve must be over finite field");
        }
        FiniteField f = (FiniteField) field;
        Element e1 = f.element(a1);
        Element e3 = f.element(a3);
        Element y = p.y().neg();
        y = y.sub(e1.mult(p.x()));
        y = y.sub(e3);
        return new AffinePoint(p.x(), y.mod());
    }

    public void setStrategy(AddStrategy add) {
        if (add == null) {
            throw new NullPointerException("add cannot be null");
        }
        this.add = add;
    }

    public void setStrategy(DoubleStrategy doubles) {
        if (doubles == null) {
            throw new NullPointerException("doubles cannot be null");
        }
        this.doubles = doubles;
    }

    public void setStrategy(MultiplyStrategy multiply) {
        if (multiply == null) {
            throw new NullPointerException("multiply cannot be null");
        }
        this.multiply = multiply;
    }

    public AffinePoint subtract(AffinePoint p, AffinePoint q) {
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        if (q == null) {
            throw new NullPointerException("q cannot be null");
        }
        return add(p, negate(q));
    }
}