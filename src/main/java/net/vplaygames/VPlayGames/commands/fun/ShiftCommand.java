package net.vplaygames.VPlayGames.commands.fun;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.util.MiscUtil;
import net.vplaygames.VPlayGames.util.Strings;

public class ShiftCommand extends Command {
    public static final String chars = "abcdefghijklmnopqrstuvwxyz";

    public ShiftCommand() {
        super("shift", 2, 0);
    }

    @Override
    public void onCommandRun(GuildMessageReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String shift = Strings.reduceToAlphabets(msg[1]).toLowerCase();
        if (shift.length()!=2) {
            MiscUtil.send(e, "The given shift \""+shift+"\" is not applicable, as it doesn't have only 2 characters.\nSee this command's help for more info", true);
            return;
        }
        String toShift = e.getMessage().getContentRaw().substring((msg[0]+msg[1]+" ").length());
        int offset = chars.indexOf(shift.charAt(0))-chars.indexOf(shift.charAt(1));
    }

    public String shift(String toShift, int offset) {
        if (offset==0) {
            return toShift;
        }
        String sheet = chars+chars;
        if (offset<0) {
            offset = 26+offset;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : toShift.toCharArray()) {
            if (sheet.contains(c+"")) {
                sb.append(sheet.charAt(sheet.indexOf(c)+offset));
            } else if (isNormalUppercaseLetter(c)) {
                sb.append(sheet.toUpperCase().charAt(sheet.toUpperCase().indexOf(c)+offset));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public boolean isNormalUppercaseLetter(char c) {
        String string = c + "";
        return !chars.contains(string) && chars.contains(string.toLowerCase());
    }
}
