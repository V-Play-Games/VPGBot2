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
package net.vplaygames.VPlayGames.commands;

import net.vplaygames.VPlayGames.db.Damage;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static net.vplaygames.VPlayGames.commands.TrainerCommand.returnSP;
import static net.vplaygames.VPlayGames.db.botresources.data;
import static net.vplaygames.VPlayGames.db.botresources.prefix;

public class StatRegisterCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String to_send;
        int tstr;
        long aid=e.getAuthor().getIdLong();
        if (!e.getAuthor().isBot()&&msg[0].equals(prefix+"stat"))
        {
            if (data.containsKey(aid))
            {
                if(data.get(aid).getApp_stts()>1)
                {
                    if(msg.length>=4)
                    {
                        tstr=Integer.parseInt(msg[3]);
                        to_send=returnStatMsg(msg[2],msg[1],tstr,aid);
                    } else
                        to_send="Not enough inputs!";
                } else
                    to_send="Please choose a Sync Pair first!";
            } else
                to_send="Start a Pokemon Masters Damage Calculation Application first!";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
    public static String returnStatMsg(String m, String target, int s, long aid)
    {
        Damage d = data.get(aid);
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
        data.put(aid,d);
        return "Set the "+((t==1)?"target":"**"+returnSP(d.getUid())+"**")+"'s "+stt_nm+" stat to "+s+"!";
    }
}
