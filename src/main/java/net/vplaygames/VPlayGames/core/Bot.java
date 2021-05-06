/*
 * Copyright 2020-2021 Vaibhav Nargwani
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
package net.vplaygames.VPlayGames.core;

import com.vplaygames.PM4J.Logger;
import com.vplaygames.PM4J.caches.TrainerDataCache;
import com.vplaygames.PM4J.entities.Trainer;
import com.vplaygames.PM4J.json.JSONArray;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.vplaygames.VPlayGames.commands.ICommand;
import net.vplaygames.VPlayGames.util.Array;
import net.vplaygames.VPlayGames.util.MiscUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Bot {
    public static final String VERSION = "1.6.0b";
    static final Logger logger = new Logger(Bot.class);
    public static final File logFile = new File("src/main/resources/logFile.txt");
    public static final File errorFile = new File("src/main/resources/errorFile.txt");
    public static JDA jda;
    public static boolean closed = false;
    public static boolean rebooted = false;
    public static int syncCount;
    public static long BOT_OWNER = 701660977258561557L;
    public static long LOG_CHANNEL_ID = 762950187492179995L;
    public static long SYNC_CHANNEL_ID = 762950187492179995L;
    public static final long[] botStaff = new long[4];
    public static AtomicLong lastCommandId = new AtomicLong(1);
    public static Instant instantAtBoot;
    public static String lastRefresh = "never";
    public static final String TOKEN = System.getenv("TOKEN");
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
    public static HashMap<Long, JSONObject> responses = new HashMap<>();
    public static Runnable rebootTasks = () -> {};

    public static void setCustomStreams() throws FileNotFoundException {
        System.setOut(new SplitStream(new PrintStream(new FileOutputStream(logFile)), System.out));
        System.setErr(new SplitStream(new PrintStream(new FileOutputStream(errorFile)), System.err));
        System.out.println("---- " + MiscUtil.dateTimeNow() + " ----");
        System.err.println("---- " + MiscUtil.dateTimeNow() + " ----");
    }

    public static void start() throws LoginException {
        jda = JDABuilder.createDefault(Bot.TOKEN,
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_EMOJIS)
            .addEventListeners(EventHandler.getInstance())
            .build();
    }

    public static void init() throws Exception {
        syncCount = 0;
        setLogChannel(jda.getTextChannelById(LOG_CHANNEL_ID));
        setSyncChannel(jda.getTextChannelById(SYNC_CHANNEL_ID));
        initData();
        startTimer();
        loadCommands();
        initSamples();
        setDefaultActivity();
        setBooted();
        rebootTasks.run();
        logChannel.sendMessage("I am ready for anything!\n\t-Morty, Johto Gym Leader, 2020").queue();
    }

    public static void setLogChannel(TextChannel c) {
        if (c != null) {
            LOG_CHANNEL_ID = c.getIdLong();
            logChannel = c;
        }
    }

    public static void setSyncChannel(TextChannel c) {
        if (c != null) {
            LOG_CHANNEL_ID = c.getIdLong();
            syncChannel = c;
        }
    }

    public static void initSamples() {
        new Damage(jda.getSelfUser().getIdLong(), "SAMPLE")
            .setTrainer(TrainerDataCache.getInstance().get("red"))
            .setPokemon(1)
            .setAttack(4)
            .setMod(2, 1)
            .setSml(5)
            .setStats(0, 1, 420)
            .setStats(1, 3, 57)
            .setBuffs(0, 1, 6)
            .setMod(0, 1)
            .setMod(1, 1)
            .setWeather(Damage.Weather.SUN)
            .enable()
            .incrementAppStatus()
            .incrementAppStatus()
            .incrementAppStatus()
            .incrementAppStatus();
    }

    public static void initData() throws Exception {
        System.out.println("Initialising Data");
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/botStaff.json"))) {
            int i = 0;
            for (Object o : (org.json.simple.JSONArray) new JSONParser().parse(reader))
                botStaff[i++] = Long.parseLong(o.toString());
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("src\\main\\resources\\dataFile.json"))) {
            JSONArray.parse((org.json.simple.JSONArray) new JSONParser().parse(reader), Trainer::parse);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("src\\main\\resources\\passives.json"))) {
            JSONArray.parse((org.json.simple.JSONArray) new JSONParser().parse(reader), Passive.Skill::parse);
        }
        System.out.println("Data Initialised");
    }

    public static void startTimer() {
        if (timer != null) timer.shutdown();
        (timer = new ScheduledThreadPoolExecutor(2, r -> new Thread(r, "VPG Timer")))
            .scheduleWithFixedDelay(() -> {
                syncCount++;
                logger.log("Syncing data [" + syncCount + "]\n", Logger.Mode.INFO);
                syncChannel.sendMessage("Sync [" + syncCount + "]").queue();
                syncChannel.sendMessage("logFile").addFile(logFile).queue();
                syncChannel.sendMessage("errorFile").addFile(errorFile).queue();
            }, 0, 20, TimeUnit.MINUTES);
    }

    public static void loadCommands() {
        try (ScanResult scanResult = new ClassGraph().acceptPackages("net.vplaygames.VPlayGames").scan()) {
            HashMap<Class<?>, Throwable> errors = new HashMap<>();
            scanResult.getAllClasses()
                .stream()
                .filter(x -> !x.isAbstract() && !x.isInterface() && x.implementsInterface("net.vplaygames.VPlayGames.commands.ICommand"))
                .forEach(x -> {
                    try {
                        logger.log("Loaded " + x.loadClass().getConstructor().newInstance() + " Command\n", Logger.Mode.INFO);
                    } catch (Throwable e) {
                        errors.put(x.loadClass(), e);
                    }
                });
            errors.forEach((k, v) -> {
                logger.log("Failed to load " + k.getSimpleName() + "\n", Logger.Mode.ERROR);
                v.printStackTrace();
            });
            System.out.println("All Commands Loaded!");
        }
    }

    public static void setDefaultActivity() {
        jda.getPresence()
            .setActivity(Activity
                .playing("Version " + VERSION + " with " + jda.getGuilds()
                    .stream()
                    .mapToLong(Guild::getMemberCount)
                    .sum() + " people in " + jda.getGuilds().size() + " servers"));
    }

    public static void setBooted() {
        instantAtBoot = Instant.now();
    }

    public static boolean isStaff(long uid) {
        return Array.contains(uid, botStaff);
    }

    public static JDA getJDA() {
        return jda;
    }
}
