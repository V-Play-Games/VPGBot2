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
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PMCommand
{
    public static void process(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        if (msg.length!=2) {
            MiscUtil.send(e, Bot.current.INVALID_INPUTS,true);
            return;
        }
        long aid = e.getAuthor().getIdLong();
        String to_send;
        if(msg[1].equals("start")) {
            if (!Bot.current.DATA.containsKey(aid)) {
                Bot.current.DATA.put(aid,new Damage(e));
                to_send = e.getAuthor().getAsMention()+", has created a new PM Damage Calculation Application.";
            } else
                to_send = "A PM Damage Calculator App is already open.";
        } else if (msg[1].equals("end")) {
            if (Bot.current.DATA.containsKey(aid)) {
                if (!Bot.current.DATA.get(aid).isEnabled()) Bot.current.DAMAGE_CODES.remove(Bot.current.DATA.get(aid).getCode());
                Bot.current.DATA.remove(aid);
                to_send = e.getAuthor().getAsMention() + " has deleted their PM Damage Calculation Application.";
            } else
                to_send = e.getAuthor().getAsMention() + ", I can't find your PM Damage Calculation Application.";
        } else
            to_send="Invalid Input.";
        MiscUtil.send(e,to_send,true);
    }
}