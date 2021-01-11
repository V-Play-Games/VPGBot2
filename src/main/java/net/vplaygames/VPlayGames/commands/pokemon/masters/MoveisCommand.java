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
import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.data.Bot;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MoveisCommand
{
    public static void process(GuildMessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw(),to_send,
                rslt=msg.substring(msg.split(" ")[0].length()+1);
        long aid=e.getAuthor().getIdLong();
        if(Bot.current.DATA.containsKey(aid))
        {
            Damage d = Bot.current.DATA.get(aid);
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
            Bot.current.DATA.put(aid,d);
        } else
            to_send="Create a PM Damage Calculator App first!";
        Response.get(Bot.current.messagesProcessed).Responded(to_send);
        e.getChannel().sendMessage(to_send).queue();
    }
}