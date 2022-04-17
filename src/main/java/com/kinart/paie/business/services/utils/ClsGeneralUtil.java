package com.kinart.paie.business.services.utils;

public class ClsGeneralUtil {

	int _d_per = 0;
	int _f_per = 0;
	int _p = 0;
	int _nbm = 0;
/**
	 * @return the _d_per
	 */
	public int get_d_per() {
		return _d_per;
	}

	/**
	 * @param _d_per the _d_per to set
	 */
	public void set_d_per(int _d_per) {
		this._d_per = _d_per;
	}

	/**
	 * @return the _f_per
	 */
	public int get_f_per() {
		return _f_per;
	}

	/**
	 * @param _f_per the _f_per to set
	 */
	public void set_f_per(int _f_per) {
		this._f_per = _f_per;
	}

	/**
	 * @return the _nbm
	 */
	public int get_nbm() {
		return _nbm;
	}

	/**
	 * @param _nbm the _nbm to set
	 */
	public void set_nbm(int _nbm) {
		this._nbm = _nbm;
	}

	/**
	 * @return the _p
	 */
	public int get_p() {
		return _p;
	}

	/**
	 * @param _p the _p to set
	 */
	public void set_p(int _p) {
		this._p = _p;
	}

//	-------------------------------------------------------------------------------
//	-- calcul periode a partir d'1 periode et d'1 nb annees et/ou mois
//	-------------------------------------------------------------------------------
	public static int addPer(int per, int nba, int nbm){
//	   per1  NUMBER(4,0);
//	   per2  NUMBER(2,0);
//	   w_per NUMBER(6,0);
		int w_per = -1;
//
//	BEGIN
//	   per2 := MOD(per,100);
		int per2 = per % 100;
//	   per1 := (per - per2) / 100;
		int per1 = (per - per2) / 100;
//	   per1 := per1 + nba;
		per1 = per1 + nba; 
//	   per2 := per2 + nbm;
		per2 = per2 + nbm;
//
//	   WHILE per2 < 1 OR per2 > 12 LOOP
		while(per2 < 1 || per2 > 12){
//	      IF per2 > 12 THEN
			if(per2 > 12){
//	         per1 := per1 + 1;
				per1 = per1 + 1;
//	         per2 := per2 - 12;
				per2 = per2 - 12;
			}
//	      END IF;
//	      IF per2 < 1 THEN
			if(per2 < 1){
//	         per1 := per1 - 1;
				per1 = per1 - 1;
//	         per2 := per2 + 12;
				per2 = per2 + 12;
			}
//	      END IF;
//	   END LOOP;
		}
//
//	   w_per := per1 * 100 + per2;
		w_per = per1 * 100 + per2;
//
//	   RETURN w_per;
		return w_per;
//
//	END add_per;
	}

	//modifie les champs de la classe en fonction de <w_am>
	public void annee(int w_am){
		
		int w_d_per = -1;		
		int w_f_per = -1;
		w_d_per = w_am ;
		w_f_per = w_am;
		w_f_per = ClsGeneralUtil.addPer(w_f_per, 0, 11);
		
		_d_per = w_d_per;
		_f_per = w_f_per;
		_nbm = 12;
		_p = 12;
	}
	
//	CREATE OR REPLACE FUNCTION semes (w_am IN VARCHAR2,rg IN NUMBER,d_per OUT VARCHAR2,f_per OUT VARCHAR2,p OUT NUMBER) RETURN BOOLEAN
//	IS
//
	public void semes(String w_am, int rg){
//	   w_d_per     NUMBER(6,0);
//	   w_f_per     NUMBER(6,0);
//	   w_rg	   NUMBER;
//
//
//	BEGIN
//
//	   p := 6;
		_p = 6;
//	   w_d_per := w_am;
//	   w_f_per := w_am;
//	   w_rg := rg;
//
//
//	   IF w_rg <= 6 THEN
		if(rg <= 6)
//	      w_f_per := PA_ALGO.add_per(w_f_per,0,5);
			_f_per = ClsGeneralUtil.addPer(_f_per, 0, 5);
//	   ELSE
		else{
//	      w_d_per := PA_ALGO.add_per(w_d_per,0,6);
			_d_per = ClsGeneralUtil.addPer(_d_per, 0, 6);
//	      w_f_per := PA_ALGO.add_per(w_f_per,0,11);
			_f_per = ClsGeneralUtil.addPer(_f_per, 0, 11);
		}
//	   END IF;
//
//	   d_per := w_d_per;
//	   f_per := w_f_per;
//
//	   RETURN TRUE;
//
//	END semes;
//	/
	}

	public void trime(String w_am, int rg){
	}
}
