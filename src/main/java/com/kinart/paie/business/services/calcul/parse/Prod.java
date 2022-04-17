package com.kinart.paie.business.services.calcul.parse;

import java.util.Vector;

public class Prod extends Function {
    /**
     * Constructor.
     */
	public Prod() {
    }

    /**
     * Constructor.
     * This function requires one or more parameters and
     * calculates Prod(a,b,c,...)
     * For example Prod(3,4,5) = 3*4*5 = 60
     * @param values   A vector with values
     */
	public Prod(Vector<Function> values) {
        set(values);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws java.lang.Error, Error {
        if (values_.size() == 0) {
            throw new java.lang.Error("8"); // wrong number of parameters
        }

        double ans = 1.0;
        for (int i = 0; i < values_.size(); i++)
        {
            ans *= values_.get(i).eval();
        }

        return ans;
    }

    /**
     * Return the name of the function
     */
    public String name() {
        return "Prod";
    }
}
