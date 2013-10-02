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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class defines non-blocking server - it can handle many connections from client in one thread. Except for
 * lifecycle support of connections with clients, this class calls appropriate service by clint's request. Service
 * receive as a parameter request from client and return required data for client.
 */
public class ServerConnectionManager {
    private static final Logger log = Logger.getLogger(ServerConnectionManager.class);

    static int SERVER_PORT = 8000;

    static ConcurrentHashMap<InetSocketAddress, ResponseDTO> responses = new ConcurrentHashMap<InetSocketAddress, ResponseDTO>();

    static ExecutorService executor = Executors.newFixedThreadPool(10);

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

                //log.debug("Next key in queue is selected");

                if (key.isAcceptable()) {
                    log.debug("Key is acceptable");

                    SocketChannel client;

                    try {
                        client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    } catch (IOException e) {
                        log.error("Can't accept incoming connection: " + e);
                    }

                    log.debug("Current socketChannel is registered");

                    continue;
                }

                if (key.isReadable()) {
                    log.debug("Key is readable");

                    SocketChannel client = (SocketChannel) key.channel();
                    RequestDTO reqObj = null;

                    //Receive data from client
                    try {
                        ByteBuffer buf = ByteBuffer.allocate(2048);
                        client.read(buf);
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf.array());
                        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                        reqObj = (RequestDTO) objectInputStream.readObject();
                    } catch (Exception e) {
                        log.error("Exception: " + e);
                    }

                    log.debug("Data from client successfully read");

                    InetSocketAddress tmpChannelId = null;
                    try {
                        tmpChannelId = (InetSocketAddress) client.getRemoteAddress();
                    } catch (IOException e) {
                        log.debug("Exception: " + e);
                    }

                    executor.execute(new ServiceWork(responses, reqObj, tmpChannelId));
                    log.debug("Thread with service executing is launched");

                    key.interestOps(SelectionKey.OP_WRITE);

                    log.debug("Reading is over. Change interest of current channel to OP_WRITE");
                    continue;
                }
                if (key.isWritable()) {
                    //log.debug("Key is writable");

                    SocketChannel client = (SocketChannel) key.channel();
                    ResponseDTO respObject = null;

                    InetSocketAddress tmpChannelID = null;
                    try {
                        tmpChannelID = (InetSocketAddress) client.getRemoteAddress();
                    } catch (IOException e) {
                        log.debug("Exception: " + e);
                    }

                    respObject = responses.get(tmpChannelID);

                    if (respObject == null) continue;

                    log.debug("There is some info fo current channel -> write");

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

                    log.debug("Writing is successful");

                    key.cancel();

                    continue;
                }
            }
        }
    }

    public static void main(String[] args) {
        connect();
    }

    /**
     * This class implements executing of requested service.
     */
    public static class ServiceWork implements Runnable {
        private ConcurrentHashMap<InetSocketAddress, ResponseDTO> responses;
        private RequestDTO request;
        private InetSocketAddress channelID;

        public ServiceWork(ConcurrentHashMap<InetSocketAddress, ResponseDTO> resp, RequestDTO req, InetSocketAddress id) {
            responses = resp;
            request = req;
            channelID = id;
        }

        @Override
        public void run() {
            ResponseDTO result = Service.execute(request);
            responses.put(channelID, result);
        }
    }

}
