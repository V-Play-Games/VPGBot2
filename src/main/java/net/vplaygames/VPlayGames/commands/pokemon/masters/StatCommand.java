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
package net.vplaygames.VPlayGames.commands.pokemon.masters;

import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class StatCommand
{
    public static void process(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String to_send;
        int tstr;
        long aid=e.getAuthor().getIdLong();
        if (Bot.current.DATA.containsKey(aid))
        {
            if(Bot.current.DATA.get(aid).getAppStatus()>1)
            {
                if(msg.length>=4)
                {
                    tstr= Strings.toInt(msg[3]);
                    to_send=returnStatMsg(msg[2],msg[1],tstr,aid);
                } else
                    to_send="Not enough inputs!";
            } else
                to_send="Please choose a Sync Pair first!";
        } else
            to_send="Start a Pokemon Masters Damage Calculation Application first!";
        MiscUtil.send(e,to_send,true);
    }

    public static String returnStatMsg(String m, String target, int s, long aid) {
        Damage d = Bot.current.DATA.get(aid);
        String stt_nm;
        int t;
        switch (target) {
            case "user":
            case "u":
                t=0;
                break;
            case "target":
            case "t":
                t=1;
                break;
            default:
                return "Cannot find \""+target+"\" entry in list \"stats\"";
        }
        if(s<1)
            return "Invalid Stat! Stat cannot be in negative!";
        switch (m) {
            case "att":
                d.setStats(t,0,s);
                stt_nm="attack";
                break;
            case "spa":
                d.setStats(t,1,s);
                stt_nm="special attack";
                break;
            case "def":
                d.setStats(t,2,s);
                stt_nm="defense";
                break;
            case "spd":
                d.setStats(t,3,s);
                stt_nm="special defense";
                break;
            default:
                return "Cannot find sub-entry \""+m+"\" in entry \""+target+"\" in list \"stats\"";
        }
        Bot.current.DATA.put(aid,d);
        return "Set the "+((t==1)?"target":"**"+MiscUtil.returnSP(d.getUid())+"**")+"'s "+stt_nm+" stat to "+s+"!";
    }
}
