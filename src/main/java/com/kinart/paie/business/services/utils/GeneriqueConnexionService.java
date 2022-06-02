package com.kinart.paie.business.services.utils;

import com.kinart.paie.business.model.InterfComptable;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.SequenceAuto;
import com.kinart.paie.business.repository.SequenceAutoRepository;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

    public List find(String query) throws DataAccessException {
        Session sess = getSession();
        try {
            org.hibernate.Query querySQL = sess.createQuery(query);
            return querySQL.getResultList();
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        } finally {
            closeSession(sess);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see cdi.rh.dal.IDAOSAVE#update(java.lang.Object)
     */
    public void update ( Object entity ) throws DataAccessException {
        // TODO Auto-generated method stub
        Session sess=getSession();
        Transaction tx = null;
        try{
            tx=sess.beginTransaction();
            /*this.dao*/sess.update ( entity );
            tx.commit();
        }catch(Exception ex){
            ex.printStackTrace();
            if(tx!=null)
                tx.rollback();
        }finally{
            //sess.flush();
            closeSession(sess);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see cdi.rh.dal.IDAOSAVE#delete(java.lang.Object)
     */
    public void delete ( Object entity ) throws DataAccessException {
        // TODO Auto-generated method stub
        Session sess=getSession();
        Transaction tx = null;
        try{
            tx=sess.beginTransaction();
            sess.delete(entity);
            tx.commit();
        }catch(Exception ex){
            ex.printStackTrace();
            if(tx!=null)
                tx.rollback();
        }finally{
            sess.flush();
            closeSession(sess);
        }
    }

    public List findByQuery ( String queryString ) throws DataAccessException {

        Session session = this.getSession();
        Query query = session.createQuery ( queryString );
        List liste = query.getResultList();
        this.closeSession(session);
        return liste;
    }

    public Serializable save (Object entity ) throws DataAccessException {
        // TODO Auto-generated method stub
        Serializable ret = null;
        Session sess=getSession();
        //Transaction tx = null;
        try{
            //tx=sess.beginTransaction();
            //ret = /*this.dao*/
                    sess.saveOrUpdate ( entity );
            //tx.commit();
        }catch(Exception ex){
            ex.printStackTrace();
//            if(tx!=null)
//                tx.rollback();
        }finally{
            //sess.flush();
            closeSession(sess);
        }
        return ret;
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

    public int deleteFromTable ( String queryString ) throws DataAccessException
    {
        Session session = this.getSession();
        Transaction tx = null;

        try
        {
            tx = session.beginTransaction();

            session.createSQLQuery(queryString).executeUpdate();

            tx.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            if (tx != null) tx.rollback();
        }
        finally
        {
            this.closeSession(session);
        }

        return 0;

    }

    public List<ParamData> findAnyColumnFromNomenclature (String strCodeDossier , String codeLangue , String strCodeTable , String strColumnNumber ) {
        return findAnyColumnFromNomenclature (  strCodeDossier ,  codeLangue ,  strCodeTable ,  strColumnNumber, ClsNomenclatureSortColumnEnum.NomenclatureSortColumnEnum.CACC );
    }


    public List<ParamData> findAnyColumnFromNomenclature ( String strCodeDossier , String codeLangue , String strCodeTable , String strColumnNumber, ClsNomenclatureSortColumnEnum.NomenclatureSortColumnEnum enumSortColumn) {
        ArrayList<ParamData> liste = new ArrayList<ParamData> ( );
        ParamData nomenclature = null;
        String strSortColumn = enumSortColumn==ClsNomenclatureSortColumnEnum.NomenclatureSortColumnEnum.CACC ? "cacc" : "vall";
        String queryString = "Select ParamData.cacc,ParamData.vall,ParamData.valm,ParamData.valt,ParamData.vald from ParamData ParamData where ParamData.identreprise=" + "'" + strCodeDossier + "'"
                + " and ParamData.ctab="  + strCodeTable +  " and ParamData.nume=" + strColumnNumber;
        queryString += " order by ParamData."+strSortColumn+" ASC";
        Session session = this.getSession();
        try {
            org.hibernate.Query query = session.createQuery ( queryString );
            Iterator iterator = query.iterate();
            String montant = "";
            String taux = "";
            String date = "";
            String code = "";
            String libelle = "";
            while (iterator.hasNext ( )) {
                try {
                    montant = "";
                    taux = "";
                    date = "";
                    code="";
                    libelle="";
                    Object [ ] ligne = ( Object [ ] ) iterator.next ( );
                    code = ligne [ 0 ]+"";
                    libelle = ligne [ 1 ]+"";
                    if (ligne [ 2 ] != null) montant = ligne [ 2 ]+"";
                    if (ligne [ 3 ] != null) taux = ligne [ 3 ]+"";
                    if (ligne [ 4 ] != null) date = ligne [ 4 ]+"";
                    //libelle = Convertisseur.getMessage2(libelle, codeLangue, libelle); //getLibelleFromEvMsg(session,libelle,codeLangue);
                    nomenclature = new ParamData();
                    nomenclature.setCacc(code);
                    nomenclature.setVall(libelle);
                    nomenclature.setValm(Long.valueOf(montant));
                    nomenclature.setValt(new BigDecimal(taux));
                    nomenclature.setVald(new ClsDate(date).getDate());
                    liste.add ( nomenclature );
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
            this.closeSession(session);
        }
        return liste;
    }

    public ParamData findAnyColumnFromNomenclature ( String strCodeDossier , String codeLangue , String strCodeTable , String strCodeCacc , String strColumnNumber ) {
        ParamData nomenclature = null;
        String code = "";
        String libelle = "";
        String montant = "";
        String taux = "";
        String date = "";
        String queryString = "Select a.cacc,a.vall,a.valm,a.valt,a.vald from ParamData a where a.idEntreprise=" + "'" + strCodeDossier + "'"
                + " and a.ctab=" +  strCodeTable   + " and a.cacc=" + "'"
                + strCodeCacc + "'"+ " and a.nume="  + strColumnNumber;
        queryString += " order by a.vall ASC";
        Session session = this.getSession();
        try {
            org.hibernate.Query query = session.createQuery ( queryString );
            Iterator iterator = query.iterate();
            if (iterator.hasNext ( )) {
                Object [ ] ligne = ( Object [ ] ) iterator.next ( );
                code = ligne [ 0 ]+"";
                if (ligne [ 1 ] != null) libelle = ligne [ 1 ]+"";
                if (ligne [ 2 ] != null) montant = ligne [ 2 ]+"";
                if (ligne [ 3 ] != null) taux = ligne [ 3 ]+"";
                if (ligne [ 4 ] != null) date = ligne [ 4 ]+"";
                //libelle = Convertisseur.getMessage2(libelle, codeLangue, libelle); //libelle = getLibelleFromEvMsg(session,libelle,codeLangue);
            }
            nomenclature = new ParamData();
            nomenclature.setCacc(code);
            nomenclature.setVall(libelle);
            nomenclature.setValm(Long.valueOf(StringUtils.isEmpty(montant)?"0":montant));
            nomenclature.setValt(new BigDecimal(StringUtils.isEmpty(taux)?"0":taux));
            nomenclature.setVald(new ClsDate(StringUtils.isEmpty(date)?"01/01/2000":date).getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            this.closeSession(session);
        }

        return nomenclature;
    }

    public ParamData findAnyByOrderColumnFromNomenclature ( String strCodeDossier , String codeLangue , String strCodeTable , String strCodeCacc , String strColumnNumber,String strSortColumn ) {
        ParamData nomenclature = null;
        String code = "";
        String libelle = "";
        String montant = "";
        String taux = "";
        String date = "";
        String queryString = "Select ParamData.cacc,ParamData.vall,ParamData.valm,ParamData.valt,ParamData.vald from Rhfnom rhfnom where ParamData.identreprise=" + "'" + strCodeDossier + "'"
                + " and ParamData.ctab="  + strCodeTable  + " and ParamData.cacc=" + "'"
                + strCodeCacc + "'"+ " and ParamData.nume="  + strColumnNumber;
        queryString += " order by ParamData."+strSortColumn+" ASC";
        Session session = this.getSession();
        try {
            org.hibernate.Query query = session.createQuery ( queryString );
            Iterator iterator = query.iterate();
            if (iterator.hasNext ( )) {
                Object [ ] ligne = ( Object [ ] ) iterator.next ( );
                code = ligne [ 0 ]+"";
                if (ligne [ 1 ] != null) libelle = ligne [ 1 ]+"";
                if (ligne [ 2 ] != null) montant = ligne [ 2 ]+"";
                if (ligne [ 3 ] != null) taux = ligne [ 3 ]+"";
                if (ligne [ 4 ] != null) date = ligne [ 4 ]+"";
                //libelle = Convertisseur.getMessage2(libelle, codeLangue, libelle); //libelle = getLibelleFromEvMsg(session,libelle,codeLangue);
            }
            nomenclature = new ParamData();
            nomenclature.setCacc(code);
            nomenclature.setVall(libelle);
            nomenclature.setValm(Long.valueOf(montant));
            nomenclature.setValt(new BigDecimal(taux));
            nomenclature.setVald(new ClsDate(date).getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            this.closeSession(session);
        }
        return nomenclature;
    }

    public String getMoisPaieReel(final String cdos, final String datedebut){
        Session session = this.getSession();
        try {
            org.hibernate.Query query = session.createQuery("select a.cacc"
                    + " from ParamData a, ParamData b, ParamData c"
                    + " where a.identreprise = '"+cdos+"' and a.ctab = 91"
                    + " and a.identreprise = b.identreprise and a.identreprise = c.identreprise"
                    + " and a.cacc = b.cacc and a.cacc = c.cacc"
                    + " and a.ctab = b.ctab and a.ctab = c.ctab"
                    + " and a.nume = 1 and b.nume = 2 and c.nume = 3"
                    + " and to_date('"+datedebut+"', 'dd/MM/yyyy') between to_date(b.vall, 'dd/MM/yyyy') and to_date(c.vall, 'dd/MM/yyyy')");
            List list = query.list();
            if(list.isEmpty())
            {
                return "0";
            }
            else return (String) list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            this.closeSession(session);
        }
        return "0";
    }

    /**
     * Creer le dossier qui contient le fichier en paramètres
     * @param strAbsoluteFilePath
     * @return
     */
    public static void _createFileFolder(String strAbsoluteFilePath, String strFileSeparator)
    {
        String folder = null;
        try
        {
            File foutput = new File(strAbsoluteFilePath);
            int indexOfFile = strAbsoluteFilePath.lastIndexOf(strFileSeparator);
            if(indexOfFile != -1)
            {
                folder = strAbsoluteFilePath.substring(0, indexOfFile);
                foutput = new File(folder);
                if(! foutput.exists())
                {
                    foutput.mkdirs();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public boolean insertIntoTable ( String queryString ) throws SQLException
    {
        this.updateFromTable(queryString);

        return true;
		/*
		boolean result=false;
		try {
			Session session = this.getSession();
			Connection conn = session.connection ( );
			Statement statement = conn.createStatement ( );
			result = statement.execute ( queryString );
			statement.close ( );
			conn.close ( );
			this.closeConnexion(session);

		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		*/

    }

    public List<InterfComptable> findFromCpInt(String sqlQuery)
    {
        Session session = this.getSession();
        List result = session.createSQLQuery(sqlQuery).list();
        closeSession(session);
        return result;
    }

    public List<InterfComptable> findFromCpInt(Session _o_Session, String sqlQuery)
    {
        //List<InterfComptable> oList = new ArrayList<InterfComptable>();
        return _o_Session.createSQLQuery(sqlQuery).list();
//        Connection oConnexion = null;
//        Statement oStatement = null;
//        ResultSet oResultSet = null;
//        InterfComptable       _o_Data     = null;
//        try
//        {
//            oConnexion = _o_Session.connection();
//            oStatement = oConnexion.createStatement();
//            oResultSet  = oStatement.executeQuery(sqlQuery);
//
//            while(oResultSet.next() )
//            {
//                try {
//                    _o_Data = new InterfComptable();
//
//                    _o_Data.setIdEntreprise(oResultSet.getInt("cdos"));
//                    _o_Data.setCodabr(oResultSet.getString("codabr"));
//                    _o_Data.setCoddes1(oResultSet.getString("coddes1"));
//                    _o_Data.setCoddes2(oResultSet.getString("coddes2"));
//                    _o_Data.setCoddes3(oResultSet.getString("coddes3"));
//                    _o_Data.setCoddes4(oResultSet.getString("coddes4"));
//                    _o_Data.setCoddes5(oResultSet.getString("coddes5"));
//                    _o_Data.setCoddes6(oResultSet.getString("coddes6"));
//                    _o_Data.setCoddes7(oResultSet.getString("coddes7"));
//                    _o_Data.setCoddes8(oResultSet.getString("coddes8"));
//                    _o_Data.setCoddes9(oResultSet.getString("coddes9"));
//                    _o_Data.setCoderr(oResultSet.getString("coderr"));
//                    _o_Data.setCodets(oResultSet.getString("codets"));
//                    _o_Data.setCodjou(oResultSet.getString("codjou"));
//                    _o_Data.setCodtre(oResultSet.getString("codtre"));
//                    _o_Data.setCoduti(oResultSet.getString("coduti"));
//                    _o_Data.setDatcpt(oResultSet.getDate("datcpt"));
//                    _o_Data.setDatcre(oResultSet.getDate("datcre"));
//                    _o_Data.setDatech(oResultSet.getDate("datech"));
//                    _o_Data.setDatmod(oResultSet.getDate("datmod"));
//                    _o_Data.setDatpce(oResultSet.getDate("datpce"));
//                    _o_Data.setDevpce(oResultSet.getString("devpce"));
//                    _o_Data.setLibecr(oResultSet.getString("libecr"));
//                    _o_Data.setLiberr(oResultSet.getString("liberr"));
//                    _o_Data.setNumcpt(oResultSet.getString("numcpt"));
//                    _o_Data.setNumpce(oResultSet.getString("numpce"));
//                    _o_Data.setNumtie(oResultSet.getString("numtie"));
//                    _o_Data.setPceMt(oResultSet.getBigDecimal("pce_Mt"));
//                    _o_Data.setQuantite(oResultSet.getBigDecimal("quantite"));
//                    _o_Data.setReflet(oResultSet.getString("reflet"));
//                    _o_Data.setSens(oResultSet.getString("sens"));
//                    oList.add(_o_Data);
//
//                } catch (Exception e) {
//                    // TODO: handle exception
//                    e.printStackTrace ( );
//                    if(_o_Data != null)
//                        oList.add(_o_Data);
//                    continue;
//                }
//                finally
//                {
//                    _o_Data = null;
//                }
//            }
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace ( );
//        }
//        finally
//        {
//            try
//            {
//                oResultSet.close();
//                oResultSet = null;
//                oStatement.close();
//                oStatement =null;
//                oConnexion.close();
//                oConnexion =null;
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//        }

        //return oList;
    }


}
