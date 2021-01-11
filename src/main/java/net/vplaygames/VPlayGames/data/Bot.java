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

import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.util.Array;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;

import static net.vplaygames.VPlayGames.data.GameData.*;

public class Bot
{
    public static Bot current;
    public JDA jda;
    public boolean closed;
    public long booted;
    public final long SELF_USER_ID;
    public final long BOT_OWNER;
    public final long MISS_SHANAYA;
    public final long FLASH_TGB;
    public final long[] botStaff;
    public long messagesProcessed;
    public long lastExceptionID;
    public static final String TOKEN = System.getenv("TOKEN");
    public final String LOCATION;
    public final String SELF_USER_TAG;
    public final String PREFIX;
    public final String SUPPORT_SERVER_INVITE;
    public final String INVALID_INPUTS;
    public final String[] COMMANDS;
    public final String[] STAFF_COMMANDS;
    public TextChannel logChan;
    public final HashMap<Long, Damage> DATA;
    public final HashMap<String,Damage> DAMAGE_CODES;

    public static final String BASE64 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-";

    public Bot(JDA jda) {
        current = this;
        closed = false;
        messagesProcessed = 1;
        lastExceptionID = 0;
        PREFIX = "v!";
        booted = System.currentTimeMillis();
        BOT_OWNER = 701660977258561557L;
        SELF_USER_ID = 747328310828204143L;
        this.jda = jda;
        MISS_SHANAYA = 679625927453704203L;
        FLASH_TGB = 685365867160010836L;
        SELF_USER_TAG = "V Play Games#5877";
        LOCATION = "LOCAL";
        COMMANDS = new String[]{"buff", "cd", "choose", "c", "moveis", "mi", "pm", "stat", "sml", "trainer", "verify", "view", "weather", "wthr", "status", "skill", "gauge", "ping", "invite", "dc", "search", "next", "n", "back", "b", "NA"};
        STAFF_COMMANDS = new String[]{"log", "debug", "close", "restart", "wipe", "request", "parseForId", "parse", "activate"};
        SUPPORT_SERVER_INVITE = "https://discord.gg/amvPsGU";
        INVALID_INPUTS = "Invalid Amount of Inputs!";
        DATA = new HashMap<>();
        DAMAGE_CODES = new HashMap<>();
        botStaff = new long[]{BOT_OWNER, SELF_USER_ID, MISS_SHANAYA, FLASH_TGB};
        initSamples();
    }

    public void setBooted() {
        booted=System.currentTimeMillis();
    }

    public void setLogChan(TextChannel c) {
        logChan = c;
    }

    public boolean isStaff(long uid) {
        return Array.contains(uid,botStaff);
    }

    public void initSamples() {
        Damage d=new Damage(SELF_USER_ID,SELF_USER_TAG,"SAMPLE",false);
        d.setTid(Array.returnID(trnrs, "red"))
                .setUc(tdabs[d.getTid()-1])
                .setUid(d.getUc()[1])
                .setPid(d.getUc()[1]%1000000)
                .setMSet(msets[d.getPid()%1000-1])
                .setSmd(d.getMSet()[0]/1000)
                .setMInfo(minfos[(d.getMSet()[0]-1)%1000])
                .setMoveName(moves[(d.getMSet()[0]-1)%1000])
                .setMod(2,1)
                .setSml(5)
                .setStats(0,1,420)
                .setStats(0,3,57)
                .setBuffs(0,1,6)
                .setMod(0,1)
                .setMod(1,1)
                .setWthr(0)
                .verify()
                .enable()
                .updateAppStatus()
                .updateAppStatus()
                .updateAppStatus()
                .updateAppStatus()
                .enableUpdates();
    }
}