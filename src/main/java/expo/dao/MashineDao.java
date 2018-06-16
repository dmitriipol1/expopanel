package expo.dao;


import expo.model.Mashine;

import java.util.List;

public interface MashineDao {

    String backupLayoutJSON(Mashine target);

    List<Mashine> getAllMashines(boolean showOnline);

    boolean uploadModules(Mashine target, Mashine server);

    boolean uploadVVVV(Mashine target, Mashine server);

    boolean uploadContent(Mashine target, Mashine server);

    boolean uploadAll(Mashine target, Mashine server);

    void addNewServer(Mashine server);

    void deleteSrv(String name);

    void setKinect(String name);
}
