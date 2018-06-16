package expo.dao;

import expo.model.Mashine;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Component
public class MashineDaoImpl implements MashineDao {
    private Logger logger = Logger.getLogger(MashineDaoImpl.class.getName());
    private final String dest = "/Expo/";
    private static List<Mashine> mashines = new ArrayList<>();
    private ExecutorService service = Executors.newCachedThreadPool();

    {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("e://serverList.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (scanner != null) {
            while (scanner.hasNext()) {
                String[] srv = scanner.nextLine().trim().split(" ");
                if (srv.length > 1 && srv[1].equals("k"))
                    mashines.add(new Mashine(srv[0], true));
                else
                    mashines.add(new Mashine(srv[0]));
            }
        }
        if (scanner != null) {
            scanner.close();
        }
        pingList();
    }

    public String backupLayoutJSON(Mashine target) {
        StringBuilder stringBuilder = new StringBuilder();
        AtomicBoolean flag = new AtomicBoolean(true);
        mashines.stream().filter(Mashine::isOnline).forEach(server -> {
            File source = new File("//" + server.getName() + "/Expo//modules/_layout.json");
            if (source.exists()) {
                File dest = new File("//" + target.getName() + "/ExpoBackup/" + server.getName() + "/_layout.json");
                if (!dest.exists()) {
                    try {
                        if (!dest.getParentFile().mkdirs() && dest.createNewFile())
                            throw new IOException("cannot create dirs and file");
                    } catch (IOException e) {
                        logger.severe(e.getMessage());
                        flag.set(false);
                    }
                }
                try {
                    copyFileUsingChannel(source, dest);
                    stringBuilder.append(server.getName()).append(" -ok<br>");
                } catch (IOException e) {
                    logger.severe(e.getMessage());
                    flag.set(false);
                }
            } else {
                stringBuilder.append("no JSON in ").append(server.getName()).append("<br>");
                logger.warning("no JSON in " + server.getName());
            }
        });
        return stringBuilder.toString();
    }

    private static void copyFileUsingChannel(File source, File dest) throws IOException {
        try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
             FileChannel destChannel = new FileOutputStream(dest).getChannel()) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
    }

    public List<Mashine> getAllMashines(boolean showOnline) {
        if (showOnline) {
            return mashines.stream().filter(Mashine::isOnline).collect(Collectors.toList());
        } else {
            return mashines;
        }
    }

    private void pingList() {
        service.submit(() -> {
            MashineDaoImpl.mashines.parallelStream().forEach(m -> {
                try {
                    InetAddress ip = InetAddress.getByName(m.getName());
                    m.setOnline(ip.isReachable(10));
                } catch (UnknownHostException ignored) {
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.info(e.getMessage());
            }
        });
    }

    public boolean uploadModules(Mashine target, Mashine server) {
        if (new File("//" + server.getName() + dest).exists()) {
            service.submit(() -> {
                try {
                    File targ = new File("//" + target.getName() + "/Expo/_hronomapper/modules");
                    File destin = new File("//" + server.getName() + dest + "modules");
                    if (targ.exists())
                        if (!destin.exists())
                            if (destin.getParentFile().mkdirs()) {
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
                                        if (destin.createNewFile())
                                            FileUtils.copyFile(targ, destin);
                                } else logger.severe(targ + " NOT Exist");
                            } else
                                throw new IOException("cannot create Dirs");
                } catch (IOException e) {
                    logger.severe(e.getMessage());
                    server.setModulesLoaded(0);
                }
                logger.info("Upload Modules to " + server.getName() + " - OK");
                server.setModulesLoaded(2);
            });
            server.setModulesLoaded(1);
            return true;
        } else return false;
    }

    @Override
    public boolean uploadVVVV(Mashine target, Mashine server) {
        if (new File("//" + server.getName() + dest).exists()) {
            service.submit(() -> {
                try {
                    File targ = new File("//" + target.getName() + "/Expo/vvvv");
                    File destin = new File("//" + server.getName() + dest + "vvvv");
                    copyDir(targ, destin);
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                    server.setVVVVLoaded(0);
                }
                logger.info("Upload VVVV to " + server.getName() + " - OK");
                server.setVVVVLoaded(2);

            });
            server.setVVVVLoaded(1);
            return true;
        } else return false;
    }

    @Override
    public boolean uploadContent(Mashine target, Mashine server) {
        if (new File("//" + server.getName() + dest).exists()) {
            service.submit(() -> {
                try {
                    File targ = new File("//" + target.getName() + "/Expo/content/" + server.getName());
                    File destin = new File("//" + server.getName() + dest + "content");
                    copyDir(targ, destin);
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                    server.setContentLoaded(0);
                }
                logger.info("Upload Content to " + server.getName() + "  - OK");
                server.setContentLoaded(2);
            });
            server.setContentLoaded(1);
            return true;
        } else return false;
    }

    private void copyDir(File targ, File destin) throws IOException {
        if (targ.exists())
            if (!destin.exists())
                if (destin.getParentFile().mkdirs()) {
                    FileUtils.copyDirectory(targ, destin, false);
                } else
                    throw new IOException("cannot create Dirs");
    }

    @Override
    public boolean uploadAll(Mashine target, Mashine server) {
        return uploadModules(target, server) && uploadVVVV(target, server) && uploadContent(target, server);
    }

    @Override
    public void addNewServer(Mashine server) {
        try {
            server.setOnline(InetAddress.getByName(server.getName()).isReachable(10));
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
        mashines.add(server);
    }

    @Override
    public void deleteSrv(String name) {
        mashines.removeIf(m -> m.getName().equals(name));
    }

    @Override
    public void setKinect(String name) {
        Mashine srv = mashines.stream().filter(s -> s.getName().equals(name)).findFirst().get();
        srv.setKinect(!srv.isKinect);
    }
}
