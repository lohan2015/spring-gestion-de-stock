package com.kinart.paie.business.services.calcul;

import org.apache.commons.lang3.StringUtils;

public class ClsChoixAlgorithme
{

	public static IAlgorithm choixAlgorithm(ClsSalaryToProcess salary, String classeAlgorithm)
	{
		if (StringUtils.isBlank(classeAlgorithm))
			return new ClsAlgorithm(salary);

		if (StringUtils.equals("TOUT", classeAlgorithm))
			return new ClsAlgorithm(salary);
		
		if (StringUtils.equals("CNSS", classeAlgorithm))
			return new ClsAlgorithm(salary);

		return new ClsAlgorithm(salary);
	}

	public static IFictifAlgorithm choixFictifAlgorithm(ClsFictifSalaryToProcess salary, String classeAlgorithm)
	{
		if (StringUtils.isBlank(classeAlgorithm))
			return new ClsFictifAlgorithm(salary);

		if (StringUtils.equals("TOUT", classeAlgorithm))
			return new ClsFictifAlgorithm(salary);
		
		if (StringUtils.equals("CNSS", classeAlgorithm))
			return new ClsFictifAlgorithm(salary);

		return new ClsFictifAlgorithm(salary);
	}
}
