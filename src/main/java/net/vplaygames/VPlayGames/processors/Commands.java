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
package net.vplaygames.VPlayGames.processors;

import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.Array;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Commands
{
    public static boolean process(GuildMessageReceivedEvent e)
    {
        String MSG=e.getMessage().getContentRaw();
        boolean resp=false;
        if (MSG.startsWith(Bot.current.PREFIX)&&MSG.length()>Bot.current.PREFIX.length()) {
            String[] msg=MSG.substring(Bot.current.PREFIX.length()).split(" ");
            if (Array.contains(msg[0],Bot.current.STAFF_COMMANDS))
                resp=BotStaffCommands.process(e);
            if (Array.contains(msg[0],Bot.current.COMMANDS))
                resp=resp||CommonCommands.process(e);
        }
        return resp;
    }
}
