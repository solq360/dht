package org.solq.dht.core.protocol.bencode.codec;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.util.Pair;

public class TypeFactory {
    public static BType<?> createType(InputStream is) throws IOException {
        char c = (char) is.read();
        if (c == BEncodeUtils.INT) {
            return new BInt();
        } else if (c == BEncodeUtils.ARRAY) {
            return new BArray();
        } else if (c == BEncodeUtils.DICTIONARY) {
            return new BDictionary();
        } else if (Character.isDigit(c)) {
            BString bString = new BString();
            bString.setLength(stringLength(c, is));
            return bString;
        } else if (c == BEncodeUtils.END) {
            return StubType.END;
        } else if ((byte) c == -1) {
            return StubType.END;
        } else {
            throw new IllegalStateException("Unknown b type " + c + " left bytes " + is.available());
        }
    }

    private static int stringLength(char firstDigit, InputStream is) throws IOException {
        String restLength = BEncodeUtils.readDataUntilToken(is, BEncodeUtils.STRING_SPLITTER);
        return Integer.valueOf(firstDigit + restLength);
    }

    public static BInt bInt(Integer i) {
        notNulCheck(i);
        BInt bInt = new BInt();
        bInt.setValue(i);
        return bInt;
    }

    public static BString bString(String s) {
        notNulCheck(s);
        BString bString = new BString();
        bString.setLength(s.length());
        bString.setValue(s);
        return bString;
    }

    public static BArray bArray(BType<?>... values) {
        notNulCheck(values);
        BArray bArray = new BArray();
        ArrayList<BType<?>> list = new ArrayList<>(values.length);
        Collections.addAll(list, values);
        bArray.setValue(list);
        return bArray;
    }

    @SafeVarargs
    public static BDictionary bDictionary(Pair<BString, BType<?>>... pairs) {
        notNulCheck(pairs);
        BDictionary bDictionary = new BDictionary();
        List<Pair<BString, BType<?>>> value = new ArrayList<>();
        for (Pair<BString, BType<?>> pair : pairs) {
            notNulCheck(pair.getKey());
            notNulCheck(pair.getValue());
            value.add(pair);
        }
        /**
         * According spec we should keep data in lexicographical order by keys
         */
        Collections.sort(value, (o1, o2) -> o1.getKey().value().compareTo(o2.getKey().value()));
        bDictionary.setValue(value);
        return bDictionary;
    }

    private static void notNulCheck(Object o) {
        if (o == null) {
            throw new IllegalStateException("Parameter cannot be null");
        }
    }

}