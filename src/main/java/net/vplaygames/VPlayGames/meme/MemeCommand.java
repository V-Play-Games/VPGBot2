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
package net.vplaygames.VPlayGames.meme;

import net.vplaygames.VPlayGames.processors.EventHandler;
import net.vplaygames.VPlayGames.util.Strings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class MemeCommand {
    public static void process(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        try {
            Connection conn = new Connection();
            if (msg.length == 1) {
                sendMeme(e.getChannel(),conn.getMeme());
            } else if (msg.length == 2) {
                if (Strings.toInt(msg[1]) == 0) {
                    sendMeme(e.getChannel(),conn.getMeme(msg[1]));
                } else {
                    ArrayList<Meme> memes = conn.getMemes(Strings.toInt(msg[1]));
                    for (int i = 1; i<memes.size(); i++) {
                        sendMeme(e.getChannel(),memes.get(i));
                    }
                    sendMeme(e.getChannel(),memes.get(0));
                }
            } else if (msg.length == 3) {
                ArrayList<Meme> memes = conn.getMemes(Strings.toInt(msg[2]),msg[1]);
                for (int i = 1; i<memes.size(); i++) {
                    sendMeme(e.getChannel(),memes.get(i));
                }
                sendMeme(e.getChannel(),memes.get(0));
            } else {
                e.getChannel().sendMessage("An error occurred while getting the meme. Please Try Again Later.").queue();
            }
        } catch (Exception exc) {
            EventHandler.getInstance().process(exc);
            e.getChannel().sendMessage("An error occurred while getting the meme. Please Try Again Later.").queue();
        }
    }

    public static EmbedBuilder memeToEmbed(Meme meme) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle(meme.title,meme.postLink);
        eb.setImage(meme.url);
        eb.addField("Details","Posted by "+meme.author+" in r/"+meme.subreddit,false);
        eb.setFooter("This meme post has got "+meme.ups+" upvotes so far.");
        return eb;
    }

    public static void sendMeme(TextChannel tc, Meme meme) {
        if (meme.nsfw) {
            if (tc.isNSFW()) {
                tc.sendMessage(memeToEmbed(meme).build()).queue();
            } else {
                tc.sendMessage("The Meme I just saw was not funny enough. Please try again later.").queue();
            }
        } else {
            tc.sendMessage(memeToEmbed(meme).build()).queue();
        }
    }
}
