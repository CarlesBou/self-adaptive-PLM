

import org.uma.jmetal.algorithm.AlgorithmBuilder;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.selection.SelectionOperator;
import org.uma.jmetal.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import java.util.Comparator;
import java.util.List;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class MyNSGAIIBuilder<S extends Solution<?>> implements AlgorithmBuilder<MyNSGAII<S>> {
  // public enum NSGAIIVariant {MyNSGAII, NSGAII, SteadyStateNSGAII, Measures, NSGAII45, DNSGAII}

  /**
   * NSGAIIBuilder class
   */
  private final Problem<S> problem;
  private int maxEvaluations;
  private int populationSize;
  protected int matingPoolSize;
  protected int offspringPopulationSize ;

  private CrossoverOperator<S> crossoverOperator;
  private MutationOperator<S> mutationOperator;
  private SelectionOperator<List<S>, S> selectionOperator;
  private SolutionListEvaluator<S> evaluator;
  private Comparator<S> dominanceComparator ;

  //private NSGAIIVariant variant;

  /**
   * NSGAIIBuilder constructor
   */
  public MyNSGAIIBuilder(Problem<S> problem, CrossoverOperator<S> crossoverOperator,
      MutationOperator<S> mutationOperator, int populationSize) {
    this.problem = problem;
    this.maxEvaluations = 25000;
    this.populationSize = populationSize;
    matingPoolSize = populationSize;
    offspringPopulationSize = populationSize ;
    this.crossoverOperator = crossoverOperator ;
    this.mutationOperator = mutationOperator ;
    selectionOperator = new BinaryTournamentSelection<S>(new RankingAndCrowdingDistanceComparator<S>()) ;
    evaluator = new SequentialSolutionListEvaluator<S>();
    dominanceComparator = new DominanceComparator<>()  ;

    //this.variant = NSGAIIVariant.NSGAII ;
  }

  public MyNSGAIIBuilder<S> setMaxEvaluations(int maxEvaluations) {
    if (maxEvaluations < 0) {
      throw new JMetalException("maxEvaluations is negative: " + maxEvaluations);
    }
    this.maxEvaluations = maxEvaluations;

    return this;
  }

  public MyNSGAIIBuilder<S> setMatingPoolSize(int matingPoolSize) {
    if (matingPoolSize < 0) {
      throw new JMetalException("The mating pool size is negative: " + populationSize);
    }

    this.matingPoolSize = matingPoolSize;

    return this;
  }
  public MyNSGAIIBuilder<S> setOffspringPopulationSize(int offspringPopulationSize) {
    if (offspringPopulationSize < 0) {
      throw new JMetalException("Offspring population size is negative: " + populationSize);
    }

    this.offspringPopulationSize = offspringPopulationSize;

    return this;
  }

  public MyNSGAIIBuilder<S> setSelectionOperator(SelectionOperator<List<S>, S> selectionOperator) {
    if (selectionOperator == null) {
      throw new JMetalException("selectionOperator is null");
    }
    this.selectionOperator = selectionOperator;

    return this;
  }

  public MyNSGAIIBuilder<S> setSolutionListEvaluator(SolutionListEvaluator<S> evaluator) {
    if (evaluator == null) {
      throw new JMetalException("evaluator is null");
    }
    this.evaluator = evaluator;

    return this;
  }

  public MyNSGAIIBuilder<S> setDominanceComparator(Comparator<S> dominanceComparator) {
    if (dominanceComparator == null) {
      throw new JMetalException("dominanceComparator is null");
    }
    this.dominanceComparator = dominanceComparator ;

    return this;
  }


  //public MyNSGAIIBuilder<S> setVariant(NSGAIIVariant variant) {
  //  this.variant = variant;
  //
  //  return this;
  //}

  public MyNSGAII<S> build() {
    MyNSGAII<S> algorithm = null ;
    algorithm = new MyNSGAII<S>(problem, maxEvaluations, populationSize, matingPoolSize, 
    							offspringPopulationSize, crossoverOperator, mutationOperator, 
    							selectionOperator, dominanceComparator, evaluator);

    return algorithm ;
  }

  /* Getters */
  public Problem<S> getProblem() {
    return problem;
  }

  public int getMaxIterations() {
    return maxEvaluations;
  }

  public int getPopulationSize() {
    return populationSize;
  }

  public CrossoverOperator<S> getCrossoverOperator() {
    return crossoverOperator;
  }

  public MutationOperator<S> getMutationOperator() {
    return mutationOperator;
  }

  public SelectionOperator<List<S>, S> getSelectionOperator() {
    return selectionOperator;
  }

  public SolutionListEvaluator<S> getSolutionListEvaluator() {
    return evaluator;
  }
}
