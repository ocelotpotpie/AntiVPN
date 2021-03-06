package me.egg82.antivpn.api.model.player;

import com.google.common.collect.ImmutableList;
import com.velocitypowered.api.proxy.ProxyServer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import me.egg82.antivpn.api.APIException;
import me.egg82.antivpn.config.CachedConfig;
import me.egg82.antivpn.config.ConfigUtil;
import me.egg82.antivpn.messaging.packets.PlayerPacket;
import me.egg82.antivpn.services.lookup.PlayerLookup;
import me.egg82.antivpn.storage.StorageService;
import me.egg82.antivpn.storage.models.PlayerModel;
import me.egg82.antivpn.utils.PacketUtil;
import me.egg82.antivpn.utils.VelocityTailorUtil;
import me.gong.mcleaks.MCLeaksAPI;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class VelocityPlayerManager extends AbstractPlayerManager {
    private final ProxyServer proxy;
    private final MCLeaksAPI api;

    public VelocityPlayerManager(@NonNull ProxyServer proxy, int webThreads, String mcleaksKey, long cacheTime, TimeUnit cacheTimeUnit) {
        super(cacheTime, cacheTimeUnit);

        this.proxy = proxy;
        api = MCLeaksAPI.builder()
            .nocache()
            .threadCount(webThreads)
            .userAgent("egg82/AntiVPN")
            .apiKey(mcleaksKey)
            .build();
    }

    public void cancel() {
        api.shutdown();
    }

    public @NonNull CompletableFuture<Player> getPlayer(@NonNull UUID uniqueId) {
        return CompletableFuture.supplyAsync(() -> {
            CachedConfig cachedConfig = ConfigUtil.getCachedConfig();
            if (cachedConfig == null) {
                throw new APIException(false, "Cached config could not be fetched.");
            }

            for (StorageService service : cachedConfig.getStorage()) {
                PlayerModel model = service.getPlayerModel(uniqueId, cachedConfig.getSourceCacheTime());
                if (model != null) {
                    return new VelocityPlayer(proxy, uniqueId, model.isMcleaks());
                }
            }
            return null;
        });
    }

    public @NonNull CompletableFuture<Player> getPlayer(@NonNull String username) {
        return PlayerLookup.get(username, proxy).thenApply(info -> {
            CachedConfig cachedConfig = ConfigUtil.getCachedConfig();
            if (cachedConfig == null) {
                throw new APIException(false, "Cached config could not be fetched.");
            }

            for (StorageService service : cachedConfig.getStorage()) {
                PlayerModel model = service.getPlayerModel(info.getUUID(), cachedConfig.getSourceCacheTime());
                if (model != null) {
                    return new VelocityPlayer(proxy, info.getUUID(), model.isMcleaks());
                }
            }
            return null;
        });
    }

    public boolean kickForMcLeaks(@NonNull String playerName, @NonNull UUID playerUuid, @NonNull String ip) {
        CachedConfig cachedConfig = ConfigUtil.getCachedConfig();
        if (cachedConfig == null) {
            logger.error("Cached config could not be fetched.");
            return false;
        }

        Optional<com.velocitypowered.api.proxy.Player> p = proxy.getPlayer(playerUuid);
        if (!p.isPresent()) {
            return false;
        }

        List<String> commands = VelocityTailorUtil.tailorCommands(cachedConfig.getMCLeaksActionCommands(), playerName, playerUuid, ip);
        for (String command : commands) {
            proxy.getCommandManager().executeImmediatelyAsync(proxy.getConsoleCommandSource(), command);
        }
        if (!cachedConfig.getMCLeaksKickMessage().isEmpty()) {
            p.get().disconnect(LegacyComponentSerializer.legacyAmpersand().deserialize(VelocityTailorUtil.tailorKickMessage(cachedConfig.getMCLeaksKickMessage(), playerName, playerUuid, ip)));
        }
        return true;
    }

    public @Nullable String getMcLeaksKickMessage(@NonNull String playerName, @NonNull UUID playerUuid, @NonNull String ip) {
        CachedConfig cachedConfig = ConfigUtil.getCachedConfig();
        if (cachedConfig == null) {
            logger.error("Cached config could not be fetched.");
            return null;
        }

        if (!cachedConfig.getMCLeaksKickMessage().isEmpty()) {
            return VelocityTailorUtil.tailorKickMessage(cachedConfig.getMCLeaksKickMessage(), playerName, playerUuid, ip);
        }
        return null;
    }

    public @NonNull List<String> getMcLeaksCommands(@NonNull String playerName, @NonNull UUID playerUuid, @NonNull String ip) {
        CachedConfig cachedConfig = ConfigUtil.getCachedConfig();
        if (cachedConfig == null) {
            logger.error("Cached config could not be fetched.");
            return ImmutableList.of();
        }

        if (!cachedConfig.getMCLeaksActionCommands().isEmpty()) {
            return ImmutableList.copyOf(VelocityTailorUtil.tailorCommands(cachedConfig.getMCLeaksActionCommands(), playerName, playerUuid, ip));
        }
        return ImmutableList.of();
    }

    protected @NonNull PlayerModel calculatePlayerResult(@NonNull UUID uuid, boolean useCache) throws APIException {
        CachedConfig cachedConfig = ConfigUtil.getCachedConfig();
        if (cachedConfig == null) {
            throw new APIException(false, "Cached config could not be fetched.");
        }

        if (useCache) {
            for (StorageService service : cachedConfig.getStorage()) {
                PlayerModel model = service.getPlayerModel(uuid, cachedConfig.getSourceCacheTime());
                if (model != null) {
                    if (cachedConfig.getDebug()) {
                        logger.info("Found database value for player " + uuid + ".");
                    }
                    return model;
                }
            }
        }

        if (cachedConfig.getDebug()) {
            logger.info("Getting web result for player " + uuid + ".");
        }

        PlayerModel retVal = new PlayerModel();
        retVal.setUuid(uuid);

        MCLeaksAPI.Result result = api.checkAccount(uuid);
        if (result.hasError()) {
            throw new APIException(result.getError().getMessage().contains("key"), result.getError());
        }

        retVal.setMcleaks(result.isMCLeaks());

        if (useCache) {
            storeResult(retVal, cachedConfig);
            sendResult(retVal, cachedConfig);
        }
        return retVal;
    }

    private void storeResult(@NonNull PlayerModel model, @NonNull CachedConfig cachedConfig) {
        for (StorageService service : cachedConfig.getStorage()) {
            PlayerModel m = service.getOrCreatePlayerModel(model.getUuid(), model.isMcleaks());
            service.storeModel(m);
        }

        if (cachedConfig.getDebug()) {
            logger.info("Stored data for " + model.getUuid() + " in storage.");
        }
    }

    private void sendResult(@NonNull PlayerModel model, @NonNull CachedConfig cachedConfig) {
        PlayerPacket packet = new PlayerPacket();
        packet.setUuid(model.getUuid());
        packet.setValue(model.isMcleaks());
        PacketUtil.queuePacket(packet);

        if (cachedConfig.getDebug()) {
            logger.info("Queued packet for " + model.getUuid() + " in messaging.");
        }
    }
}
