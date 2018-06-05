package apiclient;

import clients.ISymClient;
import model.InboundMessage;
import model.OutboundMessage;
import model.Room;
import model.RoomInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PublicClient {
    private final Logger logger = LoggerFactory.getLogger(PublicClient.class);
    private ExtentedMessagesClient messagesClient;
    private ExtentedStreamsClient streamsClient;
    private ExtentedUserClient userClient;

    public PublicClient(ISymClient botClient) {
        this.messagesClient = new ExtentedMessagesClient(botClient);
        this.streamsClient = new ExtentedStreamsClient(botClient);
        this.userClient = new ExtentedUserClient(botClient);
    }

    public RoomInfo createRoom(String roomName) {
        Room room = new Room();
        room.setName(roomName);
        room.setDescription("TestDesc");
        room.setDiscoverable(true);
        room.setPublic(true);
        room.setViewHistory(true);
        room.setCopyProtected(false);
        room.setCrossPod(false);
        room.setMembersCanInvite(true);
        room.setReadOnly(false);
        try {
            return this.streamsClient.createRoom(room);
        } catch (Exception e) {
            logger.error("Failed to create new Room.", e);
        }
        return null;
    }

    public InboundMessage sendMsgToRoom(String msg, String streamId) {
        OutboundMessage outboundMessage = new OutboundMessage();
        outboundMessage.setMessage(msg);
        try {
            return messagesClient.sendMessage(streamId, outboundMessage);
        } catch (Exception e) {
            logger.error("Failed to send msg.", e);
        }

        return null;
    }

    public void addMemberToRoom(long userId, String streamId) {
        try {
            this.streamsClient.addMemberToRoom(streamId, userId);
        } catch (Exception e) {
            logger.error("Failed to add room member.", e);
        }
    }

    public void removeMemberFromRoom(long userId, String streamId) {
        try {
            this.streamsClient.removeMemberFromRoom(streamId, userId);
        } catch (Exception e) {
            logger.error("Failed to remove member from room.", e);
        }
    }

    public void deactiveRoom(String streamId) {
        try {
            this.streamsClient.deactivateRoom(streamId);
        } catch (Exception e) {
            logger.error("Failed to deactive room.", e);
        }
    }

    public List searchUsers(String query) {
        try {
            return this.userClient.searchUsers(query);
        } catch (Exception e) {
            logger.error("Failed to search users.", e);
        }
        return null;
    }

    public List searchRooms(String query) {
        try {
            return this.streamsClient.searchRooms(query);
        } catch (Exception e) {
            logger.error("Failed to search rooms.", e);
        }

        return null;
    }

    public List getChatHistory(String streamId) {
        try {
            return this.messagesClient.getAllHistoryMsgForRoom(streamId);
        } catch (Exception e) {
            logger.error("Failed to fetch history from room.", e);
        }

        return null;
    }
}
