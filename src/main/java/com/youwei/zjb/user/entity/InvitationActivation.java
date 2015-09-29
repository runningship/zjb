package com.youwei.zjb.user.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class InvitationActivation {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;

	//被邀请人,唯一
	public Integer inviteeUid;
	
	public Integer invitationCode;
	
	public Integer active;
	
	public Date addtime;
	
	public Date activetime;
	
	public String bouns;
}
