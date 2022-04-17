package com.kinart.paie.business.services.calcul.parse;


public class Unequal extends Function {
    /**
     * Constructor.
     */
	public Unequal() {
    }

    /**
     * Constructor.
     * This function requires two parameters and calculates Unequal(a,b) or a<>b
     * @param a
     * @param b
     */
	public Unequal(Function a, Function b) {
        set(a, b);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws Error {
        if (values_.size() != 2) {
            throw new Error(8, name()); // wrong number of parameters
        }

        double a = values_.get(0).eval();
        double b = values_.get(1).eval();
        double unequal = (Double.compare(a, b) != 0) ? 1.0 : 0.0;

        return (unequal);
    }

    /**
     * Return the name of the function
     */
    public String name() {
        return "<>";
    }
}
