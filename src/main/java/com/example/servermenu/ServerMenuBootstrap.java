package com.example.servermenu;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.event.RegistryEvents;
import io.papermc.paper.registry.keys.DialogKeys;
import io.papermc.paper.registry.keys.tags.DialogTagKeys;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.object.ObjectContents;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class ServerMenuBootstrap implements PluginBootstrap {

    public static final String NAMESPACE = "servermenu";
    private static final MiniMessage MM = MiniMessage.miniMessage();

    @Override
    public void bootstrap(BootstrapContext context) {
        Path folder = context.getDataDirectory();          // plugins/ServerMenu
        Path dialogsFolder = folder.resolve("dialogs");

        try {
            Files.createDirectories(dialogsFolder);
            copyDefault("menu.yml", folder.resolve("menu.yml"));
        } catch (Exception e) {
            context.getLogger().error("Could not create ServerMenu files", e);
            return;
        }

        // dialog id -> file
        Map<String, Path> files = new LinkedHashMap<>();
        files.put("main", folder.resolve("menu.yml"));
        try (Stream<Path> s = Files.list(dialogsFolder)) {
            s.filter(p -> p.getFileName().toString().endsWith(".yml")).forEach(p -> {
                String name = p.getFileName().toString().replace(".yml", "").toLowerCase(Locale.ROOT);
                if (!name.equals("main")) files.put(name, p);
            });
        } catch (Exception e) {
            context.getLogger().error("Could not read dialogs folder", e);
        }

        // Load all files now; skip dialogs that have no buttons (a dialog needs at least one)
        Set<String> registered = new LinkedHashSet<>();
        Map<String, Loaded> loaded = new LinkedHashMap<>();
        files.forEach((id, path) -> {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(path.toFile());
            List<ActionButton> buttons = new ArrayList<>();
            for (Map<?, ?> b : cfg.getMapList("buttons")) {
                Component label = MM.deserialize(String.valueOf(b.get("label")));
                if (b.get("icon") != null) {
                    label = icon(String.valueOf(b.get("icon"))).append(Component.space()).append(label);
                }
                ActionButton.Builder btn = ActionButton.builder(label);
                if (b.get("open") != null) {
                    btn.action(DialogAction.staticAction(
                        ClickEvent.custom(Key.key(NAMESPACE, "open/" + b.get("open")))));
                } else if (b.get("command") != null) {
                    btn.action(DialogAction.staticAction(
                        ClickEvent.runCommand(String.valueOf(b.get("command")))));
                }
                buttons.add(btn.build());
            }
            if (buttons.isEmpty()) {
                context.getLogger().info("Menu '{}' has no buttons yet, skipping.", id);
                return;
            }
            loaded.put(id, new Loaded(cfg.getString("title", "Menu"), cfg.getInt("columns", 2), buttons));
            registered.add(id);
        });

        // 1. Register every dialog that has buttons
        context.getLifecycleManager().registerEventHandler(
            RegistryEvents.DIALOG.compose().newHandler(event ->
                loaded.forEach((id, d) ->
                    event.registry().register(DialogKeys.create(Key.key(NAMESPACE, id)), builder -> builder
                        .base(DialogBase.builder(MM.deserialize(d.title())).build())
                        .type(DialogType.multiAction(d.buttons()).columns(d.columns()).build())
                    )
                )
            )
        );

        // 2. Put menu.yml ("main") on the ESC screen, only if it has buttons
        if (registered.contains("main")) {
            context.getLifecycleManager().registerEventHandler(
                LifecycleEvents.TAGS.postFlatten(RegistryKey.DIALOG), event ->
                    event.registrar().addToTag(
                        DialogTagKeys.PAUSE_SCREEN_ADDITIONS,
                        Set.of(DialogKeys.create(Key.key(NAMESPACE, "main")))
                    )
            );
        }
    }

    private record Loaded(String title, int columns, List<ActionButton> buttons) {}

    /** Builds an icon component from the "icon:" value. */
    private static Component icon(String value) {
        if (value.startsWith("head:")) {
            return Component.object(ObjectContents.playerHead(value.substring(5)));
        }
        if (value.startsWith("sprite:")) {
            String sprite = value.substring(7);
            String atlas = sprite.startsWith("block/") ? "minecraft:blocks" : "minecraft:items";
            return Component.object(ObjectContents.sprite(Key.key(atlas), Key.key(sprite)));
        }
        if (value.startsWith("text:")) {
            return MM.deserialize(value.substring(5));
        }
        return MM.deserialize(value);
    }

    private void copyDefault(String resource, Path target) throws Exception {
        if (Files.exists(target)) return;
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resource)) {
            if (in != null) Files.copy(in, target);
        }
    }
}
