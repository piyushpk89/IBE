package nuim.cs.crypto.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class BitUtility {
    public static final int BIT_MASK = 0x80;

    public static int firstTrueBit(byte array[]) {
        for (int j = 0; j < array.length; j++) {
            int and = BIT_MASK;
            for (int i = 0; and > 0; i++) {
                if ((array[j] & and) == and) {
                    return j * 8 + i;
                }
                and = and >>> 1;
            }
        }
        return -1;
    }

    public static synchronized Serializable fromBytes(byte bytes[]) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);

            return (Serializable) ois.readObject();
        } catch (IOException ioe) {
            throw new RuntimeException(
                                       "cannot convert byte array to Serializable object");
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException(
                                       "cannot convert byte array to Serializable object");
        }
    }

    /**
     * Pads out an array by preprending zero value bytes so that its length is a
     * multiple of the specified value. Useful for performing operations on
     * blocks of byte arrays at a time.
     * 
     * @param input
     *            the array to be padded.
     * @param multiple
     *            the length of the padded array will be a multiple of this
     *            value.
     * @return the padded array.
     */
    public static byte[] pad(byte input[], int multiple) {
        if (input == null) {
            throw new NullPointerException("input cannot be null");
        }
        if (multiple <= 0) {
            throw new IllegalArgumentException(
                                               "multiple must be greater than zero");
        }
        if (multiple == 1) {
            return input;
        }
        if (input.length == 0) {
            return new byte[multiple];
        }
        int remainder = input.length % multiple;
        int blocks = input.length / multiple;
        int required = 0;
        if (remainder != 0) {
            blocks++;
            required = multiple - remainder;
        }

        byte output[] = new byte[blocks * multiple];
        System.arraycopy(input, 0, output, required, input.length);

        return output;
    }

    public static byte[] square(byte b) {
        byte r[] = new byte[2];
        int and = BIT_MASK;
        for (int i = 0; and > 0; i += 2) {
            r[(i / 8)] <<= 2;
            r[(i / 8)] += (b & and) == and ? 1 : 0;
            and = and >>> 1;
        }
        return r;
    }

    public static synchronized byte[] toBytes(Serializable obj) {
        try {
            ByteArrayOutputStream bas = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bas);

            oos.writeObject(obj);
            oos.close();

            return bas.toByteArray();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException(
                                       "cannot convert Serializable object to byte array");
        }
    }

    public static String toByteString(byte array[]) {
        StringBuffer buffer = new StringBuffer();
        for (byte element : array) {
            int and = BIT_MASK;
            while (and > 0) {
                buffer.append(((element & and) == and ? "1" : "0"));
                and = and >>> 1;
            }
        }
        return new String(buffer);
    }

    public static String toByteString(byte b) {
        StringBuffer buffer = new StringBuffer();
        int and = BIT_MASK;
        while (and > 0) {
            buffer.append(((b & and) == and ? "1" : "0"));
            and = and >>> 1;
        }
        return new String(buffer);
    }

    public static String toIntString(byte array[]) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < array.length - 1; i++) {
            buffer.append(String.valueOf(array[i]) + ":");
        }
        buffer.append(String.valueOf(array[array.length - 1]));
        return new String(buffer);
    }
}