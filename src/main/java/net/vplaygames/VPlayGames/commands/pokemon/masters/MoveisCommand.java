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

public class MoveisCommand extends DamageAppCommand {
    public MoveisCommand() {
        super("moveis", Damage.AppStatus.MOVE_CHOSEN, 1, 0, "mi");
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String toSend;
        Damage d = DATA.get(e.getAuthor().getIdLong());
        switch (String.join("", e.getArgsFrom(1))) {
            case "criticalhit":
            case "ch":
                d.setMod(0, (d.getMod()[0] == 1) ? 0 : 1);
                toSend = "Ok, I'll remember that the move was" + (d.getMod()[0] == 0 ? " not" : "") + " critical hit.";
                break;
            case "supereffective":
            case "se":
                d.setMod(1, (d.getMod()[1] == 1) ? 0 : 1);
                toSend = "Ok, I'll remember that the move was" + (d.getMod()[1] == 0 ? " not" : "") + " super effective.";
                break;
            default:
                toSend = "Invalid Modifier!";
        }
        e.send(toSend).queue();
    }
}
