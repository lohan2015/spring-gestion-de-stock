package com.kinart.paie.business.services.calcul;

/**
 * Classe permettant de faire des calculs spècifiques sur les pèriodes.
 * @author c.mbassi
 *
 */
public class ClsFictifPeriodUtil {
	private int debutPeriode = 0;
	private int finPeriode = 0;
	private int p = 0;
	private int nbreDeMois = 0;
	private int anneeExercice = 0;
	private int moisExercice = 0;
	private int aammExercice = 0;//w_am
	private int rangMoisPaieExercice = 0;//rg
	
	public synchronized int getAammExercice() {
		return aammExercice;
	}
	public synchronized void setAammExercice(int aammExercice) {
		this.aammExercice = aammExercice;
	}
	public synchronized int getAnneeExercice() {
		return anneeExercice;
	}
	public synchronized void setAnneeExercice(int anneeExercice) {
		this.anneeExercice = anneeExercice;
	}
	public synchronized int getMoisExercice() {
		return moisExercice;
	}
	public synchronized void setMoisExercice(int moisExercice) {
		this.moisExercice = moisExercice;
	}
	public int getNbreDeMois() {
		return nbreDeMois;
	}
	public void setNbreDeMois(int nbreDeMois) {
		this.nbreDeMois = nbreDeMois;
	}
	public int getDebutPeriode() {
		return debutPeriode;
	}
	public void setDebutPeriode(int debutPeriode) {
		this.debutPeriode = debutPeriode;
	}
	public int getFinPeriode() {
		return finPeriode;
	}
	public void setFinPeriode(int finPeriode) {
		this.finPeriode = finPeriode;
	}
	public int getP() {
		return p;
	}
	public void setP(int p) {
		this.p = p;
	}


	/**
	 * Calcule les paramètres de la pèriode lorsqu'elle est en semetre
	 */
	public void semetre(){
//		p := 6;
//	   d_per := w_am;
//	   f_per := w_am;
//
//	   IF rg <= 6 THEN
//	      f_per := PA_ALGFIC.add_per(f_per,0,5);
//	   ELSE
//	      d_per := PA_ALGFIC.add_per(d_per,0,6);
//	      f_per := PA_ALGFIC.add_per(f_per,0,11);
//	   END IF;
//
//	   RETURN TRUE;
		debutPeriode = aammExercice;
		finPeriode = aammExercice;
		p = 6;
		if(rangMoisPaieExercice <= 6)
			finPeriode = addPer(aammExercice, 0, 5);
		else{
			debutPeriode = addPer(aammExercice, 0, 6);
			finPeriode = addPer(aammExercice, 0, 11);
		}
	}

	/**
	 * Calcule les paramètres de la pèriode lorsqu'elle est en trimestre
	 */
	public void trimestre(){
//		 p := 3;
//	   d_per := w_am;
//	   f_per := w_am;
//
//	   IF rg <= 3 THEN
//	      f_per := PA_ALGFIC.add_per(f_per,0,2);
//	   ELSIF rg <= 6 THEN
//	      d_per := PA_ALGFIC.add_per(d_per,0,3);
//	      f_per := PA_ALGFIC.add_per(f_per,0,5);
//	   ELSIF rg <= 9 THEN
//	      d_per := PA_ALGFIC.add_per(d_per,0,6);
//	      f_per := PA_ALGFIC.add_per(f_per,0,8);
//	   ELSE
//	      d_per := PA_ALGFIC.add_per(d_per,0,9);
//	      f_per := PA_ALGFIC.add_per(f_per,0,11);
//	   END IF;
//
//	   RETURN TRUE;
		p = 3;
		debutPeriode = aammExercice;
		finPeriode = aammExercice;
		if(rangMoisPaieExercice <= 3)
			finPeriode = addPer(aammExercice,0,2);
		else if(rangMoisPaieExercice <= 6){
			debutPeriode = addPer(aammExercice,0,3);
			finPeriode = addPer(aammExercice,0,5);
		}
		else if(rangMoisPaieExercice <= 9){
			debutPeriode = addPer(aammExercice,0,6);
			finPeriode = addPer(aammExercice,0,8);
		}		
		else{
			debutPeriode = addPer(aammExercice,0,9);
			finPeriode = addPer(aammExercice,0,11);
		}
	}

	/**
	 * Calcule les paramètres de la pèriode lorsqu'elle est en annèe
	 */
	public void annee(){
//		 p := 12;
//	   d_per := w_am;
//	   f_per := w_am;
//	   f_per := PA_ALGFIC.add_per(f_per,0,11);
//
//	   RETURN TRUE;
		 debutPeriode = aammExercice;		
		finPeriode = aammExercice;
		p = 12;
		finPeriode = addPer(finPeriode, 0, 11);
	}

	/**
	 * full constructor
	 * @param monthOfPay la pèriode de paie
	 * @param regu le numero de règularisation
	 */
	public ClsFictifPeriodUtil(int monthOfPay, int regu){
		this.debutPeriode = monthOfPay;
		this.finPeriode = monthOfPay;
		this.p = regu;
	}
	
	/**
	 * default constructor
	 */
	private ClsFictifPeriodUtil(){
		
	}
	
	/**
	 * calcule la periode è partir d'1 periode et d'1 nb annees et/ou mois
	 * @param per nbre de pèriodes
	 * @param nba nbre d'annèes
	 * @param nbm nbre de mois
	 * @return une pèriode sous forme d'entier
	 */
	public int addPer(int per, int nba, int nbm){
	//   per1  NUMBER(4,0);
	//   per2  NUMBER(2,0);
	//   w_per NUMBER(6,0);
		int w_per = 0;
	//
	//BEGIN
	//   per2 := MOD(per,100);
		int per2 = per % 100;//le nombre de mois
	//   per1 := (per - per2) / 100;//le nbre d'annèes
		int per1 = (per - per2) / 100;
	//   per1 := per1 + nba;
		per1 = per1 + nba; 
	//   per2 := per2 + nbm;
		per2 = per2 + nbm;
	//
	//   WHILE per2 < 1 OR per2 > 12 LOOP
		while(per2 < 1 || per2 > 12){
	//      IF per2 > 12 THEN
			if(per2 > 12){
	//         per1 := per1 + 1;
				per1 = per1 + 1;
	//         per2 := per2 - 12;
				per2 = per2 - 12;
			}
	//      END IF;
	//      IF per2 < 1 THEN
			if(per2 < 1){
	//         per1 := per1 - 1;
				per1 = per1 - 1;
	//         per2 := per2 + 12;
				per2 = per2 + 12;
			}
	//      END IF;
	//   END LOOP;
		}
	//
	//   w_per := per1 * 100 + per2;
		w_per = per1 * 100 + per2;
	//
	//   RETURN w_per;
		return w_per;
	//
	//END add_per;
	}
	public int getRangMoisPaieExercice() {
		return rangMoisPaieExercice;
	}
	public void setRangMoisPaieExercice(int rangMoisPaieExercice) {
		this.rangMoisPaieExercice = rangMoisPaieExercice;
	}
}
