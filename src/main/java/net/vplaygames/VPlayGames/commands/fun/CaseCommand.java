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

import java.util.regex.Pattern;

public class CaseCommand extends Command {
    public CaseCommand() {
        super("case", 2, 0);
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) {
        String input = e.getContent().substring(e.getContent().lastIndexOf(e.getArg(1))+1);
        switch (e.getArg(1).toLowerCase()) {
            case "upper":
            case "u":
                input = input.toUpperCase();
                break;
            case "lower":
            case "l":
                input = input.toLowerCase();
                break;
            case "proper":
            case "p": {
                boolean capitalNextLetter = true;
                StringBuilder sb = new StringBuilder();
                Pattern delimiter = Pattern.compile("[\n\\s]");
                for (int i = 0; i < input.length(); i++) {
                    char c = input.charAt(i);
                    if (Character.isLetter(c))
                        if (capitalNextLetter)
                            sb.append(Character.toUpperCase(c));
                        else
                            sb.append(Character.toLowerCase(c));
                    else
                        sb.append(c);
                    capitalNextLetter = delimiter.matcher(c+"").matches();
                }
                input = sb.toString();
            }
            break;
            default:
                input = "Invalid Case!";
        }
        e.send(input).queue();
    }
}
