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
package net.vplaygames.VPlayGames.commands.botStaff;

import net.vplaygames.VPlayGames.commands.BotStaffCommand;
import net.vplaygames.VPlayGames.commands.CommandReceivedEvent;
import net.vplaygames.VPlayGames.core.Bot;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Arrays;
import java.util.List;

public class EvalCommand extends BotStaffCommand {
    public static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");

    public EvalCommand() {
        super("eval");
    }

    @Override
    public void onCommandRun(CommandReceivedEvent e) throws ScriptException {
        if (e.getAuthor().getIdLong()!= Bot.BOT_OWNER) {
            e.send("Access Denied. Sorry, not sorry.").queue();
            return;
        }
        List<String> lines = Arrays.asList(e.getContent().split("\n"));
        String script = String.join("\n", lines.subList(2,lines.size()-1));
        long startTime = System.currentTimeMillis();
        Bindings bindings = engine.createBindings();
        bindings.put("e", e);
        bindings.put("event", e);
        bindings.put("guild", e.getGuild());
        bindings.put("channel", e.getChannel());
        bindings.put("message", e.getMessage());
        bindings.put("author", e.getAuthor());
        bindings.put("jda", e.getJDA());
        bindings.put("script", script);
        bindings.put("startTime", startTime);
        Object result = engine.eval(script, bindings);
        e.send(result == null?"Successfully Executed in "+(System.currentTimeMillis()-startTime)+" ms with no result":result.toString()).queue();
    }
}
