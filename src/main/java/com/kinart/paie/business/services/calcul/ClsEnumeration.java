package com.kinart.paie.business.services.calcul;

public class ClsEnumeration {
	public enum EnEnterprise{
		SHELL_GABON,
		SGMB,
		UNKNOWN
	}
	public enum EnLoan{
		STC,
		UNKNOWN
	}
	public enum EnModePaiement{
		C,//ch�que
		E,
		V,//virement
		UNKNOWN
	}
	public enum EnSalaryToTreat{
		HASELFIX,//ch�que
		HASELVAR,
		HASELFIXANDVAR,//virement
		UNKNOWN
	}

	public enum EnColumnToRead{
		AMOUNT //pour lire le montant
		, RATES // pour lire le taux
		, LABEL // pour lire le libell�
		, UNKNOWN
	}
	
	public enum EnTypeOfColumn{
		AMOUNT //pour lire le montant
		, RATES // pour lire le taux
		, BASE // pour lire la base
		, UNKNOWN
	}
	
	public enum EnTypeOfDay{
		C //pour lire le montant,
		, A
		, UNKNOWN
	}
	/**
	 * permet de d�finir la liste de salari�s � traiter
	 * @author CDInformatique2
	 *
	 */
	public enum EnTypeOfSalaryList{
		LIST		//Choix d'une liste de salari�s
		,INTERVAL	//Choix d'un intervalle
		,ELTFIX		//Choix des salaries ayant un EF
		,ELTVAR		//Choix des salaries ayant un EV
		,ELTVAR_FIX	//Choix des salaries ayant un EF ou un EV
		,CONGES		//Choix des salaries ayant un conges
		,UNKNOWN
	}
}
