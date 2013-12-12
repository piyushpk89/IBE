/**
 * Copyright (c) 2012, salesforce.com, inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 *    Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *    following disclaimer.
 *
 *    Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 *    the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 *    Neither the name of salesforce.com, inc. nor the names of its contributors may be used to endorse or
 *    promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.hellblazer.security;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @author hhildebrand
 * 
 */
public class SelfSignedInsertion {
    private static final String REPLACEMENT_CRT = "replacement.crt";

    @SuppressWarnings("restriction")
    public static void allowSelfSigned(X509Certificate cert) throws Exception {
        Class<?> clazz = Class.forName("javax.crypto.JarVerifier");

        Field exemptValidator = clazz.getDeclaredField("exemptValidator");
        exemptValidator.setAccessible(true);

        Field providerValidator = clazz.getDeclaredField("providerValidator");
        providerValidator.setAccessible(true);

        Constructor<sun.security.validator.SimpleValidator> constructor = sun.security.validator.SimpleValidator.class.getDeclaredConstructor(String.class,
                                                                                                                                              Collection.class);
        constructor.setAccessible(true);
        ArrayList<X509Certificate> certs = new ArrayList<X509Certificate>();
        certs.add(cert);
        sun.security.validator.SimpleValidator validator = constructor.newInstance("",
                                                                                   certs);

        exemptValidator.set(null, validator);
        providerValidator.set(null, validator);
    }

    public static void allowSelfSigned() throws Exception {
        InputStream replacementIs = SelfSignedInsertion.class.getResourceAsStream(REPLACEMENT_CRT);
        X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(replacementIs);
        allowSelfSigned(cert);
    }
}
