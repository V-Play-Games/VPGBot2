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

public class BuffCommand
{
    public static void process(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String s;
        long aid=e.getAuthor().getIdLong();
        if (Bot.current.DATA.containsKey(aid))
        {
            if(Bot.current.DATA.get(aid).getAppStatus()>1)
            {
                if(msg.length>=4)
                    s=returnBuffMsg(msg[2],msg[1], Strings.toInt(msg[3]),aid);
                else
                    s="Not enough Inputs";
            } else
                s="Please choose a Sync Pair first!";
        } else
            s="Start a Pokemon Masters Damage Calculation Application first!";
        MiscUtil.send(e,s,true);
    }

    public static String returnBuffMsg(String m, String target, int b, long aid)
    {
        Damage d = Bot.current.DATA.get(aid);
        String bff_nm;
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
                return "Cannot find \""+target+"\" entry in list \"buffs\"";
        }
        if(b<-6||b>6)
            return "Invalid Stat! "+((b<0)?"":"+")+b+" stat buff not possible.";
        switch (m) {
            case "att":
                d.setBuffs(t,0,b);
                bff_nm="attack";
                break;
            case "spa":
                d.setBuffs(t,1,b);
                bff_nm="special attack";
                break;
            case "def":
                d.setBuffs(t,2,b);
                bff_nm="defense";
                break;
            case "spd":
                d.setBuffs(t,3,b);
                bff_nm="special defense";
                break;
            case "spe":
                d.setBuffs(t,4,b);
                bff_nm="speed";
                break;
            case "acc":
                d.setBuffs(t,5,b);
                bff_nm="accuracy";
                break;
            case "eva":
                d.setBuffs(t,6,b);
                bff_nm="evasiveness";
                break;
            default:
                return "Cannot find sub-entry \""+m+"\" in entry \""+target+"\" in list \"stats\"";
        }
        Bot.current.DATA.put(aid,d);
        return "Set the "+((t==1)?"target":"**"+MiscUtil.returnSP(d.getUid())+"**")+"'s "+bff_nm+" stat buff to "+b+"!";
    }
}