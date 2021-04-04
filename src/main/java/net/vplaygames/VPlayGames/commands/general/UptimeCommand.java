/*
 * Copyright 2020-2021 Vaibhav Nargwani
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

import net.dv8tion.jda.api.EmbedBuilder;
import net.vplaygames.VPlayGames.commands.Command;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.util.MiscUtil;

import static net.vplaygames.VPlayGames.core.Bot.*;

public class UptimeCommand extends Command {
    public UptimeCommand() {
        super("uptime");
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        e.send(new EmbedBuilder()
            .addField("Uptime", MiscUtil.msToString(System.currentTimeMillis() - booted) + " (" + (System.currentTimeMillis() - booted) + " ms)", false)
            .setFooter("Bot booted " + timeAtBoot + (isStaff(e.getAuthor().getIdLong()) ? "\nLast refresh: " + lastRefresh : ""))
            .setColor(0x1abc9c).build(), "Bot Uptime").queue();
    }
}
