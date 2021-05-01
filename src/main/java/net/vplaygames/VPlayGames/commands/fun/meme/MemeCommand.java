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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.vplaygames.VPlayGames.commands.Command;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.core.Bot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MemeCommand extends Command {
    public static final ArrayList<Meme> randomMemes = new ArrayList<>();
    public Connection conn = new Connection();

    public MemeCommand() {
        super("meme", 10, TimeUnit.SECONDS, 0, 1);
        Bot.timer.execute(() -> {
            try {
                randomMemes.addAll(conn.getMemes(10));
                randomMemes.addAll(conn.getMemes(10, "PokemonMasters"));
                randomMemes.addAll(conn.getMemes(10, "Pokemon"));
            } catch (IOException ignored) {}
        });
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) throws IOException {
        if (!e.isFromGuild()) {
            e.send("Sorry, but this command cannot be used in DMs.").queue();
            return;
        }
        Meme meme = conn.getMeme(e.getArgs().size() == 2 ? e.getArg(1) : "");
        if (meme.nsfw && !((TextChannel) e.getChannel()).isNSFW()) {
            meme = randomMemes.get(new Random().nextInt(randomMemes.size()));
        } else {
            Optional<Meme> optional = Optional.of(meme);
            if (randomMemes.stream().noneMatch(m -> m.url.equals(optional.get().url)))
                randomMemes.add(meme);
        }
        e.send(new EmbedBuilder()
            .setTitle(meme.title, meme.postLink)
            .setDescription("Meme by u/" + meme.author + " in r/" + meme.subreddit)
            .setImage(meme.url)
            .setFooter(meme.ups + " Upvotes").build(), meme.url).queue();
    }
}
