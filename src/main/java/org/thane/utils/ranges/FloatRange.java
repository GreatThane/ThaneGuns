package org.thane.utils.ranges;

public class FloatRange extends NumberRange<Float> {

    @Override
    public FloatRange clone() throws CloneNotSupportedException {
        FloatRange range = (FloatRange) super.clone();
        range.setMax(new Float(getMax()));
        range.setMin(new Float(getMin()));
        return range;
    }
}
