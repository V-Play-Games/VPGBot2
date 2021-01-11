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
package net.vplaygames.VPlayGames.commands;

import net.vplaygames.VPlayGames.db.Damage;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import static net.vplaygames.VPlayGames.db.botresources.data;

public class WeatherCommand
{
    public static void WeatherCommand(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String to_send;
        long aid=e.getAuthor().getIdLong();
        if(data.containsKey(aid))
        {
            Damage d = data.get(aid);
            switch (msg[1]) {
                case "sunny":
                case "sun":
                    d.setWthr(0);
                    to_send = "OK! So, the weather was sunny";
                    break;
                case "rainy":
                case "rain":
                    d.setWthr(1);
                    to_send = "OK! So, it was raining";
                    break;
                case "sandstorm":
                    d.setWthr(2);
                    to_send = "OK! So, a sandstorm was raging";
                    break;
                case "hail":
                case "hailstorm":
                    d.setWthr(3);
                    to_send = "OK! So, it was hailing";
                    break;
                default:
                    to_send = "That is a very weird weather!";
            }
        } else
            to_send="Create a PM Damage Calculator App first!";
        e.getChannel().sendMessage(to_send).queue();
    }
}