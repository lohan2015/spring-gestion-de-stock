package com.kinart.paie.business.utils;

public class ClsGenericPrintObject {
    private String[][] infos;
    private Integer[][] position;

    public ClsGenericPrintObject(int rows,int cols) {
        infos = new String[rows][cols];
        position = new Integer[rows][cols];
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++) position[i][j] = 0;
        }
    }
    public String[][] getInfos() {
        return infos;
    }
    public void setInfos(String[][] infos) {
        this.infos = infos;
    }
    public Integer[][] getPosition() {
        return position;
    }
    public void setPosition(Integer[][] position) {
        this.position = position;
    }
}
