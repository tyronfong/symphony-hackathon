package apiclient;

import clients.ISymClient;
import clients.symphony.api.APIClient;
import clients.symphony.api.UsersClient;
import exceptions.*;
import model.ClientError;
import model.Query;
import model.UserInfo;
import model.UserInfoList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.NoContentException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class ExtentedUserClient extends UsersClient {
    private final Logger logger = LoggerFactory.getLogger(ExtentedUserClient.class);

    private ISymClient botClient;

    public ExtentedUserClient(ISymClient client) {
        super(client);
        this.botClient = client;
    }

    public List<UserInfo> searchUsers(String query) throws Exception {
        List<UserInfo> infoList = new ArrayList();
        if (query != null) {
            if (query.isEmpty()) {
                throw new NoContentException("No query sent for search");
            }

        }

        Response response = this.botClient.getPodClient()
                .target("https://" + this.botClient.getConfig().getPodHost() + ":" + this.botClient.getConfig().getPodPort()).path("/pod/v1/user/search")
                .request(new String[]{"application/json"})
                .header("sessionToken", this.botClient.getSymAuth().getSessionToken())
                .post(Entity.entity(new Query(query), "application/json"));
        if (response.getStatus() == 204) {
            return infoList;
        } else {
            if (response.getStatus() == 200) {
                UserInfoList userInfo = (UserInfoList)response.readEntity(UserInfoList.class);
                infoList = userInfo.getUsers();
            }

            if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
                try {
                    this.handleError(response, this.botClient);
                    return null;
                } catch (UnauthorizedException e) {
                    this.logger.error("Unauthorized Exception", e);
                }
            } else {
                return infoList;
            }
        }

        return infoList;
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
