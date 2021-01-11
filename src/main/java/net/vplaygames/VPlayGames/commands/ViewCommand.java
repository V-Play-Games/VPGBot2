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
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static net.vplaygames.VPlayGames.db.botresources.data;
import static net.vplaygames.VPlayGames.db.database.trnrs;
import static net.vplaygames.VPlayGames.db.database.types;

public class ViewCommand extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        long aid=e.getAuthor().getIdLong();
        if(!e.getAuthor().isBot()&&msg[0].equals("v!view"))
        {
            String to_send;
            if(data.containsKey(aid))
            {
                Damage d = data.get(aid);
                if(msg.length==3)
                {
                    String m1 = msg[1], m2 = msg[2];
                    switch (m1) {
                        case "trainer":
                        case "t":
                            if (d.getApp_stts() < 1)
                                to_send = "Choose a Trainer first!";
                            else if (m2.equals("name"))
                                to_send = "The chosen trainer is named " + trnrs[d.getTid()];
                            else if (m2.equals("id"))
                                to_send = "The chosen trainer's id is " + d.getTid();
                            else
                                to_send = "Cannot find entry \"" + m2 + "\" in list \"" + m1 + "\"";
                            break;
                        case "move":
                        case "m":
                            if (d.getApp_stts() < 3)
                                to_send = "Choose a Move first!";
                            else {
                                switch (m2) {
                                    case "name":
                                    case "n":
                                        to_send = "The current chosen move is named " + d.getMvnam();
                                        break;
                                    case "info":
                                    case "i":
                                        to_send = "Move Info:-\nBase Power: " + d.getMInfo()[0] +
                                                "\nCategory: " + ((d.getMInfo()[2] == 1) ? "Special" : "Physical") +
                                                "\nReach: " + ((d.getMInfo()[1] == 1) ? "All opponents" : "An opponent") +
                                                "\nType: " + types[d.getMInfo()[3] - 1];
                                        break;
                                    case "level":
                                    case "lvl":
                                        to_send = (d.getSml() != 0) ? "Currently chosen move level: " + d.getSml() : "Set the Sync Move Level first!";
                                        break;
                                    case "modifier":
                                    case "mod":
                                        to_send=((d.getMod()[0]==1)?"Critical Hit\n":"")+((d.getMod()[1]==1)?"Super-Effective\n":"");
                                        to_send+=(to_send.isEmpty())?"None":"";
                                        to_send="Available Modifiers:\n"+to_send;
                                        break;
                                    default:
                                        to_send = "Cannot find entry \"" + m2 + "\" in list \"" + m1 + "\"";
                                        break;
                                }
                            }
                            break;
                        case "weather":
                        case "w":
                            to_send = ((d.getWthr()[0] == 1) ? "The weather was sunny" : ((d.getWthr()[1] == 1) ? "It was raining" : ((d.getWthr()[2] == 1) ? "There was a sandstorm" : ((d.getWthr()[3] == 1) ? "It was hailing" : "The weather was normal")))) + ".";
                            break;
                        default:
                            to_send = "Cannot find list \"" + m1 + "\"";
                            break;
                    }
                } else
                    to_send="Not enough inputs!";
            } else
                to_send="Cannot find a Pokemon Masters Damage Calculator created by "+e.getAuthor().getAsMention();
            e.getChannel().sendMessage(to_send).queue();
        }
    }
}
