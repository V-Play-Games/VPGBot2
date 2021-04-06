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
package net.vplaygames.VPlayGames.commands.fun.meme;

import net.dv8tion.jda.api.entities.TextChannel;
import net.vplaygames.VPlayGames.commands.Command;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.util.Strings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class MemeCommand extends Command {
    public static final ArrayList<Meme> randomMemes = new ArrayList<>();
    public Connection conn = new Connection();

    public MemeCommand() {
        super("meme", 10, TimeUnit.SECONDS, 0, 2);
        try {
            randomMemes.addAll(conn.getMemes(10));
            randomMemes.addAll(conn.getMemes(10, "PokemonMasters"));
            randomMemes.addAll(conn.getMemes(10, "Pokemon"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        if (!e.isFromGuild()) {
            e.send("Sorry, but this command cannot be used in DMs.").queue();
            return;
        }
        StringJoiner toSend = new StringJoiner("\n");
        TextChannel tc = (TextChannel) e.getChannel();
        try {
            switch (e.getArgs().size()) {
                case 1:
                    toSend.add(getMemeLink(tc, conn.getMeme()));
                    break;
                case 2:
                    if (Strings.toInt(e.getArg(1)) < 2) {
                        toSend.add(getMemeLink(tc, conn.getMeme(e.getArg(1))));
                    } else {
                        conn.getMemes(Integer.parseInt(e.getArg(1))).forEach(meme -> toSend.add(getMemeLink(tc, meme)));
                    }
                    break;
                case 3:
                    conn.getMemes(Strings.toInt(e.getArg(2)), e.getArg(1)).forEach(meme -> toSend.add(getMemeLink(tc, meme)));
                    break;
            }
            e.send(toSend.toString()).queue();
        } catch (Exception exc) {
            exc.printStackTrace();
            e.getChannel().sendMessage("An error occurred while getting the meme. Please try Again Later.").queue();
        }
    }

    public static String getMemeLink(TextChannel tc, Meme meme) {
        return (meme.nsfw && !tc.isNSFW() ? randomMemes.get(new Random().nextInt(randomMemes.size())) : meme).postLink;
    }
}
