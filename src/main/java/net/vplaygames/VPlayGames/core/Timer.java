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
package net.vplaygames.VPlayGames.core;

import com.vplaygames.PM4J.Logger;
import net.vplaygames.VPlayGames.data.Bot;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static net.vplaygames.VPlayGames.data.Bot.syncCount;
import static net.vplaygames.VPlayGames.util.MiscUtil.writeDamageData;

public class Timer {
    static ScheduledThreadPoolExecutor timer;
    
    public static void newTimer() {
        if (timer != null)
            timer.shutdown();
        (timer = new ScheduledThreadPoolExecutor(1, r -> new Thread(r, "VPG Timer")))
                .scheduleWithFixedDelay(() -> {
            syncCount++;
            Logger.log("Syncing data [" + syncCount + "]\n", Logger.Mode.INFO, Timer.class);
            writeDamageData();
            Bot.syncChannel
                    .sendMessage("Sync [" + Bot.LOCATION + "] [" + syncCount + "]")
                    .addFile(Bot.logFile)
                    .addFile(Bot.errorFile)
                    .addFile(Bot.damageData)
                    .queue();
        },0,20, TimeUnit.MINUTES);
    }
}
