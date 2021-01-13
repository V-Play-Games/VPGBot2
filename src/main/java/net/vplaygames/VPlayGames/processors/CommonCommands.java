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

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.data.Bot;

public class CommonCommands
{
    public static boolean process(GuildMessageReceivedEvent e) {
        new Response(e.getMessage());
        String[] msg = Commands.split(e.getMessage().getContentRaw());
        Bot.commands.get(msg[0]).run(e);
        return true;
    }
}