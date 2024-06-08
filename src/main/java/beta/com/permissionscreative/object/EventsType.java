package beta.com.permissionscreative.object;

/**
 * The EventsType class encapsulates the details of an event, specifically the action to be taken and the message associated with it.
 * This class serves as a simple data structure with getter methods to access the private final fields. It is typically
 * used to represent
 * and transport event-related data throughout an application, ensuring that both the action and message are tightly coupled and managed
 * together. This can be particularly useful in systems where events trigger specific actions and
 * corresponding messages need to be logged or communicated.
 */


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