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
import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;

import static net.vplaygames.VPlayGames.data.Bot.DATA;

public class StatusCommand extends Command {
    public StatusCommand() {
        super("status");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        if (msg.length != 3) {
            MiscUtil.send(e, Bot.INVALID_INPUTS, true);
            return;
        }
        String toSend = "OK! So, the target was ";
        long aid = e.getAuthor().getIdLong();
        PROCESS:
        if (DATA.containsKey(aid)) {
            int t;
            switch (msg[1]) {
                case "user":
                case "u":
                    t = 0;
                    break;
                case "target":
                case "t":
                    t = 1;
                    break;
                default:
                    toSend = "Cannot find \"" + msg[1] + "\" entry in list \"status\"";
                    break PROCESS;
            }
            Damage d = DATA.get(aid);
            switch (msg[2]) {
                case "paralyzed":
                case "paralyze":
                case "par":
                    d.setStatus(t, 0);
                    toSend += "paralyzed";
                    break;
                case "asleep":
                case "sleep":
                case "sleeping":
                case "slp":
                    d.setStatus(t, 1);
                    toSend += "sleeping";
                    break;
                case "poisoned":
                case "poison":
                case "psn":
                    d.setStatus(t, 2);
                    toSend += "poisoned";
                    break;
                case "badly-poisoned":
                case "bdp":
                case "tox":
                    d.setStatus(t, 3);
                    toSend += "badly poisoned";
                    break;
                case "burnt":
                case "burn":
                case "brn":
                    d.setStatus(t, 4);
                    toSend += "burnt";
                    break;
                case "freeze":
                case "frozen":
                case "frz":
                    d.setStatus(t, 5);
                    toSend += "frozen";
                    break;
                case "flinching":
                case "fliched":
                case "flinch":
                case "fln":
                    d.setSStatus(t, 0);
                    toSend += ((d.getSStatus()[t][0] == 0) ? "not " : "") + "flinched";
                    break;
                case "confused":
                case "confuse":
                case "cnf":
                    d.setSStatus(t, 1);
                    toSend += ((d.getSStatus()[t][1] == 0) ? "not " : "") + "confused";
                    break;
                case "trapped":
                case "trap":
                case "trp":
                    d.setSStatus(t, 2);
                    toSend += ((d.getSStatus()[t][2] == 0) ? "not " : "") + "trapped";
                    break;
                default:
                    toSend = "That is a very weird status";
            }
            DATA.put(aid,d);
        } else
            toSend = Bot.APP_NOT_STARTED;
        MiscUtil.send(e, toSend, true);
    }
}