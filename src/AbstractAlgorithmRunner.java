//package org.uma.jmetal.util;

import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.impl.PISAHypervolume;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.impl.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.front.util.FrontUtils;
import org.uma.jmetal.util.point.PointSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.JMetalLogger;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Abstract class for Runner classes
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public abstract class AbstractAlgorithmRunner {
  /**
   * Write the population into two files and prints some data on screen
   * @param population
   */
  public static void printFinalSolutionSet(List<? extends Solution<?>> population) {

    new SolutionListOutput(population)
        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.csv", ","))
        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.csv", ","))
        .print();

    JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
  }

  /**
   * Print all the available quality indicators
   * @param population
   * @param paretoFrontFile
   * @throws FileNotFoundException
   */
  public static <S extends Solution<?>> void printQualityIndicators(List<S> population, String paretoFrontFile)
      throws FileNotFoundException {
    Front referenceFront = new ArrayFront(paretoFrontFile, "[\\t ,]+");
    FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront) ;

    Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront) ;
    Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population)) ;
    List<PointSolution> normalizedPopulation = FrontUtils
        .convertFrontToSolutionList(normalizedFront) ;
    
    List<PointSolution> originalReferenceFrontSolutions = FrontUtils
            .convertFrontToSolutionList(referenceFront) ;
    List<PointSolution> normalizedOriginalReferenceFrontSolutions = FrontUtils
            .convertFrontToSolutionList(normalizedReferenceFront) ;

    String outputString = "\n" ;
    outputString += "Base Hypervolume (N) : " +
            new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedOriginalReferenceFrontSolutions) + "\n";
    outputString += "Base Hypervolume     : " +
            new PISAHypervolume<PointSolution>(referenceFront).evaluate(originalReferenceFrontSolutions) + "\n";
    outputString += "Relative Hypervolume (N) : " +
            new NormalizedHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation) + "\n";
    outputString += "Hypervolume (N) : " +
        new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation) + "\n";
    outputString += "Hypervolume     : " +
        new PISAHypervolume<S>(referenceFront).evaluate(population) + "\n";
    outputString += "Relative Hypervolume (N) : " +
            new NormalizedHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation) + "\n";
    outputString += "Relative Hypervolume     : " +
            new NormalizedHypervolume<S>(referenceFront).evaluate(population) + "\n";
    outputString += "Epsilon (N)     : " +
        new Epsilon<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation) +
        "\n" ;
    outputString += "Epsilon         : " +
        new Epsilon<S>(referenceFront).evaluate(population) + "\n" ;
    outputString += "GD (N)          : " +
        new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation) + "\n";
    outputString += "GD              : " +
        new GenerationalDistance<S>(referenceFront).evaluate(population) + "\n";
    outputString += "IGD (N)         : " +
        new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation) + "\n";
    outputString +="IGD             : " +
        new InvertedGenerationalDistance<S>(referenceFront).evaluate(population) + "\n";
    outputString += "IGD+ (N)        : " +
        new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation) + "\n";
    outputString += "IGD+            : " +
        new InvertedGenerationalDistancePlus<S>(referenceFront).evaluate(population) + "\n";
    outputString += "Spread (N)      : " +
        new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation) + "\n";
    outputString += "Spread          : " +
        new Spread<S>(referenceFront).evaluate(population) + "\n";
//    outputString += "R2 (N)          : " +
//        new R2<List<DoubleSolution>>(normalizedReferenceFront).runAlgorithm(normalizedPopulation) + "\n";
//    outputString += "R2              : " +
//        new R2<List<? extends Solution<?>>>(referenceFront).runAlgorithm(population) + "\n";
//    outputString += "Error ratio     : " +
//        new ErrorRatio<List<? extends Solution<?>>>(referenceFront).evaluate(population) + "\n";
    
    JMetalLogger.logger.info(outputString);
  }
  
  public static <S extends Solution<?>> AlgoResults getQualityIndicators(List<S> population, String paretoFrontFile)
	      throws FileNotFoundException {
	    Front referenceFront = new ArrayFront(paretoFrontFile, "[\\t ,]+");
	    FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront) ;

	    Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront) ;
	    Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population)) ;
	    List<PointSolution> normalizedPopulation = FrontUtils
	        .convertFrontToSolutionList(normalizedFront) ;
	    
//	    List<PointSolution> originalReferenceFrontSolutions = FrontUtils
//	            .convertFrontToSolutionList(referenceFront) ;
	    List<PointSolution> normalizedOriginalReferenceFrontSolutions = FrontUtils
	            .convertFrontToSolutionList(normalizedReferenceFront) ;

	    double hvbase = 
	    		new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedOriginalReferenceFrontSolutions);
	    
	    double hv =
	    		new PISAHypervolume<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
	    
	    double gd =
	        new GenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
	    double igd =
	        new InvertedGenerationalDistance<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
	    double igdp =
	        new InvertedGenerationalDistancePlus<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
	    double spread =
	        new Spread<PointSolution>(normalizedReferenceFront).evaluate(normalizedPopulation);
	    
	    AlgoResults ar = new AlgoResults(hvbase, hv, gd, igd, igdp, spread);
	    
	    return ar;
	    
	  }
}