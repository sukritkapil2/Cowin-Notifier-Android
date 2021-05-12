package com.cowicheck.cowinnotifier.Models;

import java.util.ArrayList;

public class CenterSession {
    public ArrayList<Session> sessions;
    public String center;

    public CenterSession(ArrayList<Session> sessions, String center) {
        this.sessions = sessions;
        this.center = center;
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }
}
