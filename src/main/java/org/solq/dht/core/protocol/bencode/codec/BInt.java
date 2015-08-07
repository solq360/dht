package org.solq.dht.core.protocol.bencode.codec;

import java.io.IOException;
import java.io.InputStream;

import org.solq.dht.core.protocol.bencode.Parser;

public class BInt extends CommonBType<Integer> {

    BInt() {
    }

    @Override
    public Integer value() {
        return value;
    }

    @Override
    public void decode(Parser<InputStream> p, InputStream is) throws IOException {
        decodePreconditions();
        value = Integer.valueOf(BEncodeUtils.readDataUntilToken(is, BEncodeUtils.END));
    }

    @Override
    public String encode() {
        encodePreconditions();
        return String.valueOf(BEncodeUtils.INT) + value + BEncodeUtils.END;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}