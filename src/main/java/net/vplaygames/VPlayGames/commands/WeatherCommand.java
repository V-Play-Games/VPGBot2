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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vplaygames.VPlayGames.util.Array;

import static net.vplaygames.VPlayGames.db.botresources.prefix;
import static net.vplaygames.VPlayGames.db.userdatabase.*;
import static net.vplaygames.VPlayGames.util.Array.sumAll;

public class WeatherCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String to_send;
        int user_pv = Array.returnID(user_ids,e.getAuthor().getIdLong());
        if(msg[0].equals(prefix+"weather")||msg[0].equals(prefix+"wthr"))
        {
            if(isRegistered(e.getAuthor().getIdLong()))
            {
                if(sumAll(wthrs[user_pv])==1)
                {
                    to_send="No, "+((wthrs[user_pv][0]==1)?"the weather was sunny":((wthrs[user_pv][1]==1)?"it was raining":((wthrs[user_pv][2]==1)?"there was a sandstorm":"it was hailing")))+"!";
                } else {
                    switch (msg[1])
                    {
                        case "sunny":
                        case "sun":
                            setWeather(0,user_pv);
                            to_send="OK! So, the weather was sunny";
                        break;
                        case "rainy":
                        case "rain":
                            setWeather(1,user_pv);
                            to_send="OK! So, it was raining";
                        break;
                        case "sandstorm":
                            setWeather(2,user_pv);
                            to_send="OK! So, a sandstorm was raging";
                        break;
                        case "hail":
                        case "hailstorm":
                            setWeather(3,user_pv);
                            to_send="OK! So, it was hailing";
                        break;
                        default:
                            to_send="That is a very weird weather!";
                    }
                }
            } else
                to_send="Create a PM Damage Calculator App first!";
            e.getChannel().sendMessage(to_send).queue();
        }
    }
    public static void setWeather(int wthr, int u)
    {

        for(int i:wthrs[u])
            wthrs[u][i]=0;
        wthrs[u][wthr]=1;
    }
}
