package com.kinart.paie.business.services.utils;

import com.kinart.paie.business.model.ParamData;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.*;
import org.hibernate.query.Query;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

import com.mchange.v1.util.StringTokenizerUtils;

@Service
public class HibernateConnexionService extends HibernateDaoSupport {

        @Autowired
        public HibernateConnexionService(SessionFactory sessionFactory){
                super.setSessionFactory(sessionFactory);
        }

        @Override
        protected void initDao() throws Exception {
                super.initDao();
        }

        public List find(String query) throws DataAccessException {
                return this.getHibernateTemplate().find(query);
        }

        /* (non-Javadoc)
         * @see cdi.rh.dal.IDAOSAVE#save(java.lang.Object)
         */
        public Serializable save(Object entity) throws DataAccessException {
                // TODO Auto-generated method stub
                return getHibernateTemplate().save(entity);
        }

        public Session getSession (){
                return getHibernateTemplate().getSessionFactory().openSession();
        }

        public void closeConnexion (Session session){
                SessionFactoryUtils.closeSession(session);
        }

        /*
         * (non-Javadoc)
         *
         * @see cdi.rh.dal.IDAOSAVE#loadAll(java.lang.Class)
         */
        public List loadAll ( Class entityClass ) throws DataAccessException {
                // TODO Auto-generated method stub
                return getHibernateTemplate().loadAll ( entityClass );
        }


        public List findByQuery ( String queryString , String entityName , Class entityClass ) throws DataAccessException {

                Session session = this.getSession();
                Query query = session.createSQLQuery ( queryString ).addEntity ( entityName , entityClass );
                List liste = query.list ( );
                this.closeConnexion(session);
                return liste;
        }


        public List findByQuery ( String queryString ) throws DataAccessException {

                Session session = this.getSession();
                Query query = session.createQuery ( queryString );
                List liste = query.list ( );
                this.closeConnexion(session);
                return liste;
        }

        /**
         *  Pour l'execution des requetes session.delete
         * @param session
         * @param entity
         * @throws DataAccessException
         */
        public void delete(Session session, Object entity) throws DataAccessException
        {
                this.delete(session, entity);
        }

        /**
         * Pour l'execution des requetes "delete from table where conditions"
         * @throws DataAccessException
         */
//    public void delete(Session session, String strDeleteQuery) throws DataAccessException
//    {
//        this.delete(session, strDeleteQuery);
//    }

        /**
         * Pour l'execution des requetes "delete from table where conditions"
         * @param queryString
         * @throws DataAccessException
         */
        public void delete(Session session, String queryString ) throws DataAccessException
        {
                int indx = queryString.toLowerCase().indexOf("from");

                //Gestion du cas ou la requete est du type delete table where conditions
                if(indx==-1) indx = queryString.toLowerCase().indexOf("delete");

                queryString =queryString.substring(indx+5, queryString.length()).trim();

                String[] array = StringTokenizerUtils.tokenizeToArray(queryString.trim(), " ");

                String strTable = array[0];

                indx = queryString.indexOf(strTable);

                String strWhere = queryString.substring(indx+strTable.length()+1, queryString.length()).trim();

                strTable = StringUtils.capitalize(strTable.toLowerCase());

                queryString="From "+strTable+" "+strWhere;

                //System.out.println("----------delete query = "+queryString);

                List oList = this.find(queryString);
                try
                {
                        for (Object object : oList)
                        {
                                this.delete(session, object);
                        }
                }
                catch (Exception e)
                {
                        e.printStackTrace();
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
                }
                finally
                {
                        this.closeConnexion(session);
                }

                return result;

        }


        public int deleteFromTable ( String queryString ) throws DataAccessException
        {
                Session session = this.getSession();
                Transaction tx = null;

                try
                {
                        tx = session.beginTransaction();

                        this.delete(session, queryString);

                        tx.commit();
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                        if (tx != null) tx.rollback();
                }
                finally
                {
                        this.closeConnexion(session);
                }

                return 0;

        }


        public boolean insertIntoTable ( String queryString ) throws SQLException
        {
                this.updateFromTable(queryString);

                return true;
        }

        /* (non-Javadoc)
         * @see cdi.rh.dal.IDAOSAVE#saveOrUpdate(java.lang.Object)
         */
        public void saveOrUpdate(Object entity) throws DataAccessException {
                // TODO Auto-generated method stub
                getHibernateTemplate().saveOrUpdate(entity);
        }

