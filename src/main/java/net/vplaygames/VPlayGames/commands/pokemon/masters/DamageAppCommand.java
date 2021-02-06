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
package net.vplaygames.VPlayGames.commands.pokemon.masters;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.vplaygames.VPlayGames.core.Command;
import net.vplaygames.VPlayGames.core.Damage.Status;
import net.vplaygames.VPlayGames.data.Bot;
import net.vplaygames.VPlayGames.util.MiscUtil;

import java.util.concurrent.TimeUnit;

public abstract class DamageAppCommand extends Command {
    protected Status minAppStatus;

    protected DamageAppCommand(String commandName, String... aliases) {
        this(commandName, Status.STARTED, 0, null, aliases);
    }

    protected DamageAppCommand(String commandName, long cooldown, TimeUnit cooldownUnit, String... aliases) {
        this(commandName, Status.STARTED, cooldown, cooldownUnit, 0, 0, aliases);
    }

    protected DamageAppCommand(String commandName, int args, String... aliases) {
        this(commandName, args, args, aliases);
    }

    protected DamageAppCommand(String commandName, int minArgs, int maxArgs, String... aliases) {
        this(commandName, Status.STARTED, 0, null, minArgs, maxArgs, aliases);
    }

    protected DamageAppCommand(String commandName, Status minAppStatus, String... aliases) {
        this(commandName, minAppStatus, 0, aliases);
    }

    protected DamageAppCommand(String commandName, Status minAppStatus, long cooldown, TimeUnit cooldownUnit, String... aliases) {
        this(commandName, minAppStatus, cooldown, cooldownUnit, 0, 0, aliases);
    }

    protected DamageAppCommand(String commandName, Status minAppStatus, int args, String... aliases) {
        this(commandName, minAppStatus, args, args, aliases);
    }

    protected DamageAppCommand(String commandName, Status minAppStatus, int minArgs, int maxArgs, String... aliases) {
        this(commandName, minAppStatus, 0, null, minArgs, maxArgs, aliases);
    }

    protected DamageAppCommand(String commandName, Status minAppStatus, long cooldown, TimeUnit cooldownUnit, int minArgs, int maxArgs, String... aliases) {
        super(commandName, cooldown, cooldownUnit, minArgs, maxArgs, aliases);
        this.minAppStatus = minAppStatus;
    }

    @Override
    public void onAccessDenied(GuildMessageReceivedEvent e) {
        if (!Bot.DATA.containsKey(e.getAuthor().getIdLong())) {
            MiscUtil.send(e, Bot.APP_NOT_STARTED, true);
        } else {
            MiscUtil.send(e, "There are some requirements to use this command, Please check the help for this command for more info", true);
        }
    }

    @Override
    public boolean hasAccess(long aid) {
        return Bot.DATA.containsKey(aid) && Bot.DATA.get(aid).getAppStatus() >= minAppStatus.ordinal();
    }
}
