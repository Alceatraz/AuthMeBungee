package fr.xephi.authmebungee;

import ch.jalu.configme.SettingsManager;
import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import fr.xephi.authmebungee.annotations.DataFolder;
import fr.xephi.authmebungee.commands.BungeeReloadCommand;
import fr.xephi.authmebungee.config.BungeeConfigProperties;
import fr.xephi.authmebungee.config.BungeeSettingsProvider;
import fr.xephi.authmebungee.context.Authme2FAStatusCalculator;
import fr.xephi.authmebungee.listeners.BungeeMessageListener;
import fr.xephi.authmebungee.listeners.BungeePlayerListener;
import fr.xephi.authmebungee.services.AuthPlayerManager;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.logging.Logger;

public class AuthMeBungee extends Plugin {

  private static Authme2FAStatusCalculator instance;

  public static AuthMeBungee INSTANCE;

  public AuthPlayerManager authPlayerManager;

  public AuthMeBungee() {
    if (INSTANCE == null) {
      INSTANCE = this;
    }
  }

  @Override public void onEnable() {

    // Prepare the injector and register stuff
    // Instances
    Injector injector = new InjectorBuilder().addDefaultHandlers("").create();

    injector.register(Logger.class, getLogger());
    injector.register(AuthMeBungee.class, this);
    injector.register(ProxyServer.class, getProxy());
    injector.register(PluginManager.class, getProxy().getPluginManager());
    injector.register(TaskScheduler.class, getProxy().getScheduler());

    injector.provide(DataFolder.class, getDataFolder());

    injector.registerProvider(SettingsManager.class, BungeeSettingsProvider.class);

    // Get singletons from the injector
    SettingsManager settings = injector.getSingleton(SettingsManager.class);

    authPlayerManager = injector.getSingleton(AuthPlayerManager.class);

    // Print some config information
    getLogger().info("Current auth servers:");
    for (String authServer : settings.getProperty(BungeeConfigProperties.AUTH_SERVERS)) {
      getLogger().info("> " + authServer.toLowerCase());
    }

    // Add online players (plugin hotswap, just in case)
    for (ProxiedPlayer player : getProxy().getPlayers()) {
      authPlayerManager.addAuthPlayer(player);
    }

    // Register commands
    getProxy().getPluginManager().registerCommand(this, injector.getSingleton(BungeeReloadCommand.class));

    // Registering event listeners
    getProxy().getPluginManager().registerListener(this, injector.getSingleton(BungeeMessageListener.class));
    getProxy().getPluginManager().registerListener(this, injector.getSingleton(BungeePlayerListener.class));

    // Send metrics data
    // new Metrics(this, 1880);

    if (instance == null) {
      instance = new Authme2FAStatusCalculator();
    }

    ContextManager contextManager = LuckPermsProvider.get().getContextManager();
    contextManager.registerCalculator(instance);

  }

}
