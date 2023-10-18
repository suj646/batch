package io.spring.batch.dto;

import java.sql.Timestamp;

public class BinDTO {

    private Long rowid;
    private String bankIdNo;
    private Timestamp lastUpdateTs;
    private Short hostNo;
    private Short issuerNo;
    private Short prodBrndNo;
    private Integer busLineTpNo;
    private Integer hostPlatformNo;
    private Integer pmtCpnMicrNo;
    private Integer glDestNo;

    public BinDTO() {
        // Default constructor
    }

    // Getters and setters for all fields

    public Long getRowid() {
        return rowid;
    }

    public void setRowid(Long rowid) {
        this.rowid = rowid;
    }

    public String getBankIdNo() {
        return bankIdNo;
    }

    public void setBankIdNo(String bankIdNo) {
        this.bankIdNo = bankIdNo;
    }

    public Timestamp getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(Timestamp lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
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

    public Integer getHostPlatformNo() {
        return hostPlatformNo;
    }

    public void setHostPlatformNo(Integer hostPlatformNo) {
        this.hostPlatformNo = hostPlatformNo;
    }

    public Integer getPmtCpnMicrNo() {
        return pmtCpnMicrNo;
    }

    public void setPmtCpnMicrNo(Integer pmtCpnMicrNo) {
        this.pmtCpnMicrNo = pmtCpnMicrNo;
    }

    public Integer getGlDestNo() {
        return glDestNo;
    }

    public void setGlDestNo(Integer glDestNo) {
        this.glDestNo = glDestNo;
    }
}

