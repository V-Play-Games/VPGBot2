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

import static net.vplaygames.VPlayGames.data.Bot.DATA;

public class TerrainCommand extends DamageAppCommand {
    public TerrainCommand() {
        super("terrain", 1);
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String terrain = e.getMessage().getContentRaw().split(" ")[1];
        String toSend;
        Damage d = DATA.get(e.getAuthor().getIdLong());
        switch (terrain) {
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
                toSend = "Choose a valid option! See help for this command for more info.";
        }
        MiscUtil.send(e, toSend, true);
    }
}
