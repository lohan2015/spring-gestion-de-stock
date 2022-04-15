package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.PretExterneDetail;
import com.cmbassi.gestiondepaie.services.utils.GeneriqueConnexionService;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class PretDto implements Serializable {
    public String montant = "";
    public String interet = "";
    public String taxe = "";
    public String amortissement = "";
    public String resteARembourser = "";

    public BigDecimal bgMontant = new BigDecimal("0.0");
    public BigDecimal bgInteret = new BigDecimal("0.0");
    public BigDecimal bgTaxe = new BigDecimal("0.0");
    public BigDecimal bgAmortissement = new BigDecimal("0.0");
    public BigDecimal bgResteARembourser = new BigDecimal("0.0");

    public PretExterneEnteteDto entetepret = null;



    private List<PretExterneDetail> listeEcheancierDB = null;

    @Autowired
    GeneriqueConnexionService service;

    //-------------------------------------------------------------------------/

    public TypePretDto NATURE_PRET;
    public BigDecimal TauxInteret;
    public BigDecimal TauxTaxe;
    public List<Integer> listeMoisNRemb;
    public List<LigneEcheancierDto> listeEcheancier = new ArrayList<LigneEcheancierDto>();
    public BigDecimal MontantEcheance = new BigDecimal(0);
    public BigDecimal ResteInitial;
    public BigDecimal ValeurInteret = new BigDecimal(0);
    public BigDecimal ValeurTaxeSurInteret  = new BigDecimal(0);
    public BigDecimal MontantPret = null;
    public Integer NombreEcheance = null;
    public Date DatePremiereEcheance = null;
    public Date DateProchaineEcheance;
    public BigDecimal ResteArembourser = null;
    public String IN_FINE ="IN_FINE";
    public String AMORTISSEMENT_CONSTANT = "AM_CONS";
    public String ECHEANCE_CONSTANTE= "ECH_CONS";

    public String TypeModification= "RAUTO";
    public List<PretExterneDetail> ancienneslignes=  new ArrayList<PretExterneDetail>();
    public List<PretExterneDetail> listeEcheanciermodification = new ArrayList<PretExterneDetail>();
    public BigDecimal PremiereEcho;
    public BigDecimal PremiereEchr;
    public Integer NbLignesEcheancier;
    public BigDecimal DeniereEchr;
    public BigDecimal NOUVELLE_ECHR;
    public Integer NUM_LIGNE;
    public Integer NUM_NOUVELLE_LIGNE;
    public BigDecimal MntARepartir;
    public Integer TempCurseur;
    public Date PeriodeCourante;
    public BigDecimal Ecart;

    public PretDto() {
    }

    public PretDto(String montant, String interet, String taxe, String amortissement, String resteARembourser, BigDecimal bgMontant, BigDecimal bgInteret, BigDecimal bgTaxe, BigDecimal bgAmortissement, BigDecimal bgResteARembourser, PretExterneEnteteDto entetepret, List<PretExterneDetail> listeEcheancierDB, GeneriqueConnexionService service, TypePretDto NATURE_PRET, BigDecimal tauxInteret, BigDecimal tauxTaxe, List<Integer> listeMoisNRemb, List<LigneEcheancierDto> listeEcheancier, BigDecimal montantEcheance, BigDecimal resteInitial, BigDecimal valeurInteret, BigDecimal valeurTaxeSurInteret, BigDecimal montantPret, Integer nombreEcheance, Date datePremiereEcheance, Date dateProchaineEcheance, BigDecimal resteArembourser, String IN_FINE, String AMORTISSEMENT_CONSTANT, String ECHEANCE_CONSTANTE, String typeModification, List<PretExterneDetail> ancienneslignes, List<PretExterneDetail> listeEcheanciermodification, BigDecimal premiereEcho, BigDecimal premiereEchr, Integer nbLignesEcheancier, BigDecimal deniereEchr, BigDecimal NOUVELLE_ECHR, Integer NUM_LIGNE, Integer NUM_NOUVELLE_LIGNE, BigDecimal mntARepartir, Integer tempCurseur, Date periodeCourante, BigDecimal ecart) {
        this.montant = montant;
        this.interet = interet;
        this.taxe = taxe;
        this.amortissement = amortissement;
        this.resteARembourser = resteARembourser;
        this.bgMontant = bgMontant;
        this.bgInteret = bgInteret;
        this.bgTaxe = bgTaxe;
        this.bgAmortissement = bgAmortissement;
        this.bgResteARembourser = bgResteARembourser;
        this.entetepret = entetepret;
        this.listeEcheancierDB = listeEcheancierDB;
        this.service = service;
        this.NATURE_PRET = NATURE_PRET;
        TauxInteret = tauxInteret;
        TauxTaxe = tauxTaxe;
        this.listeMoisNRemb = listeMoisNRemb;
        this.listeEcheancier = listeEcheancier;
        MontantEcheance = montantEcheance;
        ResteInitial = resteInitial;
        ValeurInteret = valeurInteret;
        ValeurTaxeSurInteret = valeurTaxeSurInteret;
        MontantPret = montantPret;
        NombreEcheance = nombreEcheance;
        DatePremiereEcheance = datePremiereEcheance;
        DateProchaineEcheance = dateProchaineEcheance;
        ResteArembourser = resteArembourser;
        this.IN_FINE = IN_FINE;
        this.AMORTISSEMENT_CONSTANT = AMORTISSEMENT_CONSTANT;
        this.ECHEANCE_CONSTANTE = ECHEANCE_CONSTANTE;
        TypeModification = typeModification;
        this.ancienneslignes = ancienneslignes;
        this.listeEcheanciermodification = listeEcheanciermodification;
        PremiereEcho = premiereEcho;
        PremiereEchr = premiereEchr;
        NbLignesEcheancier = nbLignesEcheancier;
        DeniereEchr = deniereEchr;
        this.NOUVELLE_ECHR = NOUVELLE_ECHR;
        this.NUM_LIGNE = NUM_LIGNE;
        this.NUM_NOUVELLE_LIGNE = NUM_NOUVELLE_LIGNE;
        MntARepartir = mntARepartir;
        TempCurseur = tempCurseur;
        PeriodeCourante = periodeCourante;
        Ecart = ecart;
    }
}