        /* (non-Javadoc)
         * @see cdi.rh.dal.IDAOSAVE#saveOrUpdateAll(java.util.Collection)
         */
        public void saveOrUpdateAll(Collection entities) throws DataAccessException {
                // TODO Auto-generated method stub
                Session session = this.getSession();
                Transaction tx = session.beginTransaction();
                try{
                        for (Iterator iter = entities.iterator(); iter.hasNext();) session.saveOrUpdate(iter.next());
                        tx.commit();
                }catch(Exception e){
                        if(tx!=null)
                                tx.rollback();
                        e.printStackTrace();
                }finally{
                        this.closeConnexion(session);
                }
        }

        public void saveOrUpdateAll(Session session,Collection entities) throws DataAccessException {
                // TODO Auto-generated method stub
                Transaction tx = session.beginTransaction();
                try{
                        for (Iterator iter = entities.iterator(); iter.hasNext();) session.saveOrUpdate(iter.next());
                        tx.commit();
                }catch(Exception e){
                        if(tx!=null)
                                tx.rollback();
                        e.printStackTrace();
                }finally{
                        session.close();
                }
        }

        /* (non-Javadoc)
         * @see cdi.rh.dal.IDAOSAVE#update(java.lang.Object)
         */
        public void update(Object entity) throws DataAccessException {
                // TODO Auto-generated method stub
                getHibernateTemplate().update(entity);
        }

        public List<ParamData> findFromNomenclature1 (String strCodeDossier , String codeLangue , String strCodeTable) {

                ArrayList<ParamData> liste = new ArrayList<ParamData> ( );
                ParamData nomenclature = null;
                String strSortColumn = "cacc";
                String queryString = "Select paramdata.cacc,paramdata.vall,paramdata.valm,paramdata.valt,paramdata.vald from ParamData paramdata where paramdata.cdos=" + "'" + strCodeDossier + "'"
                        + " and paramdata.ctab="  + strCodeTable + " and paramdata.nume=" + 1 ;
                queryString += " order by paramdata."+strSortColumn+" ASC";
                Session session = this.getSession();
                try {
                        Query query = session.createQuery ( queryString );
                        Iterator iterator = query.iterate ( );
                        String montant = "";
                        String taux = "";
                        String date = "";
                        String code = "";
                        String libelle = "";
                        nomenclature = new ParamData ();
                        liste.add ( nomenclature );
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
                                        libelle = libelle; //libelle = getLibelleFromEvMsg(session,libelle,codeLangue);
                                        nomenclature = new ParamData ();
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
                        this.closeConnexion(session);
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
                String queryString = "Select paramdata.cacc,paramdata.vall,paramdata.valm,paramdata.valt,paramdata.vald from ParamData paramdata where paramdata.cdos=" + "'" + strCodeDossier + "'"
                        + " and paramdata.ctab=" +  strCodeTable   + " and paramdata.cacc=" + "'"
                        + strCodeCacc + "'"+ " and paramdata.nume="  + strColumnNumber;
                queryString += " order by paramdata.vall ASC";
                Session session = this.getSession();
                try {
                        Query query = session.createQuery ( queryString );
                        Iterator iterator = query.iterate ( );
                        if (iterator.hasNext ( )) {
                                Object [ ] ligne = ( Object [ ] ) iterator.next ( );
                                code = ligne [ 0 ]+"";
                                if (ligne [ 1 ] != null) libelle = ligne [ 1 ]+"";
                                if (ligne [ 2 ] != null) montant = ligne [ 2 ]+"";
                                if (ligne [ 3 ] != null) taux = ligne [ 3 ]+"";
                                if (ligne [ 4 ] != null) date = ligne [ 4 ]+"";
                        }
                        nomenclature = new ParamData ();
                        nomenclature.setCacc(code);
                        nomenclature.setVall(libelle);
                        nomenclature.setValm(Long.valueOf(montant));
                        nomenclature.setValt(new BigDecimal(taux));
                        nomenclature.setVald(new ClsDate(date).getDate());
                } catch (Exception e) {
                        e.printStackTrace();
                }
                finally{
                        this.closeConnexion(session);
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
                String queryString = "Select paramdata.cacc,paramdata.vall,paramdata.valm,paramdata.valt,paramdata.vald from paramdata paramdata where paramdata.cdos=" + "'" + strCodeDossier + "'"
                        + " and paramdata.ctab="  + strCodeTable  + " and paramdata.cacc=" + "'"
                        + strCodeCacc + "'"+ " and paramdata.nume="  + strColumnNumber;
                queryString += " order by paramdata."+strSortColumn+" ASC";
                Session session = this.getSession();
                try {
                        Query query = session.createQuery ( queryString );
                        Iterator iterator = query.iterate ( );
                        if (iterator.hasNext ( )) {
                                Object [ ] ligne = ( Object [ ] ) iterator.next ( );
                                code = ligne [ 0 ]+"";
                                if (ligne [ 1 ] != null) libelle = ligne [ 1 ]+"";
                                if (ligne [ 2 ] != null) montant = ligne [ 2 ]+"";
                                if (ligne [ 3 ] != null) taux = ligne [ 3 ]+"";
                                if (ligne [ 4 ] != null) date = ligne [ 4 ]+""; //libelle = getLibelleFromEvMsg(session,libelle,codeLangue);
                        }

                        nomenclature = new ParamData ();
                        nomenclature.setCacc(code);
                        nomenclature.setVall(libelle);
                        nomenclature.setValm(Long.valueOf(montant));
                        nomenclature.setValt(new BigDecimal(taux));
                        nomenclature.setVald(new ClsDate(date).getDate());
                } catch (Exception e) {
                        e.printStackTrace();
                }
                finally{
                        this.closeConnexion(session);
                }
                return nomenclature;
        }

        public List<ParamData> findAnyByOrderColumnFromNomenclature ( String strCodeDossier , String codeLangue , String strCodeTable , String strColumnNumber,String strSortColumn ) {
                ArrayList<ParamData> liste = new ArrayList<ParamData> ( );
                ParamData nomenclature = null;
                String queryString = "Select rhfnom.cacc,rhfnom.vall,rhfnom.valm,rhfnom.valt,rhfnom.vald from ParamData rhfnom where rhfnom.cdos=" + "'" + strCodeDossier + "'"
                        + " and rhfnom.ctab="  + strCodeTable +  " and rhfnom.nume=" + strColumnNumber ;
                queryString += " order by rhfnom."+strSortColumn+" ASC";
                Session session;
                session = this.getSession();
                try {

                        Query query = session.createQuery ( queryString );
                        Iterator iterator = query.iterate ( );
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
                                        nomenclature = new ParamData ();
                                        nomenclature.setCacc(code);
                                        nomenclature.setVall(libelle);
                                        nomenclature.setValm(Long.valueOf(montant));
                                        nomenclature.setValt(new BigDecimal(taux));
                                        nomenclature.setVald(new ClsDate(date).getDate());
                                        liste.add ( nomenclature );
                                } catch (Exception e) {
                                        e.printStackTrace();
                                        continue;
                                }
                        }
                } catch (HibernateException e) {
                        e.printStackTrace();
                }
                finally{
                        this.closeConnexion(session);
                }

                return liste;
        }


