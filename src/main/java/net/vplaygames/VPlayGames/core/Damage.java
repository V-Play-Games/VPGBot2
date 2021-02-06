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
import net.vplaygames.VPlayGames.util.Number;
import net.vplaygames.VPlayGames.util.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

import static java.lang.Math.floor;
import static java.lang.Math.round;
import static net.vplaygames.VPlayGames.data.Bot.DATA;
import static net.vplaygames.VPlayGames.util.MiscUtil.returnSP;

public class Damage {
    static int guCount = 0;
    boolean enabled;
    int tid;
    int uid;
    int pid;
    int smd;
    int sml;
    Status appStatus;
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
    ArrayList<SkillGroup> skills;

    public Damage(GuildMessageReceivedEvent e) {
        this(e.getAuthor().getIdLong(), e.getAuthor().getAsTag());
    }

    public Damage(long userId) {
        this(userId, "@Guest User#" + Number.toStringOfLength(4, guCount++));
    }

    public Damage(long userId, String uTag) {
        this(userId, uTag, generateValid());
    }

    public Damage(long userId, String uTag, String code) {
        this(userId, uTag, code, System.currentTimeMillis());
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
        appStatus = Status.of(0);
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
        skills = new ArrayList<>();
        damage = 0;
        damageString = "0";
        Bot.DATA.put(userId,this);
    }

    // Generates Codes
    public static String generate() {
        StringBuilder tor = new StringBuilder();
        for (int i = 0; i < 6; i++)
            tor.append(Bot.BASE64.charAt((int) (Math.random() * 64)));
        return tor.toString();
    }

    public static String generateValid() {
        String code = generate();
        while (Bot.DAMAGE_CODES.containsKey(code)) code = generate();
        return code;
    }

    // Remover
    public void remove() {
        if (!enabled) {
            Bot.DAMAGE_CODES.remove(damageCode);
        }
        DATA.remove(userId);
    }

    // Setters
    public Damage copyFrom(Damage d) {
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
        skills = new ArrayList<>(d.skills);
        enabled = d.enabled;
        appStatus = d.appStatus;
        damage = d.damage;
        damageString = d.damageString;
        return this;
    }

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
        appStatus = appStatus.next();
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
        skills.add(new SkillGroup(skill));
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
        return appStatus.ordinal();
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

