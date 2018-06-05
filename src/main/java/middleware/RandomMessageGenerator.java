package middleware;

import apiclient.PublicClient;
import model.RoomInfo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomMessageGenerator implements Runnable{

    private static PublicClient publicClient;
    private static AtomicInteger msgCounter = new AtomicInteger(0);

    public static Map<Integer, String> messageIdMap = new ConcurrentHashMap<>();

    Random rand = new Random();
    public static Map<String, String> roomSet = new ConcurrentHashMap();

    private static String[] message = new String []{
            "QPS is not reachable",
            "Sales is not able to see trade",
            "Blotter not refreshing",
            "Splunk report TimeoutExcetions",
            "Application is restarting",
            "QPS is not reachable",
            "Error error error",
            "Just for your information",
            ""
    };

    @Override
    public void run() {
        int i=0;
        while(i < 10) {
            int n = rand.nextInt(message.length - 1) + 1;
            String msg = message[n];
            roomSet.keySet().stream().forEach(s -> {
                        List<RoomInfo> roomList = publicClient.searchRooms(s);
                        roomList.stream().forEach(roomInfo -> {
                            System.out.println("Posting message to room " + msg + " room name " + s);
                            Integer intId = Integer.valueOf(msgCounter.incrementAndGet());
                            messageIdMap.put(intId, msg);
                            this.publicClient.sendMsgToRoom(intId + " - " + msg, roomInfo.getRoomSystemInfo().getId());
                        });
                    }
            );
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        i++;
    }

    public PublicClient getPublicClient() {
        return publicClient;
    }

    public void setPublicClient(PublicClient publicClient) {
        this.publicClient = publicClient;
    }


    public static String getMessageForId(Integer id) {
        return messageIdMap.get(id);
    }
}
