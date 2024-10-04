package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class Main {

    private static final int THREADS_NUMBER = 10;

    private static final int PORT = 5050;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws InterruptedException {
//        for (int i = 0; i < THREADS_NUMBER; ++i) {
//            Thread thread = new RandomSequenceExample();
//            //Thread thread = new SerialSequenceExample(i);
//            System.out.println("Start: " + thread.getName());
//            thread.start();
//        }
//    }

    private static class RandomSequenceExample extends Thread {
        public void run() {
            //System.out.println("Run: " + this.getName());
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    private static class SerialSequenceExample extends Thread {
        private static int currentMax = 1;
        private int mainId;
        private final static Object waitObject = new Object();

        public SerialSequenceExample(int id) {
            this.mainId = id;
        }

        public void run() {
            try {
                System.out.println("Run: " + mainId);
                synchronized (waitObject) {
                    while (mainId > currentMax) {
                        waitObject.wait();
                        //System.out.println("Awakened: " + mainId);
                    }

                    currentMax++;
                    //System.out.println("Finished: " + mainId);
                    waitObject.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);  // Echo the received message
                if (inputLine.equals("Bue.")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
