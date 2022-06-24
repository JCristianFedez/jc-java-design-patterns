package state.states;

import state.ui.Player;

/**
 * Interfaz común para todos los estados.
 */
public abstract class State {

  protected Player player;

  State(Player player) {
    this.player = player;
  }

  public abstract String onLock();

  public abstract String onPlay();

  public abstract String onNext();

  public abstract String onPrevious();
}
