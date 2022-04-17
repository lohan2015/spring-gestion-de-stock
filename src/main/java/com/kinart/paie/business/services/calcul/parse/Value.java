package com.kinart.paie.business.services.calcul.parse;


public class Value extends Function {
    /**
     * Constructor
     */
    public Value() {
        value_ = 0.0;    
    }
    
    /**
     * Constructor
     */    
    public Value(final double value) {
        setValue(value);
    }

    /**
     * Set a new value.
     */
    public void setValue(final double value) {
        clear();
        value_ = value;
    }

    /**
     * Get the current value.
     */    
    public double getValue() {
        return value_;
    }

    /**
     * Evaluate the value
     */    
    public double eval() {
        return value_;
    }

    /**
     * Return the name of the function
     */
    public String name() {
        return "Value";
    }
    
    private double value_;
}
