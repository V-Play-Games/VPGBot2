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
package net.vplaygames.VPlayGames.commands.general;

import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.StringJoiner;

import static net.vplaygames.VPlayGames.data.Bot.*;

public class UptimeCommand extends Command {
    public UptimeCommand() {
        super("uptime");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        EmbedBuilder eb = new EmbedBuilder();
        String[] uptimeArr = MiscUtil.msToString(System.currentTimeMillis() - booted).split(" ");
        StringJoiner var0 = new StringJoiner(" ");
        for (int i = 0; i < uptimeArr.length - 1; i++) {
            var0.add(uptimeArr[i]);
        }
        eb.addField("Uptime", var0 + " (" + (System.currentTimeMillis() - booted) + " ms)", false);
        var0 = new StringJoiner("",isStaff(e.getAuthor().getIdLong()) ? "\nLast refresh: " + lastRefresh : "","");
        eb.setFooter("Bot booted " + timeAtBoot + var0);
        eb.setColor(0x1abc9c);
        e.getChannel().sendMessage(eb.build()).queue();
        Response.get(messagesProcessed).responded("Bot Uptime");
    }
}
