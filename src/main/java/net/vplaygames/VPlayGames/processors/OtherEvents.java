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
import net.vplaygames.VPlayGames.util.Strings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class OtherEvents {
    public static boolean process(GuildMessageReceivedEvent e) {
        boolean tor = false;
        String MSG = e.getMessage().getContentRaw();
        if (Strings.equalsAnyIgnoreCase(Strings.reduceToAlphanumeric(MSG), "Hi", "Hey", "Hello", "Bye")) {
            new Response(e.getMessage());
            MiscUtil.send(e,Strings.toProperCase(MSG),true);
        } else if (e.getMessage().getMentionedUsers().contains(e.getJDA().getSelfUser()))
            tor = BotPingedEvent(e);
        return tor;
    }

    public static boolean BotPingedEvent(GuildMessageReceivedEvent e) {
        String devMention = e.getJDA().retrieveUserById(Bot.BOT_OWNER).complete().getAsMention();
        EmbedBuilder emb = new EmbedBuilder();
        emb.setAuthor("V Play Games Bot Info");
        emb.setDescription("A Pokemon-related bot created by "+devMention);
        emb.setThumbnail(e.getJDA().getSelfUser().getAvatarUrl());
        emb.setColor(0x1abc9c);
        emb.addField("Developer", devMention, true);
        emb.addField("Version", Bot.VERSION, true);
        emb.addField("Server Count", Integer.toString(e.getJDA().getGuilds().size()), false);
        emb.setFooter("Sent " + MiscUtil.dateTimeNow());
        e.getChannel().sendMessage("Prefix: " + Bot.PREFIX).embed(emb.build()).queue();
        new Response(e.getMessage()).responded("Bot-Pinged-Message");
        return true;
    }
}
