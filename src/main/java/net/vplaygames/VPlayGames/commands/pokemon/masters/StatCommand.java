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
import net.vplaygames.VPlayGames.util.Strings;

public class StatCommand extends DamageAppCommand {
    public StatCommand() {
        super("stat", Damage.AppStatus.UNIT_CHOSEN, 3);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String toSend;
        legalityCheck:
        {
            int targetId;
            switch (e.getArg(1).toLowerCase()) {
                case "user":
                case "u":
                    targetId = 0;
                    break;
                case "target":
                case "t":
                    targetId = 1;
                    break;
                default:
                    toSend = "Choose a valid option! See help for this command for more info.";
                    break legalityCheck;
            }
            int stat = Strings.toInt(e.getArg(3));
            if (stat < 1) {
                toSend = "Invalid Stat! Stat cannot be less than 1!";
                break legalityCheck;
            }
            int statId;
            String statName;
            switch (e.getArg(2).toLowerCase()) {
                case "atk":
                    statId = 0;
                    statName = "Attack";
                    break;
                case "spa":
                    statId = 1;
                    statName = "Sp. Atk";
                    break;
                case "def":
                    statId = 2;
                    statName = "Defense";
                    break;
                case "spd":
                    statId = 3;
                    statName = "Sp. Def";
                    break;
                default:
                    toSend = "Choose a valid option! See help for this command for more info.";
                    break legalityCheck;
            }
            Damage d = Bot.DATA.get(e.getAuthor().getIdLong()).setStats(targetId, statId, stat);
            toSend = "Set the " + (targetId == 1 ? "target" : d.pokemon.name) + "'s " + statName + " stat to " + stat + "!";
        }
        e.send(toSend).queue();
    }
}
