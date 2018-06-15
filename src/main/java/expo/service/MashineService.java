package expo.service;

import expo.model.Mashine;

import java.util.List;

public interface MashineService {

    public List<Mashine> getAllMashines(boolean showOnline);

    List<Mashine> pingList(List<Mashine> mashinesList);

    boolean uploadModules(Mashine target, Mashine server);

    boolean uploadContent(Mashine target, Mashine server);

    boolean uploadVVVV(Mashine target, Mashine server);

    boolean uploadAll(Mashine target, Mashine server);

    void addNewServer(Mashine server);
}
