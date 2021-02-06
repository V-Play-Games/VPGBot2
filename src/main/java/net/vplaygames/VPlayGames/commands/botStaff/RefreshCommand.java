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

import com.vplaygames.PM4J.caches.PokemasDBCache;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.Driver;
import net.vplaygames.VPlayGames.processors.EventHandler;
import net.vplaygames.VPlayGames.util.MiscUtil;

import static net.vplaygames.VPlayGames.data.Bot.*;

public class RefreshCommand extends BotStaffCommand {
    public RefreshCommand() {
        super("refresh");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        lightRefresh();
        retry(PokemasDBCache.getInstance().invalidateCaches(), p -> retry(p.process(), r -> {}));
    }

    public static void lightRefresh() {
        try {
            System.out.println("Trying a reset!");
            {
                long oldBooted = booted;
                jda.shutdownNow();
                rebooted = true;
                Driver.main();
                booted = oldBooted;
                lastRefresh = MiscUtil.dateTimeNow();
            }
            System.out.println("Reset Successfully Completed!");
        } catch (Exception exc) {
            System.out.println("Reset Failed! Retrying in 10s!");
            EventHandler.getInstance().process(exc);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Sleep Interrupted!");
            }
            lightRefresh();
        }
    }
}