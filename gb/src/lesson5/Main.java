package lesson5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static final int CARS_COUNT = 4;
    final static CountDownLatch countDownLatchStart = new CountDownLatch(CARS_COUNT);
    final static CountDownLatch countDownLatchFinish = new CountDownLatch(CARS_COUNT);
    public static CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT);
    public static Semaphore semaphore = new Semaphore(CARS_COUNT / 2);
    public final static Lock lock = new ReentrantLock();

    public static void main(String[] args) {

        System.out.println("\nIMPORTANT MESSAGE >>> Preparing to race!!!\n");

        Race race = new Race(new Road(60), new Tunnel(), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }
        try {
            countDownLatchStart.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nIMPORTANT MESSAGE >>> Race start!!!\n");
        try {
            countDownLatchFinish.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nIMPORTANT MESSAGE >>> Race over!!!\n");
    }
}


