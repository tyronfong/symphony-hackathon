package middleware;

import pojos.Event;
import pojos.Project;

import java.util.ArrayList;
import java.util.List;

public class InitializeProjects {
    private static InitializeProjects singleInstance;
    private List<Project> mockProject = new ArrayList<Project>();

    private InitializeProjects() {
        Project project1 = new Project();
        project1.setProjectname("HSBC Evolve");
        project1.setChatroomname("HSBC Evolve Events");
        project1.setEmailchathistory(true);
        project1.setCallbackurl("/hsbcevolvejira");

        Project project2 = new Project();
        project2.setProjectname("Trade booking service");
        project2.setChatroomname("Trade Booking Service Events");
        project1.setCallbackurl("/tbsjira");
        project2.setEmailchathistory(true);


        Project project3 = new Project();
        project3.setProjectname("Settlement");
        project3.setChatroomname("Settlement Events");
        project3.setEmailchathistory(true);
        project1.setCallbackurl("/settlementjira");


        Project project4 = new Project();
        project4.setProjectname("Market Data Control");
        project4.setChatroomname("Market Data Control Events");
        project4.setEmailchathistory(true);
        project1.setCallbackurl("/mdcjira");

        Event event1 = new Event();
        event1.setEndpoint("Event endpoint");
        event1.setEventname("Event name");
        event1.setDescription("description");
        event1.setDestroyroomonincidentclose(true);

        Event event2 = new Event();
        event2.setEndpoint("Event endpoint");
        event2.setEventname("event name");
        event2.setDescription("description");
        event2.setDestroyroomonincidentclose(true);

        Event event3 = new Event();
        event3.setEndpoint("Event endpoint");
        event3.setEventname("event name");
        event3.setDescription("description");
        event3.setDestroyroomonincidentclose(true);

        Event event4 = new Event();
        event4.setEndpoint("Event endpoint");
        event4.setEventname("event name");
        event4.setDescription("description");
        event4.setDestroyroomonincidentclose(true);

        List<Event> events = new ArrayList<Event>();
        events.add(event1);
        events.add(event2);
        //events.add(event3);
        //events.add(event4);

        project1.setEvents(events);
        project2.setEvents(events);
        project3.setEvents(events);
        project4.setEvents(events);

        mockProject.add(project1);
        mockProject.add(project2);
        //mockProject.add(project3);
        //mockProject.add(project4);
    }

    public static InitializeProjects getInstance() {
        if(singleInstance == null) {
            singleInstance = new InitializeProjects();
        }
        return singleInstance;
    }

    public List<Project> getProjects() {
        return mockProject;
    }

    public void addProject(Project project) {
        if(project.getChatroomname() != null
                && project.getProjectname() != null)
        mockProject.add(project);
    }
}