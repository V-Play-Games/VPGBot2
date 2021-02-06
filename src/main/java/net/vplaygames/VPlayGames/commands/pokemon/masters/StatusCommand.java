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

public class StatusCommand extends DamageAppCommand {
    public StatusCommand() {
        super("status", 2);
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String toSend;
        legalityCheck:
        {
            int targetId;
            switch (msg[1]) {
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
            Damage d = DATA.get(e.getAuthor().getIdLong());
            String statusName;
            switch (msg[2]) {
                case "paralyzed":
                case "paralyze":
                case "par":
                    d.setStatus(targetId, 0);
                    statusName = "paralyzed";
                    break;
                case "asleep":
                case "sleep":
                case "sleeping":
                case "slp":
                    d.setStatus(targetId, 1);
                    statusName = "sleeping";
                    break;
                case "poisoned":
                case "poison":
                case "psn":
                    d.setStatus(targetId, 2);
                    statusName = "poisoned";
                    break;
                case "badly-poisoned":
                case "bdp":
                case "tox":
                    d.setStatus(targetId, 3);
                    statusName = "badly poisoned";
                    break;
                case "burnt":
                case "burn":
                case "brn":
                    d.setStatus(targetId, 4);
                    statusName = "burnt";
                    break;
                case "freeze":
                case "frozen":
                case "frz":
                    d.setStatus(targetId, 5);
                    statusName = "frozen";
                    break;
                case "flinching":
                case "fliched":
                case "flinch":
                case "fln":
                    d.setSStatus(targetId, 0);
                    statusName = ((d.getSStatus()[targetId][0] == 0) ? "not " : "") + "flinched";
                    break;
                case "confused":
                case "confuse":
                case "cnf":
                    d.setSStatus(targetId, 1);
                    statusName = ((d.getSStatus()[targetId][1] == 0) ? "not " : "") + "confused";
                    break;
                case "trapped":
                case "trap":
                case "trp":
                    d.setSStatus(targetId, 2);
                    statusName = ((d.getSStatus()[targetId][2] == 0) ? "not " : "") + "trapped";
                    break;
                default:
                    toSend = "Choose a valid option! See help for this command for more info.";
                    break legalityCheck;
            }
            toSend = "So, the target was " + statusName;
        }
        MiscUtil.send(e, toSend, true);
    }
}
