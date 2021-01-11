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
        String[] msg=e.getMessage().getContentRaw().split(" ");
        String msg_s = e.getMessage().getContentRaw();
        if (!e.getAuthor().isBot())
        {
            if(msg[0].equals(prefix+"buff"))
                BuffCommand(e);
            else if(msg_s.equals(prefix+"cd"))
                CalculateDamageCommand(e);
            else if((msg[0].equals(prefix+"choose")||msg[0].equals(prefix+"c"))&&msg.length==2)
                ChooseCommand(e);
            else if((msg[0].equals(prefix+"moveis")&&msg_s.length()>=prefix.length()+8)||(msg[0].equals(prefix+"mi")&&msg_s.length()>=prefix.length()+4))
                MiscCommands(e);
            else if(msg[0].equals(prefix + "pm")&&msg.length>1)
                PMCommand(e);
            else if(msg[0].equals(prefix+"stat"))
                StatRegisterCommand(e);
            else if(msg[0].equals(prefix+"sml"))
                SyncMoveLevel(e);
            else if(msg_s.startsWith(prefix+"trainer "))
                TrainerCommand(e);
            else if(msg_s.equals(prefix+"verify"))
                VerifyCommand(e);
            else if(!e.getAuthor().isBot()&&msg[0].equals(prefix+"view"))
                ViewCommand(e);
            else if(!e.getAuthor().isBot()&&(msg[0].equals(prefix+"weather")||msg[0].equals(prefix+"wthr")))
                WeatherCommand(e);
            else if(msg_s.equals(prefix+"ping"))
                PingCommand(e.getChannel());
        }
    }
}
