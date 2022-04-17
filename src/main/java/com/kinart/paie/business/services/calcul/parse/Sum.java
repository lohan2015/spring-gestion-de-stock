package com.kinart.paie.business.services.calcul.parse;

import java.util.Vector;

public class Sum extends Function {
    /**
     * Constructor.
     */
	public Sum() {
    }
        
    /**
     * Constructor.
     * This function requires one or more parameters and 
     * calculates Sum(a,b,c,...)
     * For example Sum(3,4,5) = 3+4+5 = 12
     * @param values   A vector with values
     */    
	public Sum(Vector<Function> values) {
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

        double ans = 0.0;
        for (int i = 0; i < values_.size(); i++)
        {
            ans += values_.get(i).eval();
        }
    
        return ans;       
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "Sum";
    }
}
