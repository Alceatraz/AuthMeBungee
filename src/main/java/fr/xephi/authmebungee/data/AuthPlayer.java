package fr.xephi.authmebungee.data;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class AuthPlayer {

  private final String name;

  private boolean isLogged;
  private boolean totpEnabled;

  public AuthPlayer(String name, boolean isLogged) {
    this.name = name.toLowerCase();
    this.isLogged = isLogged;
  }

  public AuthPlayer(String name) {
    this(name, false);
  }

  public String getName() {
    return name;
  }

  public boolean isLogged() {
    return isLogged;
  }

  public void setLogged(boolean isLogged) {
    this.isLogged = isLogged;
  }

  public boolean isTotpEnabled() {
    return totpEnabled;
  }

  public void setTotpEnabled(boolean totpEnabled) {
    this.totpEnabled = totpEnabled;
  }

  public boolean isOnline() {
    return getPlayer() != null;
  }

  public ProxiedPlayer getPlayer() {
    for (ProxiedPlayer current : ProxyServer.getInstance().getPlayers()) {
      if (current.getName().equalsIgnoreCase(name)) {
        return current;
      }
    }
    return null;
  }

}
