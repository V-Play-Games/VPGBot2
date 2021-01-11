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

public class Damage
{
    long user_id;
    long user_time;
    int tid,uid,pid,smd,sml,vrfy,app_stts;
    String mvnam;
    int[] uc,mSet,mInfo,mod,wthr;
    int[][] stats={{0,0,0,0},{0,0,0,0}};

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
        mod=new int[3];
        wthr=new int[4];
        for (int i:wthr){wthr[i]=2;}
        for (int i:mod){mod[i]=0;}
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

    public void setWthr(int j) {
        for (int i:wthr){wthr[i]=0;}
        wthr[j]=1;
    }

    public void setStats(int target, int sttcd, int stat) {
        stats[target][sttcd]=stat;
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

    public int[][] getStats() {
        return stats;
    }
}