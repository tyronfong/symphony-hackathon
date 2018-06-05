package apiclient;

import clients.ISymClient;
import clients.symphony.api.MessagesClient;
import exceptions.*;
import model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.ArrayList;

public class ExtentedMessagesClient extends MessagesClient {
    private final Logger logger = LoggerFactory.getLogger(ExtentedMessagesClient.class);
    private ISymClient botClient;

    public ExtentedMessagesClient(ISymClient client) {
        super(client);
        this.botClient = client;
    }

    public List getAllHistoryMsgForRoom(String streamId) throws Exception {
        if(StringUtils.isEmpty(streamId)){
            throw new NoContentException("streamId should not be empty.");
        }
        return searchMessages("streamId:" + streamId.trim() + " AND fromDate:0");
    }

    public List searchMessages(String query) throws Exception {
        java.util.List<InboundMessage> msgList = new ArrayList();
        if (StringUtils.isEmpty(query))
        {
            throw new NoContentException("No query sent for search");
        }

        Response response = this.botClient.getAgentClient()
                .target("https://" + this.botClient.getConfig().getAgentHost() + ":" + this.botClient.getConfig().getAgentPort()).path("/agent/v1/message/search")
                .queryParam("query", query)
                .request(new String[]{"application/json"})
                .header("sessionToken", this.botClient.getSymAuth().getSessionToken())
                .header("keyManagerToken", this.botClient.getSymAuth().getKmToken())
                .get();
        if (response.getStatus() == 204) {
            return msgList;
        } else {
            if (response.getStatus() == 200) {
                InboundMessageList inboundMessages = response.readEntity(InboundMessageList.class);
                return inboundMessages;
            }

            if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                try {
                    this.handleError(response, this.botClient);
                    return null;
                } catch (UnauthorizedException e) {
                    this.logger.error("Unauthorized Exception", e);
                }
            } else {
                return msgList;
            }
        }

        return msgList;
    }

    void handleError(Response response, ISymClient botClient) throws SymClientException {
        try {
            ClientError error = (ClientError)response.readEntity(ClientError.class);
            if (response.getStatus() == 400) {
                this.logger.error("Client error occurred", error);
                throw new APIClientErrorException(error.getMessage());
            }

            if (response.getStatus() == 401) {
                this.logger.error("User unauthorized, refreshing tokens");
                botClient.getSymAuth().authenticate();
                throw new UnauthorizedException(error.getMessage());
            }

            if (response.getStatus() == 403) {
                this.logger.error("Forbidden: Caller lacks necessary entitlement.");
                throw new ForbiddenException(error.getMessage());
            }

            if (response.getStatus() == 500) {
                this.logger.error(error.getMessage());
                throw new ServerErrorException(error.getMessage());
            }
        } catch (Exception e) {
            this.logger.error("Unexpected error", e);
        }

    }
}
