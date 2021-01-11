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

import static net.vplaygames.VPlayGames.db.botresources.data;
import static net.vplaygames.VPlayGames.db.botresources.prefix;

public class MiscCommands extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw(),to_send,rslt;
        long aid=e.getAuthor().getIdLong();
        if(!e.getAuthor().isBot())
        {
            if(msg.startsWith(prefix+"moveis ")&&msg.length()>=prefix.length()+8)
                rslt = msg.substring(prefix.length()+7);
            else if(msg.startsWith(prefix+"mi ")&&msg.length()>=prefix.length()+4)
                rslt = msg.substring(prefix.length()+3);
            else {
                return;
            }
            if(data.containsKey(aid))
            {
                Damage d = data.get(aid);
                if(rslt.equals("critical hit")||rslt.equals("ch"))
                {
                    d.setMod(0,(d.getMod()[0]==1)?0:1);
                    to_send="Ok, I'll remember that the move was"+((d.getMod()[0]==0)?" not":"")+" critical hit.";
                } else if(rslt.equals("super effective")||rslt.equals("se"))
                {
                    d.setMod(1,(d.getMod()[1]==1)?0:1);
                    to_send="Ok, I'll remember that the move was"+((d.getMod()[1]==0)?" not":"")+" super effective.";
                } else
                    to_send="Invalid Modifier!";
                data.put(aid,d);
            } else
                to_send="Create a PM Damage Calculator App first!";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
}