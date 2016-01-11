package nl.utwente.ewi.qwirkle.client;

public class Client implements Runnable {

    boolean running = true;

    @Override
    public void run() {
        init();
        while (running) loop();
    }

    void init() {

    }

    void loop() {

    }

    void stop() {
        running = false;
    }

    public static void main(String[] args) {
        Client client = new Client();
        new Thread(client).start();
    }
}
