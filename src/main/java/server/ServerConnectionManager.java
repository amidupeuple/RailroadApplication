package server;

import protocol.RequestObject;
import protocol.ResponseObject;
import server.Service;

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
 * This class defines non-blocking server - it can handles many connections from client in one thread. Except for
 * lifecycle support of connections with clients, this class calls appropriate service by clint's request. Service
 * receive as a parameter request from client and return required data for client.
 */
public class ServerConnectionManager {
    static int SERVER_PORT = 8000;

    /**
     * Manage connections with clients
     */
    public static void connect() {
        ServerSocketChannel server = null;
        Selector selector = null;

        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress("localhost",SERVER_PORT));
            selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            int readyChannels = 0;

            try {
                readyChannels = selector.select(5000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (readyChannels == 0) continue;

            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    SocketChannel client = null;

                    try {
                        client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    continue;
                }

                if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();

                    //Receive data from client
                    try {
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        client.read(buf);
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf.array());
                        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                        RequestObject reqObj = (RequestObject) objectInputStream.readObject();
                        System.out.println((String) reqObj.getObject());

                        //Analyse and execute needed service
                        ResponseObject respObject = Service.execute(reqObj);

                        //Send data to client
                        try {
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                            objectOutputStream.writeObject(respObject);
                            objectOutputStream.flush();
                            client.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
                        } catch (IOException e) {
                            e.printStackTrace();
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
                        objectOutputStream.writeObject(new ResponseObject(1, "Server: required data"));
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
