package com.youwei.zjb.user;

import java.util.List;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.user.entity.User;

public class UserHelper {

	public static List<User> getUserWithAuthority(String authName){
		String hql = "select u from User u, RoleAuthority ra where u.roleId=ra.roleId and ra.name='"+authName+"'";
		List<User> list1 = SimpDaoTool.getGlobalCommonDaoService().listByParams(User.class, hql);
		String hql2 = "from User u , Purview p  where u.roleId=p.unid and fy like ? ";
		List<User> list2 = SimpDaoTool.getGlobalCommonDaoService().listByParams(User.class, hql2, "%"+authName+"%");
		for(User u1 : list1){
			for(User u2 : list2){
				if(u1.id!=u2.id){
					list1.add(u2);
				}
			}
		}
		return list1;
	}
	
	public static boolean hasAuthority(User u , String authName){
		if(u==null){
			return false;
		}
		if(u.id==1){
			return true;
		}
		if(authName==null){
			return true;
		}
		Role role = u.getRole();
		if(role==null){
			return false;
		}
		for(RoleAuthority ra : role.Authorities()){
			if(authName.equals(ra.name)){
				return true;
			}
		}
		return false;
	}
}
