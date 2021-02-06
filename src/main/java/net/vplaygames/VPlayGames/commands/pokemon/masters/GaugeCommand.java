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
import net.vplaygames.VPlayGames.util.Number;
import net.vplaygames.VPlayGames.util.Strings;

import static net.vplaygames.VPlayGames.data.Bot.DATA;

public class GaugeCommand extends DamageAppCommand {
    public GaugeCommand() {
        super("gauge", 1);
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        Damage d = DATA.get(e.getAuthor().getIdLong());
        int gauge = Strings.toInt(msg[1]);
        String toSend;
        if (Number.isBetween(gauge, 0, 6)) {
            d.setGauge(Strings.toInt(msg[1]));
            toSend = "Set the gauge to " + gauge + "!";
        } else
            toSend = "Invalid Input! " + gauge + " Move Gauges not possible!";
        MiscUtil.send(e, toSend, true);
    }
}
