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

import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Objects;

public class PingCommand
{
    public static void process(GuildMessageReceivedEvent e)
    {
        long time = System.currentTimeMillis();
        try {
            Objects.requireNonNull(e.getJDA().getUserById(Bot.current.BOT_OWNER))
                    .openPrivateChannel()
                    .complete()
                    .sendMessage("!")
                    .queue(r -> r.editMessage(Bot.current.PREFIX+"ping by "+e.getAuthor().getAsMention()).queue());
        } catch (Exception exc) {
            System.out.println("Oof! An error occurred while generating a ping!");
            exc.printStackTrace();
        }
        time = System.currentTimeMillis()-time;
        String s="Pong! "+time+" ms\n[Reaction Speed(in %): "+(100.0-time/40)+"%]";
        MiscUtil.send(e,s,true);
    }
}