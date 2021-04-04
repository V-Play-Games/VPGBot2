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
package net.vplaygames.VPlayGames.commands.botStaff;

import net.vplaygames.VPlayGames.commands.BotStaffCommand;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.core.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;
import org.json.simple.JSONObject;

public class RequestCommand extends BotStaffCommand {
    public RequestCommand() {
        super("request");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        switch (msg[1]) {
            case "logs":
                JSONObject toPost = new JSONObject();
                toPost.putAll(Bot.responses);
                e.send("Here!").addFile(MiscUtil.makeFileOf(toPost, "logs")).queue();
                toPost.clear();
                break;
            case "logFile":
                e.send("logFile").addFile(Bot.logFile).queue();
                break;
            case "errorFile":
                e.send("errorFile").addFile(Bot.errorFile).queue();
                break;
                /*
            case "damageData":
                writeDamageData();
                e.send("damageData").addFile(Bot.damageData).queue();
                break;
                */
            default:
                e.send("Access Denied!").queue();
        }
    }
}
