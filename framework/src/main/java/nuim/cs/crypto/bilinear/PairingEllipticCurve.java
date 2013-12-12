package nuim.cs.crypto.bilinear;

import java.math.BigInteger;
import java.security.SecureRandom;

import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.curve.EllipticCurve;
import nuim.cs.crypto.blitz.curve.strategy.affine.PrimeAffineAddStrategy;
import nuim.cs.crypto.blitz.curve.strategy.affine.PrimeAffineDoubleStrategy;
import nuim.cs.crypto.blitz.curve.strategy.affine.PrimeAffineMultiplyStrategy;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.field.extension.Fp2;
import nuim.cs.crypto.blitz.field.extension.Fp2Element;
import nuim.cs.crypto.blitz.point.AffinePoint;
import nuim.cs.crypto.tender.elgamal.ellipticcurve.Legendre;
import nuim.cs.crypto.tender.elgamal.ellipticcurve.SquareRootModuloP;

public class PairingEllipticCurve extends EllipticCurve {
    protected BigInteger kappa;

    public PairingEllipticCurve(BigInteger a4, BigInteger a6,
                                FiniteFieldPrime field) {
        super(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO, a4, a6, field);

        setStrategy(new PrimeAffineAddStrategy(this));
        setStrategy(new PrimeAffineDoubleStrategy(this));
        setStrategy(new PrimeAffineMultiplyStrategy(this));

        kappa = new BigInteger("20");
    }

    public BigInteger getKappa() {
        return kappa;
    }

    /**
     * Finds a valid point on the curve given the y-value of the point. The
     * point is calculated by using the formula
     * 
     * <pre>
     *         ______             (2p - 1)/3
     * x = 3/ y^2 - 1 = (y^2 - 1)^
     * </pre>
     * 
     * Perhaps this could be rewritten as
     * 
     * <pre>
     *         ________
     * x = 3/ y^2 - a_6
     * </pre>
     * 
     * where a_6 is the constant coefficient of the elliptic curve function.
     */
    protected AffinePoint getPoint(BigInteger yValue) {
        if (yValue == null) {
            throw new NullPointerException("yValue cannot be null");
        }
        if (a4().compareTo(BigInteger.ZERO) != 0) {
            throw new IllegalArgumentException(
                                               "method will only work for curves with zero coefficient for "
                                                       + "x term");
        }
        ((FiniteFieldPrime) field).element(BigInteger.ONE);
        // ensure the y value is in range (0,p-1)
        Element y = ((FiniteFieldPrime) field).element(yValue);
        // calculate the x value
        Element x = y.multMod(y);
        x = x.subMod(a6());

        BigInteger exp = Constant.TWO.multiply(field.getChar());
        exp = exp.subtract(BigInteger.ONE);
        exp = exp.divide(Constant.THREE);

        x = x.powMod(exp);

        return new AffinePoint(x, y);
    }

    protected AffinePoint getPoint(BigInteger x, BigInteger y) {
        if (x == null && y == null) {
            throw new NullPointerException("x and y cannot both be null");
        } else if (x == null && y != null) {
            return getPoint(y);
        } else if (x != null && y == null) {
            if (kappa.multiply(x).compareTo(field.getChar()) > 0) {
                throw new IllegalArgumentException("cannot find point for x = "
                                                   + x
                                                   + " as (kappa)(x) > field");
            }

            //for( BigInteger i = BigInteger.ONE; i.compareTo( kappa ) <= 0;
            for (BigInteger i = BigInteger.ZERO; i.compareTo(kappa) <= 0; i = i.add(BigInteger.ONE)) {

                // alpha = x^3 + ax + b
                Element px = ((FiniteFieldPrime) field).element(kappa.multiply(x).add(i));

                //Element px = ((FiniteFieldPrime) field).element( x );
                Element alpha = px.powMod(Constant.THREE);
                alpha = alpha.add(px.mult(a4()));
                alpha = alpha.addMod(a6());
                if (alpha.equals(BigInteger.ZERO)) {
                    return new AffinePoint(px, alpha);
                } else {
                    Legendre legendre = new Legendre((FiniteFieldPrime) field);
                    SquareRootModuloP squareRootModuloP = new SquareRootModuloP(
                                                                                (FiniteFieldPrime) field);
                    // uses the Legendre test to determine if y^2 has a square root
                    // mod prime
                    if (legendre.calculate(alpha.toBigInteger()) == Legendre.RESIDUE) {
                        BigInteger iy = squareRootModuloP.calculate(alpha.toBigInteger());
                        Element py = ((FiniteFieldPrime) field).element(iy);
                        return new AffinePoint(px, py);
                    }
                }
            }
            throw new ArithmeticException(
                                          "no valid point exists for x value : "
                                                  + x);
        } else {
            Element px = ((FiniteFieldPrime) field).element(x);
            Element py = ((FiniteFieldPrime) field).element(y);
            AffinePoint p = new AffinePoint(px, py);
            if (isPoint(p)) {
                return p;
            } else {
                throw new IllegalArgumentException(
                                                   "x and y are not valid points");
            }
        }
    }

    public boolean isOrderMultiple(BigInteger n, AffinePoint p) {
        if (n == null) {
            throw new NullPointerException("n cannot be null");
        }
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        AffinePoint product = multiply(n, p);
        if (product.equals(infinity())) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean isPoint(AffinePoint p) {
        Element y2 = p.y().powMod(Constant.TWO);
        Element xy = p.x().mult(a1()).multMod(p.y());
        Element y = p.y().multMod(a3());
        Element x3 = p.x().powMod(Constant.THREE);
        Element x2 = p.x().powMod(Constant.TWO).multMod(a2());
        Element x = p.x().multMod(a4());
        Element c = new Element(a6(), p.x());

        Element left = y2.add(xy).addMod(y);
        Element right = x3.add(x2).add(x).addMod(c);

        return left.equals(right);
    }

    protected AffinePoint pointPhi(AffinePoint p) {
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        Fp2Element phi = new Fp2(field.getChar()).cubeRootOfUnity();
        return pointPhi(phi, p);
    }

    protected AffinePoint pointPhi(Fp2Element phi, AffinePoint p) {
        if (phi == null) {
            throw new NullPointerException("phi cannot be null");
        }
        if (p == null) {
            throw new NullPointerException("p cannot be null");
        }
        return new AffinePoint(phi.multMod(p.x()), p.y());
    }

    public AffinePoint randomPoint() {
        AffinePoint P = null;
        boolean problem = false;
        do {
            problem = false;
            try {
                BigInteger x = new BigInteger(
                                              getField().getChar().bitLength() - 1,
                                              new SecureRandom());
                x = x.mod(getField().getChar().divide(kappa));
                // NOTE:
                // while (0,0) is a valid point on the curve, it can lead to
                // problems.... something to be investigated at a later date!!
                if (x.compareTo(BigInteger.ZERO) == 0) {
                    problem = true;
                } else {
                    P = getPoint(x, null);
                }
            } catch (ArithmeticException e) {
                problem = true;
            }
        } while (problem);

        return P;
    }

    public void setKappa(BigInteger kappa) {
        this.kappa = new BigInteger(kappa.toString());
    }
}