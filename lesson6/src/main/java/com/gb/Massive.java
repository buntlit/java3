package com.gb;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Massive {

    public final int KEY4 = 4;
    public final int KEY1 = 1;

    public int[] cutMassiveAfterLastFourth(int[] massive) {
        List list = Arrays.stream(massive).boxed().collect(Collectors.toList());
        int placeOfFourth = list.lastIndexOf(KEY4);
        if (placeOfFourth < 0) {
            throw new RuntimeException("There no 4");
        }
        return Arrays.copyOfRange(massive, placeOfFourth + 1, massive.length);
    }

    public boolean checkFoursAndOnes(int[] massive) {
        List list = Arrays.stream(massive).boxed().collect(Collectors.toList());
        for (int i = 0; i <= 9; i++) {
            if (i != KEY1 && i != KEY4) {
                if (list.contains(i)) {
                    return false;
                }
            }
        }
        if (list.contains(KEY1) && list.contains(KEY4)) {
            return true;
        }
        return false;
    }
}
