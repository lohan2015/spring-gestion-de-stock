package com.kinart.paie.business.services.calcul.parse;

import java.util.Vector;

public class Max extends Function {
    /**
     * Constructor.
     */
	public Max() {
    }

    /**
     * Constructor.
     * This function requires one or more parameters and
     * calculates Max(a,b,c,...)
     * For example Max(3,4,5) = 5
     * @param values   A vector with values
     */
	public Max(Vector<Function> values) {
        set(values);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws java.lang.Error, Error {
        if (values_.size() < 1) {
            throw new java.lang.Error("8"); // wrong number of parameters
        }

        double max = values_.get(0).eval();
        for (int i = 1; i < values_.size(); i++) // Start at 1
        {
            max = Math.max(max, values_.get(i).eval());
        }

        return max;
    }

    /**
     * Return the name of the function
     */
    public String name() {
        return "Max";
    }
}
