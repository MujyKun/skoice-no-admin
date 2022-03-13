/*
 * Copyright 2020, 2021, 2022 Clément "carlodrift" Raynaud, Lucas "Lucas_Cdry" Cadiry and contributors
 *
 * This file is part of Skoice.
 *
 * Skoice is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Skoice is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Skoice.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.clementraynaud.skoice.commands.arguments;

import net.clementraynaud.skoice.lang.MinecraftLang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public abstract class Argument {

    public enum Option {
        CONFIGURE {
            @Override
            public void run(CommandSender sender, String arg) {
                new ConfigureArgument(sender).run();
            }},

        TOKEN {
            @Override
            public void run(CommandSender sender, String arg) {
                new TokenArgument(sender, arg).run();
            }},

        LINK {
            @Override
            public void run(CommandSender sender, String arg) {
                new LinkArgument(sender, arg).run();
            }},

        UNLINK {
            @Override
            public void run(CommandSender sender, String arg) {
                new UnlinkArgument(sender).run();
            }};

        public abstract void run(CommandSender sender, String arg);

        public static Option getOption(String option) {
            return Stream.of(Option.values()).filter(value -> value.toString().equalsIgnoreCase(option)).findFirst().orElse(null);
        }

        public static String[] getList() {
            return Stream.of(Option.values()).map(Enum::name).map(String::toLowerCase).toArray(String[]::new);
        }
    }

    protected final CommandSender sender;
    protected final String arg;
    protected final boolean allowsConsole;
    protected final boolean restrictedToOperators;

    protected Argument(CommandSender sender, String arg, boolean allowsConsole, boolean restrictedToOperators) {
        this.sender = sender;
        this.arg = arg;
        this.allowsConsole = allowsConsole;
        this.restrictedToOperators = restrictedToOperators;
    }

    protected abstract void run();

    protected boolean canExecuteCommand() {
        if (!(sender instanceof Player) && !this.allowsConsole) {
            sender.sendMessage(MinecraftLang.ILLEGAL_EXECUTOR.toString());
            return false;
        }
        if (!sender.isOp() && this.restrictedToOperators) {
            sender.sendMessage(MinecraftLang.MISSING_PERMISSION.toString());
            return false;
        }
        return true;
    }
}
