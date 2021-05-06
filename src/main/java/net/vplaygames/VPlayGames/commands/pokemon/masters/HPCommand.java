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
import net.vplaygames.VPlayGames.util.Strings;

public class HPCommand extends DamageAppCommand {
    public HPCommand() {
        super("hp", 2);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String toSend;
        int t = 0;
        switch (e.getArg(1).toLowerCase()) {
            case "user":
            case "u":
                t = -1;
            case "target":
            case "t":
                t += 1;
                int hp = Strings.toInt(e.getArg(1));
                if (0 <= hp && hp <= 100) {
                    Bot.DATA.get(e.getAuthor().getIdLong()).setHP(t, hp);
                    toSend = "OK! So, the " + (t == 1 ? "target" : "user") + " was at " + hp + "% HP.";
                } else
                    toSend = "HP Percentage cannot be less than 0 or more than 100!";
                break;
            default:
                toSend = "Invalid target \"" + e.getArg(1) + "\".";
        }
        e.send(toSend).queue();
    }
}
