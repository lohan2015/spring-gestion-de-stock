package com.kinart.paie.business.services.cloture;

import javax.servlet.http.HttpServletRequest;

public interface IOGL
{

	public abstract void generateOGL(HttpServletRequest request);
	public abstract String getErrmess1();
	public abstract String getErrmess2();
	public abstract void setNddd(Integer nddd);
}
