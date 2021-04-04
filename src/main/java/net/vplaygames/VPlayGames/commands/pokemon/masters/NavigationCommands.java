package net.vplaygames.VPlayGames.commands.pokemon.masters;

import com.vplaygames.PM4J.caches.PokemonDataCache;
import com.vplaygames.PM4J.caches.TrainerDataCache;
import com.vplaygames.PM4J.entities.Pokemon;
import com.vplaygames.PM4J.entities.Trainer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.vplaygames.VPlayGames.core.UserEmbedInfo;
import net.vplaygames.VPlayGames.commands.Command;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;

import java.util.ArrayList;

import static com.vplaygames.PM4J.caches.framework.Cache.Type.POKEMON;
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
        if (uei.proceed(next)) {
            e.getChannel().retrieveMessageById(uei.embedId).queue(m -> {
                int prog = uei.progress;
                boolean start = prog == 0;
                EmbedBuilder embed;
                if (uei.type == POKEMON) {
                    ArrayList<Pokemon> pokes = PokemonDataCache.getInstance().get(uei.dataTitle);
                    if (start) {
                        embed = prepareEmbed(pokes);
                    } else {
                        embed = prepareEmbed(pokes.get(prog - 1));
                    }
                } else {
                    Trainer t = TrainerDataCache.getInstance().get(uei.dataTitle);
                    if (start) {
                        embed = prepareEmbed(t);
                    } else {
                        embed = prepareEmbed(t.pokemonData.get(prog - 1));
                    }
                }
                m.editMessage(embed
                    .setFooter(getFooter((start ? 0 : prog) + 1, uei.limit + 1) + " | Requested by " + e.getAuthor().getAsTag(), e.getAuthor().getEffectiveAvatarUrl())
                    .setTimestamp(e.getMessage().getTimeCreated())
                    .build()).queue();
                e.send("Changed the page from " + (prog + (next ? 0 : 1)) + " to " + (prog + (next ? 1 : 0))).reference(m).queue();
            }, exc -> {
                uei.proceed(!next);
                e.send("An error occurred while turning the page! Maybe the message was deleted.").queue();
                exc.printStackTrace();
            });
        } else {
            e.send("You're at the " + (next ? "end" : "start") + " of the query result. Can't turn the page any further").queue();
        }
    }
}
