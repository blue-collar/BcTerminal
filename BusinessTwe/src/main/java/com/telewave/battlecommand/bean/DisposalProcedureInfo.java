package com.telewave.battlecommand.bean;

public class DisposalProcedureInfo {


    /**
     * id : czgclx00000001
     * isNewRecord : false
     * createDate : 2018-12-04 17:35:07
     * updateDate : 2018-12-04 17:35:07
     * dictId : czgclx000
     * codeValue : czgclx001
     * codeName : 危险化学品泄漏事故处置
     * parentIds : 0,1,
     * pId : 1
     * sort : 1
     * dictsourceType : 1
     */

    private String id;
    private boolean isNewRecord;
    private String createDate;
    private String updateDate;
    private String dictId;
    private String codeValue;
    private String codeName;
    private String parentIds;
    private String pId;
    private int sort;
    private int dictsourceType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsNewRecord() {
        return isNewRecord;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getDictsourceType() {
        return dictsourceType;
    }

    public void setDictsourceType(int dictsourceType) {
        this.dictsourceType = dictsourceType;
    }
}
