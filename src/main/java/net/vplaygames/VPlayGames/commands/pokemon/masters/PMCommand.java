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
import net.vplaygames.VPlayGames.util.MiscUtil;

import static net.vplaygames.VPlayGames.data.Bot.DATA;

public class PMCommand extends Command {
    public PMCommand() {
        super("pm", 0, null, 1, 1);
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String mode = e.getMessage().getContentRaw().split(" ")[1].toLowerCase();
        long aid = e.getAuthor().getIdLong();
        String toSend;
        boolean invalid = false;
        if (mode.equals("start")) {
            if (!DATA.containsKey(aid)) {
                new Damage(e);
                toSend = " has created a new";
            } else
                toSend = ", you already have an opened";
        } else if (mode.equals("end")) {
            if (DATA.containsKey(aid)) {
                DATA.get(aid).remove();
                toSend = " has deleted their";
            } else
                toSend = ", I can't find your";
        } else {
            invalid = true;
            toSend = "Invalid Input.";
        }
        if (!invalid) {
            toSend = e.getAuthor().getAsMention() + toSend + " Pokemon Masters Damage Calculation Application";
        }
        MiscUtil.send(e, toSend, true);
    }
}