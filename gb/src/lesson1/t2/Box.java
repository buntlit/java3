package lesson1.t2;

import java.util.ArrayList;

public class Box<T extends Fruit> {
    private ArrayList<T> box;

    public Box(ArrayList<T> box) {
        this.box = box;
    }

    private double fruitWeight() {
        return box.get(0).getWeight();
    }

    public double boxWeight() {
        if (box.size() > 0) {
            return box.size() * fruitWeight();
        } else {
            return 0;
        }
    }

    public boolean compare(Box<?> anotherBox) {
        return this.boxWeight() - anotherBox.boxWeight() < 0.00001;
    }

    public void boxCopy(Box<T> anotherBox) {
        this.box.addAll(anotherBox.box);
        anotherBox.box.clear();
    }

    public void addFruit(T fruit) {
        this.box.add(fruit);
    }
}
