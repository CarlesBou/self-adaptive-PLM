
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.problem.doubleproblem.DoubleProblem;
import org.uma.jmetal.solution.util.repairsolution.RepairDoubleSolution;
import org.uma.jmetal.solution.util.repairsolution.impl.RepairDoubleSolutionWithBoundValue;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.checking.Check;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

/**
 * This class implements a the new self-adaptive polynomial mutation operator
 *
 * @author Jose L. Carles
 */
 
@SuppressWarnings("serial")
public class MyPolynomialMutation implements MutationOperator<MyDefaultDoubleSolution> {
  private static final double DEFAULT_PROBABILITY = 0.01;
  private static final double DEFAULT_DISTRIBUTION_INDEX = 20.0;
  private double distributionIndex;
  private double mutationProbability;
  private RepairDoubleSolution solutionRepair;

  private RandomGenerator<Double> randomGenerator;

  /** Constructor */
  public MyPolynomialMutation() {
    this(DEFAULT_PROBABILITY, DEFAULT_DISTRIBUTION_INDEX);
  }

  /** Constructor */
  public MyPolynomialMutation(DoubleProblem problem, double distributionIndex) {
    this(1.0 / problem.getNumberOfVariables(), distributionIndex);
  }

  /** Constructor */
  public MyPolynomialMutation(
      DoubleProblem problem, double distributionIndex, RandomGenerator<Double> randomGenerator) {
    this(1.0 / problem.getNumberOfVariables(), distributionIndex);
    this.randomGenerator = randomGenerator;
  }

  /** Constructor */
  public MyPolynomialMutation(double mutationProbability, double distributionIndex) {
    this(mutationProbability, distributionIndex, new RepairDoubleSolutionWithBoundValue());
  }

  /** Constructor */
  public MyPolynomialMutation(
      double mutationProbability,
      double distributionIndex,
      RandomGenerator<Double> randomGenerator) {
    this(
        mutationProbability,
        distributionIndex,
        new RepairDoubleSolutionWithBoundValue(),
        randomGenerator);
  }

  /** Constructor */
  public MyPolynomialMutation(
      double mutationProbability, double distributionIndex, RepairDoubleSolution solutionRepair) {
    this(
        mutationProbability,
        distributionIndex,
        solutionRepair,
        () -> JMetalRandom.getInstance().nextDouble());
  }

  /** Constructor */
  public MyPolynomialMutation(
      double mutationProbability,
      double distributionIndex,
      RepairDoubleSolution solutionRepair,
      RandomGenerator<Double> randomGenerator) {
    Check.that(distributionIndex >= 0, "Distribution index is negative: " + distributionIndex);
    Check.probabilityIsValid(mutationProbability);
    this.mutationProbability = mutationProbability;
    this.distributionIndex = distributionIndex;
    this.solutionRepair = solutionRepair;
    this.randomGenerator = randomGenerator;
  }

  /* Getters */
  @Override
  public double getMutationProbability() {
    return mutationProbability;
  }

  public double getDistributionIndex() {
    return distributionIndex;
  }

  /* Setters */
  public void setMutationProbability(double probability) {
    this.mutationProbability = probability;
  }

  public void setDistributionIndex(double distributionIndex) {
    this.distributionIndex = distributionIndex;
  }

  /** Execute() method */
  @Override
  public MyDefaultDoubleSolution execute(MyDefaultDoubleSolution solution) throws JMetalException {
    Check.isNotNull(solution);

    doMutation(solution);

    return solution;
  }


  /** Perform the mutation operation */
  private void doMutation(MyDefaultDoubleSolution solution) {
    double rnd, delta1, delta2, mutPow, deltaq;
    double y, yl, yu, val, xy;

    double distrib_index = (double) solution.getAttribute("mutpar")
    
    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
      if (randomGenerator.getRandomValue() <= mutationProbability) {
        y = solution.getVariable(i);
        yl = solution.getLowerBound(i);
        yu = solution.getUpperBound(i);
        if (yl == yu) {
          y = yl;
        } else {
          delta1 = (y - yl) / (yu - yl);
          delta2 = (yu - y) / (yu - yl);
          rnd = randomGenerator.getRandomValue();

          mutPow = 1.0 / (distributionIndex + 1.0);
          
          if (rnd <= 0.5) {
            xy = 1.0 - delta1;
            val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, distrib_index + 1.0));
            deltaq = Math.pow(val, mutPow) - 1.0;
          } else {
            xy = 1.0 - delta2;
            val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (Math.pow(xy, distrib_index + 1.0));
            deltaq = 1.0 - Math.pow(val, mutPow);
          }
          y = y + deltaq * (yu - yl);
          y = solutionRepair.repairSolutionVariableValue(y, yl, yu);
        }
        solution.setVariable(i, y);
      }
    }
  }
}
