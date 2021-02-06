package net.vplaygames.VPlayGames.commands.botStaff;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Damage;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;

public class PutCommand extends BotStaffCommand {
    public PutCommand() {
        super("put");
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String toTranslate = e.getMessage().getContentRaw().substring(e.getMessage().getContentRaw().split(" ")[0].length());
        Bot.DATA.put(e.getAuthor().getIdLong(), Damage.parseFromString(toTranslate));
        MiscUtil.send(e,"Put translation of "+toTranslate,true);
    }
}
