package org.solq.dht.core.protocol.bencode.codec;

public abstract class CommonBType<Value> implements BType<Value> {
	Value value;
	
    public void decodePreconditions() {
        if (value != null) {
            throw new IllegalStateException("Type already decoded");
        }
    }

    public void encodePreconditions() {
        if (value == null) {
            throw new IllegalStateException("Type not decoded yet");
        }
    }
	void setValue(Value value) {
		this.value = value;
	}
}