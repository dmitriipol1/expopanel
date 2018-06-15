package expo.dao;


import expo.model.Mashine;

import java.io.File;
import java.util.List;

public interface MashineDao {

    boolean uploadDir(Mashine mashine, File path);
    boolean clearDir(Mashine mashine, File path);
    boolean backupLayoutJSON(Mashine target, List<Mashine> serverList);
    List<Mashine> pingList(List<Mashine> list);
    List<Mashine> getAllMashines();

}
