package org.solq.dht.core.protocol.bencode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.solq.dht.core.protocol.bencode.codec.BType;
import org.solq.dht.core.protocol.bencode.codec.StubType;
import org.solq.dht.core.protocol.bencode.codec.TypeFactory;
 
public class StreamParser implements Parser<InputStream> {
    @Override
    public BType<?> parseNext(InputStream is) throws IOException {
        BType<?> type = TypeFactory.createType(is);
        type.decode(this, is);
        return type;
    }

    @Override
    public List<BType<?>> parse(InputStream is) throws IOException {
        BType<?> bType;
        List<BType<?>> result = new LinkedList<>();
        while (!(bType = parseNext(is)).equals(StubType.END)) {
            result.add(bType);
        }
        return result;
    }

	@Override
	public List<BType<?>> parse(byte[] bytes) throws IOException { 
		return parse(new ByteArrayInputStream(bytes));
	}

	@Override
	public List<BType<?>> parse(String str) throws IOException {
		return parse(str.getBytes());
	}
}