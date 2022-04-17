package com.kinart.paie.business.services.calcul;

/**
 * Classe permettant de faire des calculs sp�cifiques sur les p�riodes.
 * @author c.mbassi
 *
 */
public class ClsPeriodUtil {
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
	 * Calcule les param�tres de la p�riode lorsqu'elle est en semetre
	 */
	public void semetre(){
//		w_d_per     NUMBER(6,0);
//		   w_f_per     NUMBER(6,0);
//		   w_rg	   NUMBER;
//
//
//		BEGIN
//
//		   p := 6;
//		   w_d_per := w_am;
//		   w_f_per := w_am;
//		   w_rg := rg;
//
//
//		   IF w_rg <= 6 THEN
//		      w_f_per := PA_ALGO.add_per(w_f_per,0,5);
//		   ELSE
//		      w_d_per := PA_ALGO.add_per(w_d_per,0,6);
//		      w_f_per := PA_ALGO.add_per(w_f_per,0,11);
//		   END IF;
//
//		   d_per := w_d_per;
//		   f_per := w_f_per;
//
//		   RETURN TRUE;
		p = 6;
		if(rangMoisPaieExercice <= 6)
			finPeriode = addPer(aammExercice, 0, 5);
		else{
			debutPeriode = addPer(aammExercice, 0, 6);
			finPeriode = addPer(aammExercice, 0, 11);
		}
	}

	/**
	 * Calcule les param�tres de la p�riode lorsqu'elle est en trimestre
	 */
	public void trimestre(){
//		w_d_per     NUMBER(6,0);
//		   w_f_per     NUMBER(6,0);
//		   w_rg	   NUMBER;
//
//		BEGIN
//
//		   p := 3;
//		   w_d_per := w_am;
//		   w_f_per := w_am;
//
//		   w_rg := rg;
//
//		   IF w_rg <= 3 THEN
//		      w_f_per := PA_ALGO.add_per(w_f_per,0,2);
//		   ELSIF w_rg <= 6 THEN
//		      w_d_per := PA_ALGO.add_per(w_d_per,0,3);
//		      w_f_per := PA_ALGO.add_per(w_f_per,0,5);
//		   ELSIF w_rg <= 9 THEN
//		      w_d_per := PA_ALGO.add_per(w_d_per,0,6);
//		      w_f_per := PA_ALGO.add_per(w_f_per,0,8);
//		   ELSE
//		      w_d_per := PA_ALGO.add_per(w_d_per,0,9);
//		      w_f_per := PA_ALGO.add_per(w_f_per,0,11);
//		   END IF;
//
//		   d_per := w_d_per;
//		   f_per := w_f_per;
//
//		   RETURN TRUE;
		p = 3;
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
	 * Calcule les param�tres de la p�riode lorsqu'elle est en ann�e
	 * @param monthOfPay la p�riode de paie
	 */
	public void annee(int monthOfPay){
//		com.cdi.deltarh.service.ClsParameter.println(">>annee");
//		   w_d_per     NUMBER(6,0);
//		   w_f_per     NUMBER(6,0);
//
//		BEGIN
//		   p := 12;
//		   w_d_per := w_am;
//		   w_f_per := w_am;
//
//		   w_f_per := PA_ALGO.add_per(w_f_per,0,11);
//
//		   d_per := w_d_per;
//		   f_per := w_f_per;
//		   RETURN TRUE;
		int dPeriode = monthOfPay;		
		int fPeriode = monthOfPay;
		p = 12;
		fPeriode = addPer(fPeriode, 0, 11);
		
		debutPeriode = dPeriode;
		finPeriode = fPeriode;
	}
	
	public void annee13(int monthOfPay){

		int dPeriode = monthOfPay;		
		int fPeriode = monthOfPay;
		p = 13;
		fPeriode = addPer(fPeriode, 0, 11);
		
		debutPeriode = dPeriode;
		finPeriode = fPeriode;
	}

	/**
	 * full constructor
	 * @param monthOfPay la p�riode de paie
	 * @param regu le numero de r�gularisation
	 */
	public ClsPeriodUtil(int monthOfPay, int regu){
		this.debutPeriode = monthOfPay;
		this.finPeriode = monthOfPay;
		this.p = regu;
	}
	
	/**
	 * default constructor
	 */
	private ClsPeriodUtil(){
		
	}
	
	/**
	 * calcule la periode � partir d'1 periode et d'1 nb annees et/ou mois
	 * @param per nbre de p�riodes
	 * @param nba nbre d'ann�es
	 * @param nbm nbre de mois
	 * @return une p�riode sous forme d'entier
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
	//   per1 := (per - per2) / 100;//le nbre d'ann�es
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
