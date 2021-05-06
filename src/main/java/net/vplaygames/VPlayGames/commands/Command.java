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
package net.vplaygames.VPlayGames.commands;

import net.vplaygames.VPlayGames.core.Bot;
import net.vplaygames.VPlayGames.core.Ratelimit;
import net.vplaygames.VPlayGames.util.MiscUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static net.vplaygames.VPlayGames.core.Bot.INVALID_INPUTS;

public abstract class Command implements ICommand {
    protected final int minArgs;
    protected final int maxArgs;
    protected final long cooldown;
    protected final long cooldownInMicro;
    protected final String name;
    protected final TimeUnit cooldownUnit;
    public final HashMap<Long, Ratelimit> ratelimited;

    protected Command(String name, String... aliases) {
        this(name, 0, null, aliases);
    }

    protected Command(String name, long cooldown, TimeUnit cooldownUnit, String... aliases) {
        this(name, cooldown, cooldownUnit, 0, -1, aliases);
    }

    protected Command(String name, int args, String... aliases) {
        this(name, args, args, aliases);
    }

    protected Command(String name, int minArgs, int maxArgs, String... aliases) {
        this(name, 0, null, minArgs, maxArgs, aliases);
    }

    protected Command(String name, long cooldown, TimeUnit cooldownUnit, int minArgs, int maxArgs, String... aliases) {
        this.name = name;
        this.cooldown = cooldown;
        this.cooldownUnit = cooldownUnit == null ? TimeUnit.MILLISECONDS : cooldownUnit;
        this.cooldownInMicro = this.cooldownUnit.toMillis(cooldown);
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        this.ratelimited = new HashMap<>();
        Bot.commands.put(name, this);
        for (String alias : aliases) {
            Bot.commands.put(alias, this);
        }
    }

    @Override
    public void run(CommandReceivedEvent e) {
        long aid = e.getAuthor().getIdLong();
        Ratelimit rl = ratelimited.get(aid);
        if (rl != null && System.currentTimeMillis() <= cooldownInMicro + rl.inflictedAt) {
            onRatelimit(e);
        } else {
            if (!hasAccess(e.getAuthor().getIdLong())) {
                onAccessDenied(e);
            } else {
                int args = e.args.size() - 1;
                if (minArgs > args || args > (maxArgs <= 0 ? args : maxArgs)) {
                    onInsufficientArgs(e);
                } else {
                    e.getChannel().sendTyping().queue(Void -> {
                        try {
                            onCommandRun(e);
                            ratelimited.put(aid, new Ratelimit(aid));
                        } catch (Exception exc) {
                            e.send("There was some trouble processing your request. Please try Again Later.").queue();
                            e.reportTrouble(exc);
                        } finally {
                            e.log();
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onRatelimit(CommandReceivedEvent e) {
        Ratelimit rl = ratelimited.get(e.getAuthor().getIdLong());
        long t = calculateCooldownLeft(rl.inflictedAt);
        if (!rl.informed) {
            e.send("You have to wait for **" + MiscUtil.msToString(t) + "** before using this command again.")
                .queue(m -> m.delete().queueAfter(calculateCooldownLeft(rl.inflictedAt),
                    TimeUnit.MILLISECONDS,
                    (i) -> ratelimited.remove(rl.inflictedOn),
                    (i) -> ratelimited.remove(rl.inflictedOn))
                );
            rl.informed = true;
        }
    }

    @Override
    public void onInsufficientArgs(CommandReceivedEvent e) {
        e.send(INVALID_INPUTS).queue();
    }

    private long calculateCooldownLeft(long inflictedAt) {
        return cooldownInMicro + inflictedAt - System.currentTimeMillis();
    }

    @Override
    public void onAccessDenied(CommandReceivedEvent e) {}

    @Override
    public void onHelpNeeded(CommandReceivedEvent e) {}

    @Override
    public boolean hasAccess(long aid) {
        return true;
    }

    @Override
    public String toString() {
        return Bot.PREFIX + name;
    }

    @Override
    public HashMap<Long, Ratelimit> getRateLimited() {
        return ratelimited;
    }
}
