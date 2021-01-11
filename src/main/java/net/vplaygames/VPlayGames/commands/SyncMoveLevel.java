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

import static net.vplaygames.VPlayGames.commands.TrainerCommand.returnSP;
import static net.vplaygames.VPlayGames.db.botresources.data;

public class SyncMoveLevel
{
    public static void SyncMoveLevel(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String to_send;
        int tstr;
        long aid=e.getAuthor().getIdLong();
        if(data.containsKey(aid))
        {
            Damage d = data.get(aid);
            if(d.getApp_stts()>1)
            {
                tstr = Integer.parseInt(msg[1]);
                if (tstr < 1 || tstr > 5)
                    to_send = "Invalid Sync Move Level!";
                else {
                    d.setSml(tstr);
                    to_send="The Sync Move Level of \""+returnSP(d.getUid())+"\" is set to "+tstr+".";
                }
            } else
                to_send="Choose a sync pair first!";
            data.put(aid,d);
        } else
            to_send="Start a Pokemon Masters Damage Calculation Application first!";
        e.getChannel().sendMessage(to_send).queue();
    }
}