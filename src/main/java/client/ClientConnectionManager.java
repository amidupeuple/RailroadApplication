package client;

import dto.RequestDTO;
import dto.ResponseDTO;
import dto.ScheduleDTO;
import protocol.*;

import static protocol.Constants.HOUR;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.Time;

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
    public static ResponseDTO connect(RequestDTO data) {
        SocketChannel channel = null;
        ObjectOutputStream outputStream = null;
        ResponseDTO respObj = null;

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
            ByteBuffer buf = ByteBuffer.allocate(2048);
            channel.read(buf);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf.array());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            respObj = (ResponseDTO) objectInputStream.readObject();
            System.out.println("received");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return respObj;
        }
    }

    public static void main(String[] args) {
        RequestDTO data = new RequestDTO(Constants.ClientService.getScheduleFromAtoB,
                                               new ScheduleDTO(123,
                                                                "Псков",
                                                                "Москва",
                                                                new Time(HOUR),
                                                                new Time(23*HOUR),
                                                                0));
        connect(data);
    }
}
