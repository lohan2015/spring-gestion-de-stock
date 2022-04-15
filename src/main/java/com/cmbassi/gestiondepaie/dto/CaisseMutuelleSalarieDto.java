package com.cmbassi.gestiondepaie.dto;

import com.cmbassi.gestiondepaie.model.CaisseMutuelleSalarie;
import com.cmbassi.gestiondepaie.model.Salarie;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@Builder
public class CaisseMutuelleSalarieDto {
    private Integer id;
    private Integer idEntreprise;

    private String nmat;

    private String rscm;

    private String rpcm;

    private String nadh;

    private Date dtad;

    private Date dtrd;

    private Salarie salarie;

    public CaisseMutuelleSalarieDto() {
    }

    public CaisseMutuelleSalarieDto(Integer id, Integer idEntreprise, String nmat, String rscm, String rpcm, String nadh, Date dtad, Date dtrd, Salarie salarie) {
        this.id = id;
        this.idEntreprise = idEntreprise;
        this.nmat = nmat;
        this.rscm = rscm;
        this.rpcm = rpcm;
        this.nadh = nadh;
        this.dtad = dtad;
        this.dtrd = dtrd;
        this.salarie = salarie;
    }

    public static CaisseMutuelleSalarieDto fromEntity(CaisseMutuelleSalarie caisseMutuelleSalarie) {
        if (caisseMutuelleSalarie == null) {
            return null;
        }

        CaisseMutuelleSalarieDto dto = new  CaisseMutuelleSalarieDto();
        BeanUtils.copyProperties(caisseMutuelleSalarie, dto);
        return dto;
    }

    public static CaisseMutuelleSalarie toEntity(CaisseMutuelleSalarieDto caisseMutuelleSalarieDto) {
        if (caisseMutuelleSalarieDto == null) {
            return null;
        }

        CaisseMutuelleSalarie caisseMutuelleSalarie = new CaisseMutuelleSalarie();
        BeanUtils.copyProperties(caisseMutuelleSalarieDto, caisseMutuelleSalarie);
        return caisseMutuelleSalarie;
    }
}
