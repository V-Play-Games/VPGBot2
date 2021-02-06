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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Response;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;

public class Commands {
    public static boolean process(GuildMessageReceivedEvent e) {
        Message message = e.getMessage();
        String content = message.getContentRaw();
        String command = split(content.toLowerCase())[0];
        if (Bot.commands.containsKey(command)) {
            new Response(message);
            return Bot.commands.get(command).run(e);
        } else if (Strings.equalsAnyIgnoreCase(Strings.reduceToAlphanumeric(content), "Hi", "Hey", "Hello", "Bye")) {
            new Response(message);
            MiscUtil.send(e, Strings.toProperCase(content) + ", " + e.getAuthor().getAsMention() + "!", true);
            return true;
        } else if (message.getMentionedUsers().contains(e.getJDA().getSelfUser())) {
            BotPingedEvent(e);
            return true;
        }
        return false;
    }

    public static String[] split(String s) {
        return s.replaceAll("\n", " ").substring(Bot.PREFIX.length()).split(" ");
    }

    public static void BotPingedEvent(GuildMessageReceivedEvent e) {
        String devMention = e.getJDA().retrieveUserById(Bot.BOT_OWNER).complete().getAsMention();
        EmbedBuilder emb = new EmbedBuilder();
        emb.setAuthor("V Play Games Bot Info");
        emb.setDescription("A Pokemon-related bot created by " + devMention);
        emb.setThumbnail(e.getJDA().getSelfUser().getAvatarUrl());
        emb.setColor(0x1abc9c);
        emb.addField("Developer", devMention, true);
        emb.addField("Version", Bot.VERSION, true);
        emb.addField("Server Count", Integer.toString(e.getJDA().getGuilds().size()), false);
        emb.setFooter("Sent " + MiscUtil.dateTimeNow());
        e.getChannel().sendMessage("Prefix: " + Bot.PREFIX).embed(emb.build()).queue();
        new Response(e.getMessage()).responded("Bot-Pinged-Message");
    }
}
