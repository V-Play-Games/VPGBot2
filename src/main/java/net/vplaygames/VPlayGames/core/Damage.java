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
package net.vplaygames.VPlayGames.core;

import net.vplaygames.VPlayGames.util.Array;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Number;
import net.vplaygames.VPlayGames.util.Strings;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.StringJoiner;

import static net.vplaygames.VPlayGames.data.Bot.BASE64;
import static net.vplaygames.VPlayGames.data.Bot.current;
import static net.vplaygames.VPlayGames.data.GameData.smoves;

public class Damage {
    private static int guCount = 0;
    private int tid;
    private int uid;
    private int pid;
    private int smd;
    private int sml;
    private int appStatus;
    private int gauge;
    private long userId;
    private long userTime;
    private String moveName;
    private String damageCode;
    private String userTag;
    private int[] uc;
    private int[] mSet;
    private int[] mInfo;
    private int[] mod;
    private int[] wthr;
    private int[] hpp;
    private int[][] stats;
    private int[][] buffs;
    private int[][] status;
    private int[][] sstatus;
    private boolean enabled;
    private boolean update;
    private boolean verified;
    private HashMap<String, SkillGroup> skills = new HashMap<>();

    public Damage(Damage d) {
        userId = d.userId;
        userTime = System.currentTimeMillis();
        userTag = d.userTag;
        damageCode = d.damageCode;
        tid = d.tid;
        uid = d.uid;
        pid = d.pid;
        smd = d.smd;
        sml = d.sml;
        gauge = d.gauge;
        moveName = d.moveName;
        uc = d.uc;
        mSet = d.mSet;
        mInfo = d.mInfo;
        mod = d.mod;
        wthr = d.wthr;
        stats = d.stats;
        buffs = d.buffs;
        hpp = d.hpp;
        status = d.status;
        sstatus = d.sstatus;
        skills = new HashMap<>(d.skills);
        verified = d.verified;
        enabled = d.enabled;
        update = d.update;
        appStatus = d.appStatus;
    }

    public Damage(GuildMessageReceivedEvent e) {
        this(e.getAuthor().getIdLong(), e.getAuthor().getAsTag());
    }

    public Damage(long userid) {
        this(userid, "@Guest User#" + Number.toStringOfLength(4, guCount++));
    }

    public Damage(long userid, String uTag) {
        this(userid, uTag, generateValid());
    }

    public Damage(long userid, String uTag, String code) {
        this(userid, uTag, code, true);
    }

