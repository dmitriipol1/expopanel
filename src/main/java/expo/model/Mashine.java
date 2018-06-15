package expo.model;

public class Mashine {
    private String name;
    public boolean isKinect;
    private boolean isOnline;
    private int isContentLoaded;
    private int isVVVVLoaded;
    private int isModulesLoaded;

    public int isContentLoaded() {
        return isContentLoaded;
    }

    public void setContentLoaded(int contentLoaded) {
        isContentLoaded = contentLoaded;
    }

    public int isVVVVLoaded() {
        return isVVVVLoaded;
    }

    public void setVVVVLoaded(int VVVVLoaded) {
        isVVVVLoaded = VVVVLoaded;
    }

    public int isModulesLoaded() {
        return isModulesLoaded;
    }

    public void setModulesLoaded(int modulesLoaded) {
        isModulesLoaded = modulesLoaded;
    }

    public boolean isKinect() {
        return isKinect;
    }

    public Mashine(String name) {
        this.name = name;
        isOnline = false;
        isKinect = false;
        isContentLoaded = 0;
        isModulesLoaded = 0;
        isVVVVLoaded = 0;
    }

    public Mashine() {
    }

    public void setKinect(boolean kinect) {
        isKinect = kinect;
    }

    public Mashine(String name, boolean isKinect) {
        this.name = name;
        this.isKinect = isKinect;
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

    public void setName(String target) {
        name = target;
    }
}
