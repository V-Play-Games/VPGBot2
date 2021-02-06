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
package net.vplaygames.VPlayGames.commands.botStaff;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;

public class LogCommand extends BotStaffCommand {
    public LogCommand() {
        super("log");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        TextChannel logChan = Bot.logChannel;
        String[] s = {e.getAuthor().getAsMention() + " is trying to change the log channel from %s to %s"};
        e.getJDA().retrieveUserById(Bot.BOT_OWNER).queue(u -> s[0] += ", " + u.getAsMention() + "!");
        if (Strings.equalsAny(msg[1], "open", "close")) {
            s[0] = String.format(s[0], logChan == null ? "null" : logChan.getAsMention(), msg[1].equals("open") ? e.getChannel().getAsMention() : "null");
            if (logChan != null) logChan.sendMessage(s[0]).queue();
            e.getJDA().retrieveUserById(Bot.BOT_OWNER).queue(u ->
                u.openPrivateChannel().queue(pc ->
                    pc.sendMessage(s[0]).queue(m ->
                        Bot.setLogChannel(msg[1].equals("open") ? e.getChannel() : null)
                    )
                )
            );
            s[0] = "Log " + msg[1] + "ed in " + e.getChannel().getAsMention();
        } else
            s[0] = "Wrong syntax!\nSyntax for " + Bot.PREFIX + "log command: ``" + Bot.PREFIX + "log open/close``";
        MiscUtil.send(e, s[0], true);
    }
}
