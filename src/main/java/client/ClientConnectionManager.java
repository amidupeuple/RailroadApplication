package client;

import dto.RequestDTO;
import dto.ResponseDTO;
import client.exception.ConnectToServerException;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * This class provides communication with server.
 */
public class ClientConnectionManager {
    private static final Logger log = Logger.getLogger(ClientConnectionManager.class);

    static int SERVER_PORT = 8000;

    /**
     * Method set connection with server to send and receive some data.
     * @param request - data to send to server
     * @return - response from server - state of executed service and some data, depending on service
     * @throws ConnectToServerException - if client can't make a connection with server
     */
    public static ResponseDTO connect(RequestDTO request) throws ConnectToServerException {
        SocketChannel channel = null;
        ObjectOutputStream outputStream = null;
        ResponseDTO respObj = null;

        try {
            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress("localhost", SERVER_PORT));

            log.debug("Connection to server has been establishing");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            channel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));

            log.debug("Request to server is made");
        } catch (IOException e) {
            log.error("Exception: sending data to server");
            throw new ConnectToServerException("Невозможно подключиться к серверу, повторите соединение позже");
        }

        try {
            ByteBuffer buf = ByteBuffer.allocate(2048);
            channel.read(buf);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf.array());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            respObj = (ResponseDTO) objectInputStream.readObject();

            log.debug("Server responce is received");
        } catch (Exception e) {
            log.error("Exception: receiving data from server");
            throw new ConnectToServerException("Невозможно подключиться к серверу, повторите соединение позже");
        }

        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return respObj;
        }
    }
}
