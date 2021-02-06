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
package net.vplaygames.VPlayGames.commands.fun.meme;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.processors.EventHandler;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class MemeCommand extends Command {
    public static final ArrayList<Meme> randomMemes = new ArrayList<>();
    public Connection conn = new Connection();

    public MemeCommand() throws IOException {
        super("meme", 10, TimeUnit.SECONDS, 0, 2);
        randomMemes.addAll(conn.getMemes(10));
        randomMemes.addAll(conn.getMemes(10, "PokemonMasters"));
        randomMemes.addAll(conn.getMemes(10, "Pokemon"));
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        StringJoiner toSend = new StringJoiner("\n");
        TextChannel tc = e.getChannel();
        try {
            switch (msg.length) {
                case 1:
                    toSend.add(getMemeLink(tc, conn.getMeme()));
                    break;
                case 2:
                    if (Strings.toInt(msg[1]) < 2) {
                        toSend.add(getMemeLink(e.getChannel(), conn.getMeme(msg[1])));
                    } else {
                        conn.getMemes(Integer.parseInt(msg[1])).forEach(meme -> toSend.add(getMemeLink(tc, meme)));
                    }
                    break;
                case 3:
                    conn.getMemes(Strings.toInt(msg[2]), msg[1]).forEach(meme -> toSend.add(getMemeLink(tc, meme)));
                    break;
            }
            if (toSend.length() == 0) toSend.add(randomMemes.get(new Random().nextInt(randomMemes.size())).postLink);
            MiscUtil.send(e, toSend.toString(), true);
        } catch (Exception exc) {
            EventHandler.getInstance().process(exc);
            e.getChannel().sendMessage("An error occurred while getting the meme. Please try Again Later.").queue();
        }
    }

    public static String getMemeLink(TextChannel tc, Meme meme) {
        String tor;
        if (meme.nsfw && !tc.isNSFW()) {
            tor = randomMemes.get(new Random().nextInt(randomMemes.size())).postLink;
        } else {
            tor = meme.postLink;
        }
        return tor;
    }
}