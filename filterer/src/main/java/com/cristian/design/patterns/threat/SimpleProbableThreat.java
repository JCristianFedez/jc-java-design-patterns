package com.cristian.design.patterns.threat;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class SimpleProbableThreat extends SimpleThreat implements ProbableThreat {

  private final double probability;

  public SimpleProbableThreat(final String name, final int id, final ThreatType threatType,
      final double probability) {
    super(threatType, id, name);
    this.probability = probability;
  }

  @Override
  public double probability() {
    return this.probability;
  }

  @Override
  public String toString() {
    return "SimpleProbableThreat{"
        + "probability=" + probability
        + "} "
        + super.toString();
  }
}
