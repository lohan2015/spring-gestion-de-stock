package com.kinart.paie.business.services.calcul.parse;

import java.util.Vector;

/**
 * Base class Function. All functions (like Sin, Abs, Add, Subtract, etc.)
 * inherit from this function
 */
public abstract class Function {
    /**
     * Constructor
     */
    public Function() {
    }
    
    /**
     * Delete and cleanup all parameters set in this function
     */
    public void clear() {
        // When you override this function, you have to clear values_ yourself.       
        values_.clear();
    }
    
    /**
     * Set one parameter for this function
     * @param value    The parameter for the function
     */
    public void set(Function value) {
        clear();
        values_.add(value);
    }    
    
    /**
     * Set two parameter for this function
     * @param value1    First parameter for the function
     * @param value2    Second parameter for the function
     */    
    public void set(Function value1, Function value2) {
        clear();

        values_.add(value1);
        values_.add(value2);
    }    
    
    /**
     * Set a list of parameters for this function
     * @param values    A vector containing parameters for the function
     */    
    public void set(Vector<Function> values) {
        clear();
        for (int i = 0; i < values.size(); i++)
        {
            values_.add(values.get(i));
        }
    }    
    
    /**
     * Returns the list with parameters present in this function
     * You can use this function to traverse an expression tree
     * @return 			The vector with function parameters
     */    
    public final Vector<Function> get() {
        return values_;
    }

    // eval() and name() are abstract, and must be implemented by all subclasses
    public abstract double eval() throws Error;
    public abstract String name() ;

    protected Vector<Function> values_ = new Vector<Function>();
}
