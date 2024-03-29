package com.cristian.design.patterns.strategies;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LambdaStrategy {

  public enum Strategy implements DragonSlayingStrategy {
    MeleeStrategy(() -> LOGGER.info("With your Excalibur you severe the dragon's head")),
    ProjectileStrategy(() -> LOGGER.info("You shoot the dragon with the magical crossbow and it falls dead on the ground!")),
    SpellStrategy(() -> LOGGER.info("You cast the spell of disintegration and the dragon vaporizes in a pile of dust!"));

    private final DragonSlayingStrategy slayingStrategy;

    Strategy(final DragonSlayingStrategy slayingStrategy) {
      this.slayingStrategy = slayingStrategy;
    }

    @Override
    public void execute() {
      this.slayingStrategy.execute();
    }
  }
}
