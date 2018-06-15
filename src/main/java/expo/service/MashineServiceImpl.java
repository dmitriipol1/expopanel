package expo.service;

import expo.dao.MashineDaoImpl;
import expo.model.Mashine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MashineServiceImpl implements MashineService {

    private final
    MashineDaoImpl mashineDao;

    @Autowired
    public MashineServiceImpl(MashineDaoImpl mashineDao) {
        this.mashineDao = mashineDao;
    }

    public List<Mashine> getAllMashines(boolean showOnline) {
        if (showOnline) {
            return mashineDao.getAllMashines().stream().filter(Mashine::isOnline).collect(Collectors.toList());
        } else
            return mashineDao.getAllMashines();
    }

    @Override
    public List<Mashine> pingList(List<Mashine> mashinesList) {
        return mashineDao.pingList(mashinesList);
    }
}
