package pojos.temppojo;

import pojos.Event;

public class tempProject {
    private String projectname;
    private String chatroomname;
    private boolean emailchathistory;
    private String callbackurl;
    private Events Events;

    public void setEvents(Events events) {
        Events = events;
    }

    public Events getEvents() {
        return Events;
    }

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

    public boolean getEmailchathistory() {
        return emailchathistory;
    }

    public void setEmailchathistory(boolean emailchathistory) {
        this.emailchathistory = emailchathistory;
    }

    public String getCallbackurl() {
        return callbackurl;
    }

    public void setCallbackurl(String callbackurl) {
        this.callbackurl = callbackurl;
    }
}
