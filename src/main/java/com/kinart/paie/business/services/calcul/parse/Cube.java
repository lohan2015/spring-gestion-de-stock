package com.kinart.paie.business.services.calcul.parse;


public class Cube extends Function {
    /**
     * Constructor.
     */
	public Cube() {
    }
    
    /**
     * Constructor.
     * This function requires one parameter and calculates Cube(x) = x^3 = x*x*x
     * @param x
     */    
	public Cube(Function x) {
        set(x);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws Error {
        if (values_.size() != 1) {
            throw new Error(8, name()); // wrong number of parameters
        }

        double x = values_.get(0).eval();
        return x * x * x;        
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "Cube";
    }
}