        public List<ParamData> findAnyColumnFromNomenclature ( String strCodeDossier , String codeLangue , String strCodeTable , String strColumnNumber , String strValue , boolean bool ) {
                ArrayList<ParamData> liste = new ArrayList<ParamData> ( );
                ParamData nomenclature = null;
                String queryString = "Select  rhfnom.cacc,rhfnom.vall,rhfnom.valm,rhfnom.valt,rhfnom.vald from ParamData rhfnom where rhfnom.cdos=" + "'" + strCodeDossier + "'"
                        + " and rhfnom.ctab="  + strCodeTable ;
                if (strValue != null && strValue.trim ( ).length ( ) != 0) queryString += " and rhfnom.vall = " + "'" + strValue + "'";
                if (strColumnNumber != null && strColumnNumber.trim ( ).length ( ) != 0) queryString += " and rhfnom.nume="  + strColumnNumber ;
                else queryString += " and rhfnom.nume="  + "1";
                queryString += " order by rhfnom.vall ASC";
                Session session = this.getSession();
                try {
                        Query query = session.createQuery ( queryString );
                        Iterator iterator = query.iterate ( );
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
                                        nomenclature = new ParamData ();
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
                        this.closeConnexion(session);
                }
                return liste;
        }

        public String getMoisPaieReel(final String cdos, final String datedebut){
                Session session = this.getSession();
                Query query = session.createQuery ( "select a.cacc"
                        + " from ParamData a, ParamData b, ParamData c"
                        + " where a.cdos = :cdos and a.ctab = 91"
                        + " and a.cdos = b.cdos and a.cdos = c.cdos"
                        + " and a.cacc = b.cacc and a.cacc = c.cacc"
                        + " and a.ctab = b.ctab and a.ctab = c.ctab"
                        + " and a.nume = 1 and b.nume = 2 and c.nume = 3"
                        + " and to_date(:datedebut, 'dd/MM/yyyy') between to_date(b.vall, 'dd/MM/yyyy') and to_date(c.vall, 'dd/MM/yyyy')" );
                query.setParameter("cdos", cdos, StandardBasicTypes.STRING);
                query.setParameter("datedebut", datedebut, StandardBasicTypes.STRING);
                List liste = query.list ( );
                this.closeConnexion(session);

                if(liste.isEmpty())
                {
                        return "0";
                }
                else return (String) liste.get(0);
        }
}
