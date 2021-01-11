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
package net.vplaygames.VPlayGames.BotStaffCommands;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static net.vplaygames.VPlayGames.db.botresources.*;

public class LogCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        if(!isStaff(e.getAuthor().getIdLong()))
            return;
        if(e.getMessage().getContentRaw().equals("v!log open"))
        {
            if(logChan!=null)
                logChan.sendMessage(e.getAuthor().getAsMention()+" is trying to change the log channel, "+e.getJDA().getUserById(BOT_OWNER).getAsMention()+" from "+logChan.getAsMention()+" to "+e.getChannel().getAsMention()+"!").queue();
            setLogChan(e.getChannel());
            e.getChannel().sendMessage("Log Opened in "+e.getChannel().getAsMention()).queue();
        }
        if(e.getMessage().getContentRaw().equals("v!log close"))
        {
            if(logChan!=null)
                logChan.sendMessage(e.getAuthor().getAsMention()+" is trying to change the log channel, "+e.getJDA().getUserById(BOT_OWNER).getAsMention()+" from "+logChan.getAsMention()+" to null!!!").queue();
            setLogChan(null);
            e.getChannel().sendMessage("Log Closed in "+e.getChannel().getAsMention()).queue();
        }
    }
}
