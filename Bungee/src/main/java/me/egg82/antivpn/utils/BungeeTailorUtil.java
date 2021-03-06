package me.egg82.antivpn.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BungeeTailorUtil {
    private static final Logger logger = LoggerFactory.getLogger(BungeeTailorUtil.class);

    private BungeeTailorUtil() { }

    public static @NonNull List<String> tailorCommands(@NonNull List<String> commands, @NonNull String name, @NonNull UUID uuid, @NonNull String ip) {
        List<String> retVal = new ArrayList<>();

        for (String command : commands) {
            command = command.replace("%player%", name).replace("%uuid%", uuid.toString()).replace("%ip%", ip);
            if (command.charAt(0) == '/') {
                command = command.substring(1);
            }

            retVal.add(command);
        }

        return retVal;
    }

    public static @NonNull String tailorKickMessage(@NonNull String message, @NonNull String name, @NonNull UUID uuid, @NonNull String ip) {
        message = message.replace("%player%", name).replace("%uuid%", uuid.toString()).replace("%ip%", ip);
        return ChatColor.translateAlternateColorCodes('&', message.replace("\\r", "").replace("\r", "").replace("\\n", "\n"));
    }
}
