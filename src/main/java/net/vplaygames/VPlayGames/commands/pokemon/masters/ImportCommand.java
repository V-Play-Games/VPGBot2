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
import net.vplaygames.VPlayGames.commands.Command;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;

import java.util.concurrent.TimeUnit;

import static net.vplaygames.VPlayGames.core.Bot.DAMAGE_CODES;
import static net.vplaygames.VPlayGames.core.Bot.DATA;

public class ImportCommand extends Command {
    public ImportCommand() {
        super("import", 10, TimeUnit.SECONDS, 1, 1);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String code = e.getArg(1);
        String toSend;
        Damage d = DATA.get(e.getAuthor().getIdLong());
        if (code.equals("get"))
            if (d != null)
                toSend = "Your requested Damage Code is " + d.enable().getCode();
            else
                toSend = "I cannot find a Damage Calculator App created by you.";
        else {
            Damage fromCode = DAMAGE_CODES.get(code);
            if (fromCode != null)
                if (!fromCode.equals(d)) {
                    new Damage(e.getAuthor().getIdLong()).copyFrom(fromCode);
                    toSend = "You have " + (d != null ? "overwritten you Damage App by" : "imported") + " a Damage App from code " + code;
                } else
                    toSend = "This code's Damage App was not imported because either your app is same as the code's app, or you have already imported it.";
            else
                toSend = "\"" + code + "\" Damage code not found";
        }
        e.send(toSend).queue();
    }
}
