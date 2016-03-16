package org.solq.dht.test.other;

import org.junit.Test;

public class TestOther {

    @Test
    public void test(){
	long size = SizeOfObject.fullSizeOf(new Object());
	System.out.println(size);
    }
}
