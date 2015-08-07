package org.solq.dht.core.protocol.bencode;

import java.io.IOException;
import java.util.List;

import org.solq.dht.core.protocol.bencode.codec.BType;

public interface Parser<Source> {

    BType<?> parseNext(Source is) throws IOException;

    List<BType<?>> parse(Source is) throws IOException;    
    
    List<BType<?>> parse(byte[] bytes) throws IOException;
    
    List<BType<?>> parse(String str) throws IOException;
}