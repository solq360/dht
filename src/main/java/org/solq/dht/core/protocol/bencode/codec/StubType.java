package org.solq.dht.core.protocol.bencode.codec;

import java.io.InputStream;

import org.solq.dht.core.protocol.bencode.Parser;

public enum StubType implements BType<Void> {
    /**
     * end marker
     */
    END;

    @Override
    public Void value() {
        throw new UnsupportedOperationException("Type is None");
    }

    @Override
    public void decode(Parser<InputStream> p, InputStream is) {
    }

    @Override
    public String encode() {
        throw new UnsupportedOperationException("Type is None");
    }
}