package com.mercandalli.core.fifo;

import org.junit.Assert;
import org.junit.Test;

public class DistanceFifoTest {

    @Test
    public void fifoAdd() {
        DistanceFifo distanceFifo = new DistanceFifo(5);
        for (int i = 0; i < 10; i++) {
            distanceFifo.add(i);
        }
        Assert.assertEquals(6, distanceFifo.getDistance());
    }
}
