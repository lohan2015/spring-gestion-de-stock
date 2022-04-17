package com.kinart.paie.business.services.calcul.parse;



/// The class LocalVariableGet links to a local function variable
public class LocalVariableGet extends Function {
    /**
     * Constructor
     */
    public LocalVariableGet() {
    }

    /**
     * Constructor
     */    
    public LocalVariableGet(LocalVariable localvar) {
        set(localvar);
    }
    
    /**
     * Set a new local variable link
     */
    public void set(LocalVariable localvar) {
        clear();
        values_.add(localvar);      
    }
    
    /**
     * Evaluate the linked local variable
     */
    public double eval() throws Error {
        if (values_.size() != 1) {
            throw new Error(402, "");  // local variable uninitialized
        }
        
        return values_.get(0).eval();      
    }

    /**
     * Return the name of this class
     */    
    public String name() {
        return "LocalVariableGet";
    }
}
