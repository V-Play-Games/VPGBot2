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

public class PMCommand
{
    public static void PMCommand(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        long aid = e.getAuthor().getIdLong();
        String to_send;
        if(msg[1].equals("start"))
        {
            if (!data.containsKey(aid)) {
                data.put(aid,new Damage(e));
                to_send = "A new PM Damage Calculation Application has been created by " + e.getAuthor().getAsMention();
            } else
                to_send = "A PM Damage Calculator App is already open.";
        } else if (msg[1].equals("end"))
        {
            if (data.containsKey(aid)) {
                data.remove(aid);
                to_send = e.getAuthor().getAsMention() + " has deleted their PM Damage Calculation Application.";
            } else
                to_send = e.getAuthor().getAsMention() + ", I can't find your PM Damage Calculation Application.";
        } else
            to_send="Invalid Input.";
        e.getChannel().sendMessage(to_send).queue();
    }
}