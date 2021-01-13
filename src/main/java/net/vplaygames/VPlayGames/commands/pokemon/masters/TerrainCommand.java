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

public class TerrainCommand extends Command {
    public TerrainCommand() {
        super("terrain");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        if (msg.length!=2)
        {
            MiscUtil.send(e, Bot.INVALID_INPUTS,true);
            return;
        }
        String toSend;
        long aid=e.getAuthor().getIdLong();
        if(DATA.containsKey(aid))
        {
            Damage d = DATA.get(aid);
            switch (msg[1]) {
                case "electric":
                case "e":
                    d.setTerrain(0);
                    toSend = "OK! So, the electric terrain was active.";
                    break;
                case "psychic":
                case "p":
                    d.setTerrain(1);
                    toSend = "OK! So, the psychic terrain was active.";
                    break;
                case "grassy":
                case "g":
                    d.setTerrain(2);
                    toSend = "OK! the grassy terrain was active.";
                    break;
                case "misty":
                case "m":
                    d.setTerrain(3);
                    toSend = "OK! the misty terrain was active.";
                    break;
                default:
                    toSend = "That is a very weird terrain!";
            }
            DATA.put(aid,d);
        } else
            toSend=Bot.APP_NOT_STARTED;
        MiscUtil.send(e,toSend,true);
    }
}
