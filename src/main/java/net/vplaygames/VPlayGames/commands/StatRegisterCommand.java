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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vplaygames.VPlayGames.util.Array;

import static net.vplaygames.VPlayGames.commands.TrainerCommand.returnSP;
import static net.vplaygames.VPlayGames.db.botresources.prefix;
import static net.vplaygames.VPlayGames.db.userdatabase.*;

public class StatRegisterCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String to_send;
        int tstr,user_pv=Array.returnID(user_ids,e.getAuthor().getIdLong());
        if (msg[0].equals(prefix+"stat"))
        {
            if (isRegistered(e.getAuthor().getIdLong()))
            {
                if(app_stts[user_pv]>1)
                {
                    if(msg.length>=4)
                    {
                        tstr=Integer.parseInt(msg[3]);
                        to_send=returnStatMsg(msg[2],tstr,user_pv,msg[1]);
                    } else
                        to_send="Not enough inputs!";
                } else
                    to_send="Please choose a Sync Pair first!";
            } else
                to_send="Start a Pokemon Masters Damage Calculation Application first!";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
    public static String returnStatMsg(String m, int t, int u, String target)
    {
        int trgt;
        switch (target) {
            case "user":
                trgt=0;
                break;
            case "target":
                trgt=1;
                break;
            default:
                return "Cannot find \""+target+"\" entry in list \"stats\"";
        }
        if(t<1)
            return "Invalid Stat! Stat cannot be in negative!";
        switch (m) {
            case "att":
                ((trgt==1)?stats_t:stats_u)[u][0]=t;
                return "Set the "+((trgt==1)?"target":"``"+returnSP(uids[u])+"``")+"'s attack stat to "+t+"!";
            case "spa":
                ((trgt==1)?stats_t:stats_u)[u][1]=t;
                return "Set the "+((trgt==1)?"target":"``"+returnSP(uids[u])+"``")+"'s special attack stat to "+t+"!";
            case "def":
                ((trgt==1)?stats_t:stats_u)[u][2]=t;
                return "Set the "+((trgt==1)?"target":"``"+returnSP(uids[u])+"``")+"'s defense stat to "+t+"!";
            case "spd":
                ((trgt==1)?stats_t:stats_u)[u][3]=t;
                return "Set the "+((trgt==1)?"target":"``"+returnSP(uids[u])+"``")+"'s special defense stat to "+t+"!";
            default:
                return "Cannot find sub-entry \""+m+"\" in entry \""+target+"\" in list \"stats\"";
        }
    }
}
