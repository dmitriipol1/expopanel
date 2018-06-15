package expo.dao;


import expo.model.Mashine;

import java.io.File;
import java.util.List;

public interface MashineDao {

    boolean backupLayoutJSON(Mashine target, List<Mashine> serverList);

    List<Mashine> pingList(List<Mashine> list);

    List<Mashine> getAllMashines(boolean showOnline);

    boolean uploadModules(Mashine target, Mashine server);

    boolean uploadVVVV(Mashine target, Mashine server);

    boolean uploadContent(Mashine target, Mashine server);

    boolean uploadAll(Mashine target, Mashine server);

    void addNewServer(Mashine server);
}
