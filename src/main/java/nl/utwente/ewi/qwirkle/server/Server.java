package nl.utwente.ewi.qwirkle.server;

public class Server implements Runnable {

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
        Server server = new Server();
        new Thread(server).start();
    }
}
