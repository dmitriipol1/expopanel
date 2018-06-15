package expo.dao;

import expo.model.Mashine;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Component
public class MashineDaoImpl implements MashineDao {
    private Logger logger = Logger.getLogger(MashineDaoImpl.class.getName());
    private final String dest = "/Expo/";
    private List<Mashine> mashines = new ArrayList<>();
    {
    mashines.add(new Mashine("1-1-1"));
    mashines.add(new Mashine("2-2-2", true));
    }


    public boolean backupLayoutJSON(Mashine target, List<Mashine> serverList) {
        final boolean[] error = {false};

        serverList.forEach(server -> {
            File source = new File("//" + server.getName() + "/Expo//modules/_layout.json");
            File dest = new File("//" + target + "/ExpoBackup/" + server.getName() + "/_layout.json");
            if (source.exists()) {
                if (!dest.exists()) {
                    try {
                        dest.getParentFile().mkdirs();
                        dest.createNewFile();
                    } catch (IOException e) {
                        logger.severe(e.getMessage());
                    }
                }
                try {
                    copyFileUsingChannel(source, dest);
                } catch (IOException e) {
                    logger.severe(e.getMessage());
                }
            }
        });
        return error[0];
    }


    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
             FileChannel destChannel = new FileOutputStream(dest).getChannel()) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
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

    public List<Mashine> getAllMashines(boolean showOnline) {
        if (showOnline) {
            return pingList(mashines).stream().filter(Mashine::isOnline).collect(Collectors.toList());
        } else {
            return pingList(mashines);
        }
    }

    public boolean uploadModules(Mashine target, Mashine server) {
        new Thread(() -> {
            try {
                File targ = new File("//" + target.getName() + "/Expo/_hronomapper/modules");
                File destin = new File("//" + server.getName() + dest+ "modules");
                if (targ.exists())
                    if (!destin.exists()) {
                        destin.getParentFile().mkdirs();
                    }
                FileUtils.copyDirectory(targ, destin, false);
                String fileName;
                if (server.isKinect)
                    fileName = "_slave_kinect.v4p";
                else
                    fileName = "_slave.v4p";
                targ = new File("//" + target.getName() + "/Expo/_hronomapper/" + fileName);
                destin = new File("//" + server.getName() + dest + fileName);
                if (targ.exists()) {
                    if (!destin.exists())
                        destin.createNewFile();
                    FileUtils.copyFile(targ, destin);
                } else {
                    logger.warning(targ + " NOT Exist");
                }
            } catch (IOException e) {
                logger.warning(e.getMessage());
            }finally {
                logger.info("Upload Modules to " + server.getName() + " - OK");
                server.setModulesLoaded(2);
            }
        }).start();
        server.setModulesLoaded(1);
        return true;
    }

    @Override
    public boolean uploadVVVV(Mashine target, Mashine server) {
        new Thread(() -> {
            try {
                File targ = new File("//" + target.getName() + "/Expo/vvvv");
                File destin = new File("//" + server.getName() + dest+ "vvvv");
                if (targ.exists())
                    if (!destin.exists()) {
                        destin.getParentFile().mkdirs();
                    }
                FileUtils.copyDirectory(targ, destin, false);
            } catch (IOException e) {
                logger.warning(e.getMessage());
            } finally {
                logger.info("Upload VVVV to " + server.getName() + " - OK");
                server.setVVVVLoaded(2);
            }
        }).start();
        server.setVVVVLoaded(1);
        return true;
    }

    @Override
    public boolean uploadContent(Mashine target, Mashine server) {
        new Thread(() -> {
            try {
                File targ = new File("//" + target.getName() + "/Expo/content/" + server.getName());
                File destin = new File("//" + server.getName() + dest+ "content");
                if (targ.exists())
                    if (!destin.exists()) {
                        destin.getParentFile().mkdirs();
                    }
                FileUtils.copyDirectory(targ, destin, false);
            } catch (IOException e) {
                logger.warning(e.getMessage());
            }finally {
                logger.info("Upload Content to " + server.getName() + "  - OK");
                server.setContentLoaded(2);
            }
        }).start();
        server.setContentLoaded(1);
        return true;
    }

    @Override
    public boolean uploadAll(Mashine target, Mashine server) {
        return uploadModules(target, server) && uploadVVVV(target, server) && uploadContent(target, server);
    }

    @Override
    public void addNewServer(Mashine server) {
        mashines.add(server);
    }
}
