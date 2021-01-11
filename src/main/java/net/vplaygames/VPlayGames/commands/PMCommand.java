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

import static net.vplaygames.VPlayGames.db.botresources.prefix;
import static net.vplaygames.VPlayGames.db.userdatabase.*;

public class PMCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String to_send;
        if(!e.getAuthor().isBot()&&msg[0].equals(prefix + "pm"))
        {
            if(msg[1].equals("start"))
            {
                if (!isRegistered(e.getAuthor().getIdLong())) {
                    to_send = "A new Damage Calculation Application has been created by " + e.getAuthor().getAsMention();
                    register(e.getAuthor().getIdLong());
                } else
                    to_send = "A PM Damage Calculator App is already open.";
            } else if (msg[1].equals("end"))
            {
                if (isRegistered(e.getAuthor().getIdLong())) {
                    to_send = e.getAuthor().getAsMention() + " has deleted their Damage Calculation Application.";
                    unregister(e.getAuthor().getIdLong());
                } else
                    to_send = e.getAuthor().getAsMention() + ", I can't find your Damage Calculation Application.";
            } else
                to_send="Invalid Input.";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
}