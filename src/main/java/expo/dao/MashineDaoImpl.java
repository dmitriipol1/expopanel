package expo.dao;

import expo.model.Mashine;
import jcifs.netbios.NbtAddress;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetAddress;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Component
public class MashineDaoImpl implements MashineDao {
    private Logger logger = Logger.getLogger(MashineDaoImpl.class.getName());
    private final String dest = "/Expo/";
    private static Map<String, Mashine> mashines = new ConcurrentHashMap<>();
    private ExecutorService service = Executors.newCachedThreadPool();
    private boolean isPinging = true;

    {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("D://srv.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (scanner != null) {
            while (scanner.hasNext()) {
                String[] srv = scanner.nextLine().trim().split(" ");
                if (srv.length > 1 && srv[1].equals("k"))
                    mashines.put(srv[0], new Mashine(srv[0], true));
                else
                    mashines.put(srv[0], new Mashine(srv[0]));
            }
        }
        if (scanner != null) {
            scanner.close();
        }
        init();
    }

    public String backupLayoutJSON(Mashine to) {
        StringBuilder stringBuilder = new StringBuilder();
        AtomicBoolean flag = new AtomicBoolean(true);
        mashines.values().stream().filter(Mashine::isOnline).forEach(server -> {
            File source = new File("//" + server.getIp() + "/Expo/modules/_layout.json");
            if (source.exists()) {
                File dest = new File("//" + to.getIp() + "/ExpoBackup/" + server.getName() + "/_layout.json");
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
            return mashines.values().stream().filter(Mashine::isOnline).collect(Collectors.toList());
        } else {
            return new ArrayList<>(mashines.values());
        }
    }

    private void pingList() {
        service.submit(() -> {
            while (isPinging) {
                mashines.values().parallelStream().forEach(m -> {
                    if (m.getIp() != null) {
                        logger.info("pinging..." + m.getName());
                        boolean isOnline = new File("//" + m.getIp() + "/Expo").exists();
                        if (m.isOnline() != isOnline)
                            m.setOnline(isOnline);
                    }
                });
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    logger.info(e.getMessage());
                }
            }
        });
    }


    private void init() {
        service.submit(() -> {
            jcifs.Config.setProperty("jcifs.netbios.wins", "192.168.1.220");
            for (int i = 4; i <= 5; i++) {
                int finalI = i;
                IntStream.rangeClosed(1, 254)
                        .mapToObj(num -> "192.168." + finalI + "." + num)
                        .parallel()
                        .forEach(addr -> {
                            try {
                                NbtAddress naddr = NbtAddress.getByName(String.valueOf(addr));
//                            if (naddr.isActive()) {
                                String name = naddr.getHostName();
                                logger.info("checking " + addr + " name: " + name);
                                if (!name.contains("."))
                                    if (mashines.containsKey(name)) {
                                        mashines.get(name).setIp(String.valueOf(addr));
                                        System.out.printf("found name: %s for ip: %s\n", addr, name);
                                    } else if (mashines.containsKey(name + "K")) {
                                        mashines.get(name + "K").setIp(String.valueOf(addr));
                                        System.out.printf("found name: %s for ip: %s\n", addr, name + "K");
                                    }
//                            }
                            } catch (IOException e) {
//                                e.printStackTrace();
                            }
                        });
            }
        });
        pingList();
    }


    public boolean uploadModules(Mashine from, Mashine to) {
        if (new File("//" + to.getIp() + dest).exists()) {
            Future call = service.submit(() -> {
                try {
                    //modules copy
                    File source = new File("//" + from.getName() + "/ExpoData/_hronomapper/modules");
                    File destin = new File("//" + to.getIp() + dest + "modules");
                    copyDir(source, destin);

                    // slave copy
                    String fileName;
                    if (to.isKinect)
                        fileName = "_slave_kinect.v4p";
                    else
                        fileName = "_slave.v4p";
                    source = new File("//" + from.getName() + "/ExpoData/_hronomapper/" + fileName);
                    destin = new File("//" + to.getIp() + dest + fileName);
                    if (source.exists()) {
                        if (!destin.exists())
                            if (!destin.createNewFile())
                                logger.severe("can`t create - " + source);
                        FileUtils.copyFile(source, destin);
                    } else logger.severe(source + " NOT Exist");

                } catch (IOException e) {
                    logger.severe(e.getMessage());
                    to.setModulesLoaded(0);
                    return false;
                }
                logger.info("Upload Modules to " + to.getName() + " - OK");
                to.setModulesLoaded(2);
                return true;
            });
            to.setModulesLoaded(1);
            try {
                return (boolean) call.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            logger.severe("Dest " + to.getName() + " doesnt have Expo dir");
            to.setModulesLoaded(0);
            return false;
        }
        return false;
    }

    @Override
    public boolean uploadVVVV(Mashine from, Mashine to) {
        if (new File("//" + to.getName() + dest).exists()) {
            service.submit(() -> {
                try {
                    File source = new File("//" + from.getName() + "/ExpoData/vvvv");
                    File destination = new File("//" + to.getIp() + dest + "vvvv");
                    copyDir(source, destination);
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                    to.setVVVVLoaded(0);
                }
                logger.info("Upload VVVV to " + to.getName() + " - OK");
                to.setVVVVLoaded(2);

            });
            to.setVVVVLoaded(1);
            return true;
        } else {
            logger.severe(to.getName() + " doesnt have Expo dir");
            to.setVVVVLoaded(0);
            return false;
        }
    }

    @Override
    public boolean uploadContent(Mashine from, Mashine to) {
        if (new File("//" + to.getIp() + dest).exists()) {
            service.submit(() -> {
                try {
                    File source;
                    if (to.isKinect)
                        source = new File("//" + from.getName() + "/ExpoData/content/" + to.getName() + "K/content");
                    else
                        source = new File("//" + from.getName() + "/ExpoData/content/" + to.getName() + "/content");
                    File destin = new File("//" + to.getIp() + dest + "content");
                    copyDir(source, destin);
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                    to.setContentLoaded(0);
                }
                logger.info("Upload Content to " + to.getName() + "  - OK");
                to.setContentLoaded(2);
            });
            to.setContentLoaded(1);
            return true;
        } else {
            logger.severe(to.getName() + " doesnt have Expo dir");
            to.setContentLoaded(0);
            return false;
        }
    }

    private void copyDir(File source, File destination) throws IOException {
        if (source.exists()) {
            if (!destination.exists()) {
                if (!destination.mkdirs())
                    throw new IOException("cannot create Dirs in " + destination);
            } else FileUtils.cleanDirectory(destination);
            FileUtils.copyDirectory(source, destination, false);
        } else throw new IOException("no files in SOURCE " + source);
    }

    @Override
    public boolean uploadAll(Mashine from, Mashine to) {
        return uploadModules(from, to) && uploadContent(from, to);
    }

    @Override
    public void addNewServer(Mashine newServer) {
        if (mashines.values().stream().noneMatch(s -> s.getName().equals(newServer.getName()))) {
            try {
                newServer.setOnline(InetAddress.getByName(newServer.getName()).isReachable(100));
            } catch (IOException e) {
                logger.info(e.getMessage());
            }
            mashines.put(newServer.getName(), newServer);
        }
    }

    @Override
    public void deleteSrv(String srvName) {
        mashines.remove(srvName);
    }

    @Override
    public void setKinect(String srvName) {
        Mashine srv = mashines.get(srvName);
        srv.setKinect(!Objects.requireNonNull(srv).isKinect);
    }

    public boolean getIsPinging() {
        return isPinging;
    }

    public void pingChange() {
        isPinging = !isPinging;
    }
}
