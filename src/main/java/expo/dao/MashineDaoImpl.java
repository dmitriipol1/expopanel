package expo.dao;

import expo.model.Mashine;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

@Component
public class MashineDaoImpl implements MashineDao {


    public boolean uploadDir(Mashine mashine, File path) {
        return false;
    }

    public boolean clearDir(Mashine mashine, File path) {
        return false;
    }

    public List<Mashine> pingList(List<Mashine> list) {
        list.forEach(m -> {
            try {
                InetAddress ip = InetAddress.getByName(m.getName());
                m.setOnline(ip.isReachable(100));
            } catch (IOException e) {
            }
        });
        return list;
    }

    public List<Mashine> getAllMashines() {
        Mashine[] mashines = {new Mashine("1-1-1"), new Mashine("2-2-2")};
        return pingList(Arrays.asList(mashines));
    }
}
