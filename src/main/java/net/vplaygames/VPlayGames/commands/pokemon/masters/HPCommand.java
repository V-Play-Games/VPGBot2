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
import net.vplaygames.VPlayGames.util.Strings;

import static net.vplaygames.VPlayGames.core.Bot.DATA;

public class HPCommand extends DamageAppCommand {
    public HPCommand() {
        super("hp", 2);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String toSend;
        legalityCheck:
        {
            int t;
            switch (e.getArg(1).toLowerCase()) {
                case "user":
                case "u":
                    t = 0;
                    break;
                case "target":
                case "t":
                    t = 1;
                    break;
                default:
                    toSend = "Invalid target \"" + e.getArg(1) + "\".";
                    break legalityCheck;
            }
            int hpp = Strings.toInt(e.getArg(1));
            if (hpp < 0 || hpp > 100) {
                toSend = "HP Percentage cannot be less than 0 or more than 100!";
                break legalityCheck;
            }
            DATA.get(e.getAuthor().getIdLong()).setHP(t, hpp);
            toSend = "OK! So, the " + (t == 1 ? "target" : "user") + " was at " + hpp + "% HP.";
        }
        e.send(toSend).queue();
    }
}
