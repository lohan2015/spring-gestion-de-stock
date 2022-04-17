package com.kinart.paie.business.services.calcul.parse;

import java.util.Vector;

public class Avg extends Function {
    /**
     * Constructor.
     */
	public Avg() {
    }

    /**
     * Constructor.
     * This function requires one or more parameters and
     * calculates Avg(a,b,c,...)
     * For example Avg(3,4,5) = (3+4+5)/3 = 4
     * @param values   A vector with values
     */
	public Avg(Vector<Function> values) {
        set(values);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws Error {
        if (values_.size() == 0) {
            throw new Error(8, name()); // wrong number of parameters
        }

        double sum = 0.0;
        for (int i = 0; i < values_.size(); i++)
        {
            sum += values_.get(i).eval();
        }
        double num = (double)values_.size();
        double avg = sum / num;

        return avg;
    }

    /**
     * Return the name of the function
     */
    public String name() {
        return "Avg";
    }
}
