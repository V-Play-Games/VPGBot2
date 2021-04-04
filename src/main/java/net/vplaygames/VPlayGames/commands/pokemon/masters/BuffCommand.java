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
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;

import static net.vplaygames.VPlayGames.core.Bot.DATA;

public class BuffCommand extends DamageAppCommand {
    public BuffCommand() {
        super("buff", Damage.AppStatus.UNIT_CHOSEN, 3, 0);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String toSend;
        legalityCheck:
        {
            int targetId;
            switch (msg[1].toLowerCase()) {
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
            int buff = Strings.toInt(msg[3]);
            if (buff < -6 || buff > 6) {
                toSend = "Invalid Stat! " + ((buff < 0) ? "" : "+") + buff + " stat buff not possible.";
                break legalityCheck;
            }
            int buffId;
            String buffName;
            switch (msg[2].toLowerCase()) {
                case "atk":
                    buffId = 0;
                    buffName = "attack";
                    break;
                case "spa":
                    buffId = 1;
                    buffName = "special attack";
                    break;
                case "def":
                    buffId = 2;
                    buffName = "defense";
                    break;
                case "spd":
                    buffId = 3;
                    buffName = "special defense";
                    break;
                case "spe":
                    buffId = 4;
                    buffName = "speed";
                    break;
                case "acc":
                    buffId = 5;
                    buffName = "accuracy";
                    break;
                case "eva":
                    buffId = 6;
                    buffName = "evasiveness";
                    break;
                default:
                    toSend = "Choose a valid option! See help for this command for more info.";
                    break legalityCheck;
            }
            Damage d = DATA.get(e.getAuthor().getIdLong());
            d.setBuffs(targetId, buffId, buff);
            toSend = "Set the " + ((targetId == 1) ? "target" : MiscUtil.returnSP(d.getUid())) + "'s " + buffName + " stat buff to " + buff + "!";
        }
        e.send(toSend).queue();
    }
}