    public ArrayList<SkillGroup> getSkills() {
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

    public String getPassiveString() {
        if (skills.isEmpty()) return "";
        StringJoiner tor = new StringJoiner("\n").add("Skills affecting the damage:-");
        int[] i = {1};
        skills.stream()
            .filter(sg -> sg.isActive(this))
            .forEach(sg -> tor.add((i[0]++) + ". " + sg.getPassiveString()));
        return tor.toString();
    }

    public String getMultiplierString() {
        return skills.stream()
            .filter(sg -> sg.isActive(this))
            .collect(() -> new StringJoiner(""),
                ((stringJoiner, skillGroup) -> stringJoiner.add(skillGroup.getMultiplierString())),
                StringJoiner::merge)
            .toString();
    }

    public double getPassiveMultiplier() {
        double tor = 0.0d;
        for (SkillGroup sg : skills)
            if (sg.isActive(this))
                tor += sg.getMultiplier();
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
        return "Damage{" +
            "enabled=" + enabled +
            ", tid=" + tid +
            ", uid=" + uid +
            ", pid=" + pid +
            ", smd=" + smd +
            ", sml=" + sml +
            ", appStatus=" + appStatus +
            ", gauge=" + gauge +
            ", damage=" + damage +
            ", userId=" + userId +
            ", userTime=" + userTime +
            ", moveName=" + moveName +
            ", damageCode=" + damageCode +
            ", userTag=" + userTag +
            ", damageString=" + damageString +
            ", uc=" + Arrays.toString(uc) +
            ", mSet=" + Arrays.toString(mSet) +
            ", mInfo=" + Arrays.toString(mInfo) +
            ", mod=" + Arrays.toString(mod) +
            ", wthr=" + Arrays.toString(wthr) +
            ", terrain=" + Arrays.toString(terrain) +
            ", hpp=" + Arrays.toString(hpp) +
            ", stats=" + Arrays.toString(stats) +
            ", buffs=" + Arrays.toString(buffs) +
            ", status=" + Arrays.toString(status) +
            ", sstatus=" + Arrays.toString(sstatus) +
            ", skills=" + skills +
            '}';
    }

    // Parsers
    public String parseToString() {
        // int tid,uid,pid,smd,sml,appStatus,gauge;
        // boolean enabled;
        String ints = tid + ";" + uid + ";" + pid + ";" + smd + ";" + sml + ";" + appStatus.ordinal() + ";" + gauge + ";" + (enabled ? 1 : 0);
        // long userId,userTime;
        String longs = userId + ";" + userTime;
        // String moveName,damageCode,userTag;
        String Strings = (moveName.endsWith("e)") ? "s" : moveName) + ";" + damageCode + ";" + userTag;
        // int[] uc,mSet,mInfo,mod,wthr,hpp;
        String SingleArrays = Array.manyToString(uc, mSet, mInfo, mod, wthr, terrain, hpp);
        // int[][] stats,buffs,status,sstatus;
        String DoubleArrays = Array.manyToString(stats, buffs, status, sstatus);
        StringJoiner skill = new StringJoiner("-", "[", "]");
        skills.forEach(sg -> skill.add(sg.translateToString()));
        return ints + ";" + longs + ";" + Strings + ";" + SingleArrays + ";" + DoubleArrays + ";" + skill;
    }

    public static Damage parseFromString(String damage) {
        String[] TDATA = damage.split(";");
        /* 84;10084140;84140;1;5;4;0;1;
         * 747328310828204143;1608818463475;
         * Heat Wave;SAMPLE;V Play Games#5877;
         * [10084139,10084140];[1065,1057,1015];[109,1,1,7];[1,1,1,0];[1,0,0,0];[0,0,0,0];[0,0];
         * [[0,420,0,57]-[0,0,0,0]];[[0,6,0,0,0,0,0]-[0,0,0,0,0,0,0]];[[0,0,0,0,0,0,0]-[0,0,0,0,0,0,0]];[[0,0,0]-[0,0,0]];
         * [];
         */
        int tid = Strings.toInt(TDATA[0]);
        int uid = Strings.toInt(TDATA[1]);
        int pid = Strings.toInt(TDATA[2]);
        int smd = Strings.toInt(TDATA[3]);
        int sml = Strings.toInt(TDATA[4]);
        boolean enabled = TDATA[5].charAt(0) == '1';
        int appStatus = Strings.toInt(TDATA[6]);
        int gauge = Strings.toInt(TDATA[7]);
        long userId = Long.parseLong(TDATA[8]);
        long userTime = Long.parseLong(TDATA[9]);
        String moveName = TDATA[10].equals("s") ? GameData.smoves[pid%1000-1]+" (Sync Move)" : TDATA[10];
        String damageCode = TDATA[11];
        String userTag = TDATA[11];
        int[] uc = Strings.toSingleIntArray(TDATA[12]);
        int[] mSet = Strings.toSingleIntArray(TDATA[14]);
        int[] mInfo = Strings.toSingleIntArray(TDATA[15]);
        int[] mod = Strings.toSingleIntArray(TDATA[16]);
        int[] wthr = Strings.toSingleIntArray(TDATA[17]);
        int[] terrain = Strings.toSingleIntArray(TDATA[18]);
        int[] hpp = Strings.toSingleIntArray(TDATA[19]);
        int[][] stats = Strings.toDoubleIntArray(TDATA[20]);
        int[][] buffs = Strings.toDoubleIntArray(TDATA[21]);
        int[][] status = Strings.toDoubleIntArray(TDATA[22]);
        int[][] sstatus = Strings.toDoubleIntArray(TDATA[23]);
        ArrayList<SkillGroup> skills = new ArrayList<>();
        if (!TDATA[24].equals("[]")) {
            String[] s = TDATA[24].substring(1, TDATA[24].length() - 1).split("-");
            for (String skill : s) {
                SkillGroup sg = SkillGroup.parse(skill);
                if (sg == null) continue;
                skills.add(sg);
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
        d.appStatus = Status.of(appStatus);
        return d;
    }

    public enum Status {
        STARTED,
        TRAINER_CHOSEN,
        UNIT_CHOSEN,
        TARGETS_CHOSEN,
        MOVE_CHOSEN,
        ABLE_TO_CALCULATE;

        public Status next() {
            return of(ordinal()+1);
        }

        public static Status of(int i) {
            for (Status s : values()) {
                if (s.ordinal() == i) {
                    return s;
                }
            }
            return ABLE_TO_CALCULATE;
        }
    }
}
