package com.springboot.MyTodoList.model;

import java.time.OffsetDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "LOGS")
public class Logs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int logId;

    @Column(name = "TABLE_TYPE")
    String tableType;

    @Column(name = "ROW_ID")
    private Integer rowId;

    @Column(name = "RECORDED_ACTION")
    String recordedAction;

    @Column(name = "FIELD_NAME")
    String fieldName;

    @Column(name = "OLD_VALUE")
    String oldValue;

    @Column(name = "NEW_VALUE")
    String newValue;

    @Column(name = "CHANGED_AT")
    private OffsetDateTime changedAt;

    @Column(name = "CHANGED_BY_USER_ID")
    private Integer changedByUserId;

    public Logs(){}

    public Logs(int logId, String tableType, Integer rowId, String recordedAction, String fieldName, String oldValue, String newValue, OffsetDateTime changedAt, Integer changedByUserId){
        this.logId = logId;
        this.tableType = tableType;
        this.rowId = rowId;
        this.recordedAction = recordedAction;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedAt = changedAt;
        this.changedByUserId = changedByUserId;
    }

    public int getLogId(){
        return logId;
    }

    public void setLogId(int logId){
        this.logId = logId;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public String getRecordedAction() {
        return recordedAction;
    }

    public void setRecordedAction(String recordedAction) {
        this.recordedAction = recordedAction;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public OffsetDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(OffsetDateTime changedAt) {
        this.changedAt = changedAt;
    }

    public Integer getChangedByUserId() {
        return changedByUserId;
    }

    public void setChangedByUserId(Integer changedByUserId) {
        this.changedByUserId = changedByUserId;
    }
    
}
