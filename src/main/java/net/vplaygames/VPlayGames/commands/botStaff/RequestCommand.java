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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;

import static net.vplaygames.VPlayGames.util.MiscUtil.writeDamageData;

public class RequestCommand extends Command {
    public RequestCommand() {
        super("request");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        switch (msg[1]) {
            case "logs":
                for (int i = 1; i <= Bot.messagesProcessed; i++) {
                    e.getChannel().sendMessage(Response.get(i).getAsEmbed(true)).queue();
                }
                MiscUtil.send(e,"Sent all requests!",true);
                break;
            case "errors":
                Response.getExceptionLog().forEach((k, v) -> e.getChannel().sendMessage(v).queue());
                break;
            case "logFile":
                e.getChannel().sendMessage("logFile").addFile(Bot.logFile).queue();
                break;
            case "errorFile":
                e.getChannel().sendMessage("errorFile").addFile(Bot.errorFile).queue();
                break;
            case "damageData":
                writeDamageData();
                e.getChannel().sendMessage("damageData").addFile(Bot.damageData).queue();
                break;
            default:
                e.getChannel().sendMessage("Access Denied!").queue();
        }
    }
}
