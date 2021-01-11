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

import static net.vplaygames.VPlayGames.db.botresources.*;

public class TerminatingCommands extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw();
        if (msg.equals(prefix+"byeeeeeeeeee")||msg.equalsIgnoreCase(prefix+"terminate"))
        {
            if (e.getAuthor().getIdLong()==BOT_OWNER) {
                e.getChannel().sendMessage("Successfully Terminated!!").complete();
                e.getJDA().shutdown();
            } else
                e.getChannel().sendMessage(e.getAuthor().getIdLong()+"You do not have the permission to do that!").queue();
        }
    }
}
