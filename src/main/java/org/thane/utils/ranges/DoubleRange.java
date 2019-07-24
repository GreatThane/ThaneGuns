package org.thane.utils.ranges;

public class DoubleRange extends NumberRange<Double> {

    public DoubleRange(Double min, Double max) {
        super(min, max);
    }

    @Override
    public DoubleRange clone() {
        DoubleRange range = (DoubleRange) super.clone();
        range.setMax(getMax());
        range.setMin(getMin());
        return range;
    }
}
