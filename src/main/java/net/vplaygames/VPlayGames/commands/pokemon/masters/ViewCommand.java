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
package net.vplaygames.VPlayGames.commands.pokemon.masters;

import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.commands.DamageAppCommand;
import net.vplaygames.VPlayGames.core.Bot;
import net.vplaygames.VPlayGames.core.Damage;

public class ViewCommand extends DamageAppCommand {
    public ViewCommand() {
        super("view", 2);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String toSend;
        Damage d = Bot.DATA.get(e.getAuthor().getIdLong());
        String list = e.getArg(1), entry = e.getArg(2);
        switch (list) {
            case "trainer":
            case "t":
                if (d.appStatus.ordinal() < 1)
                    toSend = "Choose a Trainer first!";
                else if (entry.equals("name"))
                    toSend = "The chosen trainer is named " + d.trainer.name;
                else
                    toSend = "Cannot find entry \"" + entry + "\" in list \"" + list + "\"";
                break;
            case "move":
            case "m":
                if (d.appStatus.ordinal() < 3) {
                    toSend = "Choose a Move first!";
                    break;
                }
                switch (entry) {
                    case "name":
                    case "n":
                        toSend = "The current chosen move is named " + d.attack.name;
                        break;
                    case "info":
                    case "i":
                        toSend = "Move Info:-" +
                            "\nBase Power: " + d.attack.minPower +
                            "\nCategory: " + d.attack.category +
                            "\nTarget: " + d.attack.target +
                            "\nType: " + d.attack.type;
                        break;
                    case "level":
                    case "lvl":
                        toSend = "Sync Move Level: " + d.sml;
                        break;
                    case "modifier":
                    case "mod":
                        toSend = "Available Modifiers:"
                            + (d.mod[0] + d.mod[1] == 0
                            ? "\nNone" :
                            (d.mod[0] == 1 ? "\nCritical Hit" : "")
                                + (d.mod[1] == 1 ? "\nSuper-Effective" : ""));
                        break;
                    default:
                        toSend = "Cannot find entry \"" + entry + "\" in list \"" + list + "\"";
                }
                break;
            case "weather":
            case "w":
                // First letter Capital
                toSend = d.weather.toString().substring(0, 1).toUpperCase() + d.weather.toString().substring(1).toUpperCase() + ".";
                break;
            default:
                toSend = "Cannot find list \"" + list + "\"";
                break;
        }
        e.send(toSend).queue();
    }
}
