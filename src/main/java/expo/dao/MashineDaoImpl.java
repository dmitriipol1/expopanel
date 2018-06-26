package expo.dao;

import expo.model.Mashine;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Component
public class MashineDaoImpl implements MashineDao {
    private Logger logger = Logger.getLogger(MashineDaoImpl.class.getName());
    private final String dest = "/Expo/";
    private static List<Mashine> mashines = new CopyOnWriteArrayList<>();
    private ExecutorService service = Executors.newCachedThreadPool();

    {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("D://serverList.txt"));
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
        if (stringBuilder.length() < 1)
            stringBuilder.append("нет ножек - нет мультиков<br> все оффлайн");
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
            while (true) {
                mashines.parallelStream().forEach(m -> {
                    logger.info("pinging..." + m.getName());
                    m.setOnline(new File("//" + m.getName() + "/Expo").exists());
                });
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    logger.info(e.getMessage());
                }
            }
        });
    }

    public boolean uploadModules(Mashine sourceSrv, Mashine destinationServer) {
        if (new File("//" + destinationServer.getName() + dest).exists()) {
            Future call = service.submit(() -> {
                try {
                    //modules copy
                    File source = new File("//" + sourceSrv.getName() + "/ExpoData/_hronomapper/modules");
                    File destin = new File("//" + destinationServer.getName() + dest + "modules");
                    copyDir(source, destin);

                    // slave copy
                    String fileName;
                    if (destinationServer.isKinect)
                        fileName = "_slave_kinect.v4p";
                    else
                        fileName = "_slave.v4p";
                    source = new File("//" + sourceSrv.getName() + "/ExpoData/_hronomapper/" + fileName);
                    destin = new File("//" + destinationServer.getName() + dest + fileName);
                    if (source.exists()) {
                        if (!destin.exists())
                            if (!destin.createNewFile())
                                logger.severe("can`t create - " + source);
                        FileUtils.copyFile(source, destin);
                    } else logger.severe(source + " NOT Exist");

                } catch (IOException e) {
                    logger.severe(e.getMessage());
                    destinationServer.setModulesLoaded(0);
                    return false;
                }
                logger.info("Upload Modules to " + destinationServer.getName() + " - OK");
                destinationServer.setModulesLoaded(2);
                return true;
            });
            destinationServer.setModulesLoaded(1);
            try {
                return (boolean) call.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            logger.severe(destinationServer.getName() + " doesnt have Expo dir");
            destinationServer.setModulesLoaded(0);
            return false;
        }
        return false;
    }

    @Override
    public boolean uploadVVVV(Mashine sourceSrv, Mashine destinationServer) {
        if (new File("//" + destinationServer.getName() + dest).exists()) {
            service.submit(() -> {
                try {
                    File source = new File("//" + sourceSrv.getName() + "/ExpoData/vvvv");
                    File destination = new File("//" + destinationServer.getName() + dest + "vvvv");
                    copyDir(source, destination);
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                    destinationServer.setVVVVLoaded(0);
                }
                logger.info("Upload VVVV to " + destinationServer.getName() + " - OK");
                destinationServer.setVVVVLoaded(2);

            });
            destinationServer.setVVVVLoaded(1);
            return true;
        } else {
            logger.severe(destinationServer.getName() + " doesnt have Expo dir");
            destinationServer.setVVVVLoaded(0);
            return false;
        }
    }

    @Override
    public boolean uploadContent(Mashine sourceSrv, Mashine destinationServer) {
        if (new File("//" + destinationServer.getName() + dest).exists()) {
            service.submit(() -> {
                try {
                    File source = new File("//" + sourceSrv.getName() + "/ExpoData/content/" + destinationServer.getName());
                    File destin = new File("//" + destinationServer.getName() + dest + "content");
                    copyDir(source, destin);
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                    destinationServer.setContentLoaded(0);
                }
                logger.info("Upload Content to " + destinationServer.getName() + "  - OK");
                destinationServer.setContentLoaded(2);
            });
            destinationServer.setContentLoaded(1);
            return true;
        } else {
            logger.severe(destinationServer.getName() + " doesnt have Expo dir");
            destinationServer.setContentLoaded(0);
            return false;
        }
    }

    private void copyDir(File source, File destination) throws IOException {
        if (source.exists()) {
            if (!destination.exists()) {
                if (!destination.getParentFile().mkdirs())
                    throw new IOException("cannot create Dirs in " + destination);
            } else FileUtils.cleanDirectory(destination);
            FileUtils.copyDirectory(source, destination, false);
        } else throw new IOException("no files in SOURCE");
    }

    @Override
    public boolean uploadAll(Mashine sourceSrv, Mashine destinationServer) {
        return uploadModules(sourceSrv, destinationServer) && uploadVVVV(sourceSrv, destinationServer) && uploadContent(sourceSrv, destinationServer);
    }

    @Override
    public void addNewServer(Mashine newServer) {
        if (mashines.stream().noneMatch(s -> s.getName().equals(newServer.getName()))) {
            try {
                newServer.setOnline(InetAddress.getByName(newServer.getName()).isReachable(100));
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
            mashines.add(newServer);
        }
    }

    @Override
    public void deleteSrv(String srvName) {
        mashines.removeIf(m -> m.getName().equals(srvName));
    }

    @Override
    public void setKinect(String srvName) {
        Mashine srv = mashines.stream().filter(s -> s.getName().equals(srvName)).findFirst().orElse(null);
        srv.setKinect(!Objects.requireNonNull(srv).isKinect);
    }
}
