

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.operator.selection.SelectionOperator;
import org.uma.jmetal.operator.selection.impl.RankingAndCrowdingSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * @author Carles
 * 
 * Modified version of NSGAII implemented in jMetal to support the mutation distribution index
 * evolution included in individual solutions
 *
 */
@SuppressWarnings("serial")
public class MyNSGAII<S extends Solution<?>> extends AbstractGeneticAlgorithm<S, List<S>> {
  protected final int maxEvaluations;

  protected final SolutionListEvaluator<S> evaluator;

  protected int evaluations;
  protected Comparator<S> dominanceComparator ;

  protected int matingPoolSize;
  protected int offspringPopulationSize ;

  /**
   * Constructor
   */
  public MyNSGAII(Problem<S> problem, int maxEvaluations, int populationSize,
                int matingPoolSize, int offspringPopulationSize,
                CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
                SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator) {
    this(problem, maxEvaluations, populationSize, matingPoolSize, offspringPopulationSize,
            crossoverOperator, mutationOperator, selectionOperator, new DominanceComparator<S>(), evaluator);
  }
  /**
   * Constructor
   */
  public MyNSGAII(Problem<S> problem, int maxEvaluations, int populationSize,
                int matingPoolSize, int offspringPopulationSize,
                CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
      SelectionOperator<List<S>, S> selectionOperator, Comparator<S> dominanceComparator,
                SolutionListEvaluator<S> evaluator) {
    super(problem);
    this.maxEvaluations = maxEvaluations;
    setMaxPopulationSize(populationSize);

    this.crossoverOperator = crossoverOperator;
    this.mutationOperator = mutationOperator;
    this.selectionOperator = selectionOperator;

    this.evaluator = evaluator;
    this.dominanceComparator = dominanceComparator;

    this.matingPoolSize = matingPoolSize ;
    this.offspringPopulationSize = offspringPopulationSize;
  }

  @Override protected void initProgress() {
    evaluations = getMaxPopulationSize();
  }

  @Override protected void updateProgress() {
    evaluations += offspringPopulationSize ;
  }

  @Override protected boolean isStoppingConditionReached() {
    return evaluations >= maxEvaluations;
  }

  @Override protected List<S> evaluatePopulation(List<S> population) {
    population = evaluator.evaluate(population, getProblem());

    S s = population.get(0);
    double mutpar = (double) s.getAttribute("mutpar");

    return population;
  }

  /**
   * This method iteratively applies a {@link SelectionOperator} to the population to fill the mating pool population.
   *
   * @param population
   * @return The mating pool population
   */
  @Override
  protected List<S> selection(List<S> population) {
    List<S> matingPopulation = new ArrayList<>(population.size());
    for (int i = 0; i < matingPoolSize; i++) {
      S solution = selectionOperator.execute(population);
      matingPopulation.add(solution);
    }

    return matingPopulation;
  }

  void modif_distrib(S sol) {
	  double tau = 0.03;
	  
	  double r1 = JMetalRandom.getInstance().nextDouble();
	  if (r1 < tau) {
		  sol.setAttribute("mutpar", JMetalRandom.getInstance().nextDouble());
	  }
  }
  
  double get_modif_distrib(List<S> sols) {
	  double mutpar = 0;

	  for (S s : sols) {
		  mutpar += (double) s.getAttribute("mutpar");
	  }
	  mutpar = mutpar / sols.size();
	  
	  Random r = new Random();
	  
	  mutpar = mutpar + r.nextGaussian();

	  mutpar = mutpar - (int) mutpar;
	  
	  return mutpar;
  }
  
  void modif_distrib(List<S> sols) {
	  double tau = 0.1;
	  //double tau = 1;
	 
	  double r1 = JMetalRandom.getInstance().nextDouble();
	  
	  if (r1 < tau) {
		  double mutpar = get_modif_distrib(sols);
		  for (S s : sols) {
			  s.setAttribute("mutpar", mutpar);
		  }
	  }	  
  }
  
  void set_distrib(List<S> sols, double mutpar) {
	  for (S s : sols) {
		  s.setAttribute("mutpar", mutpar);
	  }	  
  }
  
  /**
   * This methods iteratively applies a {@link CrossoverOperator} a  {@link MutationOperator} to the population to
   * create the offspring population. The population size must be divisible by the number of parents required
   * by the {@link CrossoverOperator}; this way, the needed parents are taken sequentially from the population.
   *
   * The number of solutions returned by the {@link CrossoverOperator} must be equal to the offspringPopulationSize
   * state variable
   *
   * @param matingPool
   * @return The new created offspring population
   */
  @Override
  protected List<S> reproduction(List<S> matingPool) {
    int numberOfParents = crossoverOperator.getNumberOfRequiredParents() ;

    checkNumberOfParents(matingPool, numberOfParents);

    List<S> offspringPopulation = new ArrayList<>(offspringPopulationSize);
    for (int i = 0; i < matingPool.size(); i += numberOfParents) {
      List<S> parents = new ArrayList<>(numberOfParents);
      for (int j = 0; j < numberOfParents; j++) {
        parents.add(matingPool.get(i+j));
      }

      // Carles: modify mut_par for every parent      
	  modif_distrib(parents);
      
	  // Apply crossover
      List<S> offspring = crossoverOperator.execute(parents);

	  // Set initial mut_par in each child
      offspring.get(0).setAttribute("mutpar", (double) parents.get(0).getAttribute("mutpar"));
      offspring.get(1).setAttribute("mutpar", (double) parents.get(1).getAttribute("mutpar"));
      
	  // Apply mutation
      for(S s: offspring){
        mutationOperator.execute(s);
        offspringPopulation.add(s);
        if (offspringPopulation.size() >= offspringPopulationSize)
          break;
      }
    }
    return offspringPopulation;
  }

  @Override protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
    List<S> jointPopulation = new ArrayList<>();
    jointPopulation.addAll(population);
    jointPopulation.addAll(offspringPopulation);

    RankingAndCrowdingSelection<S> rankingAndCrowdingSelection ;
    rankingAndCrowdingSelection = new RankingAndCrowdingSelection<S>(getMaxPopulationSize(), dominanceComparator) ;

    return rankingAndCrowdingSelection.execute(jointPopulation) ;
  }

  @Override public List<S> getResult() {
    return SolutionListUtils.getNonDominatedSolutions(getPopulation());
  }

  @Override public String getName() {
    return "MyNSGAII" ;
  }

  @Override public String getDescription() {
    return "New version of Nondominated Sorting Genetic Algorithm version II" ;
  }
}
