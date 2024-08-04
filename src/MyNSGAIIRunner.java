// package org.uma.jmetal.example.multiobjective;

/**import org.uma.jmetal.algorithm.impl.*;
import org.uma.jmetal.algorithm.multiobjective.ibea.IBEA;
import org.uma.jmetal.algorithm.multiobjective.ibea.IBEABuilder;
import org.uma.jmetal.example.AlgorithmRunner;
 **/


//import org.uma.jmetal.algorithm.multiobjective.ibea.IBEA;
//import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
//import org.uma.jmetal.operator.crossover.impl.SBXCrossover;
import org.uma.jmetal.operator.mutation.MutationOperator;
//import org.uma.jmetal.operator.mutation.impl.PolynomialMutation;
//import org.uma.jmetal.operator.selection.SelectionOperator;
//import org.uma.jmetal.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
//import org.uma.jmetal.solution.doublesolution.DoubleSolution;
//import org.uma.jmetal.solution.doublesolution.impl.DefaultDoubleSolution;
//import org.uma.jmetal.util.AbstractAlgorithmRunner;
//import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.observer.impl.EvaluationObserver;

import java.util.List;
import java.util.Locale;


public class MyNSGAIIRunner extends AbstractAlgorithmRunner {
	/**
	 * @param args Command line arguments.
	 * @throws java.io.IOException
	 * @throws SecurityException
	 * @throws ClassNotFoundException
	 * Invoking command:
  java org.uma.jmetal.runner.multiobjective.IBEARunner problemName [referenceFront]
	 */
	List<MyDefaultDoubleSolution> population;
	String referenceParetoFront;
	AlgoResults ar;
	int max_evaluations;
	
	MyNSGAIIRunner(String problemName) throws Exception {
		Problem<MyDefaultDoubleSolution> problem;
		MyNSGAII<MyDefaultDoubleSolution> algorithm;
		CrossoverOperator<MyDefaultDoubleSolution> crossover;
		MutationOperator<MyDefaultDoubleSolution> mutation;
		// SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;

		String referenceParetoFront = "resources/" + problemName + ".csv";
		if (problemName.contains("ZDT")) {
			max_evaluations = 25000;
			//problemName = "org.uma.jmetal.problem.multiobjective.zdt." + problemName;
		} else {
			max_evaluations = 40000;
		}
		// JMetalLogger.logger.info("Starting ... ");

		//String line = "0,	1	,   2";
		//String tokens[] = line.split("[\\t ,]+");
		//System.out.printf("Tokens=[%s] [%s] [%s]\n",  tokens[0], tokens[1], tokens[2]);
		//System.exit(1);

		problem = ProblemUtils.loadProblem(problemName);

		double crossoverProbability = 0.9 ;
		double crossoverDistributionIndex = 20.0 ;
		crossover = new MySBXCrossover(crossoverProbability, crossoverDistributionIndex);

		double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
		double mutationDistributionIndex = 20.0 ;
		mutation = new MyPolynomialMutation(mutationProbability, mutationDistributionIndex);

		//selection = new BinaryTournamentSelection<DoubleSolution>() ;

		algorithm = new MyNSGAIIBuilder<MyDefaultDoubleSolution>(problem, crossover, mutation, 300)
				.setMaxEvaluations(max_evaluations)
				.build();

		//EvaluationObserver progress_bar = new EvaluationObserver();
		
		@SuppressWarnings("unused")
		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
				.execute() ;

		population = algorithm.getResult() ;
		
		
		// long computingTime = algorithmRunner.getComputingTime();
		// JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

		ar = getQualityIndicators(population, referenceParetoFront);

		 
		// printResults(ar);

		/*
	    printFinalSolutionSet(population);
	    if (!referenceParetoFront.equals("")) {
	      printQualityIndicators(population, referenceParetoFront) ;
	    }*/
	}

	void printResults() {
		printResults(ar);
	}
	
	static void printResults(AlgoResults ar) {

		Locale loc = Locale.ENGLISH;
		
		String hvpercentage = String.format(loc, "%,.2f", ar.hypervolume * 100 / ar.baseHypervolume) + "%%";

		String outputString = "Results\n" +
				"  Base Hypervolume = " + String.format(loc, "%.6f", ar.baseHypervolume) + "\n" +
				"  Hypervolume      = " + String.format(loc, "%.6f", ar.hypervolume) + " " + hvpercentage + "\n" +
				"  GD               = " + String.format(loc, "%.6f", ar.gd) + "\n" +
				"  IGD              = " + String.format(loc, "%.6f", ar.igd) + "\n" +
				"  IGD+             = " + String.format(loc, "%.6f", ar.igdPlus) + "\n" +
				"  Spread           = " + String.format(loc, "%.6f", ar.spread) + "\n";

		System.out.printf(outputString);
	}

	String getResults() {
		return getResults(ar);
	}
	
	String getResults(AlgoResults ar) {
		Locale loc = Locale.ENGLISH;
		
		String hvpercentage = String.format(loc, "%,.2f", ar.hypervolume * 100 / ar.baseHypervolume) + "%";

		String outputString = 
				"BHV = " + String.format(loc, "%.6f", ar.baseHypervolume) +
				" HV = " + String.format(loc, "%.6f", ar.hypervolume) + " " + hvpercentage +
				" GD = " + String.format(loc, "%.6f", ar.gd) +
				" IGD = " + String.format(loc, "%.6f", ar.igd) +
				" IGD+ = " + String.format(loc, "%.6f", ar.igdPlus) +
				" Spread = " + String.format(loc, "%.6f", ar.spread);
		
		return outputString;
	}
}
