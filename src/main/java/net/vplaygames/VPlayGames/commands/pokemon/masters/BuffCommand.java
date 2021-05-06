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
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.util.Strings;

import static net.vplaygames.VPlayGames.core.Bot.DATA;

public class BuffCommand extends DamageAppCommand {
    public BuffCommand() {
        super("buff", Damage.AppStatus.UNIT_CHOSEN, 3, 0);
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
            int buff = Strings.toInt(e.getArg(3));
            if (buff < -6 || buff > 6) {
                toSend = "Invalid Stat! " + ((buff < 0) ? "" : "+") + buff + " stat buff not possible.";
                break legalityCheck;
            }
            int buffId;
            String buffName;
            switch (String.join("", e.getArgsFrom(2)).toLowerCase()) {
                case "atk":
                case "attack":
                    buffId = 0;
                    buffName = "attack";
                    break;
                case "spa":
                case "specialattack":
                    buffId = 1;
                    buffName = "special attack";
                    break;
                case "def":
                case "defense":
                    buffId = 2;
                    buffName = "defense";
                    break;
                case "spd":
                case "specialdefense":
                    buffId = 3;
                    buffName = "special defense";
                    break;
                case "spe":
                case "speed":
                    buffId = 4;
                    buffName = "speed";
                    break;
                case "acc":
                case "accuracy":
                    buffId = 5;
                    buffName = "accuracy";
                    break;
                case "eva":
                case "evasiveness":
                    buffId = 6;
                    buffName = "evasiveness";
                    break;
                default:
                    toSend = "Choose a valid option! See help for this command for more info.";
                    break legalityCheck;
            }
            toSend = "Set the " + (targetId == 1 ? "target"
                : DATA.get(e.getAuthor().getIdLong()).setBuffs(targetId, buffId, buff).pokemon.name)
                + "'s " + buffName + " stat buff to " + buff + "!";
        }
        e.send(toSend).queue();
    }
}