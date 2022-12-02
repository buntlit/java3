package lesson5;

import static lesson5.Main.semaphore;

public abstract class Stage {
    protected final String START = "starts stage:";
    protected final String END = "ends stage:";
    protected final String PREPARE = "preparing to stage(waiting):";
    protected int length;
    protected String description;

    public String getDescription() {
        return description;
    }

    public abstract void go(Car c);

    void printMessage(String name, String text) {
        System.out.println(String.format("%s %s %s", name, text, description));
    }
}


class Road extends Stage {
    public Road(int length) {
        this.length = length;
        this.description = String.format("Road %d meters", length);
    }

    @Override
    public void go(Car c) {
        String name = c.getName();
        try {
            printMessage(name, START);
            Thread.sleep(length / c.getSpeed() * 1000);
            printMessage(name, END);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Tunnel extends Stage {
    public Tunnel() {
        this.length = 80;
        this.description = String.format("Tunnel %d meters", length);
    }

    @Override
    public void go(Car c) {
        String name = c.getName();
        try {
            try {
                printMessage(name, PREPARE);
                semaphore.acquire();
                printMessage(name, START);
                Thread.sleep(length / c.getSpeed() * 1000);
                printMessage(name, END);
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


