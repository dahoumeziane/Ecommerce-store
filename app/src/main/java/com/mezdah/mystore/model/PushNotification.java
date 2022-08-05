package com.mezdah.mystore.model;

public class PushNotification {
    NotificationData data;
    String to ; // a topic , or a specific user

    public PushNotification(NotificationData data, String to) {
        this.data = data;
        this.to = to;
    }
}
