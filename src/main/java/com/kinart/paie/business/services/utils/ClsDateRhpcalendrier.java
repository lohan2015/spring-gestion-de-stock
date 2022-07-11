package com.kinart.paie.business.services.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.kinart.api.gestiondepaie.dto.ParamCalendarDto;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.paie.business.services.calendrier.ClsCalendar;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;

public class ClsDateRhpcalendrier {

	private String DATE_FORMAT = "dd/MM/yyyy";
	private String _strFormat = DATE_FORMAT;
	private DateFormat _format = null;
	private GeneriqueConnexionService _service = null;
	private ParamDataRepository paramDataRepository;
	private Session _session;
	private String _dossier;
	private static String QUERY_TEXT_JOURS_OUVRES=" ouvr ='O'";
	private static String QUERY_TEXT_JOURS_FERIES=" fer ='F'";
	private static String QUERY_TEXT_JOURS_TRAVAILLES=" trav ='O'";
	private static String QUERY_TEXT_JOURS_NON_OUVRES=" ouvr ='N'";
	private static String QUERY_TEXT_JOURS_NON_FERIES=" fer ='N'";
	private static String QUERY_TEXT_JOURS_NON_TRAVAILLES=" trav ='N'";
	private static String QUERY_TEXT_JOURS_ABSENCES=" trav ='O' and fer='N'";
	private static String QUERY_TEXT_JOURS_CONGES=" ouvr ='O'";
	
	private static String SELECT_COUNT_QUERY_TEXT="Select count(*) From Rhpcalendrier where comp_id.cdos = :cdos and comp_id.jour >= :datedebut and comp_id.jour <= :datefin";
	
	public ClsDateRhpcalendrier(GeneriqueConnexionService service, ParamDataRepository paramDataRepository, String dossier, String strFormat)
	{
		this._service = service;
		this.paramDataRepository = paramDataRepository;
		_format = new SimpleDateFormat(strFormat);
		_service = service;
		_dossier = dossier;
	}
	
	private int _getNombreJours(String queryString,Date startDate, Date endDate)
	{
		queryString = SELECT_COUNT_QUERY_TEXT + " and "+ queryString;
		ParameterUtil.println(queryString);
		String startDateS = new SimpleDateFormat(_strFormat).format(startDate);
		String endDateS = new SimpleDateFormat(_strFormat).format(endDate);
		try
		{
			_session = _service.getSession();
			
			Query q = _session.createQuery(queryString);
			q.setParameter("cdos", _dossier, StandardBasicTypes.STRING);
			q.setParameter("datedebut", startDateS,StandardBasicTypes.STRING);
			q.setParameter("datefin", endDateS,StandardBasicTypes.STRING);
			List list = q.list();
			
			_service.closeSession(_session);
			
			if(! list.isEmpty())
				return Integer.parseInt(list.get(0).toString());
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
			return 0;
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			return 0;
		}
		
		return 0;
	}
	
	/**
	 * Le nombre de jours ouvr�s entre deux dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	
	public int getNombreDeJoursOuvres(Date startDate, Date endDate)
	{
		return this._getNombreJours(QUERY_TEXT_JOURS_OUVRES, startDate, endDate);
	}
	
	
	/**
	 * Le nombre de jours non ouvr�s entre deux dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	
	public int getNombreDeJoursNonOuvres(Date startDate, Date endDate)
	{
		return this._getNombreJours(QUERY_TEXT_JOURS_NON_OUVRES, startDate, endDate);
	}
	
	/**
	 * Le nombre de jours f�ri�s entre deux dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	
	public int getNombreDeJoursFeries(Date startDate, Date endDate)
	{
		return this._getNombreJours(QUERY_TEXT_JOURS_FERIES, startDate, endDate);
	}
	/**
	 * Le nombre de jours non f�ri�s entre deux dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	
	public int getNombreDeJoursNonFeries(Date startDate, Date endDate)
	{
		return this._getNombreJours(QUERY_TEXT_JOURS_NON_FERIES, startDate, endDate);
	}
	
	/**
	 * Le nombre de jours travaill�s entre deux dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	
	public int getNombreDeJoursTravailles(Date startDate, Date endDate)
	{
		return this._getNombreJours(QUERY_TEXT_JOURS_TRAVAILLES, startDate, endDate);
	}
	/**
	 * Le nombre de jours non travaill�s entre deux dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	
	public int getNombreDeJoursNonTravailles(Date startDate, Date endDate)
	{
		return this._getNombreJours(QUERY_TEXT_JOURS_NON_TRAVAILLES, startDate, endDate);
	}
	
	/**
	 * Le nombre de jours d'absences entre deux dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	
	public int getNombreDeJoursAbsences(Date startDate, Date endDate, String typeBD)
	{
		//return this._getNombreJours(QUERY_TEXT_JOURS_ABSENCES, startDate, endDate);
		return (int)AbsenceCongeUtil.pr_compte_jours(_service, paramDataRepository, _dossier, "A", startDate, endDate, typeBD);
	}
	
	
	/**
	 * Le nombre de jours de cong�s entre deux dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	
	public int getNombreDeJoursConges(Date startDate, Date endDate, String typeBD)
	{
		//return this._getNombreJours(QUERY_TEXT_JOURS_CONGES, startDate, endDate);
		return (int)AbsenceCongeUtil.pr_compte_jours(_service, paramDataRepository, _dossier, "C", startDate, endDate, typeBD);
	}
	
	/**
	 * La date de fin d'un cong� lorsqu'on a droit � un nombre de jours de cong�s donn�e qui commence � la date de d�part
	 * @param startDate
	 * @return
	 */
	
