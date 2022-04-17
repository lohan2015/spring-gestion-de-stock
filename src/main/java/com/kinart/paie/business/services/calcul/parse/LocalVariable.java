package com.kinart.paie.business.services.calcul.parse;



/** 
 * The class LocalVar stores local variables for an assigned function
 * This object will link to another object value, to be defined when evaluating
 * the function
 */
public class LocalVariable extends Function {
    /**
     * Constructor
     */
    public LocalVariable() {
    }

    /**
     * Constructor
     */    
    public LocalVariable(final String name) {
        setName(name);
    }

    /**
     * Set a variable name
     * Note that the variable value must be set via LocalVar.set(value);
     */
    public void setName(final String name) {
        clear();
        name_ = name;         
    }
    
    /**
     * Get the variable name
     */    
    public final String getName() {
        return name_;        
    }
    
    /**
     * Clear all settings: name, values
     */    
    public void clear() {
        name_ = "";
    	values_.clear();
    }
    
    /**
     * Evaluate the linked function
     * Note that the variable value must be set via LocalVariable.set(value);
     */
    public double eval() throws java.lang.Error, Error {
        if (values_.size() != 1) {
            throw new java.lang.Error("402");  // local variable uninitialized
        }
        
        return values_.get(0).eval();        
    }

    /**
     * Return the name of this class
     */    
    public String name() {
        return "LocalVariable";
    }

    private String name_;
}
