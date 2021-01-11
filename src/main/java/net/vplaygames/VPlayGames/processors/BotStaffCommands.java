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

import net.vplaygames.VPlayGames.botStaffCommands.*;
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.Array;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class BotStaffCommands
{
    public static boolean process(GuildMessageReceivedEvent e) {
        Bot bot = Bot.current;
        String MSG = e.getMessage().getContentRaw().substring(bot.PREFIX.length());
        String[] msg = MSG.split(" ");
        if (bot.isStaff(e.getAuthor().getIdLong())) {
            switch (Array.returnID(bot.STAFF_COMMANDS, msg[0])) {
                case 0: LogCommand.process(e); break;
                case 1: DebugCommand.process(e); break;
                case 2: TerminatingCommand.Terminate(e); break;
                case 3: ResetCommand.Reset(""); break;
                case 4: Wipe.all(); break;
                case 5: Request.process(e); break;
                case 6: Parse.process(e); break;
                case 7: putDamage(e); break;
                case 8: activate(e); break;
            }
        } else {
            new Response(e.getMessage());
            String s = e.getAuthor().getAsMention() + ", You do not have the permission to do that!";
            MiscUtil.send(e, s, true);
            return true;
        }
        return false;
    }

    public static void activate(GuildMessageReceivedEvent e) {
        if (!e.getMessage().getContentRaw().equals(Bot.current.PREFIX+"activate")) return;
        String s;
        if (e.getAuthor().getIdLong() == Bot.current.BOT_OWNER) {
            Bot.current.closed = false;
            s = "Thanks for activating me again!";
        } else {
            s = e.getAuthor().getAsMention() + ", You do not have the permission to do that!";
        }
        e.getChannel().sendMessage(s).queue();
    }

    public static void putDamage(GuildMessageReceivedEvent e) {
        String MSG = e.getMessage().getContentRaw();
        String[] msg = MSG.split(" ");
        Bot.current.DATA.put(
                e.getAuthor().getIdLong(),
                Damage.parseFromString(MSG.substring(msg[0].length())));
    }
}