	public Date getDateFinConges(Date startDate, int nbrJoursConges, String typeBD)
	{
		ClsDate oDate = new ClsDate(startDate);
		Date endDate = oDate.addDay(nbrJoursConges);
		if(nbrJoursConges <= 0)
			return endDate;
		
		if(nbrJoursConges == 0)
			return endDate;
		
		long nbr = AbsenceCongeUtil.pr_compte_jours(_service, paramDataRepository, _dossier, "C", startDate, endDate, typeBD);
		if( nbr == nbrJoursConges)
			return endDate;
		
		int nbrOld = 0;
		int nbToAdd = nbr>nbrJoursConges ? -1:1;
		while(Math.abs(nbr -nbrJoursConges) != 0)
		{
			nbrOld++;
			endDate = new ClsDate(endDate).addDay(nbToAdd);
			nbr = AbsenceCongeUtil.pr_compte_jours(_service, paramDataRepository, _dossier, "C", startDate, endDate, typeBD);
			if(nbrOld == 200)
				break;
		}
		
		return endDate;
	}
	
	public Date getDateFinAbsences(Date startDate, int nbrJoursAbsences, String typeBD)
	{
		ClsDate oDate = new ClsDate(startDate);
		Date endDate = oDate.addDay(nbrJoursAbsences);
		if(nbrJoursAbsences <= 0)
			return endDate;
		
		if(nbrJoursAbsences == 0)
			return endDate;
		
		long nbr = AbsenceCongeUtil.pr_compte_jours(_service, paramDataRepository, _dossier, "A", startDate, endDate, typeBD);
		if( nbr == nbrJoursAbsences)
			return endDate;
		
		int nbrOld = 0;
		int nbToAdd = nbr>nbrJoursAbsences ? -1:1;
		while(Math.abs(nbr -nbrJoursAbsences) != 0)
		{
			nbrOld++;
			endDate = new ClsDate(endDate).addDay(nbToAdd);
			nbr = AbsenceCongeUtil.pr_compte_jours(_service, paramDataRepository, _dossier, "A", startDate, endDate, typeBD);
			if(nbrOld == 200)
				break;
		}
		
		return endDate;
	}
	
//	public Date getDateFinConges(Date startDate, int nbrJoursConges)
//	{
//		ClsDate oDate = new ClsDate(startDate);
//		Date endDate = oDate.addDay(nbrJoursConges - 1);
//		
//		int nbrJoursNonOuvrables = this.getNombreDeJoursNonOuvres(startDate, endDate);
//		if(nbrJoursNonOuvrables > 0)
//			return getDateFinConges(new ClsDate(endDate).addDay(1), nbrJoursNonOuvrables);
//		else
//			return endDate;
//	}


	public static ClsCalendar fromParameter(ParamCalendarDto pCalDto) {
		if (pCalDto == null) {
			return null;
		}
		ClsCalendar dto = new  ClsCalendar();
		BeanUtils.copyProperties(pCalDto, dto);
		return dto;
	}

	public static ParamCalendarDto getParameter(ClsCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		ParamCalendarDto dto = new  ParamCalendarDto();
		BeanUtils.copyProperties(calendar, dto);
		return dto;
	}
	
	
}
