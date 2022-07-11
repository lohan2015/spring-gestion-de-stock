package com.kinart.paie.business.services.calendrier;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.kinart.paie.business.model.CalendrierPaie;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.services.cloture.ClsNomenclature;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

public class ClsCalendar extends CalendrierPaie implements ClsICalendar
{

	private String companykey = null;

	private String dateFormat = null;

	private String langue = null;

	private String error = null;

	private GeneriqueConnexionService service = null;

	Map<String, ParamData> daysOfWeeksMap = null;

	Map<String, ParamData> hodidaysMap = null;

	private String currentyear;

	private String currentyeartodelete;

	private String firstdaynumber;

	private String firstdayname;

	private String firstweek;

	private String weeknumber;

	private String monday;// = "monday";

	private String tuesday;// = "tuesday";

	private String wednesday;// = "wednesday";

	private String thursday;// = "thursday";

	private String friday;// = "friday";

	private String saturday;// = "saturday";

	private String sunday;// = "sunday";

	private String jomonday = "O";

	private String jotuesday = "O";

	private String jowednesday = "O";

	private String jothursday = "O";

	private String jofriday = "O";

	private String josaturday = "O";

	private String josunday = "O";

	private String jtmonday = "O";

	private String jttuesday = "O";

	private String jtwednesday = "O";

	private String jtthursday = "O";

	private String jtfriday = "O";

	private String jtsaturday = "O";

	private String jtsunday = "O";

	private String jfmonday = "N";

	private String jftuesday = "N";

	private String jfwednesday = "N";

	private String jfthursday = "N";

	private String jffriday = "N";

	private String jfsaturday = "N";

	private String jfsunday = "N";

	private String dtmonday;

	private String dttuesday;

	private String dtwednesday;

	private String dtthursday;

	private String dtfriday;

	private String dtsaturday;

	private String dtsunday;

	private String datetomodified;

	private boolean booljomonday = true;

	private boolean booljotuesday = true;

	private boolean booljowednesday = true;

	private boolean booljothursday = true;

	private boolean booljofriday = true;

	private boolean booljosaturday = false;

	private boolean booljosunday = false;

	private boolean booljtmonday = true;

	private boolean booljttuesday = true;

	private boolean booljtwednesday = true;

	private boolean booljtthursday = true;

	private boolean booljtfriday = true;

	private boolean booljtsaturday = false;

	private boolean booljtsunday = false;

	private boolean booljfmonday = false;

	private boolean booljftuesday = false;

	private boolean booljfwednesday = false;

	private boolean booljfthursday = false;

	private boolean booljffriday = false;

	private boolean booljfsaturday = false;

	private boolean booljfsunday = false;

	private Date dateToModify = null;

	public String getDateFormat()
	{
		return dateFormat;
	}

	public void setDateFormat(String dateFormat)
	{
		this.dateFormat = dateFormat;
	}

	public String getDatetomodified()
	{
		return datetomodified;
	}

	public void setDatetomodified(String datetomodified)
	{
		this.datetomodified = datetomodified;
	}

	public String getDtfriday()
	{
		return dtfriday;
	}

	public void setDtfriday(String dtfriday)
	{
		this.dtfriday = dtfriday;
	}

	public String getDtmonday()
	{
		return dtmonday;
	}

	public void setDtmonday(String dtmonday)
	{
		this.dtmonday = dtmonday;
	}

	public String getDtsaturday()
	{
		return dtsaturday;
	}

	public void setDtsaturday(String dtsaturday)
	{
		this.dtsaturday = dtsaturday;
	}

	public String getDtsunday()
	{
		return dtsunday;
	}

	public void setDtsunday(String dtsunday)
	{
		this.dtsunday = dtsunday;
	}

	public String getDtthursday()
	{
		return dtthursday;
	}

	public void setDtthursday(String dtthursday)
	{
		this.dtthursday = dtthursday;
	}

	public String getDttuesday()
	{
		return dttuesday;
	}

	public void setDttuesday(String dttuesday)
	{
		this.dttuesday = dttuesday;
	}

	public String getDtwednesday()
	{
		return dtwednesday;
	}

	public void setDtwednesday(String dtwednesday)
	{
		this.dtwednesday = dtwednesday;
	}

	public String getJffriday()
	{
		return jffriday;
	}

	public void setJffriday(String jffriday)
	{
		this.jffriday = jffriday;
	}

	public String getJfmonday()
	{
		return jfmonday;
	}

	public void setJfmonday(String jfmonday)
	{
		this.jfmonday = jfmonday;
	}

	public String getJfsaturday()
	{
		return jfsaturday;
	}

	public void setJfsaturday(String jfsaturday)
	{
		this.jfsaturday = jfsaturday;
	}

	public String getJfsunday()
	{
		return jfsunday;
	}

	public void setJfsunday(String jfsunday)
	{
		this.jfsunday = jfsunday;
	}

	public String getJfthursday()
	{
		return jfthursday;
	}

	public void setJfthursday(String jfthursday)
	{
		this.jfthursday = jfthursday;
	}

	public String getJftuesday()
	{
		return jftuesday;
	}

	public void setJftuesday(String jftuesday)
	{
		this.jftuesday = jftuesday;
	}

	public String getJfwednesday()
	{
		return jfwednesday;
	}

	public void setJfwednesday(String jfwednesday)
	{
		this.jfwednesday = jfwednesday;
	}

	public String getJofriday()
	{
		return jofriday;
	}

	public void setJofriday(String jofriday)
	{
		this.jofriday = jofriday;
	}

	public String getJomonday()
	{
		return jomonday;
	}

	public void setJomonday(String jomonday)
	{
		this.jomonday = jomonday;
	}

	public String getJosaturday()
	{
		return josaturday;
	}

