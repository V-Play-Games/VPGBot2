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

public class GaugeCommand extends DamageAppCommand {
    public GaugeCommand() {
        super("gauge", 1);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        int gauge = Strings.toInt(e.getArg(1));
        String toSend;
        if (0 <= gauge && gauge <= 6) {
            Bot.DATA.get(e.getAuthor().getIdLong()).setGauge(gauge);
            toSend = "Set the gauge to " + gauge + "!";
        } else
            toSend = "Invalid Input! " + gauge + " Move Gauges not possible!";
        e.send(toSend).queue();
    }
}
