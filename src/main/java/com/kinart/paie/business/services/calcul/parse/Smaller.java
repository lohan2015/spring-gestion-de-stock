package com.kinart.paie.business.services.calcul.parse;


public class Smaller extends Function {
    /**
     * Constructor.
     */
	public Smaller() {
    }

    /**
     * Constructor.
     * This function requires two parameters and calculates Smaller(a,b) or a<b
     * @param a
     * @param b
     */
	public Smaller(Function a, Function b) {
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
        double smaller = (Double.compare(a, b) < 0) ? 1.0 : 0.0;

        return (smaller);
    }

    /**
     * Return the name of the function
     */
    public String name() {
        return "<";
    }
}
