package hu.bme.onlab.interactor;

public class ApiCallCompletedEvent<S> {
    private int code;
    private Throwable throwable;
    private S response;

    public ApiCallCompletedEvent(int code, S response) {
        this.code = code;
        this.response = response;
    }

    public ApiCallCompletedEvent(Throwable throwable) {
        this.throwable = throwable;
    }

    public int getCode() {
        return code;
    }
    public Throwable getThrowable() {
        return throwable;
    }
    public S getResponse() {
        return response;
    }
}
