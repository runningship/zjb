package com.youwei.zjb.biz.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Leave {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer uid;
	
	public int did;
	
	public int cid;
	
	public Date starttime;
	
	public Date endtime;
	
	public Date createtime;
	
	public int hours;
	
	public String reason;
	
	public String status;
	
	//请假类别 1:事假, 2:病假, 3:年假, 4:婚假, 5:产假, 6:调休, 7:探亲假, 8:工伤假, 9:丧假
	public String type;
	
	//0 未审批，1,审批通过，2审批不通过
	public int sh;
	
	//备注 , 审批意见
	public String conts;
}
