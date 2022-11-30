package lesson4;

public class Main {
    final static char a = 'A';
    final static char b = 'B';
    final static char c = 'C';
    private static final Object mon = new Object();
    private static volatile char currentLetter = a;

    public static void main(String[] args) {
        new Thread(() -> {
            print(a, b);
        }).start();
        new Thread(() -> {
            print(b, c);
        }).start();
        new Thread(() -> {
            print(c, a);
        }).start();
    }

    public static void print(char current, char next) {
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != current) {
                        mon.wait();
                    }
                    System.out.print(current);
                    currentLetter = next;
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
