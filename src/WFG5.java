//package org.uma.jmetal.problem.multiobjective.wfg;

import org.uma.jmetal.solution.doublesolution.DoubleSolution;

/**
 * This class implements the WFG5 problem
 * Reference: Simon Huband, Luigi Barone, Lyndon While, Phil Hingston
 * A Scalable Multi-objective Test Problem Toolkit.
 * Evolutionary Multi-Criterion Optimization:
 * Third International Conference, EMO 2005.
 * Proceedings, volume 3410 of Lecture Notes in Computer Science
 */
@SuppressWarnings("serial")
public class WFG5 extends WFG {
  /**
   * Creates a default WFG5 instance with
   * 2 position-related parameters
   * 4 distance-related parameters
   * and 2 objectives
   */
  public WFG5() {
    this(2, 4, 2);
  }

  /**
   * Creates a WFG5 problem instance
   *
   * @param k            Number of position parameters
   * @param l            Number of distance parameters
   * @param m            Number of objective functions
   */
  public WFG5(Integer k, Integer l, Integer m) {
    super(k, l, m);
    setName("WFG5");

    s = new int[m];
    for (int i = 0; i < m; i++) {
      s[i] = 2 * (i + 1);
    }

    a = new int[m - 1];
    for (int i = 0; i < m - 1; i++) {
      a[i] = 1;
    }
  }

  /** Evaluate() method */
  public float[] evaluate(float[] z) {
    float[] y;

    y = normalise(z);
    y = t1(y, k);
    y = t2(y, k, m);

    float[] result = new float[m];
    float[] x = calculateX(y);
    for (int m = 1; m <= this.m; m++) {
      result[m - 1] = d * x[this.m - 1] + s[m - 1] * (new Shapes()).concave(x, m);
    }

    return result;
  }

  /**
   * WFG5 t1 transformation
   */
  public float[] t1(float[] z, int k) {
    float[] result = new float[z.length];

    for (int i = 0; i < z.length; i++) {
      result[i] = (new Transformations()).sDecept(z[i], (float) 0.35, (float) 0.001, (float) 0.05);
    }

    return result;
  }


  /**
   * WFG5 t2 transformation
   */
  public float[] t2(float[] z, int k, int M) {
    float[] result = new float[M];
    float[] w = new float[z.length];

    for (int i = 0; i < z.length; i++) {
      w[i] = (float) 1.0;
    }

    for (int i = 1; i <= M - 1; i++) {
      int head = (i - 1) * k / (M - 1) + 1;
      int tail = i * k / (M - 1);
      float[] subZ = subVector(z, head - 1, tail - 1);
      float[] subW = subVector(w, head - 1, tail - 1);

      result[i - 1] = (new Transformations()).rSum(subZ, subW);
    }

    int head = k + 1;
    int tail = z.length;
    float[] subZ = subVector(z, head - 1, tail - 1);
    float[] subW = subVector(w, head - 1, tail - 1);
    result[M - 1] = (new Transformations()).rSum(subZ, subW);

    return result;
  }

  /**
   * Evaluates a solution
   *
   * @param solution The solution to runAlgorithm
   * @throws org.uma.jmetal.util.JMetalException
   */
  public void evaluate(MyDefaultDoubleSolution solution) {
    float[] variables = new float[getNumberOfVariables()];
    double[] x = new double[getNumberOfVariables()];

    for (int i = 0; i < getNumberOfVariables(); i++) {
      x[i] = solution.getVariable(i);
    }

    for (int i = 0; i < getNumberOfVariables(); i++) {
      variables[i] = (float) x[i] ;
    }

    float[] sol2 = evaluate(variables);

    for (int i = 0; i < sol2.length; i++) {
      solution.setObjective(i, sol2[i]);
    }
  }
}
