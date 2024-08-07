//package org.uma.jmetal.problem.multiobjective;

//import org.uma.jmetal.problem.doubleproblem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

/** Class representing problem Srinivas */
@SuppressWarnings("serial")
public class Srinivas extends AbstractDoubleProblem {

  /** Constructor */
  public Srinivas()  {
    setNumberOfVariables(2);
    setNumberOfObjectives(2);
    setNumberOfConstraints(2);
    setName("Srinivas");

    List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
    List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

    for (int i = 0; i < getNumberOfVariables(); i++) {
      lowerLimit.add(-20.0);
      upperLimit.add(20.0);
    }

    setVariableBounds(lowerLimit, upperLimit);
  }

  /** Evaluate() method */
  @Override
  public void evaluate(MyDefaultDoubleSolution solution)  {
    double[] f = new double[solution.getNumberOfVariables()];

    double x1 = solution.getVariable(0);
    double x2 = solution.getVariable(1);
    f[0] = 2.0 + (x1 - 2.0) * (x1 - 2.0) + (x2 - 1.0) * (x2 - 1.0);
    f[1] = 9.0 * x1 - (x2 - 1.0) * (x2 - 1.0);

    solution.setObjective(0, f[0]);
    solution.setObjective(1, f[1]);

    evaluateConstraints(solution);
  }

  /** EvaluateConstraints() method  */
  public void evaluateConstraints(MyDefaultDoubleSolution solution)  {
    double[] constraint = new double[this.getNumberOfConstraints()];

    double x1 = solution.getVariable(0) ;
    double x2 = solution.getVariable(1) ;

    constraint[0] = 1.0 - (x1 * x1 + x2 * x2) / 225.0;
    constraint[1] = (3.0 * x2 - x1) / 10.0 - 1.0;

    for (int i = 0; i < getNumberOfConstraints(); i++) {
      solution.setConstraint(i, constraint[i]);
    }
  }
}
