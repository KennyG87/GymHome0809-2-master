package com.example.kennykao;

import java.io.Serializable;


@SuppressWarnings("serial")
public class CoachesVO implements Serializable {
	public CoachesVO(String coa_acc, String coa_no, String coa_psw, int coa_sta, String coa_name, int coa_sex, String coa_id, String coa_mail, String coa_into, byte[] coa_pic, double coa_pft) {
		this.coa_acc = coa_acc;
		this.coa_no = coa_no;
		this.coa_psw = coa_psw;
		this.coa_sta = coa_sta;
		this.coa_name = coa_name;
		this.coa_sex = coa_sex;
		this.coa_id = coa_id;
		this.coa_mail = coa_mail;
		this.coa_into = coa_into;
		this.coa_pic = coa_pic;
		this.coa_pft = coa_pft;
	}

	private String coa_acc;
	private String coa_no;
	private String coa_psw;
	private int coa_sta;
	private String coa_name;
	private int coa_sex;
	private String coa_id;
	private String coa_mail;
	private String coa_into;
	private byte[] coa_pic;
	private double coa_pft;
	public String getCoa_acc() {
		return coa_acc;
	}
	public void setCoa_acc(String coa_acc) {
		this.coa_acc = coa_acc;
	}
	public String getCoa_no() {
		return coa_no;
	}
	public void setCoa_no(String coa_no) {
		this.coa_no = coa_no;
	}
	public String getCoa_psw() {
		return coa_psw;
	}
	public void setCoa_psw(String coa_psw) {
		this.coa_psw = coa_psw;
	}
	public int getCoa_sta() {
		return coa_sta;
	}
	public void setCoa_sta(int coa_sta) {
		this.coa_sta = coa_sta;
	}
	public String getCoa_name() {
		return coa_name;
	}
	public void setCoa_name(String coa_name) {
		this.coa_name = coa_name;
	}
	public int getCoa_sex() {
		return coa_sex;
	}
	public void setCoa_sex(int coa_sex) {
		this.coa_sex = coa_sex;
	}
	public String getCoa_id() {
		return coa_id;
	}
	public void setCoa_id(String coa_id) {
		this.coa_id = coa_id;
	}
	public String getCoa_mail() {
		return coa_mail;
	}
	public void setCoa_mail(String coa_mail) {
		this.coa_mail = coa_mail;
	}
	public String getCoa_into() {
		return coa_into;
	}
	public void setCoa_into(String coa_into) {
		this.coa_into = coa_into;
	}
	public byte[] getCoa_pic() {
		return coa_pic;
	}
	public void setCoa_pic(byte[] coa_pic) {
		this.coa_pic = coa_pic;
	}
	public double getCoa_pft() {
		return coa_pft;
	}
	public void setCoa_pft(double coa_pft) {
		this.coa_pft = coa_pft;
	}
	
	

	

	
}
