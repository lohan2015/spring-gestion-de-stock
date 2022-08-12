package com.kinart.api.organisation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultatDessinOrganigrammeDto {
    private String legende;
    private String nomOrganigramme;
    private String organigramme;
    private String appUrlPath;
    private boolean showLienGen;
    private boolean showLienExtract;
    private String urlPathExtract;
}
