import clients.SymBotClient;
import listeners.RoomListener;
import middleware.RandomMessageGenerator;
import model.InboundMessage;
import model.OutboundMessage;
import model.Stream;
import model.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoomListenerTestImpl implements RoomListener {

    private SymBotClient botClient;

    public RoomListenerTestImpl(SymBotClient botClient) {
        this.botClient = botClient;
    }

    private final Logger logger = LoggerFactory.getLogger(RoomListenerTestImpl.class);


    public void onRoomMessage(InboundMessage inboundMessage) {
        OutboundMessage messageOut = new OutboundMessage();
        final String message = inboundMessage.getMessageText();

        if (message.startsWith("create jira")) {
            createJira(message.substring("create jira".length()).trim());
        }

        if (message.startsWith("close jira")) {
            closeJira(message.substring("close jira".length()).trim());
        }

        //messageOut.setMessage("Hi "+inboundMessage.getUser().getFirstName()+"!");
//        try {
//            this.botClient.getMessagesClient().sendMessage(inboundMessage.getStream().getStreamId(), messageOut);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void createJira (String message) {
        System.out.println("Create Jira Message " + message);
        String eventMessage = RandomMessageGenerator.getMessageForId(Integer.valueOf(message));
    }

    private void closeJira (String message) {
        System.out.println("Close Jira Message " + message);
        String eventMessage = RandomMessageGenerator.getMessageForId(Integer.valueOf(message));
    }

    public void onRoomCreated(RoomCreated roomCreated) {

    }

    public void onRoomDeactivated(RoomDeactivated roomDeactivated) {

    }

    public void onRoomMemberDemotedFromOwner(RoomMemberDemotedFromOwner roomMemberDemotedFromOwner) {

    }

    public void onRoomMemberPromotedToOwner(RoomMemberPromotedToOwner roomMemberPromotedToOwner) {

    }

    public void onRoomReactivated(Stream stream) {

    }

    public void onRoomUpdated(RoomUpdated roomUpdated) {

    }

    public void onUserJoinedRoom(UserJoinedRoom userJoinedRoom) {
        OutboundMessage messageOut = new OutboundMessage();
        messageOut.setMessage("Welcome "+userJoinedRoom.getAffectedUser().getFirstName()+"!");
        try {
            this.botClient.getMessagesClient().sendMessage(userJoinedRoom.getStream().getStreamId(), messageOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onUserLeftRoom(UserLeftRoom userLeftRoom) {

    }
}
