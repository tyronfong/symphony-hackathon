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

        Room room = new Room();
        room.setName("TestRoom2");
        room.setDescription("TestDesc");
        room.setDiscoverable(true);
        room.setPublic(true);
        room.setViewHistory(true);
        room.setCopyProtected(false);
        room.setCrossPod(false);
        room.setMembersCanInvite(true);
        room.setReadOnly(false);

        RoomInfo roomInfo = botClient.getStreamsClient().createRoom(room);

//        OutboundMessage outboundMessage = new OutboundMessage();
//        outboundMessage.setMessage("tyron test again");
//        InboundMessage inboundMessage = botClient.getMessagesClient().sendMessage("aPpc6mwkOarAEKgLbKtZxH___pxF9yH5dA", outboundMessage);

        System.out.println("here");
    }

    public static void sendMsgToRoom() {

    }
}
