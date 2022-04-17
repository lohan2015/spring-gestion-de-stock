package com.kinart.paie.business.services.calcul.parse;


public class Sign extends Function {
    /**
     * Constructor.
     */
	public Sign() {
    }
    
    /**
     * Constructor.
     * This function requires one parameter and calculates Sign(x)
     * @param x
     */    
	public Sign(Function x) {
        set(x);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws java.lang.Error, Error {
        if (values_.size() != 1) {
            throw new java.lang.Error("8"); // wrong number of parameters
        }

        double x = values_.get(0).eval();

        if (x > 0.0) return 1.0;
        if (x < 0.0) return -1.0;
        return 0.0;   
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "Sign";
    }
}