	public void setJosaturday(String josaturday)
	{
		this.josaturday = josaturday;
	}

	public String getJosunday()
	{
		return josunday;
	}

	public void setJosunday(String josunday)
	{
		this.josunday = josunday;
	}

	public String getJothursday()
	{
		return jothursday;
	}

	public void setJothursday(String jothursday)
	{
		this.jothursday = jothursday;
	}

	public String getJotuesday()
	{
		return jotuesday;
	}

	public void setJotuesday(String jotuesday)
	{
		this.jotuesday = jotuesday;
	}

	public String getJowednesday()
	{
		return jowednesday;
	}

	public void setJowednesday(String jowednesday)
	{
		this.jowednesday = jowednesday;
	}

	public String getJtfriday()
	{
		return jtfriday;
	}

	public void setJtfriday(String jtfriday)
	{
		this.jtfriday = jtfriday;
	}

	public String getJtmonday()
	{
		return jtmonday;
	}

	public void setJtmonday(String jtmonday)
	{
		this.jtmonday = jtmonday;
	}

	public String getJtsaturday()
	{
		return jtsaturday;
	}

	public void setJtsaturday(String jtsaturday)
	{
		this.jtsaturday = jtsaturday;
	}

	public String getJtsunday()
	{
		return jtsunday;
	}

	public void setJtsunday(String jtsunday)
	{
		this.jtsunday = jtsunday;
	}

	public String getJtthursday()
	{
		return jtthursday;
	}

	public void setJtthursday(String jtthursday)
	{
		this.jtthursday = jtthursday;
	}

	public String getJttuesday()
	{
		return jttuesday;
	}

	public void setJttuesday(String jttuesday)
	{
		this.jttuesday = jttuesday;
	}

	public String getJtwednesday()
	{
		return jtwednesday;
	}

	public void setJtwednesday(String jtwednesday)
	{
		this.jtwednesday = jtwednesday;
	}

	public String getLangue()
	{
		return langue;
	}

	public void setLangue(String langue)
	{
		this.langue = langue;
	}

	public String getCompanykey()
	{
		return companykey;
	}

	public void setCompanykey(String companykey)
	{
		this.companykey = companykey;
	}

	public String getError()
	{
		return error;
	}

	public void setError(String error)
	{
		this.error = error;
	}

	public GeneriqueConnexionService getService()
	{
		return service;
	}

	public void setService(GeneriqueConnexionService service)
	{
		this.service = service;
	}

	public String getFriday()
	{
		return friday;
	}

	public void setFriday(String friday)
	{
		this.friday = friday;
	}

	public String getMonday()
	{
		return monday;
	}

	public void setMonday(String monday)
	{
		this.monday = monday;
	}

	public String getSaturday()
	{
		return saturday;
	}

	public void setSaturday(String saturday)
	{
		this.saturday = saturday;
	}

	public String getSunday()
	{
		return sunday;
	}

	public void setSunday(String sunday)
	{
		this.sunday = sunday;
	}

	public String getThursday()
	{
		return thursday;
	}

	public void setThursday(String thursday)
	{
		this.thursday = thursday;
	}

	public String getTuesday()
	{
		return tuesday;
	}

	public void setTuesday(String tuesday)
	{
		this.tuesday = tuesday;
	}

	public String getWednesday()
	{
		return wednesday;
	}

	public void setWednesday(String wednesday)
	{
		this.wednesday = wednesday;
	}

	public String getCurrentyear()
	{
		return currentyear;
	}

	public void setCurrentyear(String currentyear)
	{
		this.currentyear = currentyear;
	}

	public String getFirstdayname()
	{
		return firstdayname;
	}

	public void setFirstdayname(String firstdayname)
	{
		this.firstdayname = firstdayname;
	}

	public String getFirstdaynumber()
	{
		return firstdaynumber;
	}

	public void setFirstdaynumber(String firstdaynumber)
	{
		this.firstdaynumber = firstdaynumber;
	}

	public String getFirstweek()
	{
		return firstweek;
	}

	public void setFirstweek(String firstweek)
	{
		this.firstweek = firstweek;
	}

	public String getWeeknumber()
	{
		return weeknumber;
	}

	public void setWeeknumber(String weeknumber)
	{
		this.weeknumber = weeknumber;
	}

	public Map<String, ParamData> getDaysOfWeeksMap()
	{
		return daysOfWeeksMap;
	}

	public void setDaysOfWeeksMap(Map<String, ParamData> daysOfWeeksMap)
	{
		this.daysOfWeeksMap = daysOfWeeksMap;
	}

	public Map<String, ParamData> getHodidaysMap()
	{
		return hodidaysMap;
	}

	public void setHodidaysMap(Map<String, ParamData> hodidaysMap)
	{
		this.hodidaysMap = hodidaysMap;
	}

	/** full constructor */
	public ClsCalendar(Integer idEntreprise, Date jour, String jsem, String ouvr, String fer, Long nsem, Long anne, String trav)
	{
		super(idEntreprise, jour, jsem, ouvr, fer, nsem, anne, trav);
	}

	public ClsCalendar(String _strCompanyKey, String _strLangue, GeneriqueConnexionService _oService)
	{
		this.setService(_oService);
		this.setCompanykey(_strCompanyKey);
		this.setLangue(_strLangue);
		this.__getYear();
		this.__getDayOfWeek();
		this.setDaysOfWeeksMap(this._getWeekDaysMap());
		this.__getFirstDay();
		this.__getFirstWeek();
		this.__getWeekNumber();
		this._loadHodidaysMap();

	}

	public void initCalendar(){
		this.__getYear();
		this.__getDayOfWeek();
		this.setDaysOfWeeksMap(this._getWeekDaysMap());
		this.__getFirstDay();
		this.__getFirstWeek();
		this.__getWeekNumber();
		this._loadHodidaysMap();
	}

