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
package net.vplaygames.VPlayGames.db;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static java.util.Arrays.fill;

public class Damage
{
    int tid,uid,pid,smd,sml,vrfy,app_stts;
    long user_id,user_time;
    String mvnam;
    int[] uc,mSet,mInfo,mod,wthr;
    int[][] stats,buffs,status,sstatus;

    public Damage(GuildMessageReceivedEvent e) {
        user_id=e.getAuthor().getIdLong();
        user_time=e.getMessage().getTimeCreated().getSecond()*1000;
        tid=0;
        uid=0;
        pid=0;
        smd=0;
        sml=0;
        vrfy=0;
        mvnam="";
        app_stts=0;
        mInfo=new int[4];
        mod=new int[]{0,0,0};
        wthr=new int[]{0,0,0};
        status=new int[][]{{0,0,0,0,0,0,0},{0,0,0,0,0,0,0}};
        sstatus=new int[][]{{0,0,0},{0,0,0}};
        stats=new int[][]{{0,0,0,0},{0,0,0,0}};
        buffs=new int[][]{{0,0,0,0,0,0},{0,0,0,0,0,0}};
        mod[2]=1;
    }

    public void setTid(int set) {
        tid=set;
    }

    public void setUid(int set) {
        uid=set;
    }

    public void setPid(int set) {
        pid=set;
    }

    public void setSmd(int set) {
        smd=set;
    }

    public void setSml(int set) {
        sml=set;
    }

    public void verify() {
        vrfy=1;
    }

    public void app_stts() {
        app_stts++;
    }

    public void setMvnam(String set) {
        mvnam=set;
    }

    public void setUc(int[] set) {
        uc=set;
    }

    public void setMSet(int[] set) {
        mSet=set;
    }

    public void setMInfo(int[] set) {
        if(set.length==4)
            mInfo = set;
        else {
            for (int i = 0; i<3; i++) {
                mInfo[(i==0)?i:i+1] = set[i];
            }
            mInfo[1] = 0;
        }
    }

    public void setMod(int i, int set) {
        mod[i]=set;
    }

    public void setWthr(int set) {
        fill(wthr,0);
        wthr[set]=1;
    }

    public void setStatus(int t, int set) {
        fill(status[t],0);
        status[t][set]=1;
    }

    public void setSStatus(int t, int set) {
        sstatus[t][set]=(sstatus[t][set]==1)?0:1;
    }

    public void setStats(int target, int sttcd, int stat) {
        stats[target][sttcd]=stat;
    }

    public void setBuffs(int target, int bffcd, int buff) {
        buffs[target][bffcd]=buff;
    }

    public long getUser_id() {
        return user_id;
    }

    public long getUser_time() {
        return user_time;
    }

    public int getApp_stts() {
        return app_stts;
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

    public int getTid() {
        return tid;
    }

    public int getUid() {
        return uid;
    }

    public int getVrfy() {
        return vrfy;
    }

    public boolean isVerified() {
        return (vrfy==1);
    }

    public String getMvnam() {
        return mvnam;
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

    public String getWthrMsg() {
        return (wthr[0]==1)?"the weather was sunny":(wthr[1]==1)?"it was raining":(wthr[2]==1)?"there was a sandstorm":(wthr[3]==1)?"it was hailing":"the weather was normal";
    }

    public String getTrgtMsg() {
        return (mod[2]==1)?"the target was the only opponent affected by the move":"there "+((mod[2]==2)?"was 1 more opponent":"were 2 more opponents")+" other than the target affected by the move";
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
}