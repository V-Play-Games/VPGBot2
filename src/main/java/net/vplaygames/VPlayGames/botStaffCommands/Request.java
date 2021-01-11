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

import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.Number;
import net.vplaygames.VPlayGames.util.Strings;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Request
{
    public static void process(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        switch (msg[1]) {
            case "log":
                switch (msg[2]) {
                    case "all": sendLogs(e.getChannel(),"\\A"); break;
                    case "first": sendLogs(e.getChannel(),"\\F"+msg[3]); break;
                    case "last": sendLogs(e.getChannel(),"\\L"+msg[3]); break;
                    default: sendLogs(e.getChannel(),msg[2]); break;
                }
        }
    }

    public static void sendLogs(TextChannel channel, String input)
    {
        long start=1,end= Bot.current.messagesProcessed;
        if (input.startsWith("\\F"))
            end=Math.min(Strings.toInt(input),end);
        else if(input.startsWith("\\L"))
            start=Math.max(Strings.toInt(input),1);
        else if(!input.startsWith("\\A")) {
            start= Number.roundInRange(Strings.toInt(input),1,end);
            end=start;
        }
        while (start<=end) {
            channel.sendMessage(Response.get(start).getAsEmbed(true)).queue();
            start++;
        }
    }
}
