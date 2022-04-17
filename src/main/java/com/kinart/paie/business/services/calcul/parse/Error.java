package com.kinart.paie.business.services.calcul.parse;


@SuppressWarnings("serial")
public class Error extends Exception {

    /**
     * Create an error with given message id and fill in given string in message
     * @param id    id of the message
     * @param str   a string which will be filled in in the message
     */
	public Error(final int id, final String str) { 
        row_ = -1;
        col_ = -1;
        id_ = id;
        
        msg_ = String.format(errorMsg(id_), str);
    }
    
    /**
     * Create an error with given message id and fill in given string in message
     * @param id    id of the message
     */
	public Error(final int id) { 
        row_ = -1;
        col_ = -1;
        id_ = id;
        
        msg_ = errorMsg(id_);
    }

    /**
     * Create an error with given message id and fill in given string in message
     * @param id    id of the message
     * @param str   a string which will be filled in in the message
     * @param row   row where the error occured
     * @param col   column where the error occured 
     */
	public Error(final int id, final String str, final int row, final int col) { 
        row_ = row;
        col_ = col;
        id_ = id;
        
        msg_ = String.format(errorMsg(id_), str);
    }

    /**
     * Create an error with given message id and fill in given string in message
     * @param id    id of the message
     * @param row   row where the error occured 
     * @param col   column where the error occured 
     */
	public Error(final int id, final int row, final int col) { 
        row_ = row;
        col_ = col;
        id_ = id;
        
        msg_ = errorMsg(id_);
    }
    
    /**
    * Returns the error message, including line and column number
    */
	public final String get() {
        String res;
        if (row_ == -1) {
            if (col_ == -1) {
                res = String.format("Erreur: %s", msg_);
            } else {
                res = String.format("Erreur: %s (col %d)", msg_, col_);
            }
        } else {
            res = String.format("Erreur: %s (ln %d, col %d)", msg_, row_, col_);
        }
        
        return res;
    }
    
	public int get_id() {
        return id_;
    }

/// Private functions
    
    /**
     * Returns a pointer to the message description for the given message id.
     * Returns "Unknown error" if id was not recognized.
     */
	/*
    private String errorMsg(final int id)
    {
        switch (id)
        {
            case 1: return "Syntax error in part %s";
            case 2: return "Syntax error";
            case 3: return "Parentesis ) missing";
            case 4: return "Empty expression";
            case 5: return "Unexpected part %s";
            case 6: return "Unexpected end of expression";
            case 7: return "Value expected";
            case 8: return "Wrong number of parameters in function %s";
            case 9: return "Parentesis ( expected";
            
            // wrong or unknown operators, functions, variables
            case 101: return "Unknown operator %s";
            case 102: return "Unknown function %s";
            case 103: return "Unknown variable %s";
            
            // domain errors
            case 200: return "Too long expression, maximum number of characters exceeded";
            
            // error in assignments of variables or functions
            case 300: return "Defining variable failed";
            case 301: return "Undefined function %s";
            case 302: return "Link undefined";
            case 303: return "Illegal variable name %s";
            
            // error in functions
            case 400: return "Integer value expected in function %s";
            case 401: return "Division by zero";    
            case 402: return "Local variable %s uninitialized";  
            case 403: return "Variable %s uninitialized";
        }
        
        return "Unknown error";
    }  
    */
    private String errorMsg(final int id)
    {
        switch (id)
        {
            case 1: return "Erreur de synthaxe dans la partie %s";
            case 2: return "Erreur de synthaxe";
            case 3: return "Parenth�se ) absente";
            case 4: return "Expression vide";
            case 5: return "Partie inatendue : %s";
            case 6: return "Fin d'expression inatendue";
            case 7: return "Valeur attendue";
            case 8: return "Nombre incorrect de parametres pour la fonction %s";
            case 9: return "Parentesis ( expected";
            
            // wrong or unknown operators, functions, variables
            case 101: return "Operateur inconnu %s";
            case 102: return "Fonction inconnue %s";
            case 103: return "Variable inconnue %s";
            
            // domain errors
            case 200: return "Expression trop longue, nombre maxi de caract�res d�pass�";
            
            // error in assignments of variables or functions
            case 300: return "Erreur lors de la definition de variable";
            case 301: return "Fonction indefinie %s";
            case 302: return "Lien indefini";
            case 303: return "Nom illegal de variable %s";
            
            // error in functions
            case 400: return "Valeur enti�re attendue pour la fonction %s";
            case 401: return "Division par zero";    
            case 402: return "Variable locale %s non initialis�e";  
            case 403: return "Variable %s non initialis�e";
        }
        
        return "Erreur inconnue";
    }  

/// Data  
    private int row_;    /// row where the error occured
    private int col_;    /// column (position) where the error occured
    private int id_;     /// id of the error
    private String msg_;
}
