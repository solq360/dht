package org.solq.dht.core.protocol.bencode.codec;

import java.io.IOException;
import java.io.InputStream;

import org.solq.dht.core.protocol.bencode.Parser;

public interface BType<Value> {
    /**
     *
     * @return Keeping alue
     */
    Value value();

    /**
     *
     * @param p parser implementation which know how to parse source
     * @param is data source
     * @throws IOException
     */
    void decode(Parser<InputStream> p, InputStream is) throws IOException;

    /**
     * Convert object to bencode representation
     * @return bencode representation
     */
    String encode();
}