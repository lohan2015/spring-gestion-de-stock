package com.kinart.paie.business.services.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kinart.api.gestiondepaie.dto.ElementVariableCongeDto;
import com.kinart.api.gestiondepaie.dto.ParamCalendarDto;
import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.portail.dto.ElementVarCongeDto;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.paie.business.services.calendrier.ClsCalendar;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
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

	public static List<ElementVarCongeDto> getAllElementAbsenceConge(GeneriqueConnexionService service, ParamDataRepository paramDataRepository, ElementVarCongeDto absenceconge) {
		List<ElementVarCongeDto> listeElementsAbsConge = new ArrayList<>();
		BigDecimal mtcg = new BigDecimal(0);
		String cdos = absenceconge.getCdos();
		String cuti = "DELT";
		String format = "dd/MM/yyyy";
		Date dfcg = (new ClsDate(absenceconge.getDfin(), format)).getDate();
		Date ddcg = (new ClsDate(absenceconge.getDdeb(), format)).getDate();
		boolean is_conge = false;
		BigDecimal saveNbja = absenceconge.getNbja();
		BigDecimal saveNbjc = absenceconge.getNbjc();
		boolean abs2mois = false;
		ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(266), "ABSEN2MOIS", Integer.valueOf(2))
				.map(ParamDataDto::fromEntity)
				.orElseThrow(() ->
						new EntityNotFoundException(
								"Aucune donnée avec l'ID = "+"ABSEN2MOIS"+" n' ete trouve dans la table 266",
								ErrorCodes.ARTICLE_NOT_FOUND)
				);
		if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
			abs2mois = StringUtils.equalsIgnoreCase("O", fnom.getVall().trim());

		try {
			if (StringUtils.isNotBlank(absenceconge.getNmat())) {
				ParamDataDto fnom1 = paramDataRepository.findByNumeroLigne(Integer.valueOf(22), absenceconge.getMotf(), Integer.valueOf(1))
						.map(ParamDataDto::fromEntity)
						.orElseThrow(() ->
								new EntityNotFoundException(
										"Aucune donnée avec l'ID = "+absenceconge.getMotf()+" n' ete trouve dans la table 22",
										ErrorCodes.ARTICLE_NOT_FOUND)
						);
				ParamDataDto fnom3 = paramDataRepository.findByNumeroLigne(Integer.valueOf(22), absenceconge.getMotf(), Integer.valueOf(3))
						.map(ParamDataDto::fromEntity)
						.orElseThrow(() ->
								new EntityNotFoundException(
										"Aucune donnée avec l'ID = "+absenceconge.getMotf()+" n' ete trouve dans la table 22",
										ErrorCodes.ARTICLE_NOT_FOUND)
						);

				//Rhfnom fnom1 = (Rhfnom)service.get(Rhfnom.class, (Serializable)new RhfnomPK(cdos, Integer.valueOf(22), absenceconge.getMotf(), Integer.valueOf(1)));
				//Rhfnom fnom3 = (Rhfnom)service.get(Rhfnom.class, (Serializable)new RhfnomPK(cdos, Integer.valueOf(22), absenceconge.getMotf(), Integer.valueOf(3)));
				if (fnom1.getValm().longValue() == 1L && fnom3.getValm().longValue() == 0L) {
					is_conge = true;
				}
				Date dated = ddcg;
				boolean fini = false;
				long cptNbja = 0L;
				long cptNbjc = 0L;
				BigDecimal ecarta = BigDecimal.ZERO;
				BigDecimal ecartc = BigDecimal.ZERO;
				while (dated.compareTo(dfcg) <= 0) {
					Date datef;
					long nbja, nbjc;
					if (fnom1.getValm().longValue() == 1L || abs2mois) {
						datef = (new ClsDate(dated)).getLastDayOfMonth();
						if (datef.compareTo(dfcg) > 0) {
							datef = dfcg;
							fini = true;
						}
					} else {
						datef = dfcg;
						fini = true;
					}
					if (fnom1.getValm().longValue() == 0L) {
						nbja = AbsenceCongeUtil.pr_compte_jours(service, paramDataRepository, cdos, "A", dated, datef, TypeBDUtil.OR);
						nbjc = 0L;
						mtcg = new BigDecimal(0);
					} else {
						nbjc = AbsenceCongeUtil.pr_compte_jours(service, paramDataRepository, cdos, "C", dated, datef, TypeBDUtil.OR);
						if (fnom3.getValm().longValue() != 2L) {
							nbja = AbsenceCongeUtil.pr_compte_jours(service, paramDataRepository, cdos, "A", dated, datef, TypeBDUtil.OR);
							mtcg = absenceconge.getMont();
						} else {
							nbja = 0L;
						}
					}
					BigDecimal bdNbja = new BigDecimal(nbja);
					BigDecimal bdNbjc = new BigDecimal(nbjc);
					cptNbja += nbja;
					cptNbjc += nbjc;
					if (fini) {
						ecarta = saveNbja.subtract(new BigDecimal(cptNbja));
						ecartc = saveNbjc.subtract(new BigDecimal(cptNbjc));
						nbja += ecarta.longValue();
						nbjc += ecartc.longValue();
						bdNbja = bdNbja.add(ecarta);
						bdNbjc = bdNbjc.add(ecartc);
					}
					ElementVarCongeDto det = new ElementVarCongeDto();
					det.setCdos(cdos);
					det.setDdeb(dated);
					det.setNbul(Integer.valueOf(9));
					det.setAamm(absenceconge.getAamm());
					det.setNmat(absenceconge.getNmat());
					det.setDfin(datef);
					det.setNbja(bdNbja);
					det.setNbjc(bdNbjc);
					det.setCuti(cuti);
					det.setMotf(absenceconge.getMotf());
					det.setMont(mtcg);
					if(is_conge == true) det.setEstUnCOnge("O");
					det.setMinDeb(absenceconge.getDdeb());
					det.setMaxFin(absenceconge.getDfin());
					det.setNbjaTotal(absenceconge.getNbja());
					det.setNbjcTotal(absenceconge.getNbjc());
					listeElementsAbsConge.add(det);
					dated = (new ClsDate(datef)).addDay(1);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return listeElementsAbsConge;
	}
	
	
}
