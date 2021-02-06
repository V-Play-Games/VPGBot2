package net.vplaygames.VPlayGames.commands.fun;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;

public class CaseCommand extends Command {
    public CaseCommand() {
        super("case", 2, 0);
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String msg = e.getMessage().getContentRaw();
        String[] args = msg.split(" ");
        String input = msg.substring((args[0]+args[1]+"  ").length());
        String toSend;
        switch (args[1].toLowerCase()) {
            case "upper":
            case "u":
                toSend = input.toUpperCase();
                break;
            case "lower":
            case "l":
                toSend = input.toLowerCase();
                break;
            case "proper":
            case "p":
                toSend = Strings.toProperCase(input);
                break;
            default:
                toSend = "Invalid Case!";
        }
        MiscUtil.send(e, toSend, true);
    }
}
