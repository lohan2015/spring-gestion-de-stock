package com.kinart.paie.business.services.calcul.parse;


public class Factorial extends Function {
    /**
     * Constructor.
     */
	public Factorial() {
    }
        
    /**
     * Constructor.
     * This function requires one parameter and calculates Factorial(x) or x!
     * for example 5! = 5*4*3*2*1 = 120
     * @param x
     */    
	public Factorial(Function x) {
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
    
        double value = values_.get(0).eval();
    
        int v = (int)value;
    
        if (value != (double)v) {
            throw new Error(400, name());
        }
    
        double ans = v;
        v--;
        while (v > 1) {
            ans *= v;
            v--;
        }
    
        if (ans == 0) {
            ans = 1;        // 0! is per definition 1
        }
    
        return ans;       
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "!";
    }
}
