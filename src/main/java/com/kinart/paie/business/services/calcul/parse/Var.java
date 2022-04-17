package com.kinart.paie.business.services.calcul.parse;

import java.util.Vector;

public class Var extends Function {
    /**
     * Constructor.
     */
	public Var() {
    }

    /**
     * Constructor.
     * This function requires one or more parameters and
     * calculates Var(a,b,c,...)
     * @param values   A vector with values
     */
	public Var(Vector<Function> values) {
        set(values);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws java.lang.Error, Error {
        if (values_.size() < 2) {
            throw new java.lang.Error("8"); // wrong number of parameters
        }

        // calculate the average
        double num = (double)values_.size();
        double sum = 0.0;
        for (int i = 0; i < values_.size(); i++)
        {
            sum += values_.get(i).eval();
        }
        double avg = sum / num;

        // calculate sum of the the squared difference
        double sumvar = 0.0;
        for (int i = 0; i < values_.size(); i++)
        {
            double diff = values_.get(i).eval() - avg;
            sumvar += diff * diff;
        }
        double var = 1.0 / (num - 1.0) * sumvar;

        return var;
    }

    /**
     * Return the name of the function
     */
    public String name() {
        return "Var";
    }
}
