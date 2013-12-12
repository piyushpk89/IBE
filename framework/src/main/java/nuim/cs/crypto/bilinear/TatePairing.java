package nuim.cs.crypto.bilinear;

import java.math.BigInteger;
import java.util.Random;

import nuim.cs.crypto.algebra.AltTangent;
import nuim.cs.crypto.algebra.Function;
import nuim.cs.crypto.algebra.StraightLine;
import nuim.cs.crypto.algebra.VerticalLine;
import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.field.extension.Fp2;
import nuim.cs.crypto.blitz.point.AffinePoint;
import nuim.cs.crypto.primality.Gordon;

public class TatePairing implements BilinearMap {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected PairingEllipticCurve curve;
    protected BigInteger l;
    protected BigInteger q;
    protected Random random;

    public TatePairing() {
        this(250);
    }

    public TatePairing(int bitLength) {
        if (bitLength <= 0) {
            throw new IllegalArgumentException(
                                               "bitLength must be greater than zero");
        }
        Gordon gordon = new Gordon();
        while (gordon.getP().mod(Constant.FOUR).compareTo(Constant.THREE) != 0) {

            gordon = new Gordon(bitLength);
        }

        BigInteger p = gordon.getP();
        BigInteger s = gordon.getS();
        BigInteger pPlus1DivideS = p.add(BigInteger.ONE).divide(s);

        init(p, s, pPlus1DivideS);
    }

    protected Function g1(AffinePoint P, AffinePoint R) {
        if (P == null) {
            throw new NullPointerException("P cannot be null");
        }
        if (R == null) {
            throw new NullPointerException("R cannot be null");
        }
        AffinePoint infinity = curve.infinity();
        Function function = null;
        if (P.equals(infinity)) {
            function = new VerticalLine(R, infinity);
        } else if (R.equals(infinity)) {
            function = new VerticalLine(P, infinity);
        } else if (P.x().equals(R.x())) {
            if (P.y().equals(R.y().negMod())) {
                function = new VerticalLine(P, infinity);
            } else {
                function = new AltTangent(P, curve.a4(), curve.a6(), infinity);
            }
        } else {
            function = new StraightLine(P, R, infinity);
        }
        return function;
    }

    protected Function g2(AffinePoint P) {
        if (P == null) {
            throw new NullPointerException("P cannot be null");
        }
        return new VerticalLine(P, curve.infinity());
    }

    @Override
    public PairingEllipticCurve getCurve() {
        return curve;
    }

    @Override
    public Element getPair(AffinePoint P, AffinePoint Q) {
        if (P == null) {
            throw new NullPointerException("P cannot be null");
        }
        if (Q == null) {
            throw new NullPointerException("Q cannot be null");
        }
        boolean problem = false;
        do {
            problem = false;
            try {
                AffinePoint R = morphPoint(randomPoint());
                // if Q == R then Qhat would be equal to oo (infinity)
                if (Q.equals(R)) {
                    problem = true;
                } else {
                    return getPair(P, Q, R);
                }
            } catch (ArithmeticException ae) {
                problem = true;
            }
        } while (problem);
        return null;
    }

    protected Element getPair(AffinePoint P, AffinePoint Q, AffinePoint R) {
        if (P == null) {
            throw new NullPointerException("P cannot be null");
        }
        if (Q == null) {
            throw new NullPointerException("Q cannot be null");
        }
        if (R == null) {
            throw new NullPointerException("R cannot be null");
        }

        AffinePoint R1 = curve.infinity();
        AffinePoint R2 = (AffinePoint) R.clone();

        AffinePoint Phat = curve.add(P, R1);
        AffinePoint Qhat = curve.add(Q, R2);

        // get the result of miller's algorithm
        Element result = millersAlgorithm(P, Phat, Qhat, R1, R2);
        return result;
    }

    @Override
    public BigInteger getQ() {
        return new BigInteger(q.toString());
    }

    /*
    protected AffinePoint generalRandomPoint( Fp2Element phi ) {
        if( phi == null ) {
            throw new NullPointerException( "phi cannot be null" );
        }
        AffinePoint p = randomPoint();
        AffinePoint p2 = randomPoint();

        Element x = p2.x().mult( phi );
        return( curve.add( p, new AffinePoint( x, p2.y() ) ) );
    }
     */

