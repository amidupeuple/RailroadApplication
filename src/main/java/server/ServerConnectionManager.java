package server;

import dto.RequestDTO;
import dto.ResponseDTO;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * This class defines non-blocking server - it can handle many connections from client in one thread. Except for
 * lifecycle support of connections with clients, this class calls appropriate service by clint's request. Service
 * receive as a parameter request from client and return required data for client.
 */
public class ServerConnectionManager {
    private static final Logger log = Logger.getLogger(ServerConnectionManager.class);

    static int SERVER_PORT = 8000;

    /**
     * Manage connections with clients
     */
    public static void connect() {
        log.debug("Start: connect()");

        ServerSocketChannel server = null;
        Selector selector = null;

        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress("localhost",SERVER_PORT));
            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            log.error("Exception: " + e);
        }

        log.debug("ServerSocketChannel is opened");

        while (true) {
            int readyChannels = 0;

            try {
                readyChannels = selector.select(5000);
            } catch (IOException e) {
                log.error("Exception: " + e);
            }

            if (readyChannels == 0) continue;

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    SocketChannel client;

                    try {
                        client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    } catch (IOException e) {
                        log.error("Can't accept incoming connection: " + e);
                    }

                    continue;
                }

                if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();

                    //Receive data from client
                    try {
                        ByteBuffer buf = ByteBuffer.allocate(2048);
                        client.read(buf);
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf.array());
                        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                        RequestDTO reqObj = (RequestDTO) objectInputStream.readObject();
                        log.debug("Data from client received");

                        log.debug("Start required service");
                        //Analyse and execute needed service
                        ResponseDTO respObject = Service.execute(reqObj);
                        log.debug("Finish required service");

                        //Send data to client
                        try {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                            objectOutputStream.writeObject(respObject);
                            objectOutputStream.flush();
                            client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
                        } catch (IOException e) {
                            log.error(e);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    key.cancel();

                    continue;
                }

                /*
                if (key.isWritable()) {
                    SocketChannel client = (SocketChannel) key.channel();

                    try {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeObject(new ResponseDTO(1, "Server: required data"));
                        System.out.println("Server: required data");
                        objectOutputStream.flush();
                        client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    key.cancel();
                    continue;
                }*/
            }
        }
    }

    public static void main(String[] args) {
        connect();
    }
}
