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
    public void onAccessDenied(CommandReceivedEvent e) {
        e.send("You do not have the permission to do that!").queue();
    }

    @Override
    public boolean hasAccess(long aid) {
        return Bot.isStaff(aid);
    }
}
