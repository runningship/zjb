package com.youwei.zjb.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.DeptGroup;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/dept/")
public class DepartmentService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	@WebMethod
	public ModelAndView add(Department dept){
		if(dept.fid==null){
			dept.fid=0;
		}
		dept.addtime = new Date();
		dao.saveOrUpdate(dept);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView get(int deptId){
		ModelAndView mv = new ModelAndView();
		Department dept = dao.get(Department.class, deptId);
		mv.data.put("dept", JSONHelper.toJSON(dept));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Department dept){
		Department po = dao.get(Department.class, dept.id);
		po.namea = dept.namea;
		po.lxr = dept.lxr;
		po.tel = dept.tel;
		po.beizhu = dept.beizhu;
		po.sh = dept.sh;
		dao.saveOrUpdate(po);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView delete(int deptId){
		Department po = dao.get(Department.class, deptId);
		long count = dao.countHqlResult("from Department where fid=?", deptId);
		if(count>0){
			throw new GException(PlatformExceptionType.BusinessException, "请先删除分公司");
		}
		count = dao.countHqlResult("from User where deptId=?", deptId);
		if(count>0){
			throw new GException(PlatformExceptionType.BusinessException, "该分公司下有人员信息，请先删除公司人员信息");
		}
		dao.delete(po);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView listDept(String authCode){
		ModelAndView mv = new ModelAndView();
		Department comp = dao.getUniqueByKeyValue(Department.class, "authCode", authCode);
		if(comp!=null){
			mv.data.put("cname", comp.namea);
		}else{
			throw new GException(PlatformExceptionType.BusinessException, "授权码不正确");
		}
		List<Object> params = new ArrayList<Object>();
		//TODO  sh ,flag 什么意思
		StringBuilder hql = new StringBuilder("select id as id , namea as name from Department where fid=(select id from Department where authCode=?) and sh=1");
		List<Map> list = dao.listAsMap(hql.toString(), new Object[]{authCode});
		mv.data.put("depts", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getDeptTree(Integer cid){
		ModelAndView mv = new ModelAndView();
		if(cid==1){
			List<Department> comps = dao.listByParams(Department.class, "from Department where 1=1 order by cnum");
			JSONArray result = new JSONArray();
			for(Department dept : comps){
				JSONObject json = new JSONObject();
				json.put("id", dept.id);
				if(dept.dgroup!=null){
					json.put("pId", dept.dgroup);
				}else{
					json.put("pId", dept.fid);
				}
				json.put("name", dept.namea);
				json.put("cnum", dept.cnum);
				result.add(json);
			}
			//加载分组信息
			List<DeptGroup> groups = dao.listByParams(DeptGroup.class, "from DeptGroup where 1=1" );
			for(DeptGroup g : groups){
				JSONObject json = new JSONObject();
				json.put("id",g.id);
				if(g.pid!=null){
					json.put("pId", g.pid);
				}else{
					//中介宝用户看到的分组略有不同
					if(g.cid!=1){
						json.put("pId", g.cid);
					}
				}
				json.put("name", g.name);
				result.add(json);
			}
			mv.data.put("result", result.toString());
			return mv;
		}else{
			List<Department> depts = dao.listByParams(Department.class, "from Department where fid=? or id=?", cid , cid);
			List<DeptGroup> groups = dao.listByParams(DeptGroup.class, "from DeptGroup where cid=?", cid);
			JSONArray result = new JSONArray();
			for(Department dept : depts){
				JSONObject json = new JSONObject();
				json.put("id", dept.id);
				if(dept.id==cid){
					json.put("pId", 0);
					json.put("type", "comp");
				}else{
					json.put("pId", dept.dgroup);
					json.put("type", "dept");
				}
				json.put("name", dept.namea);
				result.add(json);
			}
			for(DeptGroup g : groups){
				JSONObject json = new JSONObject();
				json.put("id",g.id);
				if(g.pid==null){
					json.put("pId", cid);
				}else{
					json.put("pId", g.pid);
				}
				json.put("type", "group");
				json.put("name", g.name);
				result.add(json);
			}
			mv.data.put("result", result.toString());
			return mv;
		}
		
	}

}
