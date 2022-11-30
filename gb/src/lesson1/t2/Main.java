package lesson1.t2;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Apple apple = new Apple();
        Orange orange = new Orange();
        Box<Apple> box1 = new Box<>(new ArrayList<>(Arrays.asList(apple, apple, apple)));
        Box<Orange> box2 = new Box<>(new ArrayList<>(Arrays.asList(orange, orange, orange, orange)));
        Box<Apple> box3 = new Box<>(new ArrayList<>(Arrays.asList(apple, apple)));

        System.out.println("box1 weight: "+ box1.boxWeight());
        System.out.println("box2 weight: "+ box2.boxWeight());
        System.out.println("box3 weight: "+ box3.boxWeight());
        box1.boxCopy(box3);
        box1.addFruit(apple);
        System.out.println();
        System.out.println("box1 weight: "+ box1.boxWeight());
        System.out.println("box2 weight: "+ box2.boxWeight());
        System.out.println("box3 weight: "+ box3.boxWeight());
        System.out.println();
        System.out.println("box1 equals box2: "+ box1.compare(box2));
    }
}
