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
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;

import java.util.StringJoiner;

import static net.vplaygames.VPlayGames.data.GameData.*;
import static net.vplaygames.VPlayGames.util.MiscUtil.returnSP;

public class ChooseCommand extends DamageAppCommand {
    public ChooseCommand() {
        super("choose", 1, "c");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        StringJoiner toSend = new StringJoiner("\n");
        Damage d = Bot.DATA.get(e.getAuthor().getIdLong());
        int temp = Strings.toInt(msg[1]) - 1;
        int i;
        if (d.getAppStatus() == 1) {
            if (temp < 0 || temp >= d.getUc().length)
                toSend.add("Invalid Input. There is no trainer at that place.");
            else {
                d.setUid(d.getUc()[temp]);
                d.setPid(d.getUid() % 1000000);
                toSend.add("This means you want to calculate damage for " + returnSP(d.getUid()))
                    .add("Choose the move for which you want to calculate the damage:");
                d.setMSet(msets[d.getPid() % 1000 - 1]);
                for (i = 1; i <= d.getMSet().length; i++)
                    toSend.add(i + ". " + moves[(d.getMSet()[i - 1] - 1) % 1000]);
                toSend.add(i + ". " + smoves[d.getPid() % 1000 - 1] + " (Sync Move)")
                    .add("Give your choice in an integer number in the range of 1-" + (d.getMSet().length + 1))
                    .add("using the command ``" + Bot.PREFIX + "choose <choice>``");
                d.updateAppStatus();
            }
        } else if (d.getAppStatus() == 2) {
            if (temp < 0 || temp > d.getMSet().length)
                toSend.add("Invalid Input. There is no move at that place.");
            else {
                if (temp == d.getMSet().length) {
                    d.setMoveName(smoves[d.getPid() % 1000 - 1] + " (Sync Move)");
                    d.setMInfo(sminfos[d.getPid() % 1000 - 1]);
                } else {
                    d.setSmd(d.getMSet()[temp] / 1000);
                    d.setMInfo(minfos[(d.getMSet()[temp] - 1) % 1000]);
                    d.setMoveName(moves[(d.getMSet()[temp] - 1) % 1000]);
                }
                toSend.add("You chose " + d.getMoveName() + ".")
                    .add("\nMove Info:-")
                    .add("Base Power: " + d.getMInfo()[0])
                    .add("Category: " + ((d.getMInfo()[2] == 1) ? "Special" : "Physical"))
                    .add("Target: " + ((d.getMInfo()[1] == 1) ? "All opponents" : "An opponent"))
                    .add("Type: " + types[d.getMInfo()[3] - 1])
                    .add("\nUse ``" + Bot.PREFIX + "view move info`` to view this info again.");
                if (d.getMInfo()[1] == 1) {
                    toSend.add("This move can affect more than 1 targets.")
                        .add("How many targets were on the field when the move was used?")
                        .add("1. 1")
                        .add("2. 2")
                        .add("3. 3")
                        .add("Give your choice in an integer number in the range of 1-3")
                        .add("using the command ``" + Bot.PREFIX + "choose <choice>``");
                } else
                    d.updateAppStatus();
                d.updateAppStatus();
            }
        } else if (d.getAppStatus() == 3) {
            temp++;
            if (temp < 1 || temp > 3)
                toSend.add("Invalid no. of targets.");
            else {
                d.setMod(2, temp);
                toSend.add("Set the no. of targets to " + temp + ".");
                d.updateAppStatus();
            }
        } else
            toSend.add("Cannot find a list to choose from.");
        MiscUtil.send(e, toSend.toString(), true);
    }
}
