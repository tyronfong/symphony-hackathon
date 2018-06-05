package middleware;

import apiclient.PublicClient;
import model.RoomInfo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RandomMessageGenerator implements Runnable{

    private static PublicClient publicClient;

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
        while(true) {
            int n = rand.nextInt(message.length - 1) + 1;
            String msg = message[n];
            roomSet.keySet().stream().forEach(s -> {
                        List<RoomInfo> roomList = publicClient.searchRooms(s);
                        roomList.stream().forEach(roomInfo -> {
                            System.out.println("Posting message to room " + msg + " room name " + s);
                            this.publicClient.sendMsgToRoom(msg, roomInfo.getRoomSystemInfo().getId());
                        });
                    }
            );
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public PublicClient getPublicClient() {
        return publicClient;
    }

    public void setPublicClient(PublicClient publicClient) {
        this.publicClient = publicClient;
    }
}
