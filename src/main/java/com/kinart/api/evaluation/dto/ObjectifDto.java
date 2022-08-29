package com.kinart.api.evaluation.dto;

import java.util.ArrayList;
import java.util.List;

import com.kinart.api.organisation.dto.ElementDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author c.mbassi
 *
 */
@Data
@Builder
@AllArgsConstructor
@ApiModel(description = "Model des objectifs")
public class ObjectifDto {
	private int identreprise;
	private String codeCycle;
	private String libCycle;
	private String codeProc;
	private String libProc;
	private int numProc;
	private int numSsProc;
	private String codeSsProc;
	private String libSsProc;
	private String codeOperation;
	private int numOperation;
	private String libOperation;
	private String qui;
	private String quand;
	private String tache1;
	private String tache2;
	private String tache3;
	private String tache4;
	private String tache5;
	private String tache6;
	private String tache7;
	private String tache8;
	private String tache9;
	private String tache10;
	private List<ElementDto> semestres = new ArrayList<ElementDto>();
	List<ElementDto> listeTypes1 = new ArrayList<ElementDto>();
	List<ElementDto> listeTypes2 = new ArrayList<ElementDto>();
	List<ElementDto> listeTypes3 = new ArrayList<ElementDto>();
	private String type1;
	private String type2;
	private String type3;
	private String user;
	
	public ObjectifDto(){
		semestres.add(new ElementDto("1","Mensuel"));
		semestres.add(new ElementDto("2","Trimestre 1"));
		semestres.add(new ElementDto("3","Trimestre 2"));
		semestres.add(new ElementDto("4","Semestre 1"));
		semestres.add(new ElementDto("5","Semestre 2"));
		semestres.add(new ElementDto("6","Annuel"));
		
		listeTypes1.clear();
		listeTypes1.add(new ElementDto("SAL", "Salarié"));
		listeTypes1.add(new ElementDto("AGE", "Agence"));//ctab=274
		listeTypes1.add(new ElementDto("DIR", "Direction"));//ctab=1
		listeTypes1.add(new ElementDto("DEP", "Division"));//ctab=2
		listeTypes1.add(new ElementDto("SERV", "Service"));//ctab=3
		listeTypes1.add(new ElementDto("FCT", "Fonction"));//ctab=7
		listeTypes1.add(new ElementDto("CLS", "Classe"));//ctab=8
		listeTypes1.add(new ElementDto("STAT", "Type employe"));//ctab=6
		listeTypes1.add(new ElementDto("SEX", "Sexe"));//2015
		listeTypes1.add(new ElementDto("CAT", "Categorie"));//132
		listeTypes1.add(new ElementDto("ECH", "Echelon"));//133
		listeTypes1.add(new ElementDto("BRP", "Groupe de paie"));//20
		listeTypes1.add(new ElementDto("ZCT", "Zone categorielle"));//31
		listeTypes1.add(new ElementDto("TPC", "Type de contrats"));//9
		listeTypes1.add(new ElementDto("STA", "Statut"));//13
		listeTypes1.add(new ElementDto("POST", "Poste"));
		listeTypes1.add(new ElementDto("EMPT", "Métier"));
		
		listeTypes2.clear();
		listeTypes2.add(new ElementDto("SAL", "Salarié"));
		listeTypes2.add(new ElementDto("AGE", "Agence"));//ctab=274
		listeTypes2.add(new ElementDto("DIR", "Direction"));//ctab=1
		listeTypes2.add(new ElementDto("DEP", "Division"));//ctab=2
		listeTypes2.add(new ElementDto("SERV", "Service"));//ctab=3
		listeTypes2.add(new ElementDto("FCT", "Fonction"));//ctab=7
		listeTypes2.add(new ElementDto("CLS", "Classe"));//ctab=8
		listeTypes2.add(new ElementDto("STAT", "Type employe"));//ctab=6
		listeTypes2.add(new ElementDto("SEX", "Sexe"));//2015
		listeTypes2.add(new ElementDto("CAT", "Categorie"));//132
		listeTypes2.add(new ElementDto("ECH", "Echelon"));//133
		listeTypes2.add(new ElementDto("BRP", "Groupe de paie"));//20
		listeTypes2.add(new ElementDto("ZCT", "Zone categorielle"));//31
		listeTypes2.add(new ElementDto("TPC", "Type de contrats"));//9
		listeTypes2.add(new ElementDto("STA", "Statut"));//13
		listeTypes2.add(new ElementDto("POST", "Poste"));
		listeTypes2.add(new ElementDto("EMPT", "Métier"));
		
		listeTypes3.clear();
		listeTypes3.add(new ElementDto("SAL", "Salarié"));
		listeTypes3.add(new ElementDto("AGE", "Agence"));//ctab=274
		listeTypes3.add(new ElementDto("DIR", "Direction"));//ctab=1
		listeTypes3.add(new ElementDto("DEP", "Division"));//ctab=2
		listeTypes3.add(new ElementDto("SERV", "Service"));//ctab=3
		listeTypes3.add(new ElementDto("FCT", "Fonction"));//ctab=7
		listeTypes3.add(new ElementDto("CLS", "Classe"));//ctab=8
		listeTypes3.add(new ElementDto("STAT", "Type employe"));//ctab=6
		listeTypes3.add(new ElementDto("SEX", "Sexe"));//2015
		listeTypes3.add(new ElementDto("CAT", "Categorie"));//132
		listeTypes3.add(new ElementDto("ECH", "Echelon"));//133
		listeTypes3.add(new ElementDto("BRP", "Groupe de paie"));//20
		listeTypes3.add(new ElementDto("ZCT", "Zone categorielle"));//31
		listeTypes3.add(new ElementDto("TPC", "Type de contrats"));//9
		listeTypes3.add(new ElementDto("STA", "Statut"));//13
		listeTypes3.add(new ElementDto("POST", "Poste"));
		listeTypes3.add(new ElementDto("EMPT", "Métier"));
	}
}
