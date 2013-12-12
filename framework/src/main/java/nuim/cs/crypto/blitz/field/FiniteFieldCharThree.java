package nuim.cs.crypto.blitz.field;

public class FiniteFieldCharThree {//extends FiniteField {
    /*
    public static final BigInteger THREE = new BigInteger( "3" );

    public FiniteFieldCharThree( FiniteFieldCharThree f ) {
        this( f.degree );
    }

    public FiniteFieldCharThree( BigInteger degree ) {
        super( THREE, degree );
    }

    public CubicCurve isomorphism( CubicCurve curve ) {
        if( curve == null ) {
            throw new NullPointerException( "curve cannot be null" );
        }
        BigInteger two = new BigInteger( "2" );
        BigInteger four = new BigInteger( "4" );
        BigInteger twoInv = two.modInverse( THREE );
        BigInteger fourInv = four.modInverse( THREE );

        BigInteger d2 = curve.b2().multiply( fourInv );
        BigInteger d4 = curve.b4().multiply( twoInv );
        BigInteger d6 = curve.b6().multiply( fourInv );

        if( d2.compareTo( BigInteger.ZERO ) == 0 ) {
            return(
                new CubicCurve( BigInteger.ZERO, BigInteger.ZERO,
                    BigInteger.ZERO, d4, d6, (FiniteFieldCharThree) clone() ) );
        }
        else {
            BigInteger c =
                d2.modPow( THREE, THREE ).multiply( d6 ).mod( THREE );
            c = c.add( d4.modPow( THREE, THREE ) );
            c = c.subtract( d2.modPow( two, THREE ).
                    multiply( d4.modPow( two, THREE ) ) ).mod( THREE );

            return(
                new CubicCurve( BigInteger.ZERO, d2, BigInteger.ZERO,
                    BigInteger.ZERO, c, (FiniteFieldCharThree) clone() ) );
        }
    }

    public Object clone() {
        return( new FiniteFieldCharThree( this ) );
    }
     */
}