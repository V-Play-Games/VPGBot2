/*
 * Copyright 2020-2021 Vaibhav Nargwani
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
package net.vplaygames.VPlayGames.core;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.commands.ICommand;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class EventHandler extends ListenerAdapter {
    private static EventHandler instance;

    protected EventHandler() {}

    public static EventHandler getInstance() {
        return instance == null ? instance = new EventHandler() : instance;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        try {
            process(e);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void process(GuildMessageReceivedEvent e) {
        if (Bot.closed && e.getMessage().getContentRaw().equalsIgnoreCase("v!activate") && Bot.isStaff(e.getAuthor().getIdLong())) {
            Bot.closed = false;
            e.getChannel().sendMessage("Thanks for activating me again!").queue();
        } else if (!e.getAuthor().isBot() && e.getChannel().canTalk()) {
            handleEvent(e);
        }
    }

    @Override
    public void onException(@Nonnull ExceptionEvent e) {
        e.getCause().printStackTrace();
    }

    @Override
    public void onReady(@Nonnull ReadyEvent e) {
        Bot.jda = e.getJDA();
        Bot.init();
    }

    public void process(Throwable thrown) {
        thrown.printStackTrace();
    }

    public static void handleEvent(GuildMessageReceivedEvent e) {
        Message message = e.getMessage();
        String content = message.getContentRaw();
        String[] args = content.split(" ");
        if (content.toLowerCase().startsWith(Bot.PREFIX)) {
            ICommand command = Bot.commands.get(args[0].substring(Bot.PREFIX.length()).toLowerCase());
            if (command != null) {
                new CommandReceivedEvent(e, args, command);
            }
        } else if (Strings.equalsAnyIgnoreCase(Strings.reduceToAlphanumeric(content), "Hi", "Hey", "Hello", "Bye")) {
            e.getChannel().sendMessage(Strings.toProperCase(content) + "!").queue();
        } else if (message.getMentionedUsers().contains(e.getJDA().getSelfUser())) {
            BotPingedEvent(e);
        }
    }

    public static void BotPingedEvent(GuildMessageReceivedEvent e) {
        e.getJDA().retrieveUserById(Bot.BOT_OWNER).queue(dev -> e.getChannel().sendMessage("Prefix: " + Bot.PREFIX).embed(new EmbedBuilder()
            .setAuthor("V Play Games Bot Info")
            .setDescription("A Pokemon-related bot created by "+dev.getAsMention())
            .setThumbnail(e.getJDA().getSelfUser().getAvatarUrl())
            .setColor(0x1abc9c)
            .addField("Developer", dev.getAsMention(), true)
            .addField("Version", Bot.VERSION, true)
            .addField("Server Count", Integer.toString(e.getJDA().getGuilds().size()), false)
            .setFooter("Sent " + MiscUtil.dateTimeNow())
            .build()).queue());
    }
}