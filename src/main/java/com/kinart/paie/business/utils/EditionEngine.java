/**
 * 
 */
package com.kinart.paie.business.utils;

import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

/**
 * @author c.mbassi
 *
 */
public class EditionEngine {

	private String printPath;
	private String imprimante;
	
	public void generateFile(List<ClsGenericPrintObject> donnees){
		File fichier = new File(printPath);
		PrintWriter printWriter = null;
		StringBuffer ligne = new StringBuffer();
		try {
			if (fichier.exists())fichier.delete();
			GeneriqueConnexionService._createFileFolder(printPath, "\\");
			fichier.createNewFile();
			printWriter = new PrintWriter(new FileWriter(fichier));
			for (Iterator iterator = donnees.iterator(); iterator.hasNext();) {
				ClsGenericPrintObject item = (ClsGenericPrintObject) iterator
						.next();
				for(int x=0;x<item.getInfos().length;x++){
					ligne.delete(0, ligne.length());
					//Ecrire la ligne
						for(int y=0;y<item.getInfos()[x].length;y++){
							if(item.getPosition()[x][y]>ligne.length())
								ligne.append(StringUtil.lpad(" ", item.getPosition()[x][y]-ligne.length()));
								if(!StringUtil.isBlank(item.getInfos()[x][y]))ligne.append(item.getInfos()[x][y]);
						}
					//print and go to line
					printWriter.println(ligne);				
				}
			}			
			printWriter.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			printWriter.close();
		}
	}
	public void printData()
	{

		printPath = printPath.replaceAll("/", "\\\\\\\\");
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
 	   	//PrintService[] printService = PrintServiceLookup.lookupPrintServices(flavor, pras);
		PrintService[] printService = PrintServiceLookup.lookupPrintServices(null, null);
		int i = 0;
 	   	if (printService.length > 0) {
    	   
    	   //On calcule l'indice de l'imprimante s�lectionn�es
 		  i = getIndexPrinter(imprimante, printService);
 	      System.out.println("L'imprimante s�lectionn�e se trouve � la position : >>>" + i);  
 	      System.out.println("Le nom de l'imprimante est : >>>" + printService[i].getName()); 
          DocPrintJob job = printService[i].createPrintJob();
          FileInputStream fis;
          try {
              fis = new FileInputStream(printPath);
              DocAttributeSet das = new HashDocAttributeSet();
              Doc doc = new SimpleDoc(fis, flavor, das);
              try {
                  job.print(doc, pras);                   
              } catch (PrintException ex) {
                  ex.printStackTrace();
              }

           } catch (FileNotFoundException ex) {
               ex.printStackTrace();
           }
       }     
	}
	public int getIndexPrinter(String imprimante,  PrintService[] serviceImpression)
	{
		int i = 0;
		   while(!imprimante.equalsIgnoreCase(serviceImpression[i].getName()))
		   {
			   i++;
		   }
		   return i;
	}
	public String getPrintPath() {
		return printPath;
	}
	public void setPrintPath(String printPath) {
		this.printPath = printPath;
	}
	public String getImprimante() {
		return imprimante;
	}
	public void setImprimante(String imprimante) {
		this.imprimante = imprimante;
	}
	
}
