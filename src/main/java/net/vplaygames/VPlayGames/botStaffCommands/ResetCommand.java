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
package net.vplaygames.VPlayGames.botStaffCommands;

import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.processors.EventManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

public class ResetCommand
{
    public static void Reset(String... args)
    {
        try {
            System.out.println("Trying a reset!");
            Reset();
            System.out.println("Reset Successfully Completed!");
        } catch (Exception exc) {
            System.out.println("Reset Failed! Retrying in 10s!");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Sleep Interrupted!");
            }
            Reset(args);
        }
    }

    public static void Reset() throws Exception
    {
        int serverCount = Bot.current.jda.getGuilds().size();
        long memberCount=0;
        for(Guild g:Bot.current.jda.getGuilds())memberCount+=g.getMemberCount();
        Bot.current.jda.shutdownNow();
        Bot.current.jda = JDABuilder.createDefault(Bot.TOKEN)
                .setActivity(Activity.playing("Damage Calculation in "+serverCount+" servers with "+memberCount+" people."))
                .addEventListeners(EventManager.getInstance())
                .build()
                .awaitReady();
    }
}