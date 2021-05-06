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

public class WipeCommand extends BotStaffCommand {
    public WipeCommand() {
        super("wipe", 0, null, 1, 1);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        switch (e.getArg(1)) {
            case "data":
                Bot.DATA.clear();
                break;
            case "codes":
                Bot.DAMAGE_CODES.clear();
                break;
            case "all":
                Bot.DATA.clear();
                Bot.DAMAGE_CODES.clear();
            case "ratelimit":
                Bot.commands.forEach((commandName, command) -> command.getRateLimited().clear());
                break;
        }
        e.send("Data Wiped!").queue();
    }
}
