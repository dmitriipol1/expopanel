package expo.service;

import expo.model.Mashine;

import java.util.List;

public interface MashineService {

    public List<Mashine> getAllMashines(boolean showOnline);

    boolean uploadModules(Mashine target, String name);

    boolean uploadContent(Mashine target, String name);

    boolean uploadVVVV(Mashine target, String name);

    boolean uploadAll(Mashine target, String name);

    void addNewServer(Mashine server);

    String backup(Mashine target);

    void deleteSrv(String name);

    void setKinect(String name);
}
