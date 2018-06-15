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
    public List<Mashine> pingList(List<Mashine> mashinesList) {
        return mashineDao.pingList(mashinesList);
    }

    @Override
    public boolean uploadModules(Mashine target, Mashine server) {
        return server.isOnline() && mashineDao.uploadModules(target, server);
    }

    @Override
    public boolean uploadContent(Mashine target, Mashine server) {
        return server.isOnline() && mashineDao.uploadContent(target, server);
    }

    @Override
    public boolean uploadVVVV(Mashine target, Mashine server) {
        return server.isOnline() && mashineDao.uploadVVVV(target, server);
    }

    @Override
    public boolean uploadAll(Mashine target, Mashine server) {
        return server.isOnline() && mashineDao.uploadAll(target, server);
    }

    @Override
    public void addNewServer(Mashine server) {
        mashineDao.addNewServer(server);
    }
}
