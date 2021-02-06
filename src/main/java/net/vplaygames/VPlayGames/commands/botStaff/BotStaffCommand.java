package net.vplaygames.VPlayGames.commands.botStaff;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;

import java.util.concurrent.TimeUnit;

public abstract class BotStaffCommand extends Command {
    protected BotStaffCommand(String commandName, String... aliases) {
        this(commandName, 0, null, aliases);
    }

    protected BotStaffCommand(String commandName, long cooldown, TimeUnit cooldownUnit, String... aliases) {
        this(commandName, cooldown, cooldownUnit, 0, 0, aliases);
    }

    protected BotStaffCommand(String commandName, long cooldown, TimeUnit cooldownUnit, int minArgs, int maxArgs, String... aliases) {
        super(commandName, cooldown, cooldownUnit, minArgs, maxArgs, aliases);
    }

    @Override
    public void onAccessDenied(GuildMessageReceivedEvent e) {
        MiscUtil.send(e, e.getAuthor().getAsMention() + ", You do not have the permission to do that!", true);
    }

    @Override
    public boolean hasAccess(long aid) {
        return Bot.isStaff(aid);
    }
}
