package lesson1.t1;

import java.util.ArrayList;
import java.util.Arrays;

public class Generic<T> {

    private T[] generic;

    public Generic(T... generic) {
        this.generic = generic;
    }

    public void elementPlacementChange() {
        int last = generic.length - 1;
        T arrayFirst = generic[0];
        T arrayLast = generic[last];
        generic[0] = arrayLast;
        generic[last] = arrayFirst;

    }

    public ArrayList<T> arrayAsList() {
        ArrayList<T> list = new ArrayList<>(Arrays.asList(generic));
        return list;
    }
}
