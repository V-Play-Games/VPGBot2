/*
 * Copyright 2020 Vaibhav Nargwani
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
package net.vplaygames.VPlayGames.core;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static net.vplaygames.VPlayGames.data.Bot.INVALID_INPUTS;

public abstract class Command implements ICommand {
    protected final int minArgs;
    protected final int maxArgs;
    protected final long cooldown;
    protected final long cooldownInMicro;
    protected final String commandName;
    protected final TimeUnit cooldownUnit;
    protected final HashMap<Long, Ratelimit> ratelimited;

    protected Command(String commandName, String... aliases) {
        this(commandName, 0, null, aliases);
    }

    protected Command(String commandName, long cooldown, TimeUnit cooldownUnit, String... aliases) {
        this(commandName, cooldown, cooldownUnit, 0, -1, aliases);
    }

    protected Command(String commandName, long cooldown, TimeUnit cooldownUnit, int minArgs, int maxArgs, String... aliases) {
        this.commandName = commandName;
        this.cooldown = cooldown;
        this.cooldownUnit = cooldownUnit == null ? TimeUnit.MICROSECONDS : cooldownUnit;
        this.cooldownInMicro = this.cooldownUnit.toMicros(cooldown);
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.ratelimited = new HashMap<>();
        Bot.commands.put(commandName, this);
        for (String alias : aliases) {
            Bot.commands.put(alias, this);
        }
    }

    @Override
    public boolean run(GuildMessageReceivedEvent e) {
        long aid = e.getAuthor().getIdLong();
        Ratelimit rl = ratelimited.get(aid);
        if (rl != null && System.currentTimeMillis() <= cooldownInMicro + rl.inflictedAt) {
            onRatelimit(e);
            return false;
        }
        int args = e.getMessage().getContentRaw().split(" ").length - 1;
        if (!(minArgs <= args && args <= (maxArgs < 0 ? args : maxArgs))) {
            onInsufficientArgs(e);
            return false;
        }
        onCommandRun(e);
        ratelimited.put(aid, new Ratelimit(aid));
        return true;
    }

    @Override
    public void onRatelimit(GuildMessageReceivedEvent e) {
        long aid = e.getAuthor().getIdLong();
        Ratelimit rl = ratelimited.get(aid);
        long t = calculateCooldownLeft(rl.inflictedAt);
        String s = "\\RateLimited\\";
        if (!rl.informed) {
            s = e.getAuthor().getAsMention() + ", you have to wait for **" + MiscUtil.msToString(t) + "** before using this command again.";
            e.getChannel().sendMessage(s).queue(m ->
                m.delete().queueAfter(calculateCooldownLeft(rl.inflictedAt),
                    TimeUnit.MILLISECONDS,
                    (i) -> ratelimited.remove(aid),
                    (i) -> ratelimited.remove(aid))
            );
            rl.informed = true;
        }
        Response.get(Bot.messagesProcessed).responded(s);
    }

    @Override
    public void onInsufficientArgs(GuildMessageReceivedEvent e) {
        MiscUtil.send(e, INVALID_INPUTS, true);
    }

    private long calculateCooldownLeft(long inflictedAt) {
        return cooldownInMicro + inflictedAt - System.currentTimeMillis();
    }
}
