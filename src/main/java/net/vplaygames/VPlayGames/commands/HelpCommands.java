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

public class HelpCommands extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw();
        if (msg.equals(prefix+"help pm")&&!e.getAuthor().isBot())
        {
            e.getChannel().sendMessage("Sent a help message in your DMs, "+e.getAuthor().getAsMention()+"!").queue();
            e.getAuthor().openPrivateChannel().complete().sendMessage("Help for the command \""+prefix+"pm\"\n" +
                    "Command: "+prefix+"pm\n" +
                    "Inputs:\n1. start - Starts a new Pokemon Masters Damage Calculation Application.\n2. end - Ends the currently running Pokemon Masters Damage Calculation Application.\n\n" +
                    "Usage Examples: ``"+prefix+"pm start``,``"+prefix+"pm end``").queue();
        }
    }
}
