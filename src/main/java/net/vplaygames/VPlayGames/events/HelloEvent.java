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
package net.vplaygames.VPlayGames.events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class HelloEvent extends ListenerAdapter
{
    public void onGuildMessageReceived(GuildMessageReceivedEvent e)
    {
        String msg = e.getMessage().getContentRaw(),to_send="";
        int tsv=1;
        if(msg.equalsIgnoreCase("Hello"))
            to_send="Hello!";
        else if(msg.equalsIgnoreCase("Hi"))
            to_send="Hi!";
        else if(msg.equalsIgnoreCase("Bye"))
            to_send="Bye!";
        else
            tsv=0;
        if(tsv==1)
            e.getChannel().sendMessage(to_send).queue();
    }
}
