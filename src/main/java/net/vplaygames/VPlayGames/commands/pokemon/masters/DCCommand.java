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

import java.util.concurrent.TimeUnit;

import static net.vplaygames.VPlayGames.data.Bot.DAMAGE_CODES;
import static net.vplaygames.VPlayGames.data.Bot.DATA;

public class DCCommand extends Command {
    public DCCommand() {
        super("dc", 10, TimeUnit.SECONDS, 1, 1);
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String code = e.getMessage().getContentRaw().split(" ")[1];
        String toSend;
        long aid = e.getAuthor().getIdLong();
        if (code.equals("get")) {
            if (DATA.containsKey(aid)) {
                String newCode = DATA.get(aid).getCode();
                toSend = e.getAuthor().getAsMention() + ", your requested Damage Code is " + newCode;
                DATA.get(aid).enable();
            } else {
                toSend = "I cannot find a Damage Calculator App created by you, " + e.getAuthor().getAsMention();
            }
        } else if (DAMAGE_CODES.containsKey(code)) {
            Damage d = DAMAGE_CODES.get(code);
            if (!d.equals(DATA.getOrDefault(aid, null))) {
                Damage clone = new Damage(aid, e.getAuthor().getAsTag()).copyFrom(d);
                DATA.put(aid, clone);
                toSend = e.getAuthor().getAsMention() + " has " + (DATA.containsKey(aid) ? "overwritten their Damage App by" : "imported") + " a Damage App Created by " + DAMAGE_CODES.get(code).getUserTag();
            } else {
                toSend = "This code's Damage App was not imported because either your app is same as the code's app, or you have already imported it.";
            }
        } else {
            toSend = "\"" + code + "\" Damage code not found";
        }
        MiscUtil.send(e, toSend, true);
    }
}
