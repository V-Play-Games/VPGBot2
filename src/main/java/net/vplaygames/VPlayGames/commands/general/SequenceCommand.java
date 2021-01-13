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
package net.vplaygames.VPlayGames.commands.general;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.core.Wrap;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.processors.EventHandler;
import net.vplaygames.VPlayGames.util.MiscUtil;

public class SequenceCommand extends Command {
    public SequenceCommand() {
        super("sequence");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        Message m = e.getMessage();
        String msg = m.getContentRaw();
        String toSend;
        if (msg.contains("\n")) {
            toSend = "Sequence DONE!";
            for (String command : msg.split("\n")) {
                EventHandler.getInstance().process(new GuildMessageReceivedEvent(e.getJDA(),
                        e.getResponseNumber(),
                        new Wrap(m, command)));
                Bot.messagesProcessed--;
            }
        } else
            toSend = "Please Give a set of commands first!";
        new Response(m);
        MiscUtil.send(e, toSend, true);
    }
}
