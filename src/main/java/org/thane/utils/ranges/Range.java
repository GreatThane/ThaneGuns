package org.thane.utils.ranges;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Consumer;

public class Range<T extends Serializable> implements Cloneable, Serializable {

    private T min, max;

    public Range() {
    }

//    public static <T extends Comparable<T>> Range<T> rangeContaining(T... values) {
//        T max = values[0];
//        T min = values[values.length - 1];
//        for (int i = 0; i < values.length; i++) {
//            int result = values[i].compareTo(max);
//            if (result > 0) {
//                max = values[i];
//            } else {
//                result = values[i].compareTo(min);
//                if (result < 0) {
//                    min = values[i];
//                }
//            }
//        }
//        return new Range<>(min, max);
//    }

    public Range(T min, T max) {
        this.min = min;
        this.max = max;
    }

    public T getMin() {
        return min;
    }

    public void setMin(T min) {
        this.min = min;
    }

    public T getMax() {
        return max;
    }

    public void setMax(T max) {
        this.max = max;
    }

    public Range<T> clone() {
        try {
            return (Range<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
