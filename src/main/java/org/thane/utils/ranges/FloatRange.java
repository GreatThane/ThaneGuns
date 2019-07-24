package org.thane.utils.ranges;

public class FloatRange extends NumberRange<Float> {

    public FloatRange(Float min, Float max) {
        super(min, max);
    }

    @Override
    public FloatRange clone() {
        FloatRange range = (FloatRange) super.clone();
        range.setMax(getMax());
        range.setMin(getMin());
        return range;
    }
}
