package expo.model;

public class Mashine {
    private final String name;
    private boolean isOnline;

    public Mashine(String name) {
        this.name = name;
        isOnline = false;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return isOnline;
    }
}
