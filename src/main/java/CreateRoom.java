import apiclient.ExtentedMessagesClient;
import apiclient.ExtentedStreamsClient;
import apiclient.ExtentedUserClient;
import authentication.SymBotAuth;
import clients.SymBotClient;
import clients.symphony.api.StreamsClient;
import configuration.SymConfig;
import configuration.SymConfigLoader;
import listeners.IMListener;
import listeners.RoomListener;
import model.*;
import services.DatafeedEventsService;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class CreateRoom {
    public static void main(String[] args) throws Exception {
        new CreateRoom().testCreateRoom();
    }

    public void testCreateRoom() throws Exception {
        URL url = getClass().getResource("config.json");
        SymConfigLoader configLoader = new SymConfigLoader();
        SymConfig config = configLoader.loadFromFile(url.getPath());
        SymBotAuth botAuth = new SymBotAuth(config);
        botAuth.authenticate();
        SymBotClient botClient = SymBotClient.initBot(config, botAuth);

        ExtentedStreamsClient streamsClient = new ExtentedStreamsClient(botClient);
        ExtentedUserClient userClient = new ExtentedUserClient(botClient);
        ExtentedMessagesClient messagesClient = new ExtentedMessagesClient(botClient);

        //Room creation
//        Room room = new Room();
//        room.setName("TestRoom2");
//        room.setDescription("TestDesc");
//        room.setDiscoverable(true);
//        room.setPublic(true);
//        room.setViewHistory(true);
//        room.setCopyProtected(false);
//        room.setCrossPod(false);
//        room.setMembersCanInvite(true);
//        room.setReadOnly(false);
//
//        RoomInfo roomInfo = streamsClient.createRoom(room);


        //Send message to Room
//        OutboundMessage outboundMessage = new OutboundMessage();
//        outboundMessage.setMessage("tyron test again");
//        InboundMessage inboundMessage = botClient.getMessagesClient().sendMessage("aPpc6mwkOarAEKgLbKtZxH___pxF9yH5dA", outboundMessage);

//        streamsClient.addMemberToRoom("5aaumz1pzX67Cq3S6wDdU3___pw0RjKadA", 349026222342684L);
//        streamsClient.promoteUserToOwner("5aaumz1pzX67Cq3S6wDdU3___pw0RjKadA", 349026222342686L);
//        streamsClient.deactivateRoom("5aaumz1pzX67Cq3S6wDdU3___pw0RjKadA");
//        Thread.sleep(10*1000L);
//        streamsClient.removeMemberFromRoom("5aaumz1pzX67Cq3S6wDdU3___pw0RjKadA", 349026222342684L);
        List users = userClient.searchUsers("manish sharma");
//        List rooms = streamsClient.searchRooms("HSBC");
//        UserInfo userInfo = botClient.getUsersClient().getUserFromId(349026222342686L, false);

//        botClient.getPodClient()

        //Message client
//        List history = messagesClient.getAllHistoryMsgForRoom("aPpc6mwkOarAEKgLbKtZxH___pxF9yH5dA");



        System.out.println("he+re+".replace("+",""));
    }

    public static void sendMsgToRoom() {

    }
}
