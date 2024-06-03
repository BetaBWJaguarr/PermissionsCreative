package beta.com.permissionscreative.object;

public class EventsType {
    private final String action;
    private final String message;

    public EventsType(String action, String message) {
        this.action = action;
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }
}