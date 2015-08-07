package org.solq.dht.core.protocol.bencode.codec;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.math3.util.Pair;
import org.solq.dht.core.protocol.bencode.Parser;

public class BDictionary extends CommonBType<List<Pair<BString, BType<?>>>> {
    BDictionary() {
    }

    @Override
    public List<Pair<BString, BType<?>>> value() {
        return value;
    }

    @Override
    public void decode(Parser<InputStream> p, InputStream is) throws IOException {
        decodePreconditions();
        value = new ArrayList<>();
        while (true) {
            BType<?> bKey = p.parseNext(is);
            BType<?> bValue = p.parseNext(is);
            if (bKey.equals(StubType.END)) {
                break;
            } else if (!(bKey instanceof BString)) {
                throw new IllegalStateException("Wrong dictionary format, string as key expected, left bytes " + is.available());
            } else if (bValue.equals(StubType.END)) {
                throw new IllegalStateException("Wrong dictionary format, value expected, left bytes " + is.available());
            } else {
                Pair<BString,BType<?>> pair = new Pair(bKey, bValue);
                value.add(pair);
            }
        }
    }


    @Override
    public String encode() {
        encodePreconditions();
        Stream<String> map = value.stream().map(pair -> pair.getKey().encode() + pair.getValue().encode());
        return String.valueOf(BEncodeUtils.DICTIONARY) + value.stream().map(pair -> pair.getKey().encode() + pair.getValue().encode()).reduce((a, b) -> a + b).get() + BEncodeUtils.END;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for (Pair<BString, BType<?>> pair : value()) {
            sb.append(pair.getKey().toString()).append(" -> ")
                    .append(pair.getValue().toString()).append(",\r\n");
        }
        sb.delete(sb.length() - 3, sb.length());
        sb.append("}");
        return sb.toString();
    }
}