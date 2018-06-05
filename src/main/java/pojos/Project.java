package pojos;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private String projectname;
    private String chatroomname;
    private String callbackurl;
    private boolean emailchathistory;
    private List<Event> events = new ArrayList<Event>();

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getChatroomname() {
        return chatroomname;
    }

    public void setChatroomname(String chatroomname) {
        this.chatroomname = chatroomname;
    }

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }

    public boolean isEmailchathistory() {
        return emailchathistory;
    }

    public void setEmailchathistory(boolean emailchathistory) {
        this.emailchathistory = emailchathistory;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
