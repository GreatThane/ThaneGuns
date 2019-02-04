package org.thane.utils.ranges;

import java.util.concurrent.ThreadLocalRandom;

public class NumberRange<T extends Number> extends Range<T> {

    public double numberRange() {
        return getMax().doubleValue() - getMin().doubleValue();
    }

    public double random() {
        return ThreadLocalRandom.current().nextDouble(getMin().doubleValue(), getMax().doubleValue());
    }
}
