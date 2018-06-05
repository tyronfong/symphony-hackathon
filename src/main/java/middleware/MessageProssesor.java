package middleware;

import apiclient.PublicClient;
import model.RoomInfo;
import pojos.Message;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageProssesor implements Runnable{
    private static BlockingQueue<Message> queue = new ArrayBlockingQueue(1024);
    private static PublicClient publicClient;

    @Override
    public void run() {
        while (true) {
            try {
                Message msg = queue.take();

                List<RoomInfo> roomList = publicClient.searchRooms(msg.getRoomName());
                roomList.stream().forEach(roomInfo -> {
                    System.out.println("Proccessing message  " + msg);
                    this.publicClient.sendMsgToRoom(msg.getMessage(), roomInfo.getRoomSystemInfo().getId());
                });
                //Sleep after process one message
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addMessage(Message msg) {
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public PublicClient getPublicClient() {
        return publicClient;
    }

    public void setPublicClient(PublicClient publicClient) {
        this.publicClient = publicClient;
    }
}
