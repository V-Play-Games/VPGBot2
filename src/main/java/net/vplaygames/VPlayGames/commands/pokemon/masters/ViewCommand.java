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
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.util.MiscUtil;

import static net.vplaygames.VPlayGames.data.Bot.DATA;
import static net.vplaygames.VPlayGames.data.GameData.trnrs;
import static net.vplaygames.VPlayGames.data.GameData.types;

public class ViewCommand extends DamageAppCommand {
    public ViewCommand() {
        super("view", 2);
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String toSend;
        Damage d = DATA.get(e.getAuthor().getIdLong());
        String list = msg[1], entry = msg[2];
        switch (list) {
            case "trainer":
            case "t":
                if (d.getAppStatus() < 1)
                    toSend = "Choose a Trainer first!";
                else if (entry.equals("name"))
                    toSend = "The chosen trainer is named " + trnrs[d.getTid()];
                else if (entry.equals("id"))
                    toSend = "The chosen trainer's id is " + d.getTid();
                else
                    toSend = "Cannot find entry \"" + entry + "\" in list \"" + list + "\"";
                break;
            case "move":
            case "m":
                if (d.getAppStatus() < 3) {
                    toSend = "Choose a Move first!";
                    break;
                }
                switch (entry) {
                    case "name":
                    case "n":
                        toSend = "The current chosen move is named " + d.getMoveName();
                        break;
                    case "info":
                    case "i":
                        toSend = "Move Info:-\nBase Power: " + d.getMInfo()[0] +
                            "\nCategory: " + ((d.getMInfo()[2] == 1) ? "Special" : "Physical") +
                            "\nReach: " + ((d.getMInfo()[1] == 1) ? "All opponents" : "An opponent") +
                            "\nType: " + types[d.getMInfo()[3] - 1];
                        break;
                    case "level":
                    case "lvl":
                        toSend = "Sync Move Level: " + d.getSml();
                        break;
                    case "modifier":
                    case "mod":
                        toSend = (d.getMod()[0] == 1 ? "Critical Hit\n" : "") + (d.getMod()[1] == 1 ? "Super-Effective\n" : "");
                        toSend += (toSend.isEmpty()) ? "None" : "";
                        toSend = "Available Modifiers:\n" + toSend;
                        break;
                    default:
                        toSend = "Cannot find entry \"" + entry + "\" in list \"" + list + "\"";
                }
                break;
            case "weather":
            case "w":
                toSend = d.getWeatherString() + ".";
                break;
            default:
                toSend = "Cannot find list \"" + list + "\"";
                break;
        }
        MiscUtil.send(e, toSend, true);
    }
}
