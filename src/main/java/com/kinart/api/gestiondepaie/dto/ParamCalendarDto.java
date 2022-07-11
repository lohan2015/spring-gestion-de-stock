package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.CalendrierPaie;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.services.calendrier.ClsCalendar;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model de gestion des calendriers")
public class ParamCalendarDto
{
	private Date jour;

	private String jsem;

	private String ouvr;

	private String fer;

	private Long nsem;

	private Long anne;

	private String trav;

	private String companykey = null;

	private String dateFormat = null;

	private String langue = null;

	private String error = null;

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

}
