package org.thane.utils.ranges;

import java.util.concurrent.ThreadLocalRandom;

public class NumberRange<T extends Number> extends Range<T> {

    public NumberRange(T min, T max) {
        super(min, max);
    }

    public T numberRange() {
        return (T) ((Number) (getMax().doubleValue() - getMin().doubleValue()));
    }

    public T random() {
        return (T) ((Number) ThreadLocalRandom.current().nextDouble(getMin().doubleValue(), getMax().doubleValue()));
    }
}