    public Damage(long userid, String uTag, String code, boolean update) {
        this.userId = userid;
        this.userTime = System.currentTimeMillis();
        this.tid = 0;
        this.uid = 0;
        this.pid = 0;
        this.smd = 0;
        this.sml = 1;
        this.verified = false;
        this.moveName = "";
        this.appStatus = 0;
        this.gauge = 0;
        this.hpp = new int[]{0, 0};
        this.mInfo = new int[]{0, 0, 0, 0};
        this.mod = new int[]{0, 0, 0, 0};
        this.wthr = new int[]{0, 0, 0, 0};
        this.status = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        this.sstatus = new int[][]{{0, 0, 0}, {0, 0, 0}};
        this.stats = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}};
        this.buffs = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        this.mod[2] = 1;
        this.enabled = false;
        this.userTag = uTag;
        this.damageCode = code;
        this.update = update;
        update();
    }

    // Process "v!code" Command
    public static void Code(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String toSend;
        HashMap<Long, Damage> data = current.DATA;
        if (msg.length != 2) {
            MiscUtil.send(e, current.INVALID_INPUTS, true);
            return;
        }
        long aid = e.getAuthor().getIdLong();
        if (msg[1].equals("get")) {
            if (data.containsKey(aid)) {
                String code = data.get(aid).getCode();
                toSend = e.getAuthor().getAsMention() + ", you requested Damage Code is " + code;
                data.get(aid).enable();
            } else {
                toSend = "I cannot find a Damage Calculator App created by you, " + e.getAuthor().getAsMention();
            }
        } else {
            HashMap<String, Damage> dc = current.DAMAGE_CODES;
            String code = msg[1];
            if (dc.containsKey(code)) {
                Damage d = dc.get(code);
                if (d.equals(data.getOrDefault(aid, null))) {
                    toSend = "This code's Damage App was not imported because either your app is same as the code's app, or you have already imported it.";
                } else {
                    Damage top = new Damage(d);
                    top.userId = e.getAuthor().getIdLong();
                    top.damageCode = generateValid();
                    data.put(aid, top);
                    toSend = e.getAuthor().getAsMention() + " has " + (data.containsKey(aid) ? "overwritten their Damage App by" : "imported") + " a Damage App Created by " + dc.get(code).getUserTag();
                }
            } else {
                toSend = "\"" + code + "\" Damage code not found";
            }
        }
        MiscUtil.send(e, toSend, true);
    }

    // Generates Codes
    public static String generate() {
        StringBuilder tor = new StringBuilder();
        for (int i = 0; i < 6; i++)
            tor.append(BASE64.charAt((int) (Math.random() * BASE64.length())));
        return tor.toString();
    }

    public static String generateValid() {
        String code = generate();
        while (current.DAMAGE_CODES.containsKey(code)) code = generate();
        return code;
    }

    // Updater
    public void update() {
        if (this.update)
            current.DAMAGE_CODES.put(damageCode, this);
    }

    // Setters
    public Damage enable() {
        enabled = true;
        update();
        return this;
    }

    public Damage enableUpdates() {
        update = true;
        return current.DAMAGE_CODES.put(damageCode, this);
    }

    public Damage verify() {
        verified = true;
        update();
        return this;
    }

    public Damage setTid(int set) {
        tid = set;
        update();
        return this;
    }

    public Damage setUid(int set) {
        uid = set;
        update();
        return this;
    }

    public Damage setPid(int set) {
        pid = set;
        update();
        return this;
    }

    public Damage setSmd(int set) {
        smd = set;
        update();
        return this;
    }

    public Damage setSml(int set) {
        sml = set;
        update();
        return this;
    }

    public Damage updateAppStatus() {
        appStatus++;
        update();
        return this;
    }

    public Damage setGauge(int set) {
        gauge = set;
        update();
        return this;
    }

    public Damage setMoveName(String set) {
        moveName = set;
        update();
        return this;
    }

    public Damage setUc(int[] set) {
        uc = set;
        update();
        return this;
    }

    public Damage setMSet(int[] set) {
        mSet = set;
        update();
        return this;
    }

    public Damage setMInfo(int[] set) {
        if (set.length == 4)
            mInfo = set;
        else {
            for (int i = 0; i < 3; i++) {
                mInfo[(i == 0) ? i : i + 1] = set[i];
            }
            mInfo[1] = 0;
        }
        update();
        return this;
    }

    public Damage setMod(int i, int set) {
        mod[i] = set;
        update();
        return this;
    }

    public Damage setMod(int[] set) {
        mod = set;
        update();
        return this;
    }

    public Damage setWthr(int set) {
        Arrays.fill(wthr, 0);
        wthr[set] = 1;
        update();
        return this;
    }

    public Damage setWthr(int[] set) {
        wthr = set;
        update();
        return this;
    }

    public Damage setStatus(int t, int set) {
        Arrays.fill(status[t], 0);
        status[t][set] = 1;
        update();
        return this;
    }

    public Damage setStatus(int[][] set) {
        status = set;
        update();
        return this;
    }

    public Damage setSStatus(int t, int set) {
        sstatus[t][set] = (sstatus[t][set] == 1) ? 0 : 1;
        update();
        return this;
    }

    public Damage setSStatus(int[][] set) {
        sstatus = set;
        update();
        return this;
    }

    public Damage setStats(int target, int sttcd, int stat) {
        stats[target][sttcd] = stat;
        update();
        return this;
    }

    public Damage setStats(int[][] set) {
        stats = set;
        update();
        return this;
    }

    public Damage setBuffs(int target, int bffcd, int buff) {
        buffs[target][bffcd] = buff;
        update();
        return this;
    }

    public Damage setBuffs(int[][] set) {
        buffs = set;
        update();
        return this;
    }

    public Damage setHPP(int target, int set) {
        hpp[target] = set;
        update();
        return this;
    }

    public Damage setHPP(int[] set) {
        hpp = set;
        update();
        return this;
    }

    public Damage addSkill(String skill) {
        if (SkillGroup.isIntensive(skill)) {
            int intensity = MiscUtil.charToInt(skill.charAt(skill.length() - 1));
            skill = Strings.toProperCase(skill.substring(0, skill.length() - 2));
            if (skills.containsKey(skill))
                skills.get(skill).add(intensity);
            else
                skills.put(skill, new SkillGroup(skill + " " + intensity, true));
        } else
            skills.put(Strings.toProperCase(skill), new SkillGroup(skill, false));
        update();
        return this;
    }

    public Damage setSkills(HashMap<String, SkillGroup> set) {
        skills = set;
        update();
        return this;
    }

    // Getters
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public long getUserId() {
        return userId;
    }

    public long getUserTime() {
        return userTime;
    }

    public int getAppStatus() {
        return appStatus;
    }

    public int getTid() {
        return tid;
    }

    public int getUid() {
        return uid;
    }

    public int getPid() {
        return pid;
    }

    public int getSmd() {
        return smd;
    }

    public int getSml() {
        return sml;
    }

    public int getGauge() {
        return gauge;
    }

    public String getUserTag() {
        return userTag;
    }

    public String getMoveName() {
        return moveName;
    }

    public String getCode() {
        return damageCode;
    }

    public String getWeatherString() {
        return (wthr[0] == 1) ? "the weather was sunny" : (wthr[1] == 1) ? "it was raining" : (wthr[2] == 1) ? "there was a sandstorm" : (wthr[3] == 1) ? "it was hailing" : "the weather was normal";
    }

    public String getTargetString() {
        return (mod[2] == 1) ? "the target was the only opponent affected by the move" : "there " + ((mod[2] == 2) ? "was 1 more opponent" : "were 2 more opponents") + " other than the target affected by the move";
    }

    public int[] getMInfo() {
        return mInfo;
    }

    public int[] getMod() {
        return mod;
    }

    public int[] getMSet() {
        return mSet;
    }

    public int[] getUc() {
        return uc;
    }

    public int[] getWthr() {
        return wthr;
    }

    public int[] getHPP() {
        return hpp;
    }

    public int[][] getStatus() {
        return status;
    }

    public int[][] getSStatus() {
        return sstatus;
    }

    public int[][] getStats() {
        return stats;
    }

    public int[][] getBuffs() {
        return buffs;
    }

    // Getters related to Skills
    public HashMap<String, SkillGroup> getSkills() {
        return skills;
    }

    public String getSkillGroupString() {
        StringJoiner tor = new StringJoiner("\n");
        int i = 1;
        for (SkillGroup sg : skills.values())
            tor.add("Skill " + (i++) + ".\n" + sg);
        return tor.toString();
    }

    public String getPassiveString() {
        if (skills.isEmpty()) return "";
        StringJoiner tor = new StringJoiner("\n").add("Skills affecting the damage:-");
        int j = 1;
        for (SkillGroup sg : skills.values()) {
            if (sg.isActive(this)) {
                tor.add((j++) + ". " + sg.getPassiveString() + "\n");
            }
        }
        return tor.toString();
    }

    public String getMultiplierString() {
        String tor = "";
        for (SkillGroup sg : skills.values())
            tor += (sg.isActive(this)) ? sg.getMultiplierString() : "";
        return tor;
    }

    public double getPassiveMultiplier() {
        double tor = 0.0d;
        for (String s : skills.keySet().toArray(new String[0]))
            if (skills.get(s).isActive(this))
                tor += skills.get(s).getMultiplier();
        return tor;
    }

    // Methods overridden from java.lang.Object
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Damage))
            return false;
        Damage d = (Damage) o;
        return tid == d.tid &&
                uid == d.uid &&
                pid == d.pid &&
                smd == d.smd &&
                sml == d.sml &&
                verified == d.verified &&
                appStatus == d.appStatus &&
                gauge == d.gauge &&
                moveName.equals(d.moveName) &&
                uc == d.uc &&
                mSet == d.mSet &&
                mod == d.mod &&
                wthr == d.wthr &&
                stats == d.stats &&
                buffs == d.buffs &&
                status == d.status &&
                sstatus == d.sstatus &&
                skills.equals(d.skills);
    }

    @Override
    public String toString() {
        return "damageCode = " + damageCode +
                "\nenabled = " + enabled +
                "\ntid = " + tid +
                "\nuid = " + uid +
                "\npid = " + pid +
                "\nsmd = " + smd +
                "\nsml = " + sml +
                "\nverified = " + verified +
                "\nappStatus = " + appStatus +
                "\ngauge = " + gauge +
                "\nuser_id = " + userId +
                "\nuser_time = " + userTime +
                "\nmvnam = " + moveName +
                "\nuc = " + Arrays.toString(uc) +
                "\nmSet = " + Arrays.toString(mSet) +
                "\nmInfo = " + Arrays.toString(mInfo) +
                "\nmod = " + Arrays.toString(mod) +
                "\nhpp = " + Arrays.toString(hpp) +
                "\nwthr = " + Arrays.toString(wthr) +
                "\nstats = " + Arrays.deepToString(stats) +
                "\nbuffs = " + Arrays.deepToString(buffs) +
                "\nstatus = " + Arrays.deepToString(status) +
                "\nsstatus = " + Arrays.deepToString(sstatus) +
                "\nskills:-\n" + getSkillGroupString();
    }

    @Override
    public Damage clone() {
        return new Damage(this);
    }

    // Parsers
    public String parseToString() {
        //int tid,uid,pid,smd,sml,appStatus,gauge;
        String ints = tid + ";" + uid + ";" + pid + ";" + smd + ";" + sml + ";" + appStatus + ";" + gauge;
        //long userId,userTime;
        String longs = userId + ";" + userTime;
        //String moveName,damageCode,userTag;
        String Strings = (moveName.endsWith("e)") ? "s" : moveName) + ";" + damageCode + ";" + userTag;
        //int[] uc,mSet,mInfo,mod,wthr,hpp;
        String SingleArrays = Array.manyToString(uc, mSet, mInfo, mod, wthr, hpp);
        //int[][] stats,buffs,status,sstatus;
        String DoubleArrays = Array.manyToString(stats, buffs, status, sstatus);
        //boolean enabled,update,verified;
        String booleans = "" + (enabled ? 1 : 0) + (update ? 1 : 0) + (verified ? 1 : 0);
        StringBuilder skill = new StringBuilder();
        for (String str : skills.keySet().toArray(new String[0]))
            skill.append(skills.get(str).translateToString()).append("-");
        String Skills = "[" + skill.toString().substring(0, skill.length() != 0 ? skill.length() - 1 : 0) + "]";
        return ints + ";" + longs + ";" + Strings + ";" + SingleArrays + ";" + DoubleArrays + ";" + booleans + ";" + Skills;
    }

    public static Damage parseFromString(String damage) {
        return new Parser(damage).acceptAll().toDamage();
    }

    public static class Parser {
        int tid, uid, pid, smd, sml, appStatus, gauge;
        long userId, userTime;
        String mvnam, damageCode, userTag;
        int[] uc, mSet, mInfo, mod, wthr, hpp;
        int[][] stats, buffs, status, sstatus;
        boolean enabled, update, vrfy;
        HashMap<String, SkillGroup> skills = new HashMap<>();
        final String[] TDATA;

        public Parser(String translation) {
            TDATA = translation.split(";");
            System.out.println(Array.toString("\n",TDATA,"Error"));
        }

        public Parser acceptAll() {
            /* 105;105168;105168;0;1;4;0;
             * 660739620807507978;1603527220845;
             * Wild Charge;FDwoMs;Natsu 11#0769;
             * [105168];[105168];[150,164];[125,0,0,4];[0,0,1,0];[0,0,0,0];[0,0];
             * [[0,0,0,0]-[0,0,0,0]];[[0,0,0,0]-[0,0,0,0]];[[0,0,0,0,0,0,0]-[0,0,0,0,0,0,0]];[[0,0,0,0,0,0,0]-[0,0,0,0,0,0,0]];[[0,0,0]-[0,0,0]];
             * 010;
             */
            tid = Strings.toInt(TDATA[0]);
            uid = Strings.toInt(TDATA[1]);
            pid = Strings.toInt(TDATA[2]);
            smd = Strings.toInt(TDATA[3]);
            sml = Strings.toInt(TDATA[4]);
            appStatus = Strings.toInt(TDATA[5]);
            gauge = Strings.toInt(TDATA[6]);
            userId = Long.parseLong(TDATA[7]);
            userTime = Long.parseLong(TDATA[8]);
            mvnam = TDATA[9].equals("s") ? smoves[pid] : TDATA[9];
            damageCode = TDATA[10];
            userTag = TDATA[11];
            uc = Strings.toSingleIntArray(TDATA[12]);
            mSet = Strings.toSingleIntArray(TDATA[13]);
            mInfo = Strings.toSingleIntArray(TDATA[14]);
            mod = Strings.toSingleIntArray(TDATA[15]);
            wthr = Strings.toSingleIntArray(TDATA[16]);
            hpp = Strings.toSingleIntArray(TDATA[17]);
            stats = Strings.toDoubleIntArray(TDATA[18]);
            buffs = Strings.toDoubleIntArray(TDATA[19]);
            status = Strings.toDoubleIntArray(TDATA[20]);
            sstatus = Strings.toDoubleIntArray(TDATA[21]);
            enabled = TDATA[22].charAt(0) == '1';
            update = TDATA[22].charAt(1) == '1';
            vrfy = TDATA[22].charAt(2) == '1';
            String[] s = TDATA[23].substring(1, TDATA[23].length() - 1).split("-");
            for (String skill : s) {
                SkillGroup sg = SkillGroup.parse(skill);
                if (sg == null) break;
                skills.put(sg.getName(), sg);
            }
            return this;
        }

        public Damage toDamage() {
            Damage d = new Damage(userId, userTag, damageCode, update)
                    .setTid(tid)
                    .setUid(uid)
                    .setPid(pid)
                    .setSmd(smd)
                    .setSml(sml)
                    .setGauge(gauge)
                    .setMoveName(mvnam)
                    .setUc(uc)
                    .setMSet(mSet)
                    .setMInfo(mInfo)
                    .setMod(mod)
                    .setWthr(wthr)
                    .setHPP(hpp)
                    .setStats(stats)
                    .setBuffs(buffs)
                    .setStatus(status)
                    .setSStatus(sstatus)
                    .setSkills(skills);
            if (enabled) d.enable();
            if (vrfy) d.verify();
            if (update) d.enableUpdates();
            for (int i = 0; i < appStatus; i++) d.updateAppStatus();
            return d;
        }
    }
}