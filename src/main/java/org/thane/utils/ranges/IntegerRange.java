package org.thane.utils.ranges;

public class IntegerRange extends NumberRange<Integer> {

    @Override
    public IntegerRange clone() throws CloneNotSupportedException {
        IntegerRange range = (IntegerRange) super.clone();
        range.setMax(new Integer(getMax()));
        range.setMin(new Integer(getMin()));
        return range;
    }
}
