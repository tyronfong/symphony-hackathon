import authentication.SymBotAuth;
import clients.SymBotClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.SymConfig;
import configuration.SymConfigLoader;
import listeners.IMListener;
import listeners.RoomListener;
import middleware.InitializeProjects;
import model.*;
import org.apache.log4j.BasicConfigurator;
import pojos.Event;
import pojos.Project;
import pojos.temppojo.sme;
import pojos.temppojo.tempProject;
import ratpack.handling.Context;
import ratpack.http.MutableHeaders;
import ratpack.http.Request;
import ratpack.http.Response;
import ratpack.jackson.Jackson;
import ratpack.server.RatpackServer;
import services.DatafeedEventsService;
import javax.ws.rs.core.NoContentException;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BotExample {
    private ObjectMapper mapper = new ObjectMapper();

    public static void main(String [] args) {
        BasicConfigurator.configure();
        BotExample botExample = new BotExample();
        BotExample app = new BotExample();
        botExample.startWebServer();
    }

    private void startWebServer() {
        try {
//            RatpackServer.start(server -> server
//                    .handlers(chain -> chain
//                            .get("getprojects", ctx -> ctx.render(ctx.get(String.class) + " 1"))
//                            .get("getprojects1", ctx -> ctx.render("Hello World!"))
//                            //.get(":name", ctx -> ctx.render("Hello " + ctx.getPathTokens().get("name") + "!"))
//                    )
//
//            );

            RatpackServer.start(
                    server -> server
                            .handlers(chain -> chain
                                    .all(ctx -> {
                                        MutableHeaders headers = ctx.getResponse().getHeaders();
                                        headers.set("Access-Control-Allow-Origin", "*");
                                        headers.set("Access-Control-Allow-Headers", "x-requested-with, origin, content-type, accept");
                                        ctx.next();
                                    })
                            .get("allprojects",
                                    ctx -> ctx.render(Jackson.json(Jackson.json(InitializeProjects.getInstance().getProjects()))))
                            .post("addnewproject", this::handleNewProject)));

        } catch (Exception e) {
            System.out.println("Exception " + e);
            e.printStackTrace();
        }
    }

    private void handleNewProject(Context ctx) {
        Response response = ctx.getResponse();
        Request request = ctx.getRequest();

        request.getBody().then(
                 data -> {
                    String responseJson = data.getText();
                     System.out.println("recieved  " + responseJson);
                    // parse text with whatever you use, e.g. Jackson
                     tempProject newProject = mapper.readValue(responseJson, tempProject.class);

                     Project project = new Project();
                     project.setProjectname(newProject.getProjectname());
                     project.setCallbackurl(newProject.getCallbackurl());
                     project.setEmailchathistory(newProject.getEmailchathistory());
                     project.setChatroomname(newProject.getChatroomname());

                     Event event = new Event();
                     event.setEventname(newProject.getEvents().getEventname());
                     event.setDescription(newProject.getEvents().getDescription());
                     event.setDestroyroomonincidentclose(newProject.getEvents().getDestroyroomonincidentclose());

                     List<sme> smes = new ArrayList<>();
                     sme sme1 = new sme();
                     sme sme2 = new sme();
                     sme sme3 = new sme();
                     sme sme4 = new sme();

                     sme1.setEmail(newProject.getEvents().getSme1());
                     sme2.setEmail(newProject.getEvents().getSme2());
                     sme3.setEmail(newProject.getEvents().getSme3());
                     sme4.setEmail(newProject.getEvents().getSme4());

                     smes.add(sme1);
                     smes.add(sme2);
                     smes.add(sme3);
                     smes.add(sme4);

                     event.setSmes(smes);
                     project.setEvents(Arrays.asList(event));

                    System.out.println("Project Name " + newProject.getProjectname());
                    System.out.println("Chat room name " + newProject.getChatroomname());
                    System.out.println("Call back url " + newProject.getCallbackurl());

                    InitializeProjects.getInstance().addProject(project);

                    response.send("added");
        });
    }


    public BotExample() {
        URL url = getClass().getResource("config.json");
        SymConfigLoader configLoader = new SymConfigLoader();
        SymConfig config = configLoader.loadFromFile(url.getPath());
        SymBotAuth botAuth = new SymBotAuth(config);
        botAuth.authenticate();
        SymBotClient botClient = SymBotClient.initBot(config, botAuth);
        DatafeedEventsService datafeedEventsService = botClient.getDatafeedEventsService();
        RoomListener roomListenerTest = new RoomListenerTestImpl(botClient);
        datafeedEventsService.addRoomListener(roomListenerTest);
        IMListener imListener = new IMListenerImpl(botClient);
        datafeedEventsService.addIMListener(imListener);
        //createRoom(botClient);

    }

    private void createRoom(SymBotClient botClient){



        try {

            UserInfo userInfo = botClient.getUsersClient().getUserFromEmail("manuela.caicedo@example.com", true);
            //get user IM and send message
            String IMStreamId = botClient.getStreamsClient().getUserIMStreamId(userInfo.getId());
            OutboundMessage message = new OutboundMessage();
            message.setMessage("test IM");
            botClient.getMessagesClient().sendMessage(IMStreamId,message);

            Room room = new Room();
            room.setName("test room preview");
            room.setDescription("test");
            room.setDiscoverable(true);
            room.setPublic(true);
            room.setViewHistory(true);
            RoomInfo roomInfo = null;
            roomInfo = botClient.getStreamsClient().createRoom(room);
            botClient.getStreamsClient().addMemberToRoom(roomInfo.getRoomSystemInfo().getId(),userInfo.getId());

            Room newRoomInfo = new Room();
            newRoomInfo.setName("test generator");
            botClient.getStreamsClient().updateRoom(roomInfo.getRoomSystemInfo().getId(),newRoomInfo);

            List<RoomMember> members =  botClient.getStreamsClient().getRoomMembers(roomInfo.getRoomSystemInfo().getId());

            botClient.getStreamsClient().promoteUserToOwner(roomInfo.getRoomSystemInfo().getId(), userInfo.getId());

            botClient.getStreamsClient().deactivateRoom(roomInfo.getRoomSystemInfo().getId());


        } catch (NoContentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
