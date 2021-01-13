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

import com.vplaygames.PM4J.caches.PokemasDBCache;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.core.Timer;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.EmbedUtil;
import net.vplaygames.VPlayGames.util.MiscUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.time.LocalTime;

public class EventHandler extends ListenerAdapter {
    private static EventHandler instance;
    public static volatile long currentTime;
    public static volatile boolean forceNotLog;
    public static volatile boolean forceLog;
    public static volatile int consecutiveErrors = 0;
    public GuildMessageReceivedEvent current;

    protected EventHandler() {
        forceLog = false;
        forceNotLog = true;
    }

    public static EventHandler getInstance() {
        return instance == null ? instance = new EventHandler() : instance;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        try {
            process(e);
        } catch (Throwable t) {
            process(t);
        }
    }

    public void process(GuildMessageReceivedEvent e) {
        consecutiveErrors=0;
        current = e;
        if (Bot.closed) {
            BotStaffCommands.activate(e);
            return;
        }
        currentTime = System.currentTimeMillis();
        boolean log = !e.getAuthor().isBot() && e.getChannel().canTalk();
        log = log && (OtherEvents.process(e) || Commands.process(e));
        log = log || forceLog;
        if (log && (!forceNotLog)) {
            log();
        }
        forceLog = false;
        forceNotLog = false;
    }

    public static GuildMessageReceivedEvent getCurrent() {
        return instance.current;
    }

    @Override
    public void onException(@Nonnull ExceptionEvent e) {
        process(e.getCause());
    }

    @Override
    public void onReady(@Nonnull ReadyEvent e) {
        Bot.jda = e.getJDA();
        while (PokemasDBCache.getInstance() == null && MiscUtil.isThreadRunning("PM4J-Cache")) ;
        Bot.setLogChannel(Bot.jda.getTextChannelById(Bot.DEFAULT_LOG_CHANNEL_ID));
        Bot.setSyncChannel(Bot.jda.getTextChannelById(Bot.DEFAULT_SYNC_CHANNEL_ID));
        Timer.newTimer();
        Bot.syncData(Bot.damageData);
        Bot.logChannel.sendMessage("I am ready for anything!\n\t-Morty, Johto Gym Leader, 2020").queue();
        Bot.setDefaultActivity();
    }

    public void process(Throwable thrown) {
        if (consecutiveErrors==5) return;
        thrown.printStackTrace();
        MessageEmbed err = Response.addExceptionLog(EmbedUtil.prepareEmbed(thrown).build());
        if (Bot.logChannel != null)
            Bot.logChannel.sendMessage(err).queue();
    }

    public void log() {
        System.out.println(LocalTime.now() + " [" + Thread.currentThread().getName() + "] " + getClass().getName());
        System.out.println(Response.get(Bot.messagesProcessed).toString());
        if(Bot.logChannel !=null)
            Bot.logChannel.sendMessage(Response.get(Bot.messagesProcessed).getAsEmbed(false)).queue();
        Bot.messagesProcessed++;
    }
}
