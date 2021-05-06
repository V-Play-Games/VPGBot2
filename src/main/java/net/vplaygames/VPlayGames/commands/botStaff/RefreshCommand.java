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

import java.time.Instant;

import static net.vplaygames.VPlayGames.core.Bot.*;

public class RefreshCommand extends BotStaffCommand {
    public RefreshCommand() {
        super("refresh");
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        e.forceNotLog();
        e.send("Trying a reset!").queue(message -> timer.execute(() -> {
            long startedAt = System.currentTimeMillis();
            Instant oldInstant = instantAtBoot;
            System.out.println("Shutting Down");
            e.getJDA().shutdown();
            Bot.rebootTasks = () -> {
                rebooted = true;
                Bot.getJDA().getTextChannelById(e.getChannel().getIdLong())
                    .editMessageById(message.getIdLong(),
                        "Reset Successfully Completed! Took " + (System.currentTimeMillis() - startedAt) + "ms")
                    .queue();
                instantAtBoot = oldInstant;
                lastRefresh = MiscUtil.dateTimeNow();
            };
            while (true) {
                try {
                    Bot.start();
                    return;
                } catch (Exception ignored) {
                }
            }
        }));
    }
}
