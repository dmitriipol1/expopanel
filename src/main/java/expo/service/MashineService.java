package expo.service;

import expo.model.Mashine;

import java.util.List;

public interface MashineService {

    public List<Mashine> getAllMashines(boolean showOnline);

    List<Mashine> pingList(List<Mashine> mashinesList);
}
