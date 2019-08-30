package com.ezen.project.bean;

public class NotificationDTO {
  private int notifi_id;
  private int mode_id;
  private int target_id;
  private int target_sub_id;
  private int act_user_id;
  private int target_user_id;
  private boolean delete_flg;
  private String reg_date;

  public int getNotifi_id() {
    return notifi_id;
  }

  public void setNotifi_id(int notifi_id) {
    this.notifi_id = notifi_id;
  }

  public int getMode_id() {
    return mode_id;
  }

  public void setMode_id(int mode_id) {
    this.mode_id = mode_id;
  }

  public int getTarget_id() {
    return target_id;
  }

  public void setTarget_id(int target_id) {
    this.target_id = target_id;
  }

  public int getTarget_sub_id() {
    return target_sub_id;
  }

  public void setTarget_sub_id(int target_sub_id) {
    this.target_sub_id = target_sub_id;
  }

  public int getAct_user_id() {
    return act_user_id;
  }

  public void setAct_user_id(int act_user_id) {
    this.act_user_id = act_user_id;
  }

  public int getTarget_user_id() {
    return target_user_id;
  }

  public void setTarget_user_id(int target_user_id) {
    this.target_user_id = target_user_id;
  }

  public boolean isDelete_flg() {
    return delete_flg;
  }

  public void setDelete_flg(boolean delete_flg) {
    this.delete_flg = delete_flg;
  }

  public String getReg_date() {
    return reg_date;
  }

  public void setReg_date(String reg_date) {
    this.reg_date = reg_date;
  }

}
