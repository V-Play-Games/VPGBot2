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

import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.commands.DamageAppCommand;

import static net.vplaygames.VPlayGames.core.Bot.DATA;
import static net.vplaygames.VPlayGames.core.Damage.Status.*;

public class StatusCommand extends DamageAppCommand {
    public StatusCommand() {
        super("status", 2);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String toSend;
        legalityCheck:
        {
            boolean user;
            switch (e.getArg(1)) {
                case "user":
                case "u":
                    user = true;
                    break;
                case "target":
                case "t":
                    user = false;
                    break;
                default:
                    toSend = "Choose a valid option! See help for this command for more info.";
                    break legalityCheck;
            }
            Damage d = DATA.get(e.getAuthor().getIdLong());
            String statusName;
            switch (e.getArg(2)) {
                case "paralyzed":
                case "paralyze":
                case "par":
                    d.setStatus(user, PARALYZE);
                    statusName = "paralyzed";
                    break;
                case "asleep":
                case "sleep":
                case "sleeping":
                case "slp":
                    d.setStatus(user, SLEEP);
                    statusName = "sleeping";
                    break;
                case "poisoned":
                case "poison":
                case "psn":
                case "badly-poisoned":
                case "bdp":
                case "tox":
                    d.setStatus(user, POISON);
                    statusName = "poisoned";
                    break;
                case "burnt":
                case "burn":
                case "brn":
                    d.setStatus(user, BURN);
                    statusName = "burnt";
                    break;
                case "freeze":
                case "frozen":
                case "frz":
                    d.setStatus(user, FREEZE);
                    statusName = "frozen";
                    break;
                case "flinching":
                case "flinched":
                case "flinch":
                case "fln":
                    d.setSStatus(user ? 0 : 1, 0);
                    statusName = (d.getSStatus()[user ? 0 : 1][0] == 0 ? "not " : "") + "flinched";
                    break;
                case "confused":
                case "confuse":
                case "cnf":
                    d.setSStatus(user ? 0 : 1, 1);
                    statusName = (d.getSStatus()[user ? 0 : 1][1] == 0 ? "not " : "") + "confused";
                    break;
                case "trapped":
                case "trap":
                case "trp":
                    d.setSStatus(user ? 0 : 1, 2);
                    statusName = (d.getSStatus()[user ? 0 : 1][2] == 0 ? "not " : "") + "trapped";
                    break;
                default:
                    toSend = "Choose a valid option! See help for this command for more info.";
                    break legalityCheck;
            }
            toSend = "So, the target was " + statusName;
        }
        e.send(toSend).queue();
    }
}
