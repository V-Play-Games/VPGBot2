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

import static net.vplaygames.VPlayGames.db.botresources.data;

public class StatusCommand
{
    public static void StatusCommand(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        if (msg.length!=3) {
            e.getChannel().sendMessage("Invalid Amount of inputs.").queue();
            return;
        }
        String to_send="OK! So, the target was ";
        long aid=e.getAuthor().getIdLong();
        if(data.containsKey(aid))
        {
            int t;
            switch (msg[1]) {
                case "user": case "u": t=0; break;
                case "target": case "t": t=1; break;
                default: e.getChannel().sendMessage("Cannot find \""+msg[1]+"\" entry in list \"status\"").queue(); return;
            }
            Damage d = data.get(aid);
            switch (msg[2]) {
                case "paralyzed":
                case "paralyze":
                case "par":
                    d.setStatus(t,0);
                    to_send+= "paralyzed";
                    break;
                case "asleep":
                case "sleep":
                case "sleeping":
                case "slp":
                    d.setStatus(t,1);
                    to_send+= "sleeping";
                    break;
                case "poisoned":
                case "poison":
                case "psn":
                    d.setStatus(t,2);
                    to_send+= "poisoned";
                    break;
                case "badly-poisoned":
                case "bdp":
                case "tox":
                    d.setStatus(t,3);
                    to_send+= "badly poisoned";
                    break;
                case "burnt":
                case "burn":
                case "brn":
                    d.setStatus(t,4);
                    to_send+= "burnt";
                    break;
                case "freeze":
                case "frozen":
                case "frz":
                    d.setStatus(t,5);
                    to_send+= "frozen";
                    break;
                case "trapped":
                case "trap":
                case "trp":
                    d.setSStatus(t,0);
                    to_send+= ((d.getSStatus()[t][0]==0)?"not ":"")+"trapped";
                    break;
                case "flinching":
                case "fliched":
                case "flinch":
                case "fln":
                    d.setSStatus(t,1);
                    to_send+= ((d.getSStatus()[t][1]==0)?"not ":"")+"flinched";
                    break;
                case "confused":
                case "confuse":
                case "cnf":
                    d.setSStatus(t,2);
                    to_send+= ((d.getSStatus()[t][2]==0)?"not ":"")+"confused";
                    break;
                default:
                    to_send = "That is a very weird status";
            }
        } else
            to_send="Create a PM Damage Calculator App first";
        e.getChannel().sendMessage(to_send+".").queue();
    }
}
