package client;

import protocol.ScheduleObject;
import protocol.*;
import static protocol.Constants.POINT_OF_REFERENCE;
import static protocol.Constants.HOUR;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.Timestamp;

/**
 * This class defines how client manages the connection with db: establishing connection with db, send request
 * for some data, receive response from db with required data, close connection.
 */
public class ClientConnectionManager {
    static int SERVER_PORT = 8000;

    /**
     * Method set connection with db to send and receive some data.
     * @param data - data to send to db
     * @throws IOException
     * @throws InterruptedException
     */
    public static void connect(RequestObject data) {
        SocketChannel channel = null;
        ObjectOutputStream outputStream = null;

        try {
            channel = SocketChannel.open();
            channel.connect(new InetSocketAddress("localhost", SERVER_PORT));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(data);
            objectOutputStream.flush();
            channel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
            System.out.println("sent");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ByteBuffer buf = ByteBuffer.allocate(1024);
            channel.read(buf);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf.array());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            ResponseObject respObj = (ResponseObject) objectInputStream.readObject();
            System.out.println("received");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RequestObject data = new RequestObject(Constants.ClientService.getScheduleFromAtoB,
                                               new ScheduleObject(123,
                                                                "Псков",
                                                                "Москва",
                                                                new Timestamp(POINT_OF_REFERENCE),
                                                                new Timestamp(POINT_OF_REFERENCE + 23*HOUR),
                                                                0));
        connect(data);
    }
}
