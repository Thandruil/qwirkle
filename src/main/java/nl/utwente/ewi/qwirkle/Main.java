package nl.utwente.ewi.qwirkle;

import nl.utwente.ewi.qwirkle.client.Client;
import nl.utwente.ewi.qwirkle.server.Server;
import nl.utwente.ewi.qwirkle.util.Logger;

/**
 * Called to start the application.
 */
public class Main {
    static final String USAGE_STRING = "" +
            "usage: java -jar qwirkle.jar [-client] [-server] [-v[vv]]\n" +
            "\t-client\t\tRun the client.\n" +
            "\t-server\t\tRun the server.\n" +
            "\t-v\t\t\tVerbose mode. Causes qwirkle to print debugging messages. Multiple -v increase verbosity.";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(USAGE_STRING);
            System.exit(1);
        }

        boolean server = false;
        String[] newArgs = new String[]{};

        for (String arg : args) {
            switch (arg) {
                case "-client":
                    server = false;
                    break;
                case "-server":
                    server = true;
                    break;
                case "-v":
                    Logger.addOutputStream(Logger.WARN, System.out);
                    break;
                case "-vv":
                    Logger.addOutputStream(Logger.INFO, System.out);
                    break;
                case "-vvv":
                    Logger.addOutputStream(Logger.DEBUG, System.out);
                    break;
                default:
                    System.out.println(USAGE_STRING);
                    System.exit(1);
            }
        }

        if (server) {
            Server.main(newArgs);
        } else {
            Client.main(newArgs);
        }
    }
}
