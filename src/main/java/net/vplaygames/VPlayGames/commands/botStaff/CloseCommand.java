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

public class CloseCommand extends BotStaffCommand {
    public CloseCommand() {
        super("close");
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        e.getJDA().openPrivateChannelById(Bot.BOT_OWNER)
            .flatMap(pc -> pc.sendMessage(e.getAuthor().getAsTag() + " [" + e.getAuthor().getIdLong() + "] closed me!! ["+e.getMessage().getJumpUrl()+"]"))
            .flatMap(m -> e.send("Successfully Closed Event Manger!"))
            .queue();
        System.out.println(e.getAuthor().getAsTag() + " [" + e.getAuthor().getIdLong() + "] closed me!!");
        Bot.closed = true;
    }
}
