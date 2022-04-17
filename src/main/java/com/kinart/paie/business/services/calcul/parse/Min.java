package com.kinart.paie.business.services.calcul.parse;

import java.util.Vector;

public class Min extends Function {
    /**
     * Constructor.
     */
	public Min() {
    }

    /**
     * Constructor.
     * This function requires one or more parameters and
     * calculates Min(a,b,c,...)
     * For example Min(3,4,5) = 3
     * @param values   A vector with values
     */
	public Min(Vector<Function> values) {
        set(values);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws Error {
        if (values_.size() < 1) {
            throw new Error(8, name()); // wrong number of parameters
        }

        double min = values_.get(0).eval();
        for (int i = 1; i < values_.size(); i++) // start at 1
        {
            min = Math.min(min, values_.get(i).eval());
        }

        return min;
    }

    /**
     * Return the name of the function
     */
    public String name() {
        return "Min";
    }
}
