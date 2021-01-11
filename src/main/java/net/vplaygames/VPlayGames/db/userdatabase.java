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

import net.vplaygames.VPlayGames.util.Array;

import static net.vplaygames.VPlayGames.util.Array.returnID;

public class userdatabase
{
    public static int limit = 100;
    public static long[] user_ids = new long[limit], user_times = new long[limit];
    public static int[] tids = new int[limit], uids = new int[limit], pids = new int[limit], smds = new int[limit], app_stts = new int[limit], smls = new int[limit], vrfy = new int[limit],
    template={0,0,0,0};
    public static int[][] mSets = new int[limit][], ucs = new int[limit][], mInfos = new int[limit][],stats_u = new int[limit][4],stats_t = new int[limit][4],mods = new int[limit][],wthrs = new int[limit][];
    public static String[] mvnams = new String[limit];
    public static boolean isRegistered(long user_id)
    {
        return user_ids[returnID(user_ids, user_id)]!=0;
    }
    public static void register(long user_id)
    {
        int user_pv = Array.nearestFreeIndex(user_ids);
        user_ids[user_pv] = user_id;
        user_times[user_pv] = System.currentTimeMillis();
        ucs[user_pv]=new int[4];
        mSets[user_pv]=new int[4];
        mInfos[user_pv]=new int[4];
        mods[user_pv]=new int[4];
        wthrs[user_pv]=new int[4];
        for(int i:wthrs[user_pv]){wthrs[user_pv][i]=2;}
        stats_u[user_pv]=new int[4];
        stats_t[user_pv]=new int[4];
    }
    public static void unregister(long user_id)
    {
        int user_pv = returnID(user_ids,user_id);
        user_ids[user_pv]=0;
        user_times[user_pv]=0;
        tids[user_pv]=0;
        uids[user_pv]=0;
        pids[user_pv]=0;
        smds[user_pv]=0;
        smls[user_pv]=0;
        vrfy[user_pv]=0;
        mvnams[user_pv]="";
        app_stts[user_pv]=0;
        for(int i:ucs[user_pv]){ucs[user_pv][i]=0;}
        for(int i:mInfos[user_pv]){mInfos[user_pv][i]=0;}
        for(int i:mSets[user_pv]){mSets[user_pv][i]=0;}
        for(int i:mods[user_pv]){mods[user_pv][i]=0;}
        for(int i:wthrs[user_pv]){wthrs[user_pv][i]=0;}
        for(int i:stats_u[user_pv]){stats_u[user_pv][i]=0;}
        for(int i:stats_t[user_pv]){stats_t[user_pv][i]=0;}
    }
}