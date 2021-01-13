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
package net.vplaygames.VPlayGames.commands.pokemon.masters;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;

import static net.vplaygames.VPlayGames.data.Bot.DATA;

public class WeatherCommand extends Command {
    public WeatherCommand() {
        super("weather");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        if (msg.length!=2)
        {
            MiscUtil.send(e, Bot.INVALID_INPUTS,true);
            return;
        }
        String toSend;
        long aid=e.getAuthor().getIdLong();
        if(DATA.containsKey(aid))
        {
            Damage d = DATA.get(aid);
            switch (msg[1]) {
                case "sunny":
                case "sun":
                    d.setWthr(0);
                    toSend = "OK! So, the weather was sunny";
                    break;
                case "rainy":
                case "rain":
                    d.setWthr(1);
                    toSend = "OK! So, it was raining";
                    break;
                case "sandstorm":
                    d.setWthr(2);
                    toSend = "OK! So, a sandstorm was raging";
                    break;
                case "hail":
                case "hailstorm":
                    d.setWthr(3);
                    toSend = "OK! So, it was hailing";
                    break;
                default:
                    toSend = "That is a very weird weather!";
            }
            DATA.put(aid,d);
        } else
            toSend=Bot.APP_NOT_STARTED;
        MiscUtil.send(e,toSend,true);
    }
}