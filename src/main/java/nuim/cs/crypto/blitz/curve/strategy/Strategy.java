package nuim.cs.crypto.blitz.curve.strategy;

import nuim.cs.crypto.blitz.curve.EllipticCurve;

public class Strategy {
    protected EllipticCurve curve;

    protected Strategy(EllipticCurve curve) {
        if (curve == null) {
            throw new NullPointerException("curve cannot be null");
        }
        this.curve = curve;
    }
}