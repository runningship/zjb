package com.youwei.zjb.user;

import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.DeptGroup;

@Module(name="/dept/group/")
public class GroupService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(DeptGroup group){
		if(group.cid==null){
			throw new GException(PlatformExceptionType.BusinessException, "未指定分组所在公司");
		}
		DeptGroup po = dao.getUniqueByParams(DeptGroup.class, new String[]{"cid" , "name"}, new Object[]{group.cid , group.name});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, "已经存在相同名称的分组");
		}
		dao.saveOrUpdate(group);
		ModelAndView mv = new ModelAndView();
		JSONObject json = new JSONObject();
		json.put("id", group.id);
		json.put("pId", group.cid);
		json.put("type", "group");
		json.put("name", group.name);
		json.put("icon", "/style/images/icon_lock_current.png");
		mv.data = json;
		return mv;
	}
	
	@WebMethod
	public ModelAndView set(int did , int gid){
		Department dept = dao.get(Department.class, did);
		dept.dgroup = gid;
		dao.saveOrUpdate(dept);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		DeptGroup group = dao.get(DeptGroup.class, id);
		if(group!=null){
			dao.delete(group);
		}
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView updateName(DeptGroup group){
		DeptGroup po = dao.get(DeptGroup.class, group.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "已经存在相同名称的分组");
		}
		po.name = group.name;
		dao.saveOrUpdate(po);
		return new ModelAndView();
	}
}
