package com.kinart.organisation.business.services.competence;

import com.kinart.api.organisation.dto.ElementDto;
import com.kinart.organisation.business.model.Orgposteinfo;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CompetencePosteVO extends Orgposteinfo
{

	private String libelleinfo1 = null;

	private String libelleinfo2 = null;

	private String libelleinfo3 = null;
	
	private String libelletyperec = null;
	
	private List<ElementDto> typerec = new ArrayList<ElementDto>();

	boolean selected = false;

	CommonFunctions.Mode mode;

	private boolean hasbeenmodify = false; // permet de savoir si le vo a subit une modification

	public CompetencePosteVO(String cdos, String codeEntite, String typeInfo)
	{
		super();
		this.setIdEntreprise(new Integer(cdos));
		this.setCodeposte(codeEntite);
		this.setTypeinfo(typeInfo);
		typerec.add(new ElementDto("I", "Interne"));
		typerec.add(new ElementDto("E", "Externe"));
		typerec.add(new ElementDto("M", "Mixte"));
	}
	
	public CompetencePosteVO()
	{
		super();
		typerec.add(new ElementDto("I", "Interne"));
		typerec.add(new ElementDto("E", "Externe"));
		typerec.add(new ElementDto("M", "Mixte"));
	}

	public CompetencePosteVO(Integer identreprise, String codeposte, String typeinfo, String codeinfo1,
							 String codeinfo2, String codeinfo3, Integer valminfo1)
	{
		super();
		this.setIdEntreprise(identreprise);
		this.setCodeposte(codeposte);
		this.setTypeinfo(typeinfo);
		this.setCodeinfo1(codeinfo1);
		this.setCodeinfo2(codeinfo2);
		this.setCodeinfo3(codeinfo3);
		this.setValminfo1(new BigDecimal(valminfo1));
		typerec.add(new ElementDto("I", "Interne"));
		typerec.add(new ElementDto("E", "Externe"));
		typerec.add(new ElementDto("M", "Mixte"));
	}

	public String getLibelleinfo1()
	{
		return libelleinfo1;
	}

	public void setLibelleinfo1(String libelleinfo1)
	{
		this.libelleinfo1 = libelleinfo1;
	}

	public String getLibelleinfo2()
	{
		return libelleinfo2;
	}

	public void setLibelleinfo2(String libelleinfo2)
	{
		this.libelleinfo2 = libelleinfo2;
	}

	public String getLibelleinfo3()
	{
		return libelleinfo3;
	}

	public void setLibelleinfo3(String libelleinfo3)
	{
		this.libelleinfo3 = libelleinfo3;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	public void setHasbeenmodify(boolean hasbeenmodify)
	{
		this.hasbeenmodify = hasbeenmodify;
	}

	public List<ElementDto> getTyperec() {
		return typerec;
	}

	public void setTyperec(List<ElementDto> typerec) {
		this.typerec = typerec;
	}

	public String getLibelletyperec() {
		if(StringUtils.isNotEmpty(getCoeff())){
			if("I".equalsIgnoreCase(getCoeff())) libelletyperec="Interne";
			else if("E".equalsIgnoreCase(getCoeff())) libelletyperec="Externe";
			else if("M".equalsIgnoreCase(getCoeff())) libelletyperec="Mixte";
		}
		return libelletyperec;
	}
	
}
