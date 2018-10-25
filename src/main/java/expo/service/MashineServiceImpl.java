package expo.service;

import expo.dao.MashineDaoImpl;
import expo.model.Mashine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    public boolean uploadModules(Mashine source, String name) {
        Mashine server = mashineDao.getAllMashines(false).stream().filter(m -> m.getName().equals(name)).findFirst().get();
        return mashineDao.uploadModules(source, server);
    }

    @Override
    public boolean uploadContent(Mashine source, String name) {
        Mashine server = mashineDao.getAllMashines(false).stream().filter(m -> m.getName().equals(name)).findFirst().get();
        return mashineDao.uploadContent(source, server);
    }

    @Override
    public boolean uploadVVVV(Mashine source, String name) {
        Mashine server = mashineDao.getAllMashines(false).stream().filter(m -> m.getName().equals(name)).findFirst().get();
        return mashineDao.uploadVVVV(source, server);
    }

    @Override
    public boolean uploadAll(Mashine source, String name) {
        Mashine server = mashineDao.getAllMashines(true).stream().filter(m -> m.getName().equals(name)).findFirst().get();
        return mashineDao.uploadAll(source, server);
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


    //TODO Kill.bat
    public void kill() {
        Process p;
        try {
            p = new ProcessBuilder()
                    .inheritIO()
                    .command("powershell", "$user='Smart'; $pass='Expo'; $passwd=ConvertTo-SecureString -AsPlainText $pass -Force; $cred=New-Object System.Management.Automation.PSCredential -ArgumentList $user, $passwd; Invoke-command -ComputerName Crazz -ScriptBlock {Get-ChildItem C:\\} -credential $cred").start();
            p.waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getIsPinging() {
        return mashineDao.getIsPinging();
    }

    public void ping() {
        mashineDao.pingChange();
    }
}
