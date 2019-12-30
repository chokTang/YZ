package event;

public class BaseEvent {
    protected int eventId;
    protected Object data;

    public BaseEvent() {
        this(0);
    }

    public BaseEvent(int eventId) {
        this(eventId,null);
    }

    public BaseEvent(int eventId, Object data) {
        this.eventId = eventId;
        this.data = data;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

