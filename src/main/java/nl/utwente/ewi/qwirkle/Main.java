package nl.utwente.ewi.qwirkle;

import nl.utwente.ewi.qwirkle.client.Client;
import nl.utwente.ewi.qwirkle.server.Server;
import nl.utwente.ewi.qwirkle.util.Logger;

public class Main {

    static final String USAGE_STRING = "usage: java -jar qwirkle.jar [--client] [--server]";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(USAGE_STRING);
            System.exit(1);
        }

        for (String arg : args) {
            switch (args[0]) {
                case "--client":
                    Client.main(args);
                    break;
                case "--server":
                    Server.main(args);
                    break;
                case "--verbose":
                    Logger.setLevel(Logger.ALL);
                default:
                    System.out.println(USAGE_STRING);
                    System.exit(1);
            }
        }
    }
}
