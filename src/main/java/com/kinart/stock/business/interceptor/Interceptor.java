package com.kinart.stock.business.interceptor;

import org.hibernate.EmptyInterceptor;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

public class Interceptor extends EmptyInterceptor {

  @Override
  public String onPrepareStatement(String sql) {
    if (StringUtils.hasLength(sql) && sql.toLowerCase().startsWith("select")) {
      // select utilisateu0_.
      try{
        final String entityName = sql.substring(7, sql.indexOf("."));
        final String idEntreprise = MDC.get("idEntreprise");
        if (StringUtils.hasLength(entityName)
                && !entityName.toLowerCase().contains("entreprise")
                && !entityName.toLowerCase().contains("roles")
                && StringUtils.hasLength(idEntreprise)) {

          if(!sql.contains("ORDER BY") && !sql.contains("order by")){
            if (sql.contains("where") || sql.contains("WHERE")) {
              sql = sql + " and " + entityName + ".identreprise = " + idEntreprise;
            } else {
              sql = sql + " where " + entityName + ".identreprise = " + idEntreprise;
            }
          }
        }
      } catch(StringIndexOutOfBoundsException e){
        System.out.println("Erreur index: "+e.getMessage());
      }
    }
    return super.onPrepareStatement(sql);
  }
}