    public void init(BigInteger p, BigInteger q, BigInteger l) {
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        if (q == null) {
            throw new NullPointerException("q cannot be null");
        }
        if (l == null) {
            throw new NullPointerException("l cannot be null");
        }
        this.q = q;
        this.l = l;
        FiniteFieldPrime f = new FiniteFieldPrime(p);
        curve = new PairingEllipticCurve(BigInteger.ONE, BigInteger.ZERO, f);
        random = new Random();
    }

    @Override
    public AffinePoint mapToPoint(BigInteger x) {
        if (x == null) {
            throw new NullPointerException("x cannot be null");
        }
        AffinePoint P = curve.getPoint(x, null);
        // p + 1 = lq => l = (p + 1)/q
        AffinePoint lP = curve.multiply(l, P);
        if (lP.equals(curve.infinity())) {
            throw new ArithmeticException("lP = oo (infinity)");
        } else {
            return lP;
        }
    }

    @SuppressWarnings("unused")
    protected Element millersAlgorithm(AffinePoint P, AffinePoint Phat,
                                       AffinePoint Qhat, AffinePoint R1,
                                       AffinePoint R2) {
        if (P == null) {
            throw new NullPointerException("P cannot be null");
        }
        if (Phat == null) {
            throw new NullPointerException("Phat cannot be null");
        }
        if (Qhat == null) {
            throw new NullPointerException("Qhat cannot be null");
        }
        if (R1 == null) {
            throw new NullPointerException("R1 cannot be null");
        }
        if (R2 == null) {
            throw new NullPointerException("R2 cannot be null");
        }
        AffinePoint Z = curve.infinity();
        Element vn1 = new Element(BigInteger.ONE, P.x());
        Element vd1 = new Element(BigInteger.ONE, P.x());
        //if( R1.equals( curve.infinity() ) ) {
        if (false) {
            // as is
        } else {
            Function g1 = g1(P, R1);
            Function g2 = g2(Phat);

            vn1 = g2.evaluate(Qhat).multMod(g1.evaluate(R2));
            vd1 = g2.evaluate(R2).multMod(g1.evaluate(Qhat));
        }
        BigInteger k = BigInteger.ZERO;

        Element vn = new Element(BigInteger.ONE, P.x());
        Element vd = new Element(BigInteger.ONE, P.x());
        for (int i = q.bitLength() - 1; i >= 0; i--) {
            // test to see if bit i is set to one
            if (q.testBit(i)) {
                Function g1 = g1(Z, P);
                // Z = Z + P
                Z = curve.add(Z, P);
                Function g2 = g2(Z);

                // fb(Aq).fc(Aq)
                vn = vn.multMod(vn1);
                vd = vd.multMod(vd1);
                // g1(Aq)/g2(Aq)
                Element t = g1.evaluate(Qhat).mult(g2.evaluate(R2));
                Element b = g1.evaluate(R2).mult(g2.evaluate(Qhat));

                vn = vn.multMod(t);
                vd = vd.multMod(b);

                // k = k + 1
                k = k.add(BigInteger.ONE);
            }
            if (i > 0) {
                Function g1 = g1(Z, Z);
                // Z = 2Z
                Z = curve.doublePoint(Z);
                Function g2 = g2(Z);

                // fb(Aq).fc(Aq)
                vn = vn.multMod(vn);
                vd = vd.multMod(vd);
                // g1(Aq)/g2(Aq)
                Element t = g1.evaluate(Qhat).mult(g2.evaluate(R2));
                Element b = g1.evaluate(R2).mult(g2.evaluate(Qhat));

                vn = vn.multMod(t);
                vd = vd.multMod(b);

                // k = 2k
                k = k.multiply(Constant.TWO);
            }
        }
        return vn.divMod(vd);
    }

    public AffinePoint morphPoint(AffinePoint p) {
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        Fp2 fp2 = new Fp2(curve.getField().getChar());
        return new AffinePoint(fp2.element(p.x().negMod().toBigInteger(),
                                           BigInteger.ZERO),
                               fp2.element(BigInteger.ZERO,
                                           p.y().toBigInteger()));
    }

    /**
     * Generates a random point with a y-value in the range [0,p - 1]. This
     * method uses the mapToPoint method to generate a valid point.
     */
    public AffinePoint randomPoint() {
        AffinePoint P = null;
        boolean problem = false;
        do {
            problem = false;
            try {
                BigInteger y = new BigInteger(
                                              curve.getField().getChar().bitLength() - 1,
                                              random);
                y = y.mod(curve.getField().getChar().divide(curve.kappa));
                P = mapToPoint(y);
            } catch (ArithmeticException e) {
                problem = true;
            }
        } while (problem);

        return P;
    }
}