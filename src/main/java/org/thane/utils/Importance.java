package org.thane.utils;

public interface Importance extends Comparable<Importance> {

    int getImportance();

    @Override
    default int compareTo(Importance importance) {
        return Integer.compare(getImportance(), importance.getImportance());
    }
}
