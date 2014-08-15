package com.youwei.zjb.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.user.entity.Department;
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
	public ModelAndView getDeptTree(){
		ModelAndView mv = new ModelAndView();
		String hql = "select child.namea as cname,parent.namea as pname ,child.id as did ,child.fid as qid "+
				"from Department child,Department parent  where child.fid = parent.id and parent.id<>1";
		List<Map> users = dao.listAsMap(hql);
		Map<Map, JSONArray> quyus = groupByQuyu(users);
		JSONArray arr = new JSONArray();
		for(Map map : quyus.keySet()){
			JSONObject key = new JSONObject();
			key.put("text", map.get("pname").toString());
			key.put("deptId",  map.get("did"));
			key.put("children", quyus.get(map));
			arr.add(key);
		}
		mv.data.put("result", arr.toString());
		return mv;
	}
	
	private Map<Map,JSONArray>  groupByQuyu(List<Map> users){
		Map<Map,JSONArray> quyus = new HashMap<Map,JSONArray>();
		
		for(Map user : users){
			if(!quyus.containsKey(user)){
				quyus.put(user, new JSONArray());
			}
			JSONArray arr = quyus.get(user);
			JSONObject dept = new JSONObject();
			dept.put("text", user.get("cname"));
			dept.put("deptId", user.get("did"));
			if(!arr.contains(dept)){
				quyus.get(user).add(dept);
			}
		}
		return quyus;
	}
}
