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

public class CDCommand extends Command {
    public CDCommand() {
        super("cd");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String toSend;
        long aid = e.getAuthor().getIdLong();
        if (DATA.containsKey(aid)) {
            Damage d = DATA.get(aid);
            if (d.getAppStatus() < 1)
                toSend = "Choose a Trainer first!";
            else if (d.getAppStatus() < 2)
                toSend = "Choose a Sync Pair first!!";
            else if (d.getAppStatus() < 3)
                toSend = "Move is Missing!";
            else if (d.getStats()[0][d.getMInfo()[2]] == 0)
                toSend = "``" + MiscUtil.returnSP(d.getUid()) + "``'s " + ((d.getMInfo()[2] == 1) ? "Special" : "Physical") + " Attack stat is Missing!";
            else if (d.getStats()[1][d.getMInfo()[2] + 2] == 0)
                toSend = "The target's " + ((d.getMInfo()[2] == 1) ? "Special" : "Physical") + " Defense stat is Missing!";
            else
                toSend = d.getDamageString();
        } else
            toSend = Bot.APP_NOT_STARTED;
        MiscUtil.send(e, toSend, true);
    }
}