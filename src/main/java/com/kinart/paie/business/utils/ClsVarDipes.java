package com.kinart.paie.business.utils;

import java.math.BigDecimal;

public class ClsVarDipes
{
	BigDecimal nbj = new BigDecimal(0);
	BigDecimal salBrut = new BigDecimal(0);
	BigDecimal elExcep = new BigDecimal(0);
	BigDecimal salTax = new BigDecimal(0);
	BigDecimal totCnp = new BigDecimal(0);
	BigDecimal plafCnp = new BigDecimal(0);
	BigDecimal retTax = new BigDecimal(0);
	BigDecimal retSurt = new BigDecimal(0);
	BigDecimal centAdd = new BigDecimal(0);
	BigDecimal retTxco = new BigDecimal(0);
	BigDecimal cacSurtProg = new BigDecimal(0);
	BigDecimal cacTaxProp = new BigDecimal(0);
	BigDecimal pensViel = new BigDecimal(0);
	BigDecimal prestFam = new BigDecimal(0);
	BigDecimal accTrav = new BigDecimal(0);
	
	public String localToStrin()
	{
		
		String var ="Nbj = "+ nbj.toString()+" , SalBrut = "+salBrut+" , elExcep = "+elExcep+" , saltax = "+salTax+" , totcnp = "+totCnp+" , plafcnp = "+plafCnp+" , rettax = "+retTax+" retsurt = "+retSurt;
		var+= " , centAdd = "+centAdd+" , rettxco = "+retTxco+" , cacsurtprog = "+cacSurtProg+" , cactaxprop = "+cacTaxProp+" , pensviel = "+pensViel+" , prestfam = "+prestFam+" , acctrav = "+accTrav;
		return var;
	}
}
