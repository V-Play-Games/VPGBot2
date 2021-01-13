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
package net.vplaygames.VPlayGames;

import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.PokeMasDB.Caches.PokemasDBCache;
import net.vplaygames.VPlayGames.processors.EventManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

public class Driver
{
    public static void main(String[] args) throws Exception
    {
        JDA jda = JDABuilder.createDefault(Bot.TOKEN)
                .addEventListeners(EventManager.getInstance())
                .build()
                .awaitReady();
        new Bot(jda);
        jda.getPresence().setActivity(Activity.watching("pokemasdb.com's data & downloading it."));
        Thread.sleep(1000);
        PokemasDBCache.getInstance();
        long memberCount=0;
        for(Guild g:jda.getGuilds())memberCount+=g.getMemberCount();
        jda.getPresence().setActivity(Activity.playing("Damage Calculation with "+memberCount+" people in "+jda.getGuilds().size()+" servers"));
    }
}
