package org.thane.utils.ranges;

public class IntegerRange extends NumberRange<Integer> {

    public IntegerRange(Integer min, Integer max) {
        super(min, max);
    }

    @Override
    public IntegerRange clone() {
        IntegerRange range = (IntegerRange) super.clone();
        range.setMax(new Integer(getMax()));
        range.setMin(new Integer(getMin()));
        return range;
    }
}
