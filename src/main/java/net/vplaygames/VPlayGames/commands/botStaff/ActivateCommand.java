package net.vplaygames.VPlayGames.commands.botStaff;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;

public class ActivateCommand extends BotStaffCommand {
    public ActivateCommand() {
        super("activate");
        Bot.activateCommand = this;
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        Bot.closed = false;
        MiscUtil.send(e, "Thanks for activating me again!", true);
    }
}
