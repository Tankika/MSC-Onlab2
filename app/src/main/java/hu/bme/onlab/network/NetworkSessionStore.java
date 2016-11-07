package hu.bme.onlab.network;

public abstract class NetworkSessionStore {

    private static String sessionId;
    private static String xsrfToken;

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
