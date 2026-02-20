package com.fomdev.translation.init;

import com.fomdev.sasm.api.PluginClassUtil;
import com.fomdev.translation.api.LangUtil;
import com.fomdev.translation.commmand.CommandLanguage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Translation extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        LangUtil.initLanguage();
        assert getCommand("language") != null;
        getCommand("language").setExecutor(new CommandLanguage.LanguageCommand());
        getCommand("language").setTabCompleter(new CommandLanguage.LanguageCompletion());
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onServerStart(ServerLoadEvent event) {
        try {
            PluginClassUtil.rescanCache();
            LangUtil.syncDictionaryFromPackages();
            LangUtil.syncDictionaryFromFolder();
        } catch (Exception ignored) {

        }
    }
}