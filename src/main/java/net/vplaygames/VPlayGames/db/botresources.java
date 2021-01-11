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

import net.dv8tion.jda.api.entities.TextChannel;
import net.vplaygames.VPlayGames.util.Array;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class botresources
{
    public static final long BOT_OWNER = 701660977258561557L;
    public static final String TOKEN = System.getenv("TOKEN");
    public static TextChannel logChan = null;
    public static String prefix = "v!";
    public static long booted;
    public static Map<Long,Damage> data = new HashMap<>();
    public static long[] botStaff;
    public static void setBooted() {
        booted=System.currentTimeMillis();
    }
    public static void setLogChan(TextChannel c) {
        logChan = c;
    }
    public static void initStaff() {
        botStaff = new long[10];
        Arrays.fill(botStaff, 0);
        botStaff[botStaff.length-1] = 28;
    }
    public static boolean isStaff(long aid) {
        return botStaff[Array.returnID(botStaff, aid)]!=28;
    }
    public static void newStaff(long aid) {
        botStaff[Array.returnID(botStaff,0)] = aid;
    }
}
