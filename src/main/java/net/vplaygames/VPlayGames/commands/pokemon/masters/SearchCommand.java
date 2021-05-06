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
package net.vplaygames.VPlayGames.commands.pokemon.masters;

import com.vplaygames.PM4J.caches.Cache.Type;
import com.vplaygames.PM4J.caches.MoveDataCache;
import com.vplaygames.PM4J.caches.PokemonDataCache;
import com.vplaygames.PM4J.caches.SkillDataCache;
import com.vplaygames.PM4J.caches.TrainerDataCache;
import com.vplaygames.PM4J.entities.Pokemon;
import com.vplaygames.PM4J.entities.Trainer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.vplaygames.VPlayGames.commands.Command;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.core.UserEmbedInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static com.vplaygames.PM4J.caches.Cache.Type.*;
import static net.vplaygames.VPlayGames.core.Bot.PREFIX;
import static net.vplaygames.VPlayGames.util.EmbedUtil.getFooter;
import static net.vplaygames.VPlayGames.util.EmbedUtil.prepareEmbed;

public class SearchCommand extends Command {
    public static HashMap<Long, UserEmbedInfo> embedInfo = new HashMap<>();

    public SearchCommand() {
        super("search");
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String data = String.join(" ", e.getArgsFrom(1));
        Optional<Type> first = Arrays.stream(values()).filter(t -> t != UNKNOWN && t.getCache().containsKey(data)).findFirst();
        if (!first.isPresent()) {
            e.send("\"" + data + "\" not found.").queue();
            return;
        }
        Type type = first.get();
        if (isTurnable(type)) {
            int limit;
            EmbedBuilder eb;
            if (type == TRAINER) {
                Trainer t = TrainerDataCache.getInstance().get(data);
                eb = prepareEmbed(t);
                limit = t.pokemon.length;
            } else {
                ArrayList<Pokemon> pd = PokemonDataCache.getInstance().get(data);
                eb = prepareEmbed(pd);
                limit = pd.size();
            }
            e.send("You have opened a new " + type.toString().toLowerCase() + " search query!\n" +
                "Any previous queries of yours will not be used to turn pages with `" + PREFIX + "next` & `" + PREFIX + "back`")
                .flatMap(x -> e.getChannel().sendMessage(eb
                    .setFooter(getFooter(1, limit + 1) + " | Requested by " + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl())
                    .setTimestamp(e.getMessage().getTimeCreated())
                    .build()))
                .queue(response ->
                    embedInfo.put(e.getAuthor().getIdLong(),
                        new UserEmbedInfo(e.getAuthor().getIdLong(), response.getIdLong(), data, limit, type)));
        } else {
            e.send((type == MOVE ? prepareEmbed(MoveDataCache.getInstance().get(data)) : prepareEmbed(SkillDataCache.getInstance().get(data)))
                .setThumbnail(e.getAuthor().getEffectiveAvatarUrl())
                .setFooter("Requested by: " + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl())
                .setTimestamp(e.getMessage().getTimeCreated())
                .build(), type.toString() + " Data for " + data).queue();
        }
    }

    public static boolean isTurnable(Type type) {
        return type == POKEMON || type == TRAINER;
    }
}
