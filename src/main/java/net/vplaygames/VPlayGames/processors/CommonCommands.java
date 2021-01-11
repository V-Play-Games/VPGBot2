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
package net.vplaygames.VPlayGames.processors;

import net.vplaygames.VPlayGames.commands.general.InviteCommand;
import net.vplaygames.VPlayGames.commands.general.PingCommand;
import net.vplaygames.VPlayGames.commands.pokemon.masters.*;
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.Array;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommonCommands
{
    public static boolean process(GuildMessageReceivedEvent e) {
        new Response(e.getMessage());
        String MSG = e.getMessage().getContentRaw().substring(Bot.current.PREFIX.length());
        String[] msg = MSG.split(" ");
        switch (Array.returnID(Bot.current.COMMANDS,msg[0])) {
            case 0: BuffCommand.process(e); break;
            case 1: CDCommand.process(e); break;
            case 2: if(MSG.length()<8) return false;
            case 3: if(MSG.length()<3) return false; ChooseCommand.process(e); break;
            case 4:
            case 5: MoveisCommand.process(e); break;
            case 6: PMCommand.process(e); break;
            case 7: StatCommand.process(e); break;
            case 8: SMLCommand.process(e); break;
            case 9: TrainerCommand.process(e); break;
            case 10: VerifyCommand.process(e); break;
            case 11: ViewCommand.process(e); break;
            case 12:
            case 13: WeatherCommand.process(e); break;
            case 14: StatusCommand.process(e); break;
            case 15: SkillCommand.process(e); break;
            case 16: GaugeCommand.process(e); break;
            case 17: PingCommand.process(e); break;
            case 18: InviteCommand.process(e); break;
            case 19: Damage.Code(e); break;
            case 20: SearchCommand.getInstance().processSearch(e); break;
            case 21:
            case 22:
            case 23:
            case 24: SearchCommand.getInstance().processNavigation(e); break;
        }
        return true;
    }
}