package nuim.cs.crypto.bilinear;

import java.math.BigInteger;

import nuim.cs.crypto.algebra.Function;
import nuim.cs.crypto.blitz.constants.Constant;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.point.AffinePoint;

/**
 * Here we use the Weil pairing calculation from Boneh and Franklin's paper.
 */
public class WeilPairing extends TatePairing {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public WeilPairing() {
        super(10);
    }

    @Override
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

        Element t = millersAlgorithm(Q, Qhat, Phat, R2, R1);
        Element b = millersAlgorithm(P, Phat, Qhat, R1, R2);
        //BigInteger f = curve.getField().getChar();
        //return( t.multiply( b.modInverse( f ) ).mod( f ) );
        return t.divMod(b);
    }

    @Override
    public void init(BigInteger p, BigInteger q, BigInteger l) {
        super.init(p, q, l);
        FiniteFieldPrime f = new FiniteFieldPrime(p);
        curve = new PairingEllipticCurve(BigInteger.ZERO, BigInteger.ONE, f);
    }

    /**
     * Implementation of MapToPoint from Boneh and Franklin's "Identity-Based
     * Encryption from the Weil Pairing", Section 5.2.
     * 
     * @param y
     *            y value of the point on the curve.
     * @return valid point on the curve.
     * @throws ArithmeticException
     *             if non-infinity points of order dividing l are encountered.
     */
    @Override
    public AffinePoint mapToPoint(BigInteger y) {
        if (y == null) {
            throw new NullPointerException("y cannot be null");
        }
        AffinePoint P = curve.getPoint(null, y);
        // p + 1 = lq => l = (p + 1)/q
        AffinePoint lP = curve.multiply(l, P);
        if (lP.equals(curve.infinity())) {
            throw new ArithmeticException("lP = oo (infinity)");
        } else {
            return lP;
        }
    }

    //public BigInteger millersAlgorithm( AffinePoint P, AffinePoint Phat,
    @Override
    @SuppressWarnings("unused")
    public Element millersAlgorithm(AffinePoint P, AffinePoint Phat,
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
            /*
            System.out.println( "g1 : " + g1 );
            System.out.println( "g2 : " + g2 );
            System.out.println( "P : " + P );
            System.out.println( "R1 : " + R1 );
            System.out.println( "R2 : " + R2 );
            System.out.println( "P + R1 : " + Phat );
            System.out.println( "Qhat = Q + R2 : " + Qhat );
            System.out.println( "g1.evaluate( Qhat ) : " + g1.evaluate( Qhat ) );
            System.out.println( "g1.evaluate( R2 ) : " + g1.evaluate( R2 ) );
            System.out.println( "g2.evaluate( Qhat ) : " + g2.evaluate( Qhat ) );
            System.out.println( "g2.evaluate( R2 ) : " + g2.evaluate( R2 ) );
             */
        }
        //System.out.println( "f1 : " + f1 );
        BigInteger k = BigInteger.ZERO;

        //Element v = P.x().clone( BigInteger.ONE );
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
                /*
                System.out.println( "g1 : " + g1 );
                System.out.println( "g2 : " + g2 );
                System.out.println( "Z + P : " + Z );
                System.out.println( "g1.evaluate( Qhat ) : " + g1.evaluate( Qhat ) );
                System.out.println( "g1.evaluate( R2 ) : " + g1.evaluate( R2 ) );
                System.out.println( "g2.evaluate( Qhat ) : " + g2.evaluate( Qhat ) );
                System.out.println( "g2.evaluate( R2 ) : " + g2.evaluate( R2 ) );
                /*
                if( t.equals( BigInteger.ZERO ) ) {
                    System.out.println( "g1.evaluate( Qhat ) : " + g1.evaluate( Qhat ) );
                    System.out.println( "g2.evaluate( R2 ) : " + g2.evaluate( R2 ) );
                    System.out.println( "Top true t = " + t + ", g1 = " + g1 + ", g2 = " + g2 + ", Qhat = " + Qhat + ", R2 = " + R2 );
                }
                if( b.equals( BigInteger.ZERO ) ) {
                    System.out.println( "Bottom true b = " + b + ", g1 = " + g1 + ", g2 = " + g2 + ", Qhat = " + Qhat + ", R2 = " + R2 );
                }
                 */

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
                /*
                System.out.println( "g1 : " + g1 );
                System.out.println( "g2 : " + g2 );
                System.out.println( "2Z : " + Z );
                System.out.println( "g1.evaluate( Qhat ) : " + g1.evaluate( Qhat ) );
                System.out.println( "g1.evaluate( R2 ) : " + g1.evaluate( R2 ) );
                System.out.println( "g2.evaluate( Qhat ) : " + g2.evaluate( Qhat ) );
                System.out.println( "g2.evaluate( R2 ) : " + g2.evaluate( R2 ) );
                /*
                if( t.equals( BigInteger.ZERO ) ) {
                    System.out.println( "Top greater t = " + t + ", g1 = " + g1 + ", g2 = " + g2 + ", Qhat = " + Qhat + ", R2 = " + R2 );
                }
                if( b.equals( BigInteger.ZERO ) ) {
                    System.out.println( "Bottom greater b = " + b + ", g1 = " + g1 + ", g2 = " + g2 + ", Qhat = " + Qhat + ", R2 = " + R2 );
                }
                 */

                // k = 2k
                k = k.multiply(Constant.TWO);
            }
        }
        /*
        System.out.println( "k : " + k );
        System.out.println( "vn : " + vn );
        System.out.println( "vd : " + vd );
         */
        //return( vn.divMod( vd ).toBigInteger() );
        return vn.divMod(vd);
    }
}