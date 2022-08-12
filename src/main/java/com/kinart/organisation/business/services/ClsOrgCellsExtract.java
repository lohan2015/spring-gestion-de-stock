package com.kinart.organisation.business.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.organisation.business.vo.ClsLienOrganigrammeVO;
import com.kinart.organisation.business.vo.ClsMessageCelluleVO;
import com.kinart.organisation.business.vo.ClsParametreOrganigrammeVO;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import com.kinart.paie.business.services.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;

/**
 * Classe permettant de dessiner l'organigramme graphique
 * 
 * @author yannick
 * @version 1.0
 */
public class ClsOrgCellsExtract implements ICellsDesigner
{
	private GeneriqueConnexionService service;
	
	//private ClsUserAccess useraccess;

	private List<ClsOrgCellule> allCellules;

	private String cellHeigthString = "height=\"160px\"";

	private String hauteur = "160";

	private String hauteurCelluleFictive = "130";// 170 - 40 + 16

	// private String chainevide="&nbsp;";
	private String chainevide = "";

	private ClsParametreOrganigrammeVO param;

	private ClsMessageCelluleVO message;

	private ClsLienOrganigrammeVO lien;

	/**
	 * Constructeur par d�faut
	 */
	public ClsOrgCellsExtract()
	{

		super();
	}

	/**
	 * Constructeur complet
	 *            couleur des traits de l'organigramme (mis � part celle des cellules)
	 */
	public ClsOrgCellsExtract(GeneriqueConnexionService service, ClsParametreOrganigrammeVO param, ClsMessageCelluleVO message)
	{

		super();

		this.param = param;

		this.message = message;

		this.lien = new ClsLienOrganigrammeVO(this.param, message);

		this.service = service;
		
		//this.useraccess = useraccess;
		
	}

	/**
	 * @return la chaine HTML correspondant � l'organigramme
	 */

	public String _extractData(HttpServletRequest request) throws Exception
	{

		String afficherfictif = param.isAfficherLignesFictive() ? "O" : "N";

		if (ParameterUtil._isStringNull(param.getLongueurLibelle()))
			param.setLongueurLibelle("17");

		String strCouleurTrait = param.getCouleurTrait();

		int intTailleTrait = Integer.valueOf(param.getTailleBordure());

		ClsOrgCellsStyle style = new ClsOrgCellsStyle(param.getCouleurContenuCellule(), strCouleurTrait, intTailleTrait);

		param.setAfficherLignesFictive(StringUtil.equals("O", afficherfictif));

		ClsOrgCellsBuilder _o_Organigrammee = new ClsOrgCellsBuilder(service, param);

		List<ClsOrgCellule> cellules = _o_Organigrammee._getAllCellules();
		if(cellules==null || cellules.size()==0) return StringUtils.EMPTY;
		
		String appRealPath = ParameterUtil.getApplicationContextRealPath(request);
		if (!appRealPath.endsWith(File.separator))
			appRealPath += File.separator;
		String nomFichier = "EXTRAIT_ORGANIGRAMME_" + param.getCelluleDepart() + "_" + param.getNiveauArrive() + "_"
				+ new Date().getTime() + ".XLS";
		appRealPath += nomFichier;
		String appUrlPath = request.getContextPath() + "/" + "pages/genfiles/" + nomFichier;
		File fichier = new File(appRealPath);
		try {
			new File(fichier.getParent()).mkdirs();
			fichier.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		genererFicierExcel(cellules, appRealPath);
		//return appUrlPath;
		return appRealPath;
	}
	
	private void genererFicierExcel(List<ClsOrgCellule> cellules, String appRealPath) throws Exception{
		/* initialiser l'objet Excel */
		int j = 0;
		int i = 1;
		int k = 0;
		HSSFWorkbook xls = new HSSFWorkbook();
		/* cr�er */
		HSSFRow datarow = null;
		HSSFCell cell = null;
		List<HSSFSheet> listeSheets = new ArrayList<HSSFSheet>();
		HSSFSheet sheet = initializeSheet(xls,  k, "Organigramme");
//		System.out.println("TAILLE CELLULE:"+cellules.size());
		
		for(ClsOrgCellule cellule : cellules){
			datarow = sheet.createRow(i);
			
			cell = datarow.createCell(j);
			cell.setCellValue(cellule.getCodeorganigramme());
			j++;
			
			cell = datarow.createCell(j);
			if(i==1) cell.setCellValue(StringUtils.EMPTY);
			else cell.setCellValue(cellule.getCodepere());
			j++;
			
			cell = datarow.createCell(j);
			cell.setCellValue(cellule.getLibelle());
			j++;
			
			cell = datarow.createCell(j);
			cell.setCellValue(cellule.getNomagent());
			j++;
			
			cell = datarow.createCell(j);
			cell.setCellValue(cellule.getMatricule());
			j++;
			
			
			i++;
			j=0;
		}
		
		finalizeSheet(xls, listeSheets, appRealPath);
	}
	
	/**
	 * Initialisation.
	 * 
	 * @param xls
	 * @param k
	 * @return
	 */
	private HSSFSheet initializeSheet(HSSFWorkbook xls, int k, String nomFeuille) {
		HSSFSheet sheet=null;
		if (xls != null && k >= 0) {
			sheet = xls.createSheet(nomFeuille);// + "_" + k);
			/* Style */
			HSSFCellStyle style = xls.createCellStyle();
			 HSSFFont font = xls.createFont();
			 font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			 style.setFont(font);
			 
			/* Insertion de la l�gende */
			int ligne = 0;
			int j = 0;
			/* insertion de l'ent�te des donn�es */
			HSSFRow titlerow = sheet.createRow(ligne);
			HSSFCell cell = titlerow.createCell(j);
			cell.setCellValue("CELLULE");
			cell.setCellStyle(style);
			j++;
			
			HSSFCell cell01 = titlerow.createCell(j);
			cell01.setCellValue("PERE");
			cell01.setCellStyle(style);
			j++;
			
			HSSFCell cell0 = titlerow.createCell(j);
			cell0.setCellValue("FONCTION");
			cell0.setCellStyle(style);
			j++;
			
			HSSFCell cell11 = titlerow.createCell(j);
			cell11.setCellValue("NOM ET PREOM");
			cell11.setCellStyle(style);
			j++;
			
			HSSFCell cell12 = titlerow.createCell(j);
			cell12.setCellValue("MATRICULE");
			cell12.setCellStyle(style);
			j++;
			
		} 				
		
		return sheet;
	}
	
	/**
	 * 
	 * @param xls
	 * @param xlsfileName
	 */
	private void finalizeSheet(HSSFWorkbook xls, List<HSSFSheet> listeSheets, String xlsfileName) throws Exception {
		
		for(HSSFSheet sheet : listeSheets){
			for (int l = 0; l < 7; l++) {
				sheet.autoSizeColumn((short) l);
			}
		}
		
		closeSheet(xls, xlsfileName);
	}
	
	/**
	 * 
	 * @param xls
	 * @param xlsfileName
	 */
	private void closeSheet(HSSFWorkbook xls, String xlsfileName) throws Exception {
		if (xls != null)
			try {
				xls.write(new FileOutputStream(xlsfileName));
			} catch (FileNotFoundException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			}
	}
	

	public GeneriqueConnexionService getService()
	{

		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{

		this.service = service;
	}

	public List<ClsOrgCellule> getAllCellules()
	{

		return allCellules;
	}

	public void setAllCellules(List<ClsOrgCellule> allCellules)
	{

		this.allCellules = allCellules;
	}

	public String _getFlowChart() {
		// TODO Auto-generated method stub
		return null;
	}
}
