package com.kinart.api.gestiondepaie.report.service.impl;

import com.kinart.api.gestiondepaie.dto.ParamEFCMRDto;
import com.kinart.api.gestiondepaie.report.LigneDeclarationVersement;
import com.kinart.api.gestiondepaie.report.service.EFCMRService;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.NumberUtils;
import com.kinart.paie.business.services.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EFCMRServiceImpl implements EFCMRService {
    /**
     * @param service
     * @param dto
     * @return
     */
    @Override
    public LigneDeclarationVersement loadDataDeclarationVersement(GeneriqueConnexionService service, ParamEFCMRDto dto) {
        String DVCNPS="0";
        String DVIRPP="0";
        String DVCACIRPP="0";
        String DVCFCSAL="0";
        String DVCFCPAT="0";
        String DVTD="0";
        String DVRAV="0";
        String DVFNE="0";

        String query = "From ParamData where idEntreprise='" + dto.getIdEntreprise() + "' and cacc like 'DECL-VERS%' and ctab = " + 99;
        List<ParamData> fnoms = service.find(query);
        String cacc;
        Integer index = 0;
        String[] split = null;
        String formule = null;
        for (ParamData nome : fnoms)
        {
            formule = StringUtils.EMPTY;
            cacc = nome.getCacc();
            index = nome.getNume();
            split = StringUtils.split(nome.getVall(), "+");
            if(split == null)
                continue;

            for (String str2 : split)
                if(NumberUtils.isInteger(str2))
                    formule = StringUtils.isBlank(formule) ? "'" + str2 + "'" : formule + ",'" + str2 + "'";
            //Nume 1 : CNPS
            if(StringUtils.equals("DECL-VERS1", cacc))
            {
                if(index==1) DVCNPS = StringUtil.nvl(formule,"0");
                if(index==2) DVIRPP = StringUtil.nvl(formule,"0");
                if(index==3) DVCACIRPP = StringUtil.nvl(formule,"0");
                if(index==4) DVCFCSAL = StringUtil.nvl(formule,"0");
                if(index==5) DVCFCPAT = StringUtil.nvl(formule,"0");
                if(index==6) DVTD = StringUtil.nvl(formule,"0");
            }

            if(StringUtils.equals("DECL-VERS2", cacc))
            {
                if(index==1) DVRAV = StringUtil.nvl(formule,"0");
                if(index==2) DVFNE = StringUtil.nvl(formule,"0");
            }

        }

        String queryString=" SELECT '1' as RUBQ, '1' AS RUBCNPS, '1' AS RUBIRPP, '1' AS RUBCFCSAL, '1' AS RUBCFCPAT, '1' AS RUBTC, '1' AS RUBRAV, '1' AS RUBFNE, '1' AS RUBCAC,'1' AS RUBREGIRPP, 1 AS MONT ";
        queryString+=", SUM(MNTCNPS) AS MNTCNPS,SUM(MNTIRPP) AS MNTIRPP,SUM(MNTCACIRPP) AS MNTCACIRPP,SUM(MNTCFCSAL) AS MNTCFCSAL,SUM(MNTCFCPAT) AS MNTCFCPAT,SUM(MNTTD) AS MNTTD,SUM(MNTRAV) AS MNTRAV,SUM(MNTFNE) AS MNTFNE ";
        queryString+=" FROM (";
        queryString+=" SELECT  '1' as RUBQ, '1' AS RUBCNPS, '1' AS RUBIRPP, '1' AS RUBCFCSAL, '1' AS RUBCFCPAT, '1' AS RUBTC, '1' AS RUBRAV, '1' AS RUBFNE, '1' AS RUBCAC,'1' AS RUBREGIRPP, 1 AS MONT   ";
        queryString+=", (select coalesce(SUM(MONT),0) from CumulPaie where idEntreprise=agent.idEntreprise and nmat=agent.nmat and nbul>0 and aamm='"+dto.getPeriodePaie()+"' and rubq in ("+DVCNPS+")) MNTCNPS ";
        queryString+=", (select coalesce(SUM(MONT),0) from CumulPaie where idEntreprise=agent.idEntreprise and nmat=agent.nmat and nbul>0 and aamm='"+dto.getPeriodePaie()+"' and rubq in ("+DVIRPP+")) MNTIRPP ";
        queryString+=", (select coalesce(SUM(MONT),0) from CumulPaie where idEntreprise=agent.idEntreprise and nmat=agent.nmat and nbul>0 and aamm='"+dto.getPeriodePaie()+"' and rubq in ("+DVCACIRPP+")) MNTCACIRPP ";
        queryString+=", (select coalesce(SUM(MONT),0) from CumulPaie where idEntreprise=agent.idEntreprise and nmat=agent.nmat and nbul>0 and aamm='"+dto.getPeriodePaie()+"' and rubq in ("+DVCFCSAL+")) MNTCFCSAL ";
        queryString+=", (select coalesce(SUM(MONT),0) from CumulPaie where idEntreprise=agent.idEntreprise and nmat=agent.nmat and nbul>0 and aamm='"+dto.getPeriodePaie()+"' and rubq in ("+DVCFCPAT+")) MNTCFCPAT ";
        queryString+=", (select coalesce(SUM(MONT),0) from CumulPaie where idEntreprise=agent.idEntreprise and nmat=agent.nmat and nbul>0 and aamm='"+dto.getPeriodePaie()+"' and rubq in ("+DVTD+")) MNTTD ";
        queryString+=", (select coalesce(SUM(MONT),0) from CumulPaie where idEntreprise=agent.idEntreprise and nmat=agent.nmat and nbul>0 and aamm='"+dto.getPeriodePaie()+"' and rubq in ("+DVRAV+")) MNTRAV ";
        queryString+=", (select coalesce(SUM(MONT),0) from CumulPaie where idEntreprise=agent.idEntreprise and nmat=agent.nmat and nbul>0 and aamm='"+dto.getPeriodePaie()+"' and rubq in ("+DVFNE+")) MNTFNE ";
        queryString+=" FROM Salarie agent ";
        queryString+=" LEFT JOIN ParamData N1 ON (N1.idEntreprise = agent.idEntreprise) AND ( N1.CTAB = 5 ) AND ( N1.NUME = 1 )  AND (N1.CACC = agent.VILD) ";

        queryString+=" WHERE agent.idEntreprise = '"+dto.getIdEntreprise()+"'";


        if(StringUtils.isNotEmpty(dto.getVildMin())) queryString+=" AND agent.VILD >='"+dto.getVildMin()+"'";
        if(StringUtils.isNotEmpty(dto.getVildMax())) queryString+="  AND agent.VILD <= '"+dto.getVildMax()+"'";

        queryString+=" ) test ";
        Session session = service.getSession();
        Query q = session.createSQLQuery(queryString);
        List<Object[]> lst = q.getResultList();
        service.closeSession(session);

        LigneDeclarationVersement declarationVersement = new LigneDeclarationVersement();
        for (Object[] o : lst)
        {
            declarationVersement.setCnps(o[11]!=null?new BigDecimal(o[11].toString()):BigDecimal.ZERO);
            declarationVersement.setIrpp(o[12]!=null?new BigDecimal(o[12].toString()):BigDecimal.ZERO);
            declarationVersement.setCacirpp(o[13]!=null?new BigDecimal(o[13].toString()):BigDecimal.ZERO);
            declarationVersement.setCfcsal(o[14]!=null?new BigDecimal(o[14].toString()):BigDecimal.ZERO);
            declarationVersement.setCfcpat(o[15]!=null?new BigDecimal(o[15].toString()):BigDecimal.ZERO);
            declarationVersement.setTd(o[16]!=null?new BigDecimal(o[16].toString()):BigDecimal.ZERO);
            declarationVersement.setRav(o[17]!=null?new BigDecimal(o[17].toString()):BigDecimal.ZERO);
            declarationVersement.setFne(o[18]!=null?new BigDecimal(o[18].toString()):BigDecimal.ZERO);
        }

        return declarationVersement;
    }
}
