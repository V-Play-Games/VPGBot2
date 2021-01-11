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

public class SyncMoveLevel extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw();
        int tstr,user_pv=Array.returnID(user_ids,e.getAuthor().getIdLong());
        if(msg.startsWith(prefix+"sml "))
        {
            String to_send;
            if(isRegistered(e.getAuthor().getIdLong()))
            {
                if(app_stts[user_pv]>1)
                {
                    try {
                        tstr = Integer.parseInt(Character.toString(msg.charAt(prefix.length() + 4)));
                        if (tstr < 1 || tstr > 5)
                            to_send = "Invalid Sync Move Level!";
                        else {
                            smls[user_pv] = tstr;
                            to_send="The Sync Move Level of \""+returnSP(uids[user_pv])+"\" is set to "+tstr+".";
                        }
                    } catch (Error err) {
                        to_send = "Not Enough Inputs!";
                    }
                } else
                    to_send="Choose a sync pair first!";
            } else
                to_send="Start a Pokemon Masters Damage Calculation Application first!";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
}