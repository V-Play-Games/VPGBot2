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
package net.vplaygames.VPlayGames.db;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static net.vplaygames.VPlayGames.commands.BuffCommand.BuffCommand;
import static net.vplaygames.VPlayGames.commands.CalculateDamageCommand.CalculateDamageCommand;
import static net.vplaygames.VPlayGames.commands.ChooseCommand.ChooseCommand;
import static net.vplaygames.VPlayGames.commands.MiscCommands.MiscCommands;
import static net.vplaygames.VPlayGames.commands.PMCommand.PMCommand;
import static net.vplaygames.VPlayGames.commands.PingCommand.PingCommand;
import static net.vplaygames.VPlayGames.commands.StatRegisterCommand.StatRegisterCommand;
import static net.vplaygames.VPlayGames.commands.StatusCommand.StatusCommand;
import static net.vplaygames.VPlayGames.commands.SyncMoveLevel.SyncMoveLevel;
import static net.vplaygames.VPlayGames.commands.TrainerCommand.TrainerCommand;
import static net.vplaygames.VPlayGames.commands.VerifyCommand.VerifyCommand;
import static net.vplaygames.VPlayGames.commands.ViewCommand.ViewCommand;
import static net.vplaygames.VPlayGames.commands.WeatherCommand.WeatherCommand;
import static net.vplaygames.VPlayGames.db.botresources.prefix;

public class CommandManager extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg_s = e.getMessage().getContentRaw();
        if (!e.getAuthor().isBot()&&msg_s.startsWith(prefix))
        {
            msg_s=msg_s.substring(prefix.length());
            String[] msg = msg_s.split(" ");
            switch (msg[0]) {
                case "buff": BuffCommand(e); break;
                case "cd": CalculateDamageCommand(e); break;
                case "choose": if(!(msg_s.length()>=9)){return;} ChooseCommand(e); break;
                case "c": if(!(msg_s.length()>=5)){return;} ChooseCommand(e); break;
                case "moveis":
                case "mi": MiscCommands(e); break;
                case "pm": PMCommand(e); break;
                case "stat": StatRegisterCommand(e); break;
                case "sml": SyncMoveLevel(e); break;
                case "trainer": TrainerCommand(e); break;
                case "verify": VerifyCommand(e); break;
                case "view": ViewCommand(e); break;
                case "weather":
                case "wthr": WeatherCommand(e); break;
                case "status": StatusCommand(e); break;
                case "ping": PingCommand(e.getChannel()); break;
            }
        }
    }
}
