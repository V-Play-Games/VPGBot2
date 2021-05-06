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

import net.vplaygames.VPlayGames.commands.Command;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.core.Damage;

import static net.vplaygames.VPlayGames.core.Bot.DATA;

public class PMCommand extends Command {
    public PMCommand() {
        super("pm", 0, null, 1, 1);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String toSend;
        Damage d = DATA.get(e.getAuthor().getIdLong());
        boolean invalid = false;
        switch (e.getArg(1).toLowerCase()) {
            case "start":
                if (d == null) {
                    new Damage(e.getAuthor().getIdLong());
                    toSend = "have created a new";
                } else
                    toSend = "already have an open";
                break;
            case "end":
                if (d != null) {
                    d.remove();
                    toSend = "have deleted your";
                } else {
                    invalid = true;
                    toSend = "Your Pokemon Masters Damage Calculation Application was nowhere to be found. RIP";
                }
                break;
            default:
                invalid = true;
                toSend = "Invalid Input.";
        }
        e.send(invalid ? toSend : "You " + toSend + " Pokemon Masters Damage Calculation Application")
            .mentionRepliedUser(true).queue();
    }
}
