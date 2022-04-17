package com.kinart.paie.business.services.calcul.parse;


public class Divide extends Function {
    /**
     * Constructor.
     */
	public Divide() {
    }
        
    /**
     * Constructor.
     * This function requires two parameters and calculates Divide(a,b) or a/b
     * @param a
     * @param b
     */    
	public Divide(Function a, Function b) {
        set(a, b);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws java.lang.Error, Error {
        if (values_.size() != 2) {
            throw new java.lang.Error("8"); // wrong number of parameters
        }

        double a = values_.get(0).eval();
        double b = values_.get(1).eval();

        if (b == 0.0) {
            throw new java.lang.Error("401"); // division by zero
        }

        return a / b;
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "*";
    }
}
