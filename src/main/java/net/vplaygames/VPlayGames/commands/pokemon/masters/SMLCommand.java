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

public class SMLCommand extends DamageAppCommand {
    public SMLCommand() {
        super("sml", Damage.AppStatus.UNIT_CHOSEN, 1);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        int sml = Strings.toInt(e.getArg(1));
        String toSend;
        if (sml < 1 || sml > 5)
            toSend = "Sync Move Level " + sml + " is not possible!";
        else {
            Damage d = DATA.get(e.getAuthor().getIdLong());
            d.setSml(sml);
            toSend = "The Sync Move Level of \"" + d.getPokemon().name + "\" is set to " + sml + ".";
        }
        e.send(toSend).queue();
    }
}
