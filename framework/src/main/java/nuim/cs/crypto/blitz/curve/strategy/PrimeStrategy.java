package nuim.cs.crypto.blitz.curve.strategy;

import java.math.BigInteger;

import nuim.cs.crypto.blitz.curve.EllipticCurve;
import nuim.cs.crypto.blitz.field.FiniteFieldPrime;

public class PrimeStrategy extends Strategy {
    protected PrimeStrategy(EllipticCurve curve) {
        super(curve);
        // check the curve is over the correct field
        if (!(curve.getField() instanceof FiniteFieldPrime)) {
            throw new IllegalArgumentException(
                                               "curve field must have characteristic greater than 3 "
                                                       + "and characteristic degree of 1");
        }
        // check the coefficients of the curve
        if (curve.a1().compareTo(BigInteger.ZERO) != 0
            || curve.a2().compareTo(BigInteger.ZERO) != 0
            || curve.a3().compareTo(BigInteger.ZERO) != 0) {
            throw new IllegalArgumentException(
                                               "curve must have zero xy (a1), x^2 (a2) and y (a3) "
                                                       + "coefficients");
        }
    }
}