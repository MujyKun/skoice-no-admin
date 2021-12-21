// Copyright 2020, 2021 Clément "carlodrift" Raynaud, Lucas "Lucas_Cdry" Cadiry and contributors
// Copyright 2016, 2017, 2018, 2019, 2020, 2021 Austin "Scarsz" Shapiro

// This file is part of Skoice.

// Skoice is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// Skoice is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with Skoice.  If not, see <https://www.gnu.org/licenses/>.


package net.clementraynaud.util;

import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.*;

import static net.clementraynaud.Bot.getJda;
import static net.clementraynaud.Skoice.getPlugin;

public class DataGetters {

    public static String getKeyFromValue(Map<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static UUID getMinecraftID(Member member) {
        String minecraftID = getPlugin().getPlayerData().getString("Data." + member.getId());
        return minecraftID != null ? UUID.fromString(minecraftID) : null;
    }

    public static Guild getGuild() {
        VoiceChannel lobby = getLobby();
        if (lobby == null) return null;
        return lobby.getGuild();
    }

    public static VoiceChannel getLobby() {
        if (getJda() == null) return null;
        String lobbyID = getPlugin().getPlayerData().getString("lobbyID");
        if (StringUtils.isBlank(lobbyID)) return null;
        VoiceChannel lobby = getJda().getVoiceChannelById(lobbyID);
        if (lobby == null) return  null;
        if (lobby.getParent() == null) return null;
        return lobby;
    }

    public static Category getDedicatedCategory() {
        if (getJda() == null) return null;
        VoiceChannel lobby = getLobby();
        if (lobby == null) return null;
        return lobby.getParent();
    }

    public static Member getMember(UUID player) {
        String discordID = getPlugin().getPlayerData().getString("Data." + player);
        Guild guild = getGuild();
        if (guild == null) return null;
        return discordID != null ? guild.getMemberById(discordID) : null;
    }

    /**
     * Method return type-safe version of Bukkit::getOnlinePlayers
     *
     * @return {@code ArrayList} containing online players
     */
    public static List<Player> getOnlinePlayers() {
        List<Player> onlinePlayers = new ArrayList<>();
        try {
            Method onlinePlayerMethod = Server.class.getMethod("getOnlinePlayers");
            if (onlinePlayerMethod.getReturnType().equals(Collection.class)) {
                for (Object o : ((Collection<?>) onlinePlayerMethod.invoke(Bukkit.getServer()))) {
                    onlinePlayers.add((Player) o);
                }
            } else {
                Collections.addAll(onlinePlayers, ((Player[]) onlinePlayerMethod.invoke(Bukkit.getServer())));
            }
        } catch (Exception e) {
//            error(e);
        }
        return onlinePlayers;
    }

    public static double getVerticalStrength() {
        return getPlugin().getPlayerData().getInt("distance.verticalStrength");
    }

    public static double getHorizontalStrength() {
        return getPlugin().getPlayerData().getInt("distance.horizontalStrength");
    }

    public static double getFalloff() {
        return getPlugin().getPlayerData().getDouble("distance.falloff") / 2;
    }
}