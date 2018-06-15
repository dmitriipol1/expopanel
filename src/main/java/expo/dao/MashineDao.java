package expo.dao;


import expo.model.Mashine;

import java.io.File;
import java.util.List;

public interface MashineDao {

    public boolean uploadDir(Mashine mashine, File path);
    public boolean clearDir(Mashine mashine, File path);
    public List<Mashine> pingList(List<Mashine> list);
    public List<Mashine> getAllMashines();
}
