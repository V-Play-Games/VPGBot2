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
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.processors.EventHandler;
import net.vplaygames.VPlayGames.util.MiscUtil;

public class WipeCommand extends BotStaffCommand {
    public WipeCommand() {
        super("wipe");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        Bot.DATA.clear();
        Bot.DAMAGE_CODES.clear();
        MiscUtil.send(EventHandler.getCurrent(),"All Data Wiped!",true);
    }
}
