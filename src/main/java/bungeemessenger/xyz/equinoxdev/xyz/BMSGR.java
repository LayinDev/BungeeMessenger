package bungeemessenger.xyz.equinoxdev.xyz;

import bungeemessenger.xyz.equinoxdev.xyz.commands.*;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public final class BMSGR extends Plugin {

    private static BMSGR instance;
    private static ConfigManager cm;
    private File file;
    private static Configuration configuration;
    private static HashMap<ProxiedPlayer, ProxiedPlayer> messages = new HashMap<ProxiedPlayer, ProxiedPlayer>();
    private static HashMap<ProxiedPlayer, Boolean> status = new HashMap<ProxiedPlayer, Boolean>();
    private static HashMap<ProxiedPlayer, List<ProxiedPlayer>> ignoredPlayers = new HashMap<>();

    @Override
    public void onEnable() {
        setInstance(this);
        cm = new ConfigManager();
        cm.init();
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&a&lHas Been Loaded!"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new msgCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new reloadCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new replyCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ignoreCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new helpCommand());
    }

    @Override
    public void onDisable() {

    }

    public static BMSGR getInstance(){
        return instance;
    }

    public static void setInstance(BMSGR instance){
        BMSGR.instance = instance;
    }

    public static ConfigManager getConfig(){
        return cm;
    }

    public static HashMap<ProxiedPlayer, ProxiedPlayer> getMessages(){
        return messages;
    }

    public static HashMap<ProxiedPlayer, Boolean> getStatus(){
        return status;
    }

    public static HashMap<ProxiedPlayer, List<ProxiedPlayer>> getIgnoredPlayers() { return ignoredPlayers; }

}
