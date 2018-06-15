package expo.dao;

import expo.model.Mashine;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
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

    public boolean backupLayoutJSON(Mashine target, List<Mashine> serverList) {
        final boolean[] error = {false};

        serverList.forEach(s -> {
            File source = new File("//" + s + "/Expo//modules/_layout.json");
            File dest = new File("D:/ExpoBackup/" + s + "/_layout.json");
            if (source.exists()) {
                if (!dest.exists()) {
                    try {
                        dest.getParentFile().mkdirs();
                        dest.createNewFile();
                    } catch (IOException e) {
                        error[0] = true;
                    }
                }
                try {
                    copyFileUsingChannel(source, dest);
                } catch (IOException e) {
                    error[0] = true;
                }
            }
        });
        return error[0];
    }


    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
             FileChannel destChannel = new FileOutputStream(dest).getChannel()) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            System.out.println(source + " - OK");
        }
    }


    public List<Mashine> pingList(List<Mashine> list) {
        list.forEach(m -> {
            try {
                InetAddress ip = InetAddress.getByName(m.getName());
                m.setOnline(ip.isReachable(100));
            } catch (IOException ignored) {
            }
        });
        return list;
    }

    public List<Mashine> getAllMashines() {
        Mashine[] mashines = {new Mashine("1-1-1"), new Mashine("2-2-2")};
        return pingList(Arrays.asList(mashines));
    }
}
