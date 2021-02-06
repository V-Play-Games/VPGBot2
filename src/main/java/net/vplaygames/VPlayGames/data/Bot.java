/*
 * Copyright 2020 Vaibhav Nargwani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.vplaygames.VPlayGames.data;

import com.vplaygames.PM4J.Logger;
import com.vplaygames.PM4J.caches.DataCache;
import com.vplaygames.PM4J.caches.PokemasDBCache;
import com.vplaygames.PM4J.util.Queueable;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.vplaygames.VPlayGames.commands.botStaff.ActivateCommand;
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.core.ICommand;
import net.vplaygames.VPlayGames.core.SplitStream;
import net.vplaygames.VPlayGames.processors.EventHandler;
import net.vplaygames.VPlayGames.util.Array;
import net.vplaygames.VPlayGames.util.MiscUtil;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static net.vplaygames.VPlayGames.data.GameData.*;

public class Bot {
    public static final Class<Bot> clazz = Bot.class;
    public static final String VERSION = "1.5.0b";
    public static final File logFile = new File("src/main/resources/logFile.txt");
    public static final File errorFile = new File("src/main/resources/errorFile.txt");
    public static final File damageData = new File("src/main/resources/damageData.txt");
    public static JDA jda;
    public static boolean closed = false;
    public static boolean rebooted = false;
    public static int syncCount = 0;
    public static long booted;
    public static final long SELF_USER_ID = 747328310828204143L;
    public static final long BOT_OWNER = 701660977258561557L;
    public static final long MISS_SHANAYA = 679625927453704203L;
    public static final long FLASH_TGB = 685365867160010836L;
    public static final long DEFAULT_LOG_CHANNEL_ID = 762950187492179995L;
    public static final long DEFAULT_SYNC_CHANNEL_ID = 762950187492179995L;
    public static final long[] botStaff = {BOT_OWNER, SELF_USER_ID, MISS_SHANAYA, FLASH_TGB};
    public static long messagesProcessed = 1;
    public static long lastExceptionID = 0;
    public static String timeAtBoot;
    public static String lastRefresh = "never";
    public static final String TOKEN = System.getenv("TOKEN");
    public static final String LOCATION = System.getenv("LOCATION");
    public static final String SELF_USER_TAG = "V Play Games#5877";
    public static final String PREFIX = "v!";
    public static final String SUPPORT_SERVER_INVITE = "https://discord.gg/amvPsGU";
    public static final String INVALID_INPUTS = "Invalid Amount of Inputs!";
    public static final String APP_NOT_STARTED = "Start a Pokemon Masters Damage Calculation App first!";
    public static final String BASE64 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-";
    public static TextChannel logChannel;
    public static TextChannel syncChannel;
    public static ScheduledThreadPoolExecutor timer;
    public static HashMap<Long, Damage> DATA = new HashMap<>();
    public static HashMap<String, Damage> DAMAGE_CODES = new HashMap<>();
    public static HashMap<String, ICommand> commands = new HashMap<>();
    public static ActivateCommand activateCommand;

    public static void init() {
        retry(PokemasDBCache.getInstance().process(), p -> logChannel.sendMessage("I am ready for anything!\n\t-Morty, Johto Gym Leader, 2020").queue());
        setLogChannel(jda.getTextChannelById(DEFAULT_LOG_CHANNEL_ID));
        setSyncChannel(jda.getTextChannelById(DEFAULT_SYNC_CHANNEL_ID));
        initSamples();
        syncData(damageData);
        startTimer();
        loadCommands();
        setDefaultActivity();
        setBooted();
        timeAtBoot = MiscUtil.dateTimeNow();
    }

    public static void loadCommands() {
        try (ScanResult scanResult = new ClassGraph().acceptPackages("net.vplaygames.VPlayGames").scan()) {
            HashMap<Class<?>, Throwable> errors = new HashMap<>();
            scanResult.getAllClasses()
                .stream()
                .filter(x -> !x.isAbstract() && !x.isInterface())
                .filter(x -> x.implementsInterface("net.vplaygames.VPlayGames.core.ICommand"))
                .forEach(x -> {
                    try {
                        Logger.log("Loaded "+x.loadClass().getConstructor().newInstance()+" Command\n", Logger.Mode.INFO, clazz);
                    } catch (Throwable e) {
                        errors.put(x.loadClass(), e);
                    }
                });
            errors.forEach((k, v) -> {
                Logger.log("Failed to load "+k.getSimpleName()+"\n", Logger.Mode.ERROR, clazz);
                v.printStackTrace();
            });
            System.out.println("All Commands Loaded!");
        }
    }

    public static void setBooted() {
        booted = System.currentTimeMillis();
    }

    public static void setLogChannel(TextChannel c) {
        logChannel = c;
    }

    public static void setSyncChannel(TextChannel c) {
        syncChannel = c;
    }

    public static boolean isStaff(long uid) {
        return Array.contains(uid, botStaff);
    }

    public static void initSamples() {
        Damage d = new Damage(SELF_USER_ID, SELF_USER_TAG, "SAMPLE");
        d.setTid(Array.returnID(trnrs, "red"))
                .setUc(tdabs[d.getTid() - 1])
                .setUid(d.getUc()[1])
                .setPid(d.getUc()[1] % 1000000)
                .setMSet(msets[d.getPid() % 1000 - 1])
                .setSmd(d.getMSet()[0] / 1000)
                .setMInfo(minfos[(d.getMSet()[0] - 1) % 1000])
                .setMoveName(moves[(d.getMSet()[0] - 1) % 1000])
                .setMod(2, 1)
                .setSml(5)
                .setStats(0, 1, 420)
                .setStats(1, 3, 57)
                .setBuffs(0, 1, 6)
                .setMod(0, 1)
                .setMod(1, 1)
                .setWthr(0)
                .enable()
                .updateAppStatus()
                .updateAppStatus()
                .updateAppStatus()
                .updateAppStatus();
    }

    public static void setDefaultActivity() {
        long memberCount = 0;
        for (Guild g : jda.getGuilds()) memberCount += g.getMemberCount();
        jda.getPresence().setActivity(Activity.playing("Version " + VERSION + " with " + memberCount + " people in " + jda.getGuilds().size() + " servers"));
    }

    public static void setCustomStreams() throws FileNotFoundException {
        if (rebooted) return;
        System.setOut(new SplitStream(new PrintStream(new FileOutputStream(logFile,   !LOCATION.equals("LOCAL"))), System.out));
        System.setErr(new SplitStream(new PrintStream(new FileOutputStream(errorFile, !LOCATION.equals("LOCAL"))), System.err));
        System.out.println("---- "+MiscUtil.dateTimeNow()+" ----");
        System.err.println("---- "+MiscUtil.dateTimeNow()+" ----");
    }

    public static void syncData(File file) {
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String data;
            while ((data = in.readLine()) != null && !data.equals("\\RAM\\")) {
                Damage d = Damage.parseFromString(data);
                DAMAGE_CODES.put(d.getCode(),d);
            }
            while ((data = in.readLine()) != null) {
                Damage d = Damage.parseFromString(data);
                DATA.put(d.getUserId(),d);
            }
        } catch (IOException e) {
            EventHandler.getInstance().process(e);
        }
    }

    public static void startTimer() {
        (timer = new ScheduledThreadPoolExecutor(1, r -> new Thread(r, "VPG Timer")))
            .scheduleWithFixedDelay(() -> {
                syncCount++;
                Logger.log("Syncing data [" + syncCount + "]\n", Logger.Mode.INFO, clazz);
                MiscUtil.writeDamageData();
                syncChannel.sendMessage("Sync [" + LOCATION + "] [" + syncCount + "]").queue();
                syncChannel.sendMessage("logFile").addFile(logFile).queue();
                syncChannel.sendMessage("errorFile").addFile(errorFile).queue();
                syncChannel.sendMessage("damageData").addFile(damageData).queue();
            },0,20, TimeUnit.MINUTES);
    }

    public static <T extends DataCache<T, ?>> void retry(Queueable<T> action, Consumer<T> success) {
        action.queue(success, t -> retry(action, success));
    }
}
