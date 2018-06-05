import apiclient.PublicClient;
import authentication.SymBotAuth;
import clients.SymBotClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import configuration.SymConfig;
import configuration.SymConfigLoader;
import listeners.IMListener;
import listeners.RoomListener;
import middleware.InitializeProjects;
import middleware.MessageProssesor;
import middleware.RandomMessageGenerator;
import model.*;
import org.apache.log4j.BasicConfigurator;
import pojos.Event;
import pojos.Message;
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
    private PublicClient publicClient;
    private ObjectMapper mapper = new ObjectMapper();

    public static void main(String [] args) {
        BasicConfigurator.configure();
        BotExample botExample = new BotExample();
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
                            .post("addnewproject", this::handleNewProject)
                            .post("processMessage", this::handleMessage)));

        } catch (Exception e) {
            System.out.println("Exception " + e);
            e.printStackTrace();
        }
    }

    private void handleMessage(Context ctx) {
        Response response = ctx.getResponse();
        Request request = ctx.getRequest();

        request.getBody().then(
                data -> {
                    String responseJson = data.getText();
                    System.out.println("recieved  " + responseJson);
                    // parse text with whatever you use, e.g. Jackson
                    Message message = mapper.readValue(responseJson, Message.class);
                    MessageProssesor.addMessage(message);
                    response.send("Message Processed!!");
                });
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

                    RoomInfo roomInfo = this.publicClient.createRoom(newProject.getChatroomname());

                     List<UserInfo> smesList = new ArrayList<>();

                 if(sme1.getEmail() != null && !sme1.getEmail().isEmpty()) {
                     smesList.addAll(this.publicClient.searchUsers(sme1.getEmail()));
                 }
                 if(sme2.getEmail() != null && !sme2.getEmail().isEmpty()) {
                     smesList.addAll(this.publicClient.searchUsers(sme2.getEmail()));
                 }
                 if(sme3.getEmail() != null && !sme3.getEmail().isEmpty()) {
                     smesList.addAll(this.publicClient.searchUsers(sme3.getEmail()));
                 }
                 if(sme4.getEmail() != null && !sme4.getEmail().isEmpty()) {
                     smesList.addAll(this.publicClient.searchUsers(sme4.getEmail()));
                 }

                 RandomMessageGenerator.roomSet.put(newProject.getProjectname(), newProject.getProjectname());

                 smesList.stream().forEach(userinfo -> {
                     this.publicClient.addMemberToRoom(userinfo.getId(), roomInfo.getRoomSystemInfo().getId());
                 });

                 this.publicClient.sendMsgToRoom("Welcome to " + newProject.getProjectname(), roomInfo.getRoomSystemInfo().getId());
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
        this.publicClient = new PublicClient(botClient);
        DatafeedEventsService datafeedEventsService = botClient.getDatafeedEventsService();
        RoomListener roomListenerTest = new RoomListenerTestImpl(botClient);
        datafeedEventsService.addRoomListener(roomListenerTest);
        IMListener imListener = new IMListenerImpl(botClient);
        datafeedEventsService.addIMListener(imListener);
        //createRoom(botClient);

        RandomMessageGenerator.roomSet.put("testroomname1", "testroomname1");
        RandomMessageGenerator randomMessageGenerator = new RandomMessageGenerator();
        randomMessageGenerator.setPublicClient(publicClient);

        MessageProssesor messageProssesor = new MessageProssesor();
        messageProssesor.setPublicClient(publicClient);

        Thread thread = new Thread(randomMessageGenerator);
        thread.start();

        Thread thread1 = new Thread(messageProssesor);
        thread1.start();
    }
}
