package com.cristian.design.patterns;

abstract class GameObject extends Rectangle {

  private boolean damaged;

  private boolean onFire;

  protected GameObject(final int left, final int top, final int right, final int bottom) {
    super(left, top, right, bottom);
  }

  @Override
  public String toString() {
    return String.format("{} at {} damaged=%b onFire=%b", this.getClass().getSimpleName(),
        super.toString(), isDamaged(), isOnFire());
  }

  public boolean isOnFire() {
    return onFire;
  }

  public void setOnFire(boolean onFire) {
    this.onFire = onFire;
  }

  public boolean isDamaged() {
    return damaged;
  }

  public void setDamaged(boolean damaged) {
    this.damaged = damaged;
  }

  public abstract void collision(GameObject gameObject);

  public abstract void collisionResolve(FlamingAsteroid asteroid);

  public abstract void collisionResolve(Meteoroid meteoroid);

  public abstract void collisionResolve(SpaceStationMir mir);

  public abstract void collisionResolve(SpaceStationIss iss);
}