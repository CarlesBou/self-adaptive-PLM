//package org.uma.jmetal.solution.doublesolution.impl;

import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.solution.AbstractSolution;
//import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.HashMap;
import java.util.List;

/**
 * Defines an implementation of a double solution. Each variable is given by a pair <lower bound, upper bound>.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class MyDefaultDoubleSolution extends AbstractSolution<Double> {
  protected List<Pair<Double, Double>> bounds;
  protected double myMutPar;

  /** Constructor */
  public MyDefaultDoubleSolution(List<Pair<Double, Double>> bounds, int numberOfObjectives, int numberOfConstraints) {
    super(bounds.size(), numberOfObjectives, numberOfConstraints) ;

    this.bounds = bounds ;

    for (int i = 0 ; i < bounds.size(); i++) {
      setVariable(i, JMetalRandom.getInstance().nextDouble(bounds.get(i).getLeft(), bounds.get(i).getRight())); ;
    }

    setAttribute("mutpar", JMetalRandom.getInstance().nextDouble());
  }

  /** Constructor */
  public MyDefaultDoubleSolution(
      List<Pair<Double, Double>> bounds,
      int numberOfObjectives) {
    this(bounds, numberOfObjectives, 0) ;
  }

  /** Copy constructor */
  public MyDefaultDoubleSolution(MyDefaultDoubleSolution solution) {
    super(solution.getNumberOfVariables(), solution.getNumberOfObjectives(), solution.getNumberOfConstraints()) ;

    for (int i = 0; i < solution.getNumberOfVariables(); i++) {
      setVariable(i, solution.getVariable(i));
    }

    for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
      setObjective(i, solution.getObjective(i)) ;
    }

    for (int i = 0; i < solution.getNumberOfConstraints(); i++) {
      setConstraint(i, solution.getConstraint(i));
    }

    bounds = solution.bounds ;
    attributes = new HashMap<Object, Object>(solution.attributes) ;
  }

 
  public Double getLowerBound(int index) {
    return this.bounds.get(index).getLeft() ;
  }

  
  public Double getUpperBound(int index) {
    return this.bounds.get(index).getRight() ;
  }

  //@Override
  public MyDefaultDoubleSolution copy() {
    return new MyDefaultDoubleSolution(this);
  }
  
  public void setMutPar(double d) {
	  myMutPar = d;
  }
}