	public void initCalendarToModify(String _strYear, String _strDateFormat){
		this.setDateFormat(_strDateFormat);
		this.__getDayOfWeek();
		this.setDatetomodified(_strYear);
		this.__getDateToModified();
		this._loadHodidaysMap();
	}

	public ClsCalendar()
	{
	}

	public ClsCalendar(GeneriqueConnexionService _oService, String _strCompanyKey, String _strLanguage, String _strYear, String _strDateFormat)
	{
		this.setService(_oService);
		this.setCompanykey(_strCompanyKey);
		this.setLangue(_strLanguage);
		this.setDateFormat(_strDateFormat);
		this.__getDayOfWeek();
		this.setDatetomodified(_strYear);
		this.__getDateToModified();
		this._loadHodidaysMap();
	}

	public void convertFromStringToBoolean()
	{
		booljomonday = StringUtils.equals("O", jomonday);
		booljotuesday = StringUtils.equals("O", jotuesday);
		booljowednesday = StringUtils.equals("O", jowednesday);
		booljothursday = StringUtils.equals("O", jothursday);
		booljofriday = StringUtils.equals("O", jofriday);
		booljosaturday = StringUtils.equals("O", josaturday);
		booljosunday = StringUtils.equals("O", josunday);

		booljtmonday = StringUtils.equals("O", jtmonday);
		booljttuesday = StringUtils.equals("O", jttuesday);
		booljtwednesday = StringUtils.equals("O", jtwednesday);
		booljtthursday = StringUtils.equals("O", jtthursday);
		booljtfriday = StringUtils.equals("O", jtfriday);
		booljtsaturday = StringUtils.equals("O", jtsaturday);
		booljtsunday = StringUtils.equals("O", jtsunday);

		booljfmonday = StringUtils.equals("F", jfmonday);
		booljftuesday = StringUtils.equals("F", jftuesday);
		booljfwednesday = StringUtils.equals("F", jfwednesday);
		booljfthursday = StringUtils.equals("F", jfthursday);
		booljffriday = StringUtils.equals("F", jffriday);
		booljfsaturday = StringUtils.equals("F", jfsaturday);
		booljfsunday = StringUtils.equals("F", jfsunday);
	}

	private void _loadHodidaysMap()
	{
		Map<String, ParamData> hodidaysMap = new HashMap<String, ParamData>();
		List<ParamData> liste = this.getService().findAnyColumnFromNomenclature(this.getCompanykey(), this.getLangue(), ClsICalendar._STR_DAY_FERIE, "1");
		ParamData _oNomen = null;
		for (Iterator iter = liste.iterator(); iter.hasNext();)
		{
			_oNomen = (ParamData) iter.next();
			hodidaysMap.put(_oNomen.getCacc(), _oNomen);
		}
		setHodidaysMap(hodidaysMap);
	}

