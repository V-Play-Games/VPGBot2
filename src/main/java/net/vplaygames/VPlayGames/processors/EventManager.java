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

import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventManager extends ListenerAdapter
{
    private static EventManager instance;
    public static long currentTime;

    private EventManager() {}

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        if (Bot.current.closed) {
            BotStaffCommands.activate(e);
            return;
        }
        currentTime = System.currentTimeMillis();
        if (!e.getAuthor().isBot()) {
            if (OtherEvents.process(e)||Commands.process(e)) {
                log(e.getMessage(),currentTime);
            }
        }
    }

    public void log(Message msg, long time) {
        Bot bot = Bot.current;
        System.out.println("Response ID: "+bot.messagesProcessed+
                "\nProcessing time: "+(System.currentTimeMillis()-time)+"ms"+
                "\nSent by: <@"+msg.getAuthor().getId()+" in the channel <#"+msg.getChannel().getIdLong()+"> at"+ MiscUtil.dateTimeNow()+
                "\nMessage: "+msg.getContentRaw());
        if(bot.logChan!=null)
            bot.logChan.sendMessage(Response.get(bot.messagesProcessed).getAsEmbed(false)).queue();
        bot.messagesProcessed++;
    }

    public static EventManager getInstance() {
        return instance == null ? (instance = new EventManager()) : instance;
    }
}