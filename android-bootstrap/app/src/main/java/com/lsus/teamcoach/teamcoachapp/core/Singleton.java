package com.lsus.teamcoach.teamcoachapp.core;


import java.util.ArrayList;

/**
 * Created by Don on 3/7/2015.
 */
public class Singleton {

    private static Singleton mInstance = null;

    private User currentUser;
    private String authToken;
    private ArrayList<Session> userSessions;
    private ArrayList<Drill> userDrills;
    private ArrayList<Team> userTeams;
    private ArrayList<CalendarEvent> userEvents;

    private Singleton(){
    }

    public static Singleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new Singleton();
        }
        return mInstance;
    }

    public void clear(){
        mInstance = null;
    }


    public String getToken(){
        return this.authToken;
    }

    public void setToken(String token){
        authToken = token;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(User currentUser){
        this.currentUser = currentUser;
    }

    public ArrayList<Session> getUserSessions() { return userSessions; }

    public void setUserSessions(ArrayList<Session> userSessions) { this.userSessions = userSessions; }

    public ArrayList<Team> getUserTeams() { return getCurrentUser().getTeams(); }

    public void setUserTeams(ArrayList<Team> userTeams) { getCurrentUser().setTeams(userTeams); }

    public ArrayList<CalendarEvent> getUserEvents() { return getCurrentUser().getEvents(); }

    public void setUserEvents(ArrayList<CalendarEvent> userEvents) { getCurrentUser().setEvents(userEvents); }

    //TODO Change this to match userTeams above!
    public ArrayList<Drill> getUserDrills() { return userDrills; }

    public void setUserDrills(ArrayList<Drill> userDrills) { this.userDrills = userDrills; }
}
