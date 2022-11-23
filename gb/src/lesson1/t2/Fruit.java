package lesson1.t2;

public abstract class Fruit {
    private double weight;

    public Fruit() {
    }

    public double getWeight() {
        return weight;
    }
}

class Apple extends Fruit {

    public Apple() {
    }

    @Override
    public double getWeight() {
        return 1;
    }


}

class Orange extends Fruit {
    public Orange() {
    }

    @Override
    public double getWeight() {
        return 1.5;
    }
}
