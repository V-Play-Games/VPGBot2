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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.data.GameData;
import net.vplaygames.VPlayGames.util.Array;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Number;
import net.vplaygames.VPlayGames.util.Strings;

import java.util.Arrays;
import java.util.HashMap;
import java.util.StringJoiner;

import static java.lang.Math.floor;
import static java.lang.Math.round;
import static net.vplaygames.VPlayGames.util.MiscUtil.returnSP;

public class Damage {
    static int guCount = 0;
    boolean enabled;
    int tid;
    int uid;
    int pid;
    int smd;
    int sml;
    int appStatus;
    int gauge;
    long damage;
    long userId;
    long userTime;
    String moveName;
    String damageCode;
    String userTag;
    String damageString;
    int[] uc;
    int[] mSet;
    int[] mInfo;
    int[] mod;
    int[] wthr;
    int[] terrain;
    int[] hpp;
    int[][] stats;
    int[][] buffs;
    int[][] status;
    int[][] sstatus;
    HashMap<String, SkillGroup> skills;

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
        terrain = d.terrain;
        stats = d.stats;
        buffs = d.buffs;
        hpp = d.hpp;
        status = d.status;
        sstatus = d.sstatus;
        skills = new HashMap<>(d.skills);
        enabled = d.enabled;
        appStatus = d.appStatus;
        damage = d.damage;
        damageString = d.damageString;
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
        this(userid, uTag, code, System.currentTimeMillis());
    }

    public Damage(long uId, String uTag, String code, long uTime) {
        userId = uId;
        userTime = uTime;
        tid = 0;
        uid = 0;
        pid = 0;
        smd = 0;
        sml = 1;
        moveName = "";
        appStatus = 0;
        gauge = 0;
        hpp = new int[]{0, 0};
        mInfo = new int[]{0, 0, 0, 0};
        mod = new int[]{0, 0, 0, 0};
        wthr = new int[]{0, 0, 0, 0};
        terrain = new int[]{0, 0, 0, 0};
        status = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        sstatus = new int[][]{{0, 0, 0}, {0, 0, 0}};
        stats = new int[][]{{0, 0, 0, 0}, {0, 0, 0, 0}};
        buffs = new int[][]{{0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0}};
        mod[2] = 1;
        userTag = uTag;
        damageCode = code;
        enabled = false;
        skills = new HashMap<>();
        damage = 0;
        damageString = "0";
        Bot.DATA.put(userId,this);
    }

    // Process "v!code" Command
    public static void Code(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String toSend;
        HashMap<Long, Damage> data = Bot.DATA;
        if (msg.length != 2) {
            MiscUtil.send(e, Bot.INVALID_INPUTS, true);
            return;
        }
        long aid = e.getAuthor().getIdLong();
        if (msg[1].equals("get")) {
            if (data.containsKey(aid)) {
                String code = data.get(aid).getCode();
                toSend = e.getAuthor().getAsMention() + ", your requested Damage Code is " + code;
                data.get(aid).enable();
            } else {
                toSend = "I cannot find a Damage Calculator App created by you, " + e.getAuthor().getAsMention();
            }
        } else {
            HashMap<String, Damage> dc = Bot.DAMAGE_CODES;
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
            tor.append(Bot.BASE64.charAt((int) (Math.random() * Bot.BASE64.length())));
        return tor.toString();
    }

    public static String generateValid() {
        String code = generate();
        while (Bot.DAMAGE_CODES.containsKey(code)) code = generate();
        return code;
    }

    // Setters
    public Damage enable() {
        enabled = true;
        Bot.DAMAGE_CODES.put(damageCode,this);
        return this;
    }

    public Damage setTid(int set) {
        tid = set;
        return this;
    }

    public Damage setUid(int set) {
        uid = set;
        return this;
    }

    public Damage setPid(int set) {
        pid = set;
        return this;
    }

    public Damage setSmd(int set) {
        smd = set;
        return this;
    }

    public Damage setSml(int set) {
        sml = set;
        return this;
    }

    public Damage updateAppStatus() {
        appStatus++;
        return this;
    }

    public Damage setGauge(int set) {
        gauge = set;
        return this;
    }

    public Damage setMoveName(String set) {
        moveName = set;
        return this;
    }

    public Damage setUc(int[] set) {
        uc = set;
        return this;
    }

    public Damage setMSet(int[] set) {
        mSet = set;
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
        return this;
    }

    public Damage setMod(int i, int set) {
        mod[i] = set;
        return this;
    }

    public Damage setWthr(int set) {
        Arrays.fill(wthr, 0);
        wthr[set] = 1;
        return this;
    }

    public Damage setTerrain(int set) {
        Arrays.fill(terrain, 0);
        terrain[set] = 1;
        return this;
    }

    public Damage setStatus(int t, int set) {
        Arrays.fill(status[t], 0);
        status[t][set] = 1;
        return this;
    }

    public Damage setSStatus(int t, int set) {
        sstatus[t][set] = (sstatus[t][set] == 1) ? 0 : 1;
        return this;
    }

    public Damage setStats(int target, int sttcd, int stat) {
        stats[target][sttcd] = stat;
        return this;
    }

    public Damage setBuffs(int target, int bffcd, int buff) {
        buffs[target][bffcd] = buff;
        return this;
    }

    public Damage setHPP(int target, int set) {
        hpp[target] = set;

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
        return this;
    }

    // Getters
    public boolean isEnabled() {
        return enabled;
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

    public int[] getTerrain() {
        return terrain;
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

    public HashMap<String, SkillGroup> getSkills() {
        return skills;
    }

    public void refresh() {
        int mCat = mInfo[2];
        int mType = mInfo[3];
        int off = stats[0][mCat];
        int def = stats[1][mCat+2];
        int bp = mInfo[0];
        int b_o = Math.abs(buffs[0][mCat]);
        double buff_off = (b_o==0)?1:(b_o==1)?1.25:(10+b_o+2)/10.0;
        buff_off = (buffs[0][mCat]<0)?1.0/buff_off:buff_off;
        int b_d = Math.abs(buffs[1][mCat+2]);
        double buff_def = (b_d==0)?1:(b_d==1)?1.25:(10+b_d+2)/10.0;
        buff_def = (buffs[1][mCat+2]<0)?1.0/buff_def:(mod[0]==1)?1:buff_def;
        double smb = 1+(sml-1)/20.0;
        double roll = round(Math.random()*10+90)/100.0;
        double ch = (mod[0]==1)?1.5:1;
        double se = (mod[1]==1)?2:1;
        double sprd = (mod[2]==1)?1:(mod[2]==2)?2.0/3.0:0.5;
        double wthr_boost = (wthr[0]==1&&mType==7)||(wthr[1]==1&&mType==18)?1.5:1;
        double terrain_boost = (terrain[0]==1&&mType==4)||(terrain[1]==1&&mType==15)||(terrain[2]==1&&mType==10)||(terrain[3]==1&&mType==5)?1.5:1;
        double mod = ch*se*sprd*wthr_boost*terrain_boost;
        damage = round(floor(floor(bp*smb*(1+getPassiveMultiplier()))*(off*buff_off)/(def*buff_def)*mod*roll));
        damageString="Formula:- (int: (int: "+bp+"x"+smb+"x(1"+getMultiplierString()+"))x(("+off+"x"+buff_off+")/("+def+"x"+buff_def+"))x("+ch+"x"+se+"x"+wthr_boost+"x"+terrain_boost+"x"+sprd+")x"+roll+")"+
                "\n\""+returnSP(uid)+"\" with "+
                off+" "+((mInfo[2]==1)?"Special":"Physical")+" Attack stat, while using "+
                moveName+" at sync move level "+sml+
                ", can deal **__"+damage+"__** damage to an opponent with "+
                def+" "+((mInfo[2]==1)?"Special":"Physical")+" Defense stat provided that "+
                getTargetString()+", "+getWeatherString()+", "+getTerrainString()+
                ", the hit was "+((ch==1)?"not ":"")+"a critical hit and was "+
                ((se==1)?"":"super ")+"effective against the opponent.\n\n"+getPassiveString();
    }

    // Other Getters
    public String getWeatherString() {
        return (wthr[0] == 1) ? "the weather was sunny" : (wthr[1] == 1) ? "it was raining" : (wthr[2] == 1) ? "there was a sandstorm" : (wthr[3] == 1) ? "it was hailing" : "the weather was normal";
    }

    public String getTerrainString() {
        return ((terrain[0] == 1) ? "the electric" : (terrain[1] == 1) ? "the psychic" : (terrain[2] == 1) ? "the grassy" : (terrain[3] == 1) ? "the misty" : "no") + " terrain was active";
    }

    public String getTargetString() {
        return (mod[2] == 1) ? "the target was the only opponent affected by the move" : "there " + ((mod[2] == 2) ? "was 1 more opponent" : "were 2 more opponents") + " other than the target affected by the move";
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
                tor.add((j++) + ". " + sg.getPassiveString());
            }
        }
        return tor.toString();
    }

    public String getMultiplierString() {
        StringJoiner tor = new StringJoiner("");
        for (SkillGroup sg : skills.values())
            tor.add((sg.isActive(this)) ? sg.getMultiplierString() : "");
        return tor.toString();
    }

    public double getPassiveMultiplier() {
        double tor = 0.0d;
        for (String s : skills.keySet().toArray(new String[0]))
            if (skills.get(s).isActive(this))
                tor += skills.get(s).getMultiplier();
        return tor;
    }

    public long getDamage() {
        refresh();
        return damage;
    }

    public String getDamageString() {
        refresh();
        return damageString;
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
        return "damage = " + damage +
                "\ntid = " + tid +
                "\nuid = " + uid +
                "\npid = " + pid +
                "\nsmd = " + smd +
                "\nsml = " + sml +
                "\nappStatus = " + appStatus +
                "\ngauge = " + gauge +
                "\nuserId = " + userId +
                "\nuserTime = " + userTime +
                "\nmoveName = " + moveName +
                "\ndamageCode = " + damageCode +
                "\nuserTag = " + userTag +
                "\ndamageString = " + damageString +
                "\nuc = " + Arrays.toString(uc) +
                "\nmSet = " + Arrays.toString(mSet) +
                "\nmInfo = " + Arrays.toString(mInfo) +
                "\nmod = " + Arrays.toString(mod) +
                "\nwthr = " + Arrays.toString(wthr) +
                "\nterrain = " + Arrays.toString(terrain) +
                "\nhpp = " + Arrays.toString(hpp) +
                "\nstats = " + Arrays.deepToString(stats) +
                "\nbuffs = " + Arrays.deepToString(buffs) +
                "\nstatus = " + Arrays.deepToString(status) +
                "\nsstatus = " + Arrays.deepToString(sstatus) +
                "\nenabled = " + enabled +
                "\nskills =\n" + getSkillGroupString();
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
        String SingleArrays = Array.manyToString(uc, mSet, mInfo, mod, wthr, terrain, hpp);
        //int[][] stats,buffs,status,sstatus;
        String DoubleArrays = Array.manyToString(stats, buffs, status, sstatus);
        //boolean enabled,update,verified;
        String booleans = "" + (enabled ? 1 : 0);
        StringBuilder skill = new StringBuilder();
        for (String str : skills.keySet().toArray(new String[0]))
            skill.append(skills.get(str).translateToString()).append("-");
        String Skills = "[" + skill.toString().substring(0, skill.length() != 0 ? skill.length() - 1 : 0) + "]";
        return ints + ";" + longs + ";" + Strings + ";" + SingleArrays + DoubleArrays + booleans + ";" + Skills;
    }

    public static Damage parseFromString(String damage) {
        String[] TDATA = damage.split(";");
        /* 84;10084140;84140;1;5;4;0;
         * 747328310828204143;1608818463475;
         * Heat Wave;SAMPLE;V Play Games#5877;
         * [10084139,10084140];[1065,1057,1015];[109,1,1,7];[1,1,1,0];[1,0,0,0];[0,0,0,0];[0,0];
         * [[0,420,0,57]-[0,0,0,0]];[[0,6,0,0,0,0,0]-[0,0,0,0,0,0,0]];[[0,0,0,0,0,0,0]-[0,0,0,0,0,0,0]];[[0,0,0]-[0,0,0]];
         * 1;
         * [];
         */
        int tid = Strings.toInt(TDATA[0]);
        int uid = Strings.toInt(TDATA[1]);
        int pid = Strings.toInt(TDATA[2]);
        int smd = Strings.toInt(TDATA[3]);
        int sml = Strings.toInt(TDATA[4]);
        int appStatus = Strings.toInt(TDATA[5]);
        int gauge = Strings.toInt(TDATA[6]);
        long userId = Long.parseLong(TDATA[7]);
        long userTime = Long.parseLong(TDATA[8]);
        String moveName = TDATA[9].equals("s") ? GameData.smoves[pid%1000-1]+" (Sync Move)" : TDATA[9];
        String damageCode = TDATA[10];
        String userTag = TDATA[11];
        int[] uc = Strings.toSingleIntArray(TDATA[12]);
        int[] mSet = Strings.toSingleIntArray(TDATA[13]);
        int[] mInfo = Strings.toSingleIntArray(TDATA[14]);
        int[] mod = Strings.toSingleIntArray(TDATA[15]);
        int[] wthr = Strings.toSingleIntArray(TDATA[16]);
        int[] terrain = Strings.toSingleIntArray(TDATA[17]);
        int[] hpp = Strings.toSingleIntArray(TDATA[18]);
        int[][] stats = Strings.toDoubleIntArray(TDATA[19]);
        int[][] buffs = Strings.toDoubleIntArray(TDATA[20]);
        int[][] status = Strings.toDoubleIntArray(TDATA[21]);
        int[][] sstatus = Strings.toDoubleIntArray(TDATA[22]);
        boolean enabled = TDATA[23].charAt(0) == '1';
        HashMap<String, SkillGroup> skills = new HashMap<>();
        if (!TDATA[24].equals("[]")) {
            String[] s = TDATA[24].substring(1, TDATA[24].length() - 1).split("-");
            for (String skill : s) {
                SkillGroup sg = SkillGroup.parse(skill);
                if (sg == null) break;
                skills.put(sg.getName(), sg);
            }
        }
        Damage d = new Damage(userId,userTag,damageCode,userTime);
        d.tid = tid;
        d.uid = uid;
        d.pid = pid;
        d.smd = smd;
        d.sml = sml;
        d.gauge = gauge;
        d.moveName = moveName;
        d.uc = uc;
        d.mSet = mSet;
        d.mInfo = mInfo;
        d.mod = mod;
        d.wthr = wthr;
        d.terrain = terrain;
        d.stats = stats;
        d.buffs = buffs;
        d.hpp = hpp;
        d.status = status;
        d.sstatus = sstatus;
        d.skills = skills;
        d.enabled = enabled;
        d.appStatus = appStatus;
        return d;
    }
}