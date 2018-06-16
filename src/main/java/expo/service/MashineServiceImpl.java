package expo.service;

import expo.dao.MashineDaoImpl;
import expo.model.Mashine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MashineServiceImpl implements MashineService {

    private final
    MashineDaoImpl mashineDao;

    @Autowired
    public MashineServiceImpl(MashineDaoImpl mashineDao) {
        this.mashineDao = mashineDao;
    }

    public List<Mashine> getAllMashines(boolean showOnline) {
        return mashineDao.getAllMashines(showOnline);
    }

    @Override
    public boolean uploadModules(Mashine target, String name) {
        Mashine server = mashineDao.getAllMashines(true).stream().filter(m -> m.getName().equals(name)).findFirst().get();
        return mashineDao.uploadModules(target, server);
    }

    @Override
    public boolean uploadContent(Mashine target, String name) {
        Mashine server = mashineDao.getAllMashines(true).stream().filter(m -> m.getName().equals(name)).findFirst().get();
        return mashineDao.uploadContent(target, server);
    }

    @Override
    public boolean uploadVVVV(Mashine target, String name) {
        Mashine server = mashineDao.getAllMashines(true).stream().filter(m -> m.getName().equals(name)).findFirst().get();
        return mashineDao.uploadVVVV(target, server);
    }

    @Override
    public boolean uploadAll(Mashine target, String name) {
        Mashine server = mashineDao.getAllMashines(true).stream().filter(m -> m.getName().equals(name)).findFirst().get();
        return mashineDao.uploadAll(target, server);
    }

    @Override
    public void addNewServer(Mashine server) {
        mashineDao.addNewServer(server);
    }

    @Override
    public String backup(Mashine target) {
        return mashineDao.backupLayoutJSON(target);
    }

    @Override
    public void deleteSrv(String name) {
        mashineDao.deleteSrv(name);
    }

    @Override
    public void setKinect(String name) {
        mashineDao.setKinect(name);
    }
}
