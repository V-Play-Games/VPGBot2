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

import static net.vplaygames.VPlayGames.db.botresources.prefix;
import static net.vplaygames.VPlayGames.db.userdatabase.*;

public class MiscCommands extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw();
        String to_send;
        int user_pv = Array.returnID(user_ids,e.getAuthor().getIdLong());
        if(msg.startsWith(prefix+"moveis "))
        {
            if(isRegistered(e.getAuthor().getIdLong()))
            {
                try {
                    if(msg.substring(prefix.length()+7).equalsIgnoreCase("Critical Hit")||msg.substring(prefix.length()+7).equalsIgnoreCase("CH"))
                    {
                        if(mods[user_pv][0]==1)
                            to_send="I know that the move is critical hit.";
                        else
                        {
                            to_send="Ok, I'll remember that the move was critical hit.";
                            mods[user_pv][0]=1;
                        }
                    } else if(msg.substring(prefix.length()+7).equalsIgnoreCase("Super Effective")||msg.substring(prefix.length()+7).equalsIgnoreCase("SE"))
                    {
                        if(mods[user_pv][1]==1)
                            to_send="I know that the move is super effective.";
                        else
                        {
                            to_send="Ok, I'll remember that the move was super effective.";
                            mods[user_pv][1]=1;
                        }
                    } else
                        to_send="Invalid Modifier!";
                } catch (Error err) {
                    to_send="Not Enough inputs";
                }
            } else
                to_send="Create a PM Damage Calculator App first!";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
}