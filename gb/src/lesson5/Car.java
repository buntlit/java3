package lesson5;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static lesson5.Main.*;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private Race race;
    private int speed;
    private String name;


    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = String.format("Competitor №%d", CARS_COUNT);
    }

    @Override
    public void run() {
        try {
            System.out.println(String.format("%s preparing", name));
            cyclicBarrier.await();
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(String.format("%s prepare", name));
            countDownLatchStart.countDown();
            cyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        if (lock.tryLock()) {
            System.out.println(String.format("%s WIN", name));
        }
        countDownLatchFinish.countDown();
    }
}
