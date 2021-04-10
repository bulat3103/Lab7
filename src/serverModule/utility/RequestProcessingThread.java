package serverModule.utility;

import common.utility.Request;
import common.utility.Response;
import common.utility.User;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class RequestProcessingThread extends Thread{
    private RequestManager requestManager;
    private Request request;
    private InetAddress address;
    private int port;
    private DatagramSocket socket;

    public RequestProcessingThread(RequestManager requestManager, Request request, InetAddress address, int port, DatagramSocket socket) {
        this.requestManager = requestManager;
        this.request = request;
        this.address = address;
        this.port = port;
        this.socket = socket;
    }

    @Override
    public void run() {
        Response response = requestManager.manage(request);
        new ResponseSenderThread(response, address, port, socket).start();
    }
}
