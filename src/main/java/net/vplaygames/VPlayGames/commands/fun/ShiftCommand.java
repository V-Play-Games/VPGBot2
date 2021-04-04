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
package net.vplaygames.VPlayGames.commands.fun;

import net.vplaygames.VPlayGames.commands.Command;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.util.Strings;

public class ShiftCommand extends Command {
    public static final String chars = "abcdefghijklmnopqrstuvwxyz";

    public ShiftCommand() {
        super("shift", 2, 0);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String[] msg = e.getMessage().getContentRaw().split(" ");
        String shift = Strings.reduceToAlphabets(msg[1]).toLowerCase();
        if (shift.length()!=2) {
            e.send("The given shift \""+shift+"\" is not applicable, as it doesn't have only 2 characters.\nSee this command's help for more info").queue();
            return;
        }
        String toShift = String.join(" ", e.getArgsFrom(2));
        int offset = chars.indexOf(shift.charAt(0))-chars.indexOf(shift.charAt(1));
        e.send(shift(toShift, offset)).queue();
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
