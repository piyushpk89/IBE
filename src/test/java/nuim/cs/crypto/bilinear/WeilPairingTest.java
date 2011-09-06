package nuim.cs.crypto.bilinear;

import java.math.BigInteger;

import junit.framework.TestCase;
import nuim.cs.crypto.blitz.field.Element;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;
import nuim.cs.crypto.blitz.point.AffinePoint;

public class WeilPairingTest extends TestCase {
    public void testGetPair() {
        WeilPairing weil = new WeilPairing();
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("7"));
        weil.init(new BigInteger("7"), new BigInteger("3"), BigInteger.ZERO);
        weil.curve = new PairingEllipticCurve(BigInteger.ZERO,
                                              new BigInteger("2"), field);

        AffinePoint p = new AffinePoint(field.element(BigInteger.ZERO),
                                        field.element(new BigInteger("3")));
        AffinePoint q = new AffinePoint(field.element(new BigInteger("5")),
                                        field.element(BigInteger.ONE));

        AffinePoint r = new AffinePoint(field.element(new BigInteger("6")),
                                        field.element(BigInteger.ONE));

        Element v = weil.getPair(p, q, r);
        assertTrue(v.equals(new BigInteger("2")));
    }

    public void testMapToPoint() {
        BigInteger p = new BigInteger("929");
        BigInteger q = new BigInteger("5");
        BigInteger l = new BigInteger("186");
        WeilPairing pairing = new WeilPairing();
        pairing.init(p, q, l);

        BigInteger b = BigInteger.ZERO;
        BigInteger pts = BigInteger.ZERO;
        for (BigInteger i = BigInteger.ZERO; i.compareTo(p) < 0; i = i.add(BigInteger.ONE)) {
            try {
                pairing.mapToPoint(i);
                pts = pts.add(BigInteger.ONE);
            } catch (ArithmeticException ae) {
                b = b.add(BigInteger.ONE);
            }
        }

        BigInteger bPoints = l.subtract(BigInteger.ONE);

        assertTrue(bPoints.compareTo(b) == 0);
        assertTrue(pts.compareTo(p.subtract(bPoints)) == 0);

        try {
            pairing.mapToPoint(new BigInteger("821"));
            assertTrue(false);
        } catch (ArithmeticException ae) {
            assertTrue(true);
        }
    }

    /*
    public void testIbe() {
        // p = 2467253 q = 73 l = 33798
        // p = 10301 q = 17 l = 606
        // p = 4289 q = 13 l = 330
        // p = 4289 q = 11 l = 390
        // p = 443 q = 3 l = 148
        WeilPairing pairing = new WeilPairing();
        BigInteger p = new BigInteger( "2467253" );
        BigInteger q = new BigInteger( "73" );
        BigInteger l = new BigInteger( "33798" );
        pairing.init( p, q, l );

        FiniteFieldPrime field = new FiniteFieldPrime( p );
        AffinePoint P = new AffinePoint( field.element( new BigInteger( "688514" ) ),
                field.element( new BigInteger( "1641310" ) ) );
        AffinePoint Q = new AffinePoint( field.element( new BigInteger( "571311" ) ),
                field.element( new BigInteger( "2353398" ) ) );
        AffinePoint R = new AffinePoint( field.element( new BigInteger( "1110849" ) ),
                field.element( new BigInteger( "1640122" ) ) );

        boolean error = false;
        do {
            error = false;
            try {
                AffinePoint R1 = pairing.randomPoint();
                Element weil = pairing.getPair( P, Q, R1 );
                //BigInteger weil = pairing.getPair( P, Q, R1 );
                System.out.println( "e(P,Q) = " + weil );
                weil = pairing.getPair( Q, P, R1 );
                System.out.println( "e(Q,P) = " + weil );
                System.out.println( "e(Q,P)-1 = " + weil.invMod() );
                //System.out.println( "e(Q,P)-1 = " + weil.modInverse( p ) );
            }
            catch( ArithmeticException e ) {
                error = true;
            }
        }
        while( error );


        error = false;
        do {
            error = false;
            try {
                AffinePoint R1 = pairing.randomPoint();
                Element weil = pairing.getPair( P, pairing.curve.add( Q, R ), R1 );
                //BigInteger weil = pairing.getPair( P, pairing.curve.add( Q, R ), R1 );
                System.out.println( "e(P,Q1 + Q2) = " + weil );
                weil = pairing.getPair( P, Q, R1 );
                System.out.println( "e(P,Q1) = " + weil );
                System.out.println( "e(P,Q1)e(P,Q2) = " + weil.multMod( pairing.getPair( P, R, R1 ) ) );
                //System.out.println( "e(P,Q1)e(P,Q2) = " + weil.multiply( pairing.getPair( P, R, R1 ) ).mod( p ) );
            }
            catch( ArithmeticException e ) {
                error = true;
            }
        }
        while( error );
        /*
        WeilPairing pairing = new WeilPairing();
        BigInteger p = new BigInteger( "929" );
        BigInteger q = new BigInteger( "5" );
        BigInteger l = new BigInteger( "186" );
        pairing.init( p, q, l );

        // get all the valid points
        BigInteger pts = p.subtract( l.subtract( BigInteger.ONE ) );
        AffinePoint valid[] = new AffinePoint[pts.intValue()];
        int j = 0;
        for( BigInteger i = BigInteger.ZERO; i.compareTo( p ) < 0;
            i = i.add( BigInteger.ONE ) ) {
            try {
                valid[j] = pairing.mapToPoint( i );
                j++;
            }
            catch( ArithmeticException ae ) {
                // do nothing
            }
        }

        /*
        boolean error = false;
        do {
            error = false;
            try {
                int x = (int) ( Math.random() * valid.length );
                int y = (int) ( Math.random() * valid.length );
                int z = (int) ( Math.random() * valid.length );
                AffinePoint P = valid[x];
                AffinePoint Q = valid[y];
                AffinePoint R = valid[z];
                System.out.println( "trying P = " + P + ", Q = " + Q + ", R = " + R );
                BigInteger v = pairing.getPair( P, Q, R );
                System.out.println( "v : " + v );
            }
            catch( ArithmeticException ae ) {
                error = true;
            }
        }
        while( error );

        /*
        boolean solution = false;
        for( int x = 0; x < valid.length && !solution; x++ ) {
            for( int y = 0; y < valid.length && !solution; y++ ) {
                for( int z = 0; z < valid.length && !solution; z++ ) {
                    try {
                        AffinePoint P = valid[x];
                        AffinePoint Q = valid[y];
                        AffinePoint R = valid[z];
                        System.out.println( "trying P = " + x + ", Q = " + y + ", R = " + z );
                        BigInteger v = pairing.getPair( P, Q, R );
                        System.out.println( "v : " + v );
                        solution = true;
                    }
                    catch( ArithmeticException e ) {
                        // do nothing
                    }
                }
            }
        }

        /*
        AffinePoint P = valid[12];
        AffinePoint Q = valid[75];

        boolean solution = false;
        for( int i = 0; i < valid.length && !solution; i++ ) {
            try {
                AffinePoint R = valid[i];
                BigInteger v = pairing.getPair( P, Q, R );
                System.out.println( "v : " + v );
                solution = true;
            }
            catch( ArithmeticException ae ) {
            }
        }

        /*
        boolean error = false;
        do {
            error = false;
            try {
                int random = (int) ( Math.random() * valid.length );
                AffinePoint R = valid[random];
                BigInteger v = pairing.getPair( P, Q, R );
                System.out.println( "v : " + v );
            }
            catch( ArithmeticException ae ) {
                error = true;
            }
        }
        while( error );
         */
    //}

    /*
    public void testWeilPair() {
        WeilPairing pairing = new WeilPairing();
        BigInteger p = new BigInteger( "1427" );
        BigInteger q = new BigInteger( "7" );
        BigInteger l = new BigInteger( "204" );
        pairing.init( p, q, l );
        FiniteFieldInteger f = new FiniteFieldInteger( p );
        pairing.curve =
            new EllipticCurve( BigInteger.ZERO, BigInteger.ONE, f );

        AffinePoint P = pairing.randomPoint();
        AffinePoint Q = pairing.randomPoint();
        while( P.equals( Q ) ) {
            Q = pairing.randomPoint();
        }

        System.out.println( "P : " + P );
        System.out.println( "Q : " + Q );

        BigInteger weil = pairing.getPair( P, Q );

        //System.out.println( "Weil : " + weil );
    }
         */
    /*
    public void testMillersAlgorithm() {
        WeilPairing pairing = new WeilPairing();
        BigInteger p = new BigInteger( "1427" );
        BigInteger q = new BigInteger( "7" );
        BigInteger l = new BigInteger( "204" );
        pairing.init( p, q, l );

        AffinePoint P = new AffinePoint( new BigInteger( "971" ), new BigInteger( "802" ) );
        AffinePoint Q = new AffinePoint( new BigInteger( "971" ), new BigInteger( "625" ) );
        AffinePoint R1 = new AffinePoint( new BigInteger( "1187" ), new BigInteger( "693" ) );
        AffinePoint R2 = new AffinePoint( new BigInteger( "1184" ), new BigInteger( "180" ) );

        FiniteFieldInteger f = new FiniteFieldInteger( p );
        pairing.curve = new EllipticCurve( BigInteger.ZERO, BigInteger.ONE, f );
        AffinePoint Phat = pairing.curve.add( P, R1 );
        AffinePoint Qhat = pairing.curve.add( Q, R2 );

        pairing.millersAlgorithm( P, Phat, Qhat, R1, R2 );
    }
     */

    //public void testGetPair() {
    /*
    AffinePoint P = new AffinePoint( BigInteger.ONE, BigInteger.ONE );
    WeilPairing pairing = new WeilPairing();
    try {
        pairing.getPair( null, P );
        assertTrue( false );
    }
    catch( NullPointerException npe ) {
        assertTrue( true );
    }

    try {
        pairing.getPair( P, null );
        assertTrue( false );
    }
    catch( NullPointerException npe ) {
        assertTrue( true );
    }

    BigInteger p = new BigInteger( "21011" );
    BigInteger q = new BigInteger( "17" );
    BigInteger l = new BigInteger( "1236" );
    pairing = new WeilPairing();
    pairing.init( p, q, l );

    AffinePoint P1 = pairing.randomPoint();
    AffinePoint P2 = pairing.randomPoint();

    AffinePoint Q = pairing.randomPoint();

    AffinePoint P1plusP2 = pairing.curve.add(P1,P2);

    /*
    System.out.println("P1 + P2 is "+P1plusP2+"\n Q is "+Q);
    BigInteger weil1 = pairing.getPair( P1plusP2, Q );
    System.out.println("weilP1P2Q is "+weil1);

    /*
    AffinePoint P1 = pairing.getPointOrderN( q );
    AffinePoint P2 = pairing.getPointOrderN( q );

    AffinePoint Q = pairing.getPointOrderN(q);

    AffinePoint P1plusP2 = pairing.curve.add(P1,P2);

    //System.out.println("P is "+P1+"\n Q is "+Q);
    //BigInteger weil1 = pairing.getPair( P1plusP2, Q );
    //System.out.println("weilP1P2Q is "+weil1);

    /*
    BigInteger weil2=getWeilPairing(P1,Q);
    BigInteger weil3=getWeilPairing(P2,Q);
    System.out.println("P1 is "+P1);
    System.out.println("P2 is "+P2);
    System.out.println("P1 +P2 is "+P1plusP2);
    System.out.println("Q is "+Q);

    System.out.println("field size is "+fieldSize);
    System.out.println("q is "+q);
    System.out.println("weilP1P2Q is "+weil1);
    System.out.println("weilP1Q is "+weil2);
    System.out.println("weilP1Q is "+weil3);


    if(weil1.compareTo((weil2.multiply(weil3)).mod(fieldSizeSquared))==0)
    {

    System.out.println("Yep you did it ! Its Bilinear!");
    }
    else
    {
    System.out.println("Something bad here");
    }

    /* not gonna work, whole idea is based around curves of y2 = x3 + 1 !!
    BigInteger p = new BigInteger( "1427" );
    BigInteger q = new BigInteger( "7" );
    BigInteger l = new BigInteger( "204" );

    pairing.init( p, q, l );
    FiniteFieldInteger f = new FiniteFieldInteger( p );
    pairing.curve =
        new EllipticCurve( BigInteger.ZERO, new BigInteger( "2" ), f );

    P = pairing.getPointOrderN( q );
    AffinePoint Q = pairing.getPointOrderN( q );

    System.out.println( "P : " + P );
    System.out.println( "Q : " + Q );

    BigInteger weil = pairing.getPair( P, Q );

    System.out.println( "Weil : " + weil );
     */
    //}

    public void testMiller() {
        WeilPairing weil = new WeilPairing();
        FiniteFieldPrime field = new FiniteFieldPrime(new BigInteger("11"));
        weil.init(new BigInteger("11"), new BigInteger("5"), BigInteger.ZERO);
        weil.curve = new PairingEllipticCurve(BigInteger.ONE.negate(),
                                              BigInteger.ONE, field);

        AffinePoint p = new AffinePoint(field.element(new BigInteger("3")),
                                        field.element(new BigInteger("6")));
        AffinePoint q = (AffinePoint) p.clone();

        AffinePoint r1 = weil.curve.infinity();
        AffinePoint r2 = new AffinePoint(field.element(BigInteger.ZERO),
                                         field.element(BigInteger.ONE));

        AffinePoint Phat = weil.curve.add(p, r1);
        AffinePoint Qhat = weil.curve.add(q, r2);

        Element v = weil.millersAlgorithm(p, Phat, Qhat, r1, r2);
        assertTrue(v.equals(new BigInteger("5")));

        field = new FiniteFieldPrime(new BigInteger("7"));
        weil.init(new BigInteger("7"), new BigInteger("3"), BigInteger.ZERO);
        weil.curve = new PairingEllipticCurve(BigInteger.ZERO,
                                              new BigInteger("2"), field);

        p = new AffinePoint(field.element(BigInteger.ZERO),
                            field.element(new BigInteger("3")));
        q = new AffinePoint(field.element(new BigInteger("5")),
                            field.element(BigInteger.ONE));

        r1 = weil.curve.infinity();
        r2 = new AffinePoint(field.element(new BigInteger("6")),
                             field.element(BigInteger.ONE));

        Phat = weil.curve.add(p, r1);
        Qhat = weil.curve.add(q, r2);

        v = weil.millersAlgorithm(p, Phat, Qhat, r1, r2);
        assertTrue(v.equals(new BigInteger("2")));

        v = weil.millersAlgorithm(q, Qhat, Phat, r2, r1);
        assertTrue(v.equals(new BigInteger("4")));
    }
}