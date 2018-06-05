package pojos;

import pojos.temppojo.sme;

import java.util.List;

public class Event {
    private String eventname;
    private String Description;
    private String endpoint;
    private boolean destroyroomonincidentclose;

    private List<sme> smes;

    public List<sme> getSmes() {
        return smes;
    }

    public void setSmes(List<sme> smes) {
        this.smes = smes;
    }

    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public boolean isDestroyroomonincidentclose() {
        return destroyroomonincidentclose;
    }

    public void setDestroyroomonincidentclose(boolean destroyroomonincidentclose) {
        this.destroyroomonincidentclose = destroyroomonincidentclose;
    }
}
