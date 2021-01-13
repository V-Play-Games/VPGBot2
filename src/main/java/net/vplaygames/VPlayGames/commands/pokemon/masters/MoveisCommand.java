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
import static net.vplaygames.VPlayGames.data.Bot.INVALID_INPUTS;

public class MoveisCommand extends Command {
    public MoveisCommand() {
        super("moveis", "mi");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw();
        if (msg.split(" ").length<2) {
            MiscUtil.send(e,INVALID_INPUTS,true);
            return;
        }
        String rslt=msg.substring(msg.split(" ")[0].length()+1);
        String toSend;
        long aid=e.getAuthor().getIdLong();
        if(DATA.containsKey(aid))
        {
            Damage d = DATA.get(aid);
            if(rslt.equals("critical hit")||rslt.equals("ch"))
            {
                d.setMod(0,(d.getMod()[0]==1)?0:1);
                toSend="Ok, I'll remember that the move was"+((d.getMod()[0]==0)?" not":"")+" critical hit.";
            } else if(rslt.equals("super effective")||rslt.equals("se"))
            {
                d.setMod(1,(d.getMod()[1]==1)?0:1);
                toSend="Ok, I'll remember that the move was"+((d.getMod()[1]==0)?" not":"")+" super effective.";
            } else
                toSend="Invalid Modifier!";
            DATA.put(aid,d);
        } else
            toSend= Bot.APP_NOT_STARTED;
        MiscUtil.send(e,toSend,true);
    }
}