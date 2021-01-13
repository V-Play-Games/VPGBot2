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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;

public class BotStaffCommands {
    public static boolean process(GuildMessageReceivedEvent e) {
        new Response(e.getMessage());
        String[] msg = Commands.split(e.getMessage().getContentRaw());
        if (Bot.isStaff(e.getAuthor().getIdLong())) {
            Bot.commands.get(msg[0]).run(e);
        } else {
            MiscUtil.send(e, e.getAuthor().getAsMention() + ", You do not have the permission to do that!", true);
        }
        return true;
    }

    public static void activate(GuildMessageReceivedEvent e) {
        if (!e.getMessage().getContentRaw().equals(Bot.PREFIX+"activate")) return;
        if (!Bot.closed) return;
        if (!Bot.isStaff(e.getAuthor().getIdLong())) MiscUtil.send(e,"Access Denied!",true);
        Bot.closed = false;
        MiscUtil.send(e, "Thanks for activating me again!", true);
    }

    public static void putDamage(GuildMessageReceivedEvent e) {
        String MSG = e.getMessage().getContentRaw();
        String[] msg = MSG.split(" ");
        Bot.DATA.put(e.getAuthor().getIdLong(),
                Damage.parseFromString(MSG.substring(msg[0].length())));
        MiscUtil.send(e,"Put translation of "+MSG.substring(msg[0].length()),true);
    }
}
