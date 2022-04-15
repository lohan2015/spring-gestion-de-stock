package com.cmbassi.gestiondepaie.services.utils;

import java.util.Date;
import java.util.Random;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AuditInterceptor extends EmptyInterceptor{

	public static boolean use = true;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Session session;

	@Override
	public void beforeTransactionCompletion(Transaction tx) {
		// TODO Auto-generated method stub
		super.beforeTransactionCompletion(tx);
	}

	public static long randomValue = 0;
	
	public synchronized static long getRandomValue()
	{
		if(randomValue>Long.valueOf(1000000000))
		{
			randomValue = 0;
		}	
		randomValue = randomValue + 1;
		return randomValue;
		
	}
	
	@Override
	public void afterTransactionBegin(Transaction tx) {		
		super.afterTransactionBegin(tx);
		if(!use) return;
		Random r = new Random(System.currentTimeMillis());
		//int i = 10+r.nextInt(990);
		long i = Math.abs(r.nextLong());
		
		Date dt = new Date();
		String strDt = new ClsDate(dt).getDateS("HHmmssSSS");
		String cle = strDt+getRandomValue()+""; //tringUtils.substring(i+strDt,0,29);
		if(session != null){
//			String sqlStr = "insert into Evlang values ('"+cle+"','"+cle+"')";
//			session.createSQLQuery(sqlStr).executeUpdate();
//			session.flush();
//			sqlStr = "delete Evlang where clang = '"+cle+"'";
//			session.createSQLQuery(sqlStr).executeUpdate();
//			session.flush();
		}
	}


	public void setSession(Session session) {
		this.session=session;
	}
}
