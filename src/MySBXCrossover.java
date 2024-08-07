//package org.uma.jmetal.operator.crossover.impl;

import org.uma.jmetal.operator.crossover.CrossoverOperator;
//import org.uma.jmetal.solution.doublesolution.DoubleSolution;
//import org.uma.jmetal.solution.doublesolution.impl.DefaultDoubleSolution;
import org.uma.jmetal.solution.util.repairsolution.RepairDoubleSolution;
import org.uma.jmetal.solution.util.repairsolution.impl.RepairDoubleSolutionWithBoundValue;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.checking.Check;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * This class allows to apply a SBX crossover operator using two parent solutions (Double encoding).
 * A {@link RepairDoubleSolution} object is used to decide the strategy to apply when a value is out
 * of range.
 *
 * The implementation is based on the NSGA-II code available in
 * <a href="http://www.iitk.ac.in/kangal/codes.shtml">http://www.iitk.ac.in/kangal/codes.shtml</a>
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @author Juan J. Durillo
 */
@SuppressWarnings("serial")
public class MySBXCrossover implements CrossoverOperator<MyDefaultDoubleSolution> {
  /** EPS defines the minimum difference allowed between real values */
  private static final double EPS = 1.0e-14;

  private double distributionIndex ;
  private double crossoverProbability  ;
  private RepairDoubleSolution solutionRepair ;

  private RandomGenerator<Double> randomGenerator ;

  /** Constructor */
  public MySBXCrossover(double crossoverProbability, double distributionIndex) {
    this (crossoverProbability, distributionIndex, new RepairDoubleSolutionWithBoundValue()) ;
  }

  /** Constructor */
  public MySBXCrossover(double crossoverProbability, double distributionIndex, RandomGenerator<Double> randomGenerator) {
    this (crossoverProbability, distributionIndex, new RepairDoubleSolutionWithBoundValue(), randomGenerator) ;
  }

  /** Constructor */
  public MySBXCrossover(double crossoverProbability, double distributionIndex, RepairDoubleSolution solutionRepair) {
	  this(crossoverProbability, distributionIndex, solutionRepair, () -> JMetalRandom.getInstance().nextDouble());
  }

  /** Constructor */
  public MySBXCrossover(double crossoverProbability, double distributionIndex, RepairDoubleSolution solutionRepair, RandomGenerator<Double> randomGenerator) {
    Check.probabilityIsValid(crossoverProbability);
    Check.that(distributionIndex >= 0, "Distribution index is negative: " + distributionIndex);

    this.crossoverProbability = crossoverProbability ;
    this.distributionIndex = distributionIndex ;
    this.solutionRepair = solutionRepair ;

    this.randomGenerator = randomGenerator ;
  }

  /* Getters */
  @Override
  public double getCrossoverProbability() {
    return crossoverProbability;
  }

  public double getDistributionIndex() {
    return distributionIndex;
  }

  /* Setters */
  public void setCrossoverProbability(double probability) {
    this.crossoverProbability = probability ;
  }

  public void setDistributionIndex(double distributionIndex) {
    this.distributionIndex = distributionIndex ;
  }

  /** Execute() method */
  @Override
  public List<MyDefaultDoubleSolution> execute(List<MyDefaultDoubleSolution> solutions) {
    if (null == solutions) {
      throw new JMetalException("Null parameter") ;
    } else if (solutions.size() != 2) {
      throw new JMetalException("There must be two parents instead of " + solutions.size()) ;
    }

    return doCrossover(crossoverProbability, solutions.get(0), solutions.get(1)) ;
  }

  /** doCrossover method */
  public List<MyDefaultDoubleSolution> doCrossover(
      double probability, MyDefaultDoubleSolution parent1, MyDefaultDoubleSolution parent2) {
    List<MyDefaultDoubleSolution> offspring = new ArrayList<MyDefaultDoubleSolution>(2);

    offspring.add((MyDefaultDoubleSolution) parent1.copy()) ;
    offspring.add((MyDefaultDoubleSolution) parent2.copy()) ;

    int i;
    double rand;
    double y1, y2, lowerBound, upperBound;
    double c1, c2;
    double alpha, beta, betaq;
    double valueX1, valueX2;

    if (randomGenerator.getRandomValue() <= probability) {
      for (i = 0; i < parent1.getNumberOfVariables(); i++) {
        valueX1 = parent1.getVariable(i);
        valueX2 = parent2.getVariable(i);
        if (randomGenerator.getRandomValue() <= 0.5) {
          if (Math.abs(valueX1 - valueX2) > EPS) {
            if (valueX1 < valueX2) {
              y1 = valueX1;
              y2 = valueX2;
            } else {
              y1 = valueX2;
              y2 = valueX1;
            }

            lowerBound = parent1.getLowerBound(i);
            upperBound = parent1.getUpperBound(i);

            rand = randomGenerator.getRandomValue();
            beta = 1.0 + (2.0 * (y1 - lowerBound) / (y2 - y1));
            alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

            if (rand <= (1.0 / alpha)) {
              betaq = Math.pow(rand * alpha, (1.0 / (distributionIndex + 1.0)));
            } else {
              betaq = Math
                  .pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
            }
            c1 = 0.5 * (y1 + y2 - betaq * (y2 - y1));

            beta = 1.0 + (2.0 * (upperBound - y2) / (y2 - y1));
            alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

            if (rand <= (1.0 / alpha)) {
              betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
            } else {
              betaq = Math
                  .pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
            }
            c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));

            c1 = solutionRepair.repairSolutionVariableValue(c1, lowerBound, upperBound) ;
            c2 = solutionRepair.repairSolutionVariableValue(c2, lowerBound, upperBound) ;

            if (randomGenerator.getRandomValue() <= 0.5) {
              offspring.get(0).setVariable(i, c2);
              offspring.get(1).setVariable(i, c1);
            } else {
              offspring.get(0).setVariable(i, c1);
              offspring.get(1).setVariable(i, c2);
            }
          } else {
            offspring.get(0).setVariable(i, valueX1);
            offspring.get(1).setVariable(i, valueX2);
          }
        } else {
          offspring.get(0).setVariable(i, valueX2);
          offspring.get(1).setVariable(i, valueX1);
        }
      }
    }

    return offspring;
  }

  @Override
  public int getNumberOfRequiredParents() {
    return 2 ;
  }

  @Override
  public int getNumberOfGeneratedChildren() {
    return 2;
  }
}
