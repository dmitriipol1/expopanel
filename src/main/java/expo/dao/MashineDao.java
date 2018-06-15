package expo.dao;


import expo.model.Mashine;

import java.io.File;
import java.util.List;

public interface MashineDao {

    boolean uploadDir(Mashine mashine, File path);
    boolean clearDir(Mashine mashine, File path);
    List<Mashine> pingList(List<Mashine> list);
    List<Mashine> getAllMashines();
}
