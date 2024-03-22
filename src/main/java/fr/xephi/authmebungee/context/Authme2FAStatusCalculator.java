package fr.xephi.authmebungee.context;

import fr.xephi.authmebungee.AuthMeBungee;
import fr.xephi.authmebungee.data.AuthPlayer;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class Authme2FAStatusCalculator implements ContextCalculator<ProxiedPlayer> {

  private static final String CONTEXT_NAME = "totp";
  private static final String CONTEXT_Y = "enabled";
  private static final String CONTEXT_N = "disable";

  @Override public @NonNull ContextSet estimatePotentialContexts() {
    return ImmutableContextSet.builder()
      .add(CONTEXT_NAME, CONTEXT_Y)
      .add(CONTEXT_NAME, CONTEXT_N)
      .build();
  }

  @Override public void calculate(@NonNull ProxiedPlayer player, @NonNull ContextConsumer consumer) {
    String name = player.getName();
    AuthPlayer authPlayer = AuthMeBungee.INSTANCE.authPlayerManager.getAuthPlayer(name);
    String message = authPlayer != null && authPlayer.isTotpEnabled() ? CONTEXT_Y : CONTEXT_N;
    consumer.accept(CONTEXT_NAME, message);
  }

}
