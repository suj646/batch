package io.spring.batch.dto;

import java.sql.Timestamp;

public class Sst2BinStagingDTO {

    private Long rowid;
    private String jobId;
    private Timestamp binExprTs;
    private String bankIdNo;
    private Timestamp binEffTs;
    private String binAprvIn;
    private Short prinBnkNoLenCt;
    private Short acsmedNoLenCt;
    private Timestamp lastUpdateTs;
    private String mbnaOwnershipIn;
    private String mbnaTechIn;
    private Short hostNo;
    private Short issuerNo;
    private Short prodBrndNo;
    private Integer busLineTpNo;
    private String prodBrndRgnCd;
    private Integer pmtCpnMicrNo;
    private Short interfaceCntlNo;
    private Short acctBillingTpNo;
    private Integer glDestNo;
    private String legacyBinIn;
    private String prodBrndCtryCd;
    private Integer hostPlatformNo;
    private String accountingNo;
    private Timestamp extractDt;
    private String actionIndicator;
    private String responsePrcsStat;
    private String errorMessage;
    private Long threadId;
    private Long threadSeqId;
    private String inFileName;
    private String freeTextField1;
    private String freeTextField2;
    private String freeTextField3;
    private String createdBy;
    private Timestamp createdTimestamp;
    private String updatedBy;
    private Timestamp updatedTimestamp;

    public Sst2BinStagingDTO() {
        // Default constructor
    }

    // Getters and setters for all fields

    public Long getRowid() {
        return rowid;
    }

    public void setRowid(Long rowid) {
        this.rowid = rowid;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Timestamp getBinExprTs() {
        return binExprTs;
    }

    public void setBinExprTs(Timestamp binExprTs) {
        this.binExprTs = binExprTs;
    }

    public String getBankIdNo() {
        return bankIdNo;
    }

    public void setBankIdNo(String bankIdNo) {
        this.bankIdNo = bankIdNo;
    }

    public Timestamp getBinEffTs() {
        return binEffTs;
    }

    public void setBinEffTs(Timestamp binEffTs) {
        this.binEffTs = binEffTs;
    }

    public String getBinAprvIn() {
        return binAprvIn;
    }

    public void setBinAprvIn(String binAprvIn) {
        this.binAprvIn = binAprvIn;
    }

    public Short getPrinBnkNoLenCt() {
        return prinBnkNoLenCt;
    }

    public void setPrinBnkNoLenCt(Short prinBnkNoLenCt) {
        this.prinBnkNoLenCt = prinBnkNoLenCt;
    }

    public Short getAcsmedNoLenCt() {
        return acsmedNoLenCt;
    }

    public void setAcsmedNoLenCt(Short acsmedNoLenCt) {
        this.acsmedNoLenCt = acsmedNoLenCt;
    }

    public Timestamp getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(Timestamp lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }

    public String getMbnaOwnershipIn() {
        return mbnaOwnershipIn;
    }

    public void setMbnaOwnershipIn(String mbnaOwnershipIn) {
        this.mbnaOwnershipIn = mbnaOwnershipIn;
    }

    public String getMbnaTechIn() {
        return mbnaTechIn;
    }

    public void setMbnaTechIn(String mbnaTechIn) {
        this.mbnaTechIn = mbnaTechIn;
    }

    public Short getHostNo() {
        return hostNo;
    }

    public void setHostNo(Short hostNo) {
        this.hostNo = hostNo;
    }

    public Short getIssuerNo() {
        return issuerNo;
    }

    public void setIssuerNo(Short issuerNo) {
        this.issuerNo = issuerNo;
    }

    public Short getProdBrndNo() {
        return prodBrndNo;
    }

    public void setProdBrndNo(Short prodBrndNo) {
        this.prodBrndNo = prodBrndNo;
    }

    public Integer getBusLineTpNo() {
        return busLineTpNo;
    }

    public void setBusLineTpNo(Integer busLineTpNo) {
        this.busLineTpNo = busLineTpNo;
    }

    public String getProdBrndRgnCd() {
        return prodBrndRgnCd;
    }

    public void setProdBrndRgnCd(String prodBrndRgnCd) {
        this.prodBrndRgnCd = prodBrndRgnCd;
    }

    public Integer getPmtCpnMicrNo() {
        return pmtCpnMicrNo;
    }

    public void setPmtCpnMicrNo(Integer pmtCpnMicrNo) {
        this.pmtCpnMicrNo = pmtCpnMicrNo;
    }

    public Short getInterfaceCntlNo() {
        return interfaceCntlNo;
    }

    public void setInterfaceCntlNo(Short interfaceCntlNo) {
        this.interfaceCntlNo = interfaceCntlNo;
    }

    public Short getAcctBillingTpNo() {
        return acctBillingTpNo;
    }

    public void setAcctBillingTpNo(Short acctBillingTpNo) {
        this.acctBillingTpNo = acctBillingTpNo;
    }

    public Integer getGlDestNo() {
        return glDestNo;
    }

    public void setGlDestNo(Integer glDestNo) {
        this.glDestNo = glDestNo;
    }

    public String getLegacyBinIn() {
        return legacyBinIn;
    }

    public void setLegacyBinIn(String legacyBinIn) {
        this.legacyBinIn = legacyBinIn;
    }

    public String getProdBrndCtryCd() {
        return prodBrndCtryCd;
    }

    public void setProdBrndCtryCd(String prodBrndCtryCd) {
        this.prodBrndCtryCd = prodBrndCtryCd;
    }

    public Integer getHostPlatformNo() {
        return hostPlatformNo;
    }

    public void setHostPlatformNo(Integer hostPlatformNo) {
        this.hostPlatformNo = hostPlatformNo;
    }

    public String getAccountingNo() {
        return accountingNo;
    }

    public void setAccountingNo(String accountingNo) {
        this.accountingNo = accountingNo;
    }

    public Timestamp getExtractDt() {
        return extractDt;
    }

    public void setExtractDt(Timestamp extractDt) {
        this.extractDt = extractDt;
    }

    public String getActionIndicator() {
		return accountingNo;
       
    }
}
