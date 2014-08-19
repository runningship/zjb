package com.youwei.zjb.entity;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.youwei.zjb.SimpDaoTool;

/**
 * 
 *角色表，职务表
 */
@Entity
@Table(name="uc_unit")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 公司
	 */
	public Integer cid;
	
	/**
	 * 职务，相当于角色名称
	 */
	public String title;
	
	/**
	 * 角色说明
	 */
	public String cont;
	
	public Integer flag;
	
	public Integer sh;
	
	public Integer num;
	
	public List<RoleAuthority> Authorities(){
		List<RoleAuthority> list = SimpDaoTool.getGlobalCommonDaoService().listByParams(RoleAuthority.class, new String[]{"roleId"}, new Object[]{id});
		Purview purview = SimpDaoTool.getGlobalCommonDaoService().getUniqueByKeyValue(Purview.class, "unid", id);
		if(StringUtils.isNotEmpty(purview.fy)){
			merge(list, purview.fy);
		}
		if(StringUtils.isNotEmpty(purview.sz)){
			merge(list, purview.sz);
		}
		return list;
	}

	private void merge(List<RoleAuthority> list, String fy) {
		String[] arr = fy.split("|1");
		for(String item : arr){
			item = item.replace("", "");
			RoleAuthority ra = new RoleAuthority();
			if(!list.contains(ra)){
				list.add(ra);
			}
		}
	}
}
