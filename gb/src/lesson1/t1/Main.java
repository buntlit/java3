package lesson1.t1;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Generic<Integer> array = new Generic<>(1, 2, 3, 4, 5);
        Generic<String> array2 = new Generic<>("1", "2", "sss", "hhh");
        array.elementPlacementChange();
        ArrayList<Integer> arrayList = array.arrayAsList();
        ArrayList<String> arrayList1 = array2.arrayAsList();
        System.out.println(arrayList);
        System.out.println(arrayList1);
    }
}

