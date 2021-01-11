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
package net.vplaygames.VPlayGames.botStaffCommands;

import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Objects;

public class LogCommand
{
    public static void process(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        TextChannel logChan = Bot.current.logChan;
        String s=e.getAuthor().getAsMention()+" is trying to change the log channel from %s to %s, "+ Objects.requireNonNull(e.getJDA().getUserById(Bot.current.BOT_OWNER)).getAsMention()+"!";
        if(Strings.equalsAny(msg[1],"open","close"))
        {
            s=String.format(s,logChan==null?"null":logChan.getAsMention(),msg[1].equals("open")?e.getChannel().getAsMention():"null");
            if(logChan!=null) logChan.sendMessage(s).queue();
            Objects.requireNonNull(e.getJDA().getUserById(Bot.current.BOT_OWNER)).openPrivateChannel().complete().sendMessage(s).queue();
            Bot.current.setLogChan(msg[1].equals("open")?e.getChannel():null);
            s="Log "+msg[1]+"ed in "+e.getChannel().getAsMention();
        } else
            s="Wrong syntax!\nSyntax for "+Bot.current.PREFIX+"log command: ``"+Bot.current.PREFIX+"log open/close``";
        MiscUtil.send(e,s,true);
    }
}
