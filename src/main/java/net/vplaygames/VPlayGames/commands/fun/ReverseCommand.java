package net.vplaygames.VPlayGames.commands.fun;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.util.MiscUtil;

public class ReverseCommand extends Command {
    public ReverseCommand() {
        super("reverse", 1, 0);
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String toReverse = e.getMessage().getContentRaw().substring((msg[0]+msg[1]+"  ").length());
        MiscUtil.send(e, new StringBuilder(toReverse).reverse().toString(),true);
    }
}
