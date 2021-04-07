package serverModule;

import common.utility.Request;
import common.utility.Response;
import common.utility.ResponseCode;
import serverModule.utility.RequestManager;
import serverModule.utility.RequestProcessingThread;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Server {
    private int port;
    private RequestManager requestManager;
    private DatagramSocket socket;
    private InetAddress address;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
    private Request request;
    private Scanner scanner;

    public Server(int port, RequestManager requestManager) {
        this.port = port;
        this.requestManager = requestManager;
    }

    public void run() {
        do_CTRL_C_Thread();
        try {
            System.out.println("Сервер запущен.");
            socket = new DatagramSocket(this.port);
            while (true) {
                try {
                    if (!fixedThreadPool.submit(() -> {
                        try {
                            request = getRequest();
                            System.out.println("Получена команда '" + request.getCommandName() + "'");
                            return true;
                        } catch (ClassNotFoundException | IOException e) {
                            System.out.println("Произошла ошибка при чтении запроса!");
                        }
                        return false;
                    }).get()) break;
                    new RequestProcessingThread(requestManager, request, address, port, socket).start();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("При чтении запроса произошла ошибка многопоточности!");
                }
            }
        } catch (SocketException e) {
            System.out.println("Произошла ошибка при работе с сокетом!");
        }
    }

    private Request getRequest() throws IOException, ClassNotFoundException {
        byte[] getBuffer = new byte[socket.getReceiveBufferSize()];
        DatagramPacket getPacket = new DatagramPacket(getBuffer, getBuffer.length);
        socket.receive(getPacket);
        address = getPacket.getAddress();
        port = getPacket.getPort();
        return deserialize(getPacket, getBuffer);
    }

    private Request deserialize(DatagramPacket getPacket, byte[] buffer) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getPacket.getData());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Request request = (Request) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        return request;
    }

    private void do_CTRL_C_Thread() {
        scanner = new Scanner(System.in);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            new RequestProcessingThread(requestManager, new Request("save","")).start();
            System.out.println("Завершаю программу.");
        }));
    }
}
