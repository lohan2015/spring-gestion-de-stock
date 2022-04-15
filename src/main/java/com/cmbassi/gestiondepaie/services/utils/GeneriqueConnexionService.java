package com.cmbassi.gestiondepaie.services.utils;

import com.cmbassi.gestiondepaie.model.SequenceAuto;
import com.cmbassi.gestiondepaie.repository.SequenceAutoRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class GeneriqueConnexionService {

    public static String CODE_DEFAULT_COMPTEUR = "$SYS_DEFAULT$";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private SequenceAutoRepository sequenceAutoRepository;

    public Session getSession(){
        return entityManager.unwrap(Session.class);
    }

    public void closeSession(Session session){
        SessionFactoryUtils.closeSession(session);
    }

    /**
     *
     * @param query
     * @return
     */
    public List<Object[]> executeSQLQuery(String query) throws Exception{
        Session sess = getSession();
        try {
            return sess.createSQLQuery(query).getResultList();
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        } finally {
            closeSession(sess);
        }
    }

    /**
     *
     * @param query
     * @return
     */
    public List<Object[]> executeNativeSQLQuery(String query) throws Exception{
        Session sess = getSession();
        try {
            return sess.createNativeQuery(query).getResultList();
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        } finally {
            closeSession(sess);
        }
    }

    public int updateFromTable ( String queryString ) throws SQLException {
        int result=0;

        Session session = this.getSession();
        Transaction tx = null;

        try
        {
            tx = session.beginTransaction();

            result = session.createSQLQuery(queryString).executeUpdate();

            tx.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (tx != null) tx.rollback();
            throw e;
        }
        finally
        {
            this.closeSession(session);
        }

        return result;

    }

    // ==========================FONCTION DE GESTION DES COMPTEURS====================
    /**
     * @param sequenceauto
     * @return la valeur du prochain incrément
     */
    /**
     * @param codeCompteur
     * @return
     */
    public String getProchainIncrement (String codeCompteur ) throws Exception{
        SequenceAuto sequence = sequenceAutoRepository.findByCodeSequence(codeCompteur); // this.get(compteurPK,LockMode.UPGRADE);
        // rhpcompteurService.lock(rhpcompteur, LockMode.UPGRADE_NOWAIT);
        String incrementCourant,strProchainIncrement = null;
        if(sequence!=null){
            incrementCourant = sequence.getProchainincrement ( );
            int intProchainIncrement = Integer.parseInt ( incrementCourant ) + Integer.parseInt ( sequence.getLongueurincrement ( ) );
            strProchainIncrement = String.valueOf ( intProchainIncrement );

        }
        else{
            //Compteur inexistant alors incrémentation par défaut...
            SequenceAuto defaultCompteur = getDefaultCompteur();
            if(defaultCompteur!=null){
                strProchainIncrement = getProchainIncrement(defaultCompteur.getCodecompteur());
            }else{
                strProchainIncrement = null;
                throw new Exception("La sequence par défaut "+CODE_DEFAULT_COMPTEUR+"n'est pas définie");
            }

        }

        return strProchainIncrement;
    }


    public void incrementerCompteur (String codeCompteur ) throws Exception{
        SequenceAuto rhpcompteur = sequenceAutoRepository.findByCodeSequence(codeCompteur);// this.get(compteurPK,LockMode.UPGRADE);
        // rhpcompteurService.lock(rhpcompteur, LockMode.UPGRADE_NOWAIT);
        if(rhpcompteur!=null){
            String incrementCourant = rhpcompteur.getProchainincrement ( );
            int intProchainIncrement = Integer.parseInt ( incrementCourant ) + Integer.parseInt ( rhpcompteur.getLongueurincrement ( ) );
            String strProchainIncrement = String.valueOf ( intProchainIncrement );
            strProchainIncrement = this.completerIncrement ( rhpcompteur.getIncrementinitial ( ).length ( ) - strProchainIncrement.length ( ) ) + strProchainIncrement;
            rhpcompteur.setProchainincrement ( strProchainIncrement );
            sequenceAutoRepository.save ( rhpcompteur );
        }
        else{
            //Compteur inexistant alors incrémentation par défaut...
            SequenceAuto defaultCompteur = getDefaultCompteur();
            if(defaultCompteur!=null){
                incrementerCompteur(defaultCompteur.getCodecompteur());
            }else{
                throw new Exception("La sequence par défaut "+CODE_DEFAULT_COMPTEUR+"n'est pas définie");
            }

        }
        // rhpcompteurService.lock(rhpcompteur, LockMode.NONE);
    }



    /**
     * @param longueur
     * @return
     */
    private String completerIncrement ( int longueur ) {
        String val = "";
        if (longueur > 0) {
            for (int i = 0; i < longueur; i ++ ) {
                val += "0";
            }
        }
        return val;
    }



    /**
     * @param sequenceauto
     * @return la valeur du prochain incrément
     */
    /**
     * @param codeDossier
     * @param codeCompteur
     * @return
     */
    public String decrementerCompteur ( String codeDossier , String codeCompteur )throws Exception {
        String strProchainIncrement = null;
         SequenceAuto sequence = sequenceAutoRepository.findByCodeSequence(codeCompteur);
            String incrementCourant = sequence.getProchainincrement ( );
            int intProchainIncrement = Integer.parseInt ( incrementCourant ) - Integer.parseInt ( sequence.getLongueurincrement ( ) );
            strProchainIncrement = String.valueOf ( intProchainIncrement );

        return strProchainIncrement;
    }
    public SequenceAuto getDefaultCompteur(){
        return sequenceAutoRepository.findByCodeSequence(CODE_DEFAULT_COMPTEUR);

    }

    /**
     * @param identreprise
     * @param codeCompteur
     * @return
     */
    public String soucheCompteur ( Integer identreprise, String codeCompteur ) {
        SequenceAuto rhpcompteur = sequenceAutoRepository.findByCodeSequence(codeCompteur);

        String souche = "";
        String [ ] tbSouche = new String [ ] {
                "" , "" , "" , ""
        };
        int intLongueurSouche = 0;
        String ajouterSouche = rhpcompteur.getAjoutersouche ( );
        if ("1".equals ( ajouterSouche )) {

            if ("1".equals ( rhpcompteur.getAjouterdossier ( ) )) {
                intLongueurSouche += 1;
                int ordreDossier = Integer.parseInt ( rhpcompteur.getOrdredossier ( ) );
                tbSouche [ ordreDossier - 1 ] = identreprise.toString();
            }
            if ("1".equals ( rhpcompteur.getAjouterannee ( ) )) {
                intLongueurSouche += 1;
                int ordreAnnee = Integer.parseInt ( rhpcompteur.getOrdreannee ( ) );
                int longueurAnnee = Integer.parseInt ( rhpcompteur.getLongueurannee ( ) );

                String annee = longueurAnnee == 2 ? this.getCurrentDate ( ParameterUtil.YEAR_ON_2_CHARACTER ) : this.getCurrentDate ( ParameterUtil.YEAR_ON_4_CHARACTER );
                tbSouche [ ordreAnnee - 1 ] = annee;
            }

            if ("1".equals ( rhpcompteur.getAjoutermois ( ) )) {
                intLongueurSouche += 1;
                int ordreMois = Integer.parseInt ( rhpcompteur.getOrdremois ( ) );
                String mois = this.getCurrentDate ( ParameterUtil.MONTH_AS_NUMBER );
                tbSouche [ ordreMois - 1 ] = mois;
            }

            if ("1".equals ( rhpcompteur.getAjouterjour ( ) )) {
                intLongueurSouche += 1;
                int ordreJour = Integer.parseInt ( rhpcompteur.getOrdrejour ( ) );
                String jour = this.getCurrentDate ( ParameterUtil.DAY_AS_NUMBER );
                tbSouche [ ordreJour - 1 ] = jour;
            }

            if ("1".equals ( rhpcompteur.getAjouterautre ( ) )) {
                intLongueurSouche += 1;
                int ordreAutre = Integer.parseInt ( rhpcompteur.getOrdreautre ( ) );
                String autre = rhpcompteur.getValeurautre ( );
                if (autre == null) {
                    autre = "";
                }
                tbSouche [ ordreAutre - 1 ] = autre;
            }
        }
        for (int i = 0; i < intLongueurSouche; i ++ ) {
            souche += tbSouche [ i ];
        }
        // this.update(rhpcompteur);
        return souche;
    }

    /*
     * @param type
	 * @return
             */
    public String getCurrentDate ( int type ) {

        DateFormat dateFormat;
        String date = "";

        switch (type) {
            case ParameterUtil.YEAR_ON_4_CHARACTER:
                dateFormat = new SimpleDateFormat( "yyyy" );
                date = dateFormat.format ( new Date( ) );
                break;

            case ParameterUtil.YEAR_ON_2_CHARACTER:
                dateFormat = new SimpleDateFormat ( "yy" );
                date = dateFormat.format ( new Date ( ) );
                break;

            case ParameterUtil.MONTH_AS_NUMBER:
                dateFormat = new SimpleDateFormat ( "MM" );
                date = dateFormat.format ( new Date ( ) );
                break;

            case ParameterUtil.DAY_AS_NUMBER:
                dateFormat = new SimpleDateFormat ( "dd" );
                date = dateFormat.format ( new Date ( ) );
                break;
        }
        return date;
    }


}
