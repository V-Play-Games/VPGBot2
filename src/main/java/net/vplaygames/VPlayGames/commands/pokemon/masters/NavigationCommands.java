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

import com.vplaygames.PM4J.caches.PokemonDataCache;
import com.vplaygames.PM4J.caches.TrainerDataCache;
import com.vplaygames.PM4J.entities.Pokemon;
import com.vplaygames.PM4J.entities.Trainer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.vplaygames.VPlayGames.commands.Command;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.core.UserEmbedInfo;

import java.util.ArrayList;

import static com.vplaygames.PM4J.caches.Cache.Type.POKEMON;
import static net.vplaygames.VPlayGames.commands.pokemon.masters.SearchCommand.embedInfo;
import static net.vplaygames.VPlayGames.util.EmbedUtil.getFooter;
import static net.vplaygames.VPlayGames.util.EmbedUtil.prepareEmbed;

public class NavigationCommands extends Command {
    public NavigationCommands() {
        super("jump");
        new Command("next", "n") {
            @Override public void onCommandRun(CommandReceivedEvent e) { turnPage(e, true);  }
        };
        new Command("back", "b") {
            @Override public void onCommandRun(CommandReceivedEvent e) { turnPage(e, false); }
        };
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {}

    public void turnPage(CommandReceivedEvent e, boolean next) {
        UserEmbedInfo uei = embedInfo.get(e.getAuthor().getIdLong());
        if (uei == null) return;
        if (uei.proceed(next))
            e.getChannel().retrieveMessageById(uei.embedId).queue(m -> {
                int prog = uei.progress;
                boolean start = prog == 0;
                EmbedBuilder embed;
                if (uei.type == POKEMON) {
                    ArrayList<Pokemon> pokes = PokemonDataCache.getInstance().get(uei.dataTitle);
                    embed = start ? prepareEmbed(pokes) : prepareEmbed(pokes.get(prog - 1));
                } else {
                    Trainer t = TrainerDataCache.getInstance().get(uei.dataTitle);
                    embed = start ? prepareEmbed(t) : prepareEmbed(t.pokemonData.get(prog - 1));
                }
                m.editMessage(embed
                    .setFooter(getFooter(prog + 1, uei.limit + 1) + " | Requested by " + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl())
                    .setTimestamp(e.getMessage().getTimeCreated())
                    .build()).queue();
                e.send("Changed the page from " + (prog + (next ? 0 : 1)) + " to " + (prog + (next ? 1 : 0))).reference(m).queue();
            }, t -> {
                e.send("An error occurred while turning the page! Please search again.").queue();
                embedInfo.remove(e.getAuthor().getIdLong());
                e.reportTrouble(t);
            });
        else
            e.send("You're at the " + (next ? "end" : "start") + " of the query result. Can't turn the page any further").queue();
    }
}
