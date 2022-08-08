package com.kinart.organisation.business.services;

import com.kinart.organisation.business.model.Orgniveau;

import java.util.List;

public interface IOrganigramme {

	public List getObjects(String strSQL);
	public List<Orgniveau> loadAll(Class entityClass);
	
}
