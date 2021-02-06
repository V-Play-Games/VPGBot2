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
import net.vplaygames.VPlayGames.util.Strings;

import static net.vplaygames.VPlayGames.data.Bot.DATA;
import static net.vplaygames.VPlayGames.util.MiscUtil.returnSP;

public class SMLCommand extends DamageAppCommand {
    public SMLCommand() {
        super("sml", Damage.Status.UNIT_CHOSEN, 1);
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        int sml = Strings.toInt(e.getMessage().getContentRaw().split(" ")[1]);
        Damage d = DATA.get(e.getAuthor().getIdLong());
        String toSend;
        if (sml < 1 || sml > 5)
            toSend = "Invalid Sync Move Level!";
        else {
            d.setSml(sml);
            toSend = "The Sync Move Level of \"" + returnSP(d.getUid()) + "\" is set to " + sml + ".";
        }
        MiscUtil.send(e, toSend, true);
    }
}
