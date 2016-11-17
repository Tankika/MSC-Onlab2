package hu.bme.onlab.network;

import hu.bme.onlab.model.user.User;

public abstract class NetworkSessionStore {

    private static User user;
    private static String sessionId;
    private static String xsrfToken;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        NetworkSessionStore.user = user;
    }

    public static String getSessionId() {
        return sessionId;
    }

    public static void setSessionId(String sessionId) {
        NetworkSessionStore.sessionId = sessionId;
    }

    public static String getXsrfToken() {
        return xsrfToken;
    }

    public static void setXsrfToken(String xsrfToken) {
        NetworkSessionStore.xsrfToken = xsrfToken;
    }
}
