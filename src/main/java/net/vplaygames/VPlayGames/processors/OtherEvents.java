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

public class OtherEvents
{
    public static boolean process(GuildMessageReceivedEvent e) {
        boolean tor=false;
        String MSG=e.getMessage().getContentRaw();
        if (Strings.equalsAnyIgnoreCase(MSG,"Hi","Hey","Hello","Bye")&&e.getChannel().canTalk())
            e.getChannel().sendMessage(Strings.toProperCase(MSG)).queue();
        if (MSG.contains(e.getJDA().getSelfUser().getAsMention()))
            tor=BotPingedEvent(e);
        return tor;
    }

    public static boolean BotPingedEvent(GuildMessageReceivedEvent e)
    {
        EmbedBuilder emb = new EmbedBuilder();
        emb.setAuthor("V Play Games Bot Info");
        emb.setDescription("A Pokemon-related bot created by @V Play Games#9783");
        emb.setThumbnail(e.getJDA().getSelfUser().getAvatarUrl());
        emb.setColor(0x1abc9c);
        emb.addField("Developer",e.getJDA().getUserById(Bot.current.BOT_OWNER).getAsMention(),true);
        emb.addField("Version","1.4.0",true);
        emb.addField("Server Count",Integer.toString(e.getJDA().getGuilds().size()),false);
        emb.setFooter("Requested "+ MiscUtil.dateTimeNow());
        e.getChannel().sendMessage("Prefix: "+Bot.current.PREFIX).embed(emb.build()).queue();
        new Response(e.getMessage()).Responded("Bot-Pinged-Message");
        return true;
    }
}
