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

import static net.vplaygames.VPlayGames.core.Damage.AppStatus.*;
import static net.vplaygames.VPlayGames.core.Bot.DATA;

public class CDCommand extends DamageAppCommand {
    public CDCommand() {
        super("cd");
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String toSend;
        Damage d = DATA.get(e.getAuthor().getIdLong());
        if (d.getAppStatus() == STARTED.ordinal())
            toSend = "Choose a Trainer first!";
        else if (d.getAppStatus() == TRAINER_CHOSEN.ordinal())
            toSend = "Choose a Sync Pair first!!";
        else if (d.getAppStatus() == UNIT_CHOSEN.ordinal())
            toSend = "Move is Missing!";
        else if (d.getStats()[0][MiscUtil.isSpecial(d.getAttack().category)] == 0)
            toSend = d.getPokemon().name + "'s " + (MiscUtil.isSpecial(d.getAttack().category) == 1 ? "Special" : "Physical") + " Attack stat is Missing!";
        else if (d.getStats()[1][MiscUtil.isSpecial(d.getAttack().category) + 2] == 0)
            toSend = "The target's " + (MiscUtil.isSpecial(d.getAttack().category) == 1 ? "Special" : "Physical") + " Defense stat is Missing!";
        else
            toSend = d.getDamageString();
        e.send(toSend).queue();
    }
}