	private void __getDateToModified()
	{
		try
		{
			GregorianCalendar cal = new GregorianCalendar();
			SimpleDateFormat dateFormat = new SimpleDateFormat(this.getDateFormat());
			cal.setTime(dateFormat.parse(this.getDatetomodified()));

			Integer intYear = cal.get(Calendar.YEAR);
			Integer intMonth = cal.get(Calendar.MONTH) + 1;
			Integer intDay = cal.get(Calendar.DAY_OF_MONTH);

			Calendar _dtDateToGenerated = new GregorianCalendar(intYear, intMonth - 1, intDay);

			Long _lgNsem = Long.valueOf(String.valueOf(this.__getWeekNumber(_dtDateToGenerated)));
			Long _lgAnne = Long.valueOf(String.valueOf(intYear));
			//On va chercher le num�ro de la semaine du jour � modifier
			String _strQuery = "FROM CalendrierPaie WHERE idEntreprise = " + "'" + this.getCompanykey() + "'" + " AND to_char(jour, '"+this.getDateFormat()+"') = '" + new ClsDate(this.getDatetomodified(), this.getDateFormat()).getDateS() + "'";
			List<CalendrierPaie> _oCalendarCollection = this.getService().find(_strQuery);
			if (_oCalendarCollection == null || _oCalendarCollection.size() == 0)
				return;
			CalendrierPaie jour = _oCalendarCollection.get(0);
			_lgNsem = jour.getNsem();
			_lgAnne = jour.getAnne();
			
			_strQuery = "FROM CalendrierPaie WHERE idEntreprise = " + "'" + this.getCompanykey() + "'" + " AND nsem = " + _lgNsem + " AND anne = " + _lgAnne;
			ParameterUtil.println(_strQuery);
			_oCalendarCollection = this.getService().find(_strQuery);

			if (_oCalendarCollection == null || _oCalendarCollection.size() == 0)
				return;

			for (int index = 0; index < _oCalendarCollection.size(); index++)
			{
				this.__setCalendarProperty(_oCalendarCollection.get(index));
			}

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private void __setCalendarProperty(CalendrierPaie _oCalendarEntity)
	{
		try
		{
			Calendar _dtDateToGenerated = new GregorianCalendar();
			_dtDateToGenerated.setTime(_oCalendarEntity.getJour());
			int _intYear = _dtDateToGenerated.get(Calendar.YEAR);
			int _intMonth = _dtDateToGenerated.get(Calendar.MONTH) + 1;
			int _intDay = _dtDateToGenerated.get(Calendar.DATE);

			String _strYear;
			if (String.valueOf(_intYear).length() == 1)
				_strYear = "0" + String.valueOf(_intYear);
			else
				_strYear = String.valueOf(_intYear);

			String _strMonth;
			if (String.valueOf(_intMonth).length() == 1)
				_strMonth = "0" + String.valueOf(_intMonth);
			else
				_strMonth = String.valueOf(_intMonth);

			String _strDay;
			if (String.valueOf(_intDay).length() == 1)
				_strDay = "0" + String.valueOf(_intDay);
			else
				_strDay = String.valueOf(_intDay);

//			System.out.println("Date de la semaine="+_intYear+"-"+_intMonth+"-"+_intDay);
//			System.out.println("Jour de la semaine="+_oCalendarEntity.getJsem());

			if (_oCalendarEntity.getJsem().equals(_INT_MOND))
			{
				this.setJomonday(_oCalendarEntity.getOuvr());
				this.setJtmonday(_oCalendarEntity.getTrav());
				this.setJfmonday(_oCalendarEntity.getFer());
				this.setDtmonday(ClsUtilitaire.getDate(this.getService(), this.getCompanykey(), this.getLangue(), _strYear, _strMonth, _strDay));
				// this.setDtmonday(_strDay + "/" + _strMonth + "/" + _strYear);
			}
			else if (_oCalendarEntity.getJsem().equals(_INT_TUES))
			{
				this.setJotuesday(_oCalendarEntity.getOuvr());
				this.setJttuesday(_oCalendarEntity.getTrav());
				this.setJftuesday(_oCalendarEntity.getFer());
				// this.setDttuesday(_strDay + "/" + _strMonth + "/" + _strYear);
				this.setDttuesday(ClsUtilitaire.getDate(this.getService(), this.getCompanykey(), this.getLangue(), _strYear, _strMonth, _strDay));
			}
			else if (_oCalendarEntity.getJsem().equals(_INT_WED))
			{
				this.setJowednesday(_oCalendarEntity.getOuvr());
				this.setJtwednesday(_oCalendarEntity.getTrav());
				this.setJfwednesday(_oCalendarEntity.getFer());
				// this.setDtwednesday(_strDay + "/" + _strMonth + "/" + _strYear);
				this.setDtwednesday(ClsUtilitaire.getDate(this.getService(), this.getCompanykey(), this.getLangue(), _strYear, _strMonth, _strDay));
			}
			else if (_oCalendarEntity.getJsem().equals(_INT_THUR))
			{
				this.setJothursday(_oCalendarEntity.getOuvr());
				this.setJtthursday(_oCalendarEntity.getTrav());
				this.setJfthursday(_oCalendarEntity.getFer());
				// this.setDtthursday(_strDay + "/" + _strMonth + "/" + _strYear);
				this.setDtthursday(ClsUtilitaire.getDate(this.getService(), this.getCompanykey(), this.getLangue(), _strYear, _strMonth, _strDay));
			}
			else if (_oCalendarEntity.getJsem().equals(_INT_FRID))
			{
				this.setJofriday(_oCalendarEntity.getOuvr());
				this.setJtfriday(_oCalendarEntity.getTrav());
				this.setJffriday(_oCalendarEntity.getFer());
				// this.setDtfriday(_strDay + "/" + _strMonth + "/" + _strYear);
				this.setDtfriday(ClsUtilitaire.getDate(this.getService(), this.getCompanykey(), this.getLangue(), _strYear, _strMonth, _strDay));
			}
			else if (_oCalendarEntity.getJsem().equals(_INT_SAT))
			{
				this.setJosaturday(_oCalendarEntity.getOuvr());
				this.setJtsaturday(_oCalendarEntity.getTrav());
				this.setJfsaturday(_oCalendarEntity.getFer());
				// this.setDtsaturday(_strDay + "/" + _strMonth + "/" + _strYear);
				this.setDtsaturday(ClsUtilitaire.getDate(this.getService(), this.getCompanykey(), this.getLangue(), _strYear, _strMonth, _strDay));
			}
			else if (_oCalendarEntity.getJsem().equals(_INT_SUND))
			{
				this.setJosunday(_oCalendarEntity.getOuvr());
				this.setJtsunday(_oCalendarEntity.getTrav());
				this.setJfsunday(_oCalendarEntity.getFer());
				// this.setDtsunday(_strDay + "/" + _strMonth + "/" + _strYear);
				this.setDtsunday(ClsUtilitaire.getDate(this.getService(), this.getCompanykey(), this.getLangue(), _strYear, _strMonth, _strDay));
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void __getYear()
	{
		Session session = null;
		try
		{
			String _strQuery = "SELECT MAX(jour) as joursmax FROM CalendrierPaie WHERE idEntreprise = " + "'" + this.getCompanykey() + "'";
			session = this.getService().getSession();

			List lst = session.createSQLQuery(_strQuery).list();
			if (!lst.isEmpty())
			{
				System.out.println("VALEUR RESULTAT="+lst.get(0));
				ClsDate dt = null;
				if(lst.get(0)!=null)
					dt = new ClsDate(lst.get(0));
				else {
					dt = new ClsDate(new Date());
					dt = new ClsDate(dt.getLastDayOfYear());
				}
				this.setCurrentyear((dt.getYear() + 1)+"");
				// cas de la suppression du calendrier, ann�e � proposer
				this.setCurrentyeartodelete(dt.getYear()+"");
			}
			else
			{
				_strQuery = "Select ddmp From DossierPaie where idEntreprise = '" + this.getCompanykey() + "'";
				lst = session.createSQLQuery(_strQuery).list();
				if (!lst.isEmpty() && lst.get(0) != null)
				{
					this.setCurrentyear(new ClsDate(lst.get(0)).getYear()+"");
					// cas de la suppression du calendrier, ann�e � proposer
					this.setCurrentyeartodelete((new ClsDate(lst.get(0)).getYear() - 1)+"");
				}
				else
				{
					this.setCurrentyear(new ClsDate(new Date()).getYear()+"");
					// cas de la suppression du calendrier, ann�e � proposer
					this.setCurrentyeartodelete((new ClsDate(new Date()).getYear() - 1)+"");
				}
			}
		}
		catch (Exception e)
		{
			this.__initYear();
			e.printStackTrace();
		}
		finally
		{
			service.closeSession(session);
		}
	}

	private void __initYear()
	{
		DateFormat _oFormat = DateFormat.getDateInstance(DateFormat.SHORT);
		String _strDate = _oFormat.format(new Date());
		_strDate = _strDate.substring(6, 8);
		this.setCurrentyear("20" + _strDate);
		this.setCurrentyeartodelete("20" + _strDate);
	}

	public void __getDayOfWeek()
	{
		try
		{
			List<ParamData> _oDayOfWeek = this.getService().findAnyByOrderColumnFromNomenclature(this.getCompanykey(), this.getLangue(), ClsICalendar._STR_DAY_LABEL, "1", "cacc");
			if (_oDayOfWeek == null || _oDayOfWeek.size() == 0)
				return;
			this.setMonday(_oDayOfWeek.get(0).getVall());
			this.setTuesday(_oDayOfWeek.get(1).getVall());
			this.setWednesday(_oDayOfWeek.get(2).getVall());
			this.setThursday(_oDayOfWeek.get(3).getVall());
			this.setFriday(_oDayOfWeek.get(4).getVall());
			this.setSaturday(_oDayOfWeek.get(5).getVall());
			this.setSunday(_oDayOfWeek.get(6).getVall());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void __getFirstDay()
	{
		try
		{

			Calendar dtDateToGenerated = new GregorianCalendar(Integer.parseInt(this.getCurrentyear()), Calendar.JANUARY, 1);

			int _intDayNumber = dtDateToGenerated.get(Calendar.DAY_OF_WEEK);

			_intDayNumber--;
			if (_intDayNumber == 0)
				_intDayNumber = 7;
			
			String dayName = String.valueOf(_intDayNumber);
			ParamData firstDay = this.getDaysOfWeeksMap().get(dayName.toUpperCase());
			if (firstDay == null)
				firstDay = new ParamData();
			this.setFirstdaynumber(firstDay.getCacc());
			this.setFirstdayname(firstDay.getVall());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private String __getGregorianDayName(int _intDayNumber)
	{

		if (_intDayNumber == 2)
			return "MONDAY";
		if (_intDayNumber == 3)
			return "TUESDAY";
		if (_intDayNumber == 4)
			return "WEDNESDAY";
		if (_intDayNumber == 5)
			return "THURSDAY";
		if (_intDayNumber == 6)
			return "FRIDAY";
		if (_intDayNumber == 7)
			return "SATURDAY";
		if (_intDayNumber == 1)
			return "SUNDAY";

		return "UNKNOWN";
	}

	private Map<String, ParamData> _getWeekDaysMap()
	{
		Map<String, ParamData> map = new HashMap<String, ParamData>();
		List<ParamData> nome = this.getService().findAnyColumnFromNomenclature(this.getCompanykey(), this.getLangue(), ClsICalendar._STR_DAY_LABEL, "1");
		ClsNomenclature oNome = null;
		for (int i = 0; i < nome.size(); i++)
		{
			map.put(nome.get(i).getCacc().trim().toUpperCase(), nome.get(i));
		}
		return map;
	}

	private String __getDayName(int _intDayNumber)
	{
		ParamData day = this.getService().findAnyColumnFromNomenclature(this.getCompanykey(), this.getLangue(), ClsICalendar._STR_DAY_LABEL, String.valueOf(_intDayNumber), "1");

		return day.getVall();
	}

	private String __getDayName(int _intDayNumber, Calendar dtDateToGenerated)
	{

		if (_intDayNumber == dtDateToGenerated.MONDAY)
			return "MONDAY";
		if (_intDayNumber == dtDateToGenerated.TUESDAY)
			return "TUESDAY";
		if (_intDayNumber == dtDateToGenerated.WEDNESDAY)
			return "WEDNESDAY";
		if (_intDayNumber == dtDateToGenerated.THURSDAY)
			return "THURSDAY";
		if (_intDayNumber == dtDateToGenerated.FRIDAY)
			return "FRIDAY";
		if (_intDayNumber == dtDateToGenerated.SATURDAY)
			return "SATURDAY";
		if (_intDayNumber == dtDateToGenerated.SUNDAY)
			return "SUNDAY";

		return "UNKNOWN";
	}

	private void __getFirstWeek()
	{
		this.setFirstweek(getFirstWeekOfYear(this.getCurrentyear()));
	}

	private void __getWeekNumber()
	{

		this.setWeeknumber(getNumberOfWeeksInYear(this.getCurrentyear()));
	}

	public String getNumberOfWeeksInYear(String year)
	{

		Calendar gc = new GregorianCalendar(Integer.parseInt(year), Calendar.DECEMBER, 31);
		if (gc.get(Calendar.WEEK_OF_YEAR) == 1)
			gc = new GregorianCalendar(Integer.parseInt(year), Calendar.DECEMBER, 24);
		Integer max = gc.get(Calendar.WEEK_OF_YEAR);
		return String.valueOf(max);

	}

	public String getFirstWeekOfYear(String year)
	{

		Calendar gc = new GregorianCalendar(Integer.parseInt(year), Calendar.JANUARY, 1);

		return String.valueOf(gc.get(Calendar.WEEK_OF_YEAR));

	}

	private boolean __isHolidayDay(String _strDay)
	{

		if (this.getHodidaysMap().containsKey(_strDay))
			return true;

		return false;
	}

	public boolean _existCalendar()
	{

		Integer _intYear = Integer.parseInt(this.getCurrentyear());
		String _strQuery = "From CalendrierPaie where anne = " + _intYear + " and idEntreprise='" + this.getCompanykey() + "'";

		if (this.getService().find(_strQuery).size() > 100)
			return true;
		else
			return false;
	}

	public boolean __save()
	{
		List<CalendrierPaie> liste = new ArrayList<CalendrierPaie>();
		try
		{
			Integer _intYear = Integer.parseInt(this.getCurrentyear());
			Calendar _dtDateToGenerated = new GregorianCalendar(Integer.parseInt(this.getCurrentyear()), Calendar.JANUARY, 1);
			Calendar _dtDateToGeneratedAfter = null;
			CalendrierPaie _oCalendarEntity = null;
			int i = 0;

			Long _lgNsem = null;
			Long _lgAnne = null;
			while (this.__isCurrentYear(_dtDateToGenerated, _intYear))
			{
				try
				{
					/** ************************************************ */
					_lgNsem = Long.valueOf(String.valueOf(this.__getWeekNumber(_dtDateToGenerated)));

					_lgAnne = Long.valueOf(String.valueOf(this.getCurrentyear()));

					if (_lgNsem > Long.valueOf(this.getWeeknumber()))
					{
						_dtDateToGeneratedAfter = new GregorianCalendar(Integer.parseInt(this.getCurrentyear()) + 1, _dtDateToGenerated.get(Calendar.JANUARY), 1);
						// _lgAnne = _lgAnne + 1;
					}

					/** ************************************************ */
					_oCalendarEntity = this.__getCalendarEntity(_dtDateToGenerated, _dtDateToGeneratedAfter);
					if (_oCalendarEntity != null)
					{
						liste.add(_oCalendarEntity);
						// this.getService().save(_oCalendarEntity);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					continue;
				}
				finally
				{
					_dtDateToGenerated = new GregorianCalendar(Integer.parseInt(this.getCurrentyear()), _dtDateToGenerated.get(Calendar.MONTH), _dtDateToGenerated.get(Calendar.DATE) + 1);
				}
			}

			this.getService().saveCollection(liste);

			return true;
		}
		catch (Exception e)
		{
			return false;
		}

	}

	private CalendrierPaie __getCalendarEntity(Calendar _dtDateToGenerated, Calendar _dtDateToGeneratedAfter)
	{
		CalendrierPaie _oCalendarEntity = null;
		//RhpcalendrierPK _oRhcalPK = null;
		String _strJsem = null;
		String _strOuvr = null;
		String _strFer = null;
		Long _lgNsem = null;
		Long _lgAnne = null;
		String _strTrav = null;
		Date _dtDay = null;

		try
		{
			if (_dtDateToGenerated == null)
				return _oCalendarEntity;

			_dtDay = _dtDateToGenerated.getTime();
			_dtDay.setHours(0);
			_dtDay.setMinutes(0);
			_dtDay.setSeconds(0);

			try
			{
				_strJsem = String.valueOf(this.__getDayNumber(_dtDateToGenerated.get(Calendar.DAY_OF_WEEK)));
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}

			try
			{
				_strOuvr = this.__getDayWorkingStatus(Integer.parseInt(_strJsem));
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}

			try
			{

				int _intMonth = _dtDateToGenerated.get(Calendar.MONTH) + 1;
				int _intDay = _dtDateToGenerated.get(Calendar.DATE);

				String _strMonth;
				if (String.valueOf(_intMonth).length() == 1)
					_strMonth = "0" + String.valueOf(_intMonth);
				else
					_strMonth = String.valueOf(_intMonth);

				String _strDay;
				if (String.valueOf(_intDay).length() == 1)
					_strDay = "0" + String.valueOf(_intDay);
				else
					_strDay = String.valueOf(_intDay);
				if (this.__isHolidayDay(_strDay + "/" + _strMonth))
					_strFer = "F";
				else
					_strFer = "N";
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}

			try
			{
				_strTrav = this.__getDayWorkedStatus(Integer.parseInt(_strJsem));
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}

			try
			{
				_lgNsem = Long.valueOf(String.valueOf(this.__getWeekNumber(_dtDateToGenerated)));
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}

			try
			{
				_lgAnne = Long.valueOf(String.valueOf(this.getCurrentyear()));

			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			/*
			 * if(_lgNsem>Long.valueOf(this.getWeeknumber())){ _lgAnne = _lgAnne + 1; _lgNsem =
			 * Long.valueOf(String.valueOf(this.__getWeekNumber(_dtDateToGeneratedAfter))); } else{ if(_dtDateToGenerated.get(Calendar.MONTH)+1==12 && _lgNsem<
			 * 52) _lgAnne = _lgAnne + 1; }
			 */
			if ((_dtDay.getMonth() + 1) == 12 && _lgNsem == 1)
			{
				_lgAnne = _lgAnne + 1;
			}
			//_oRhcalPK = new RhpcalendrierPK(this.getCompanykey(), _dtDay);

			_oCalendarEntity = new CalendrierPaie(new Integer(this.getCompanykey()), _dtDay, _strJsem, _strOuvr, _strFer, _lgNsem, _lgAnne, _strTrav);

			return _oCalendarEntity;
		}
		catch (Exception e)
		{
			// TODO: handle exception
			return _oCalendarEntity;
		}
	}

	private int __getDayNumber(Integer _intDay)
	{
		if (_intDay == 1)
			return 7;
		else
			return _intDay - 1;
	}

	private int __getWeekNumber(Calendar _dtDateToGenerated)
	{
		return _dtDateToGenerated.get(Calendar.WEEK_OF_YEAR);
	}

	private int __getYearNumber(Calendar _dtDateToGenerated)
	{
		return _dtDateToGenerated.get(Calendar.YEAR);
	}

	private String __getDayWorkingStatus(Integer _intDayNumber)
	{
		if (_intDayNumber == 1)
			return this.getJomonday();
		if (_intDayNumber == 2)
			return this.getJotuesday();
		if (_intDayNumber == 3)
			return this.getJowednesday();
		if (_intDayNumber == 4)
			return this.getJothursday();
		if (_intDayNumber == 5)
			return this.getJofriday();
		if (_intDayNumber == 6)
			return this.getJosaturday();
		if (_intDayNumber == 7)
			return this.getJosunday();

		return "UNKNOWN";
	}

	private String __getDayWorkedStatus(Integer _intDayNumber)
	{
		if (_intDayNumber == 1)
			return this.getJtmonday();
		if (_intDayNumber == 2)
			return this.getJttuesday();
		if (_intDayNumber == 3)
			return this.getJtwednesday();
		if (_intDayNumber == 4)
			return this.getJtthursday();
		if (_intDayNumber == 5)
			return this.getJtfriday();
		if (_intDayNumber == 6)
			return this.getJtsaturday();
		if (_intDayNumber == 7)
			return this.getJtsunday();

		return "UNKNOWN";
	}

	private boolean __isCurrentYear(Calendar dtDateToGenerated, Integer _intYear)
	{
		if (dtDateToGenerated == null)
			return false;

		if (dtDateToGenerated.get(Calendar.YEAR) == _intYear)
			return true;

		return false;
	}

	public boolean __update()
	{
		try
		{
			if (this.getDtmonday() != null && this.getDtmonday().trim().length() != 0)
				this.__updateCalendarEntity(this.getDtmonday(), this.getJomonday(), this.getJtmonday(), this.getJfmonday());

			if (this.getDttuesday() != null && this.getDttuesday().trim().length() != 0)
				this.__updateCalendarEntity(this.getDttuesday(), this.getJotuesday(), this.getJttuesday(), this.getJftuesday());

			if (this.getDtwednesday() != null && this.getDtwednesday().trim().length() != 0)
				this.__updateCalendarEntity(this.getDtwednesday(), this.getJowednesday(), this.getJtwednesday(), this.getJfwednesday());

			if (this.getDtthursday() != null && this.getDtthursday().trim().length() != 0)
				this.__updateCalendarEntity(this.getDtthursday(), this.getJothursday(), this.getJtthursday(), this.getJfthursday());

			if (this.getDtfriday() != null && this.getDtfriday().trim().length() != 0)
				this.__updateCalendarEntity(this.getDtfriday(), this.getJofriday(), this.getJtfriday(), this.getJffriday());

			if (this.getDtsaturday() != null && this.getDtsaturday().trim().length() != 0)
				this.__updateCalendarEntity(this.getDtsaturday(), this.getJosaturday(), this.getJtsaturday(), this.getJfsaturday());

			if (this.getDtsunday() != null && this.getDtsunday().trim().length() != 0)
				this.__updateCalendarEntity(this.getDtsunday(), this.getJosunday(), this.getJtsunday(), this.getJfsunday());

			return true;
		}
		catch (Exception e)
		{
			// TODO: handle exception
			return false;
		}
	}

	private void __updateCalendarEntity(String _strDate, String _strWorkingStatus, String _strWorkedStatus, String _strHolidayDay)
	{

		try
		{
			String _strQuery = "UPDATE CalendrierPaie SET ouvr = " + "'" + _strWorkingStatus + "'" + ", fer = " + "'" + _strHolidayDay + "'" + ", trav = " + "'" + _strWorkedStatus + "'" + " WHERE idEntreprise = " + "'"
					+ this.getCompanykey() + "'" + " AND to_char(jour, '"+getDateFormat()+"') = " + "'" + _strDate + "'";

			service.updateFromTable(_strQuery);
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{

		}
	}

	public static boolean __delete(GeneriqueConnexionService _oService, String _strCompanyKey, String _strLanguage, String _strYear)
	{
//		String _strFirstDayOfYear = ClsUtilitaire.getDate(_oService, _strCompanyKey, _strLanguage, _strYear, "01", "01");
//		String _strLastDayOfYear = ClsUtilitaire.getDate(_oService, _strCompanyKey, _strLanguage, _strYear, "12", "31");
//		String _strQuery = "DELETE FROM Rhpcalendrier WHERE jour >= '" + _strFirstDayOfYear + "' AND jour <='" + _strLastDayOfYear + "' AND cdos = " + "'" + _strCompanyKey + "'";
		String _strQuery = "DELETE FROM CalendrierPaie WHERE anne = '" + _strYear + "' AND idEntreprise="+_strCompanyKey;
		Session session = null;
		try
		{
			session = _oService.getSession();
			session.createSQLQuery(_strQuery).executeUpdate();

			return true;
		}
		catch (Exception e)
		{
			// TODO: handle exception
			return false;
		}
		finally
		{
			_oService.closeSession(session);
		}

	}

	public void convertFromBooleanToString()
	{
		if (booljomonday)
			jomonday = "O";
		else
			jomonday = "N";

		if (booljotuesday)
			jotuesday = "O";
		else
			jotuesday = "N";

		if (booljowednesday)
			jowednesday = "O";
		else
			jowednesday = "N";

		if (booljothursday)
			jothursday = "O";
		else
			jothursday = "N";

		if (booljofriday)
			jofriday = "O";
		else
			jofriday = "N";

		if (booljosaturday)
			josaturday = "O";
		else
			josaturday = "N";

		if (booljosunday)
			josunday = "O";
		else
			josunday = "N";

		if (booljtmonday)
			jtmonday = "O";
		else
			jtmonday = "N";

		if (booljttuesday)
			jttuesday = "O";
		else
			jttuesday = "N";

		if (booljtwednesday)
			jtwednesday = "O";
		else
			jtwednesday = "N";

		if (booljtthursday)
			jtthursday = "O";
		else
			jtthursday = "N";

		if (booljtfriday)
			jtfriday = "O";
		else
			jtfriday = "N";

		if (booljtsaturday)
			jtsaturday = "O";
		else
			jtsaturday = "N";

		if (booljtsunday)
			jtsunday = "O";
		else
			jtsunday = "N";

		if (booljfmonday)
			jfmonday = "F";
		else
			jfmonday = "N";

		if (booljftuesday)
			jftuesday = "F";
		else
			jftuesday = "N";

		if (booljfwednesday)
			jfwednesday = "F";
		else
			jfwednesday = "N";

		if (booljfthursday)
			jfthursday = "F";
		else
			jfthursday = "N";

		if (booljffriday)
			jffriday = "F";
		else
			jffriday = "N";

		if (booljfsaturday)
			jfsaturday = "F";
		else
			jfsaturday = "N";

		if (booljfsunday)
			jfsunday = "F";
		else
			jfsunday = "N";
	}

	public String getCurrentyeartodelete()
	{
		return currentyeartodelete;
	}

	public void setCurrentyeartodelete(String currentyeartodelete)
	{
		this.currentyeartodelete = currentyeartodelete;
	}

	public boolean isBooljomonday()
	{
		return booljomonday;
	}

	public void setBooljomonday(boolean booljomonday)
	{
		this.booljomonday = booljomonday;
	}

	public boolean isBooljotuesday()
	{
		return booljotuesday;
	}

	public void setBooljotuesday(boolean booljotuesday)
	{
		this.booljotuesday = booljotuesday;
	}

	public boolean isBooljowednesday()
	{
		return booljowednesday;
	}

	public void setBooljowednesday(boolean booljowednesday)
	{
		this.booljowednesday = booljowednesday;
	}

	public boolean isBooljothursday()
	{
		return booljothursday;
	}

	public void setBooljothursday(boolean booljothursday)
	{
		this.booljothursday = booljothursday;
	}

	public boolean isBooljofriday()
	{
		return booljofriday;
	}

	public void setBooljofriday(boolean booljofriday)
	{
		this.booljofriday = booljofriday;
	}

	public boolean isBooljosaturday()
	{
		return booljosaturday;
	}

	public void setBooljosaturday(boolean booljosaturday)
	{
		this.booljosaturday = booljosaturday;
	}

	public boolean isBooljosunday()
	{
		return booljosunday;
	}

	public void setBooljosunday(boolean booljosunday)
	{
		this.booljosunday = booljosunday;
	}

	public boolean isBooljtmonday()
	{
		return booljtmonday;
	}

	public void setBooljtmonday(boolean booljtmonday)
	{
		this.booljtmonday = booljtmonday;
	}

	public boolean isBooljttuesday()
	{
		return booljttuesday;
	}

	public void setBooljttuesday(boolean booljttuesday)
	{
		this.booljttuesday = booljttuesday;
	}

	public boolean isBooljtwednesday()
	{
		return booljtwednesday;
	}

	public void setBooljtwednesday(boolean booljtwednesday)
	{
		this.booljtwednesday = booljtwednesday;
	}

	public boolean isBooljtthursday()
	{
		return booljtthursday;
	}

	public void setBooljtthursday(boolean booljtthursday)
	{
		this.booljtthursday = booljtthursday;
	}

	public boolean isBooljtfriday()
	{
		return booljtfriday;
	}

	public void setBooljtfriday(boolean booljtfriday)
	{
		this.booljtfriday = booljtfriday;
	}

	public boolean isBooljtsaturday()
	{
		return booljtsaturday;
	}

	public void setBooljtsaturday(boolean booljtsaturday)
	{
		this.booljtsaturday = booljtsaturday;
	}

	public boolean isBooljtsunday()
	{
		return booljtsunday;
	}

	public void setBooljtsunday(boolean booljtsunday)
	{
		this.booljtsunday = booljtsunday;
	}

	public boolean isBooljfmonday()
	{
		return booljfmonday;
	}

	public void setBooljfmonday(boolean booljfmonday)
	{
		this.booljfmonday = booljfmonday;
	}

	public boolean isBooljftuesday()
	{
		return booljftuesday;
	}

	public void setBooljftuesday(boolean booljftuesday)
	{
		this.booljftuesday = booljftuesday;
	}

	public boolean isBooljfwednesday()
	{
		return booljfwednesday;
	}

	public void setBooljfwednesday(boolean booljfwednesday)
	{
		this.booljfwednesday = booljfwednesday;
	}

	public boolean isBooljfthursday()
	{
		return booljfthursday;
	}

	public void setBooljfthursday(boolean booljfthursday)
	{
		this.booljfthursday = booljfthursday;
	}

	public boolean isBooljffriday()
	{
		return booljffriday;
	}

	public void setBooljffriday(boolean booljffriday)
	{
		this.booljffriday = booljffriday;
	}

	public boolean isBooljfsaturday()
	{
		return booljfsaturday;
	}

	public void setBooljfsaturday(boolean booljfsaturday)
	{
		this.booljfsaturday = booljfsaturday;
	}

	public boolean isBooljfsunday()
	{
		return booljfsunday;
	}

	public void setBooljfsunday(boolean booljfsunday)
	{
		this.booljfsunday = booljfsunday;
	}

	public Date getDateToModify()
	{
		return dateToModify;
	}

	public void setDateToModify(Date dateToModify)
	{
		this.dateToModify = dateToModify;
	}

}
