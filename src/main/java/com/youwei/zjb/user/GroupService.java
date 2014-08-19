package com.youwei.zjb.user;

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
			group.cid = ThreadSession.getUser().cid;
		}
		DeptGroup po = dao.getUniqueByParams(DeptGroup.class, new String[]{"cid" , "name"}, new Object[]{group.cid , group.name});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, "已经存在相同名称的分组");
		}
		dao.saveOrUpdate(group);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView set(int did , int gid){
		Department dept = dao.get(Department.class, did);
		dept.dgroup = gid;
		dao.saveOrUpdate(dept);
		return new ModelAndView();
	}
}
