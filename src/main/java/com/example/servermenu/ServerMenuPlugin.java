package com.example.servermenu;

import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.key.Key;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerMenuPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onClick(PlayerCustomClickEvent event) {
        Key id = event.getIdentifier();
        if (!id.namespace().equals(ServerMenuBootstrap.NAMESPACE) || !id.value().startsWith("open/")) return;
        if (!(event.getCommonConnection() instanceof PlayerGameConnection conn)) return;

        String target = id.value().substring("open/".length());
        Dialog dialog = RegistryAccess.registryAccess()
                .getRegistry(RegistryKey.DIALOG)
                .get(Key.key(ServerMenuBootstrap.NAMESPACE, target));
        if (dialog != null) conn.getPlayer().showDialog(dialog);
    }
}
