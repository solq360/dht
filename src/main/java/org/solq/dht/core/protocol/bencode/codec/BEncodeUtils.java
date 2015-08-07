package org.solq.dht.core.protocol.bencode.codec;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author solq
 * */
public class BEncodeUtils {
    public static final char INT = 'i';
    public static final char ARRAY = 'l';
    public static final char DICTIONARY = 'd';
    public static final char STRING_SPLITTER = ':';
    public static final char END = 'e';

    public static String readDataUntilToken(InputStream is, char token) throws IOException {
        StringBuilder sb = new StringBuilder();
        char ch;
        while (true) {
            ch = (char) is.read();
            if (ch == token) {
                break;
            } else if ((byte) ch == -1) {
                throw new IllegalStateException("Unexpected stream end, token " + token);
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
    
 
}