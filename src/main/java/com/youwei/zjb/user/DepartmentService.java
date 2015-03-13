package com.youwei.zjb.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.ThreadSession;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.DeptGroup;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;

@Module(name="/dept/")
public class DepartmentService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	@WebMethod
	public ModelAndView add(Department dept){
		if(dept.fid==null){
			dept.fid=0;
		}
		Department po = dao.getUniqueByKeyValue(Department.class, "cnum", dept.cnum);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, "cnum","公司代码"+dept.cnum+"已存在");
		}
		po = dao.getUniqueByParams(Department.class, new String[]{"fid" , "namea"}, new Object[]{dept.fid , dept.namea});
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, "公司名称重复");
		}
		if(dept.fid==0 && StringUtils.isEmpty(dept.authCode)){
			throw new GException(PlatformExceptionType.BusinessException, "请先填写授权码");
		}
		po = dao.getUniqueByKeyValue(Department.class, "authCode", dept.authCode);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, "授权码重复");
		}
		dept.addtime = new Date();
		dept.flag=1;
		dao.saveOrUpdate(dept);
		ModelAndView mv = new ModelAndView();
		JSONObject json = new JSONObject();
		json.put("id", dept.id);
		json.put("pId", dept.fid);
		if(dept.fid==0){
			json.put("type", "comp");
		}else{
			json.put("type", "dept");
		}
		json.put("sh", dept.sh);
		json.put("name", dept.namea);
		mv.data = json;
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		Department dept = dao.get(Department.class, id);
		mv.data.put("comp", JSONHelper.toJSON(dept , DataHelper.dateSdf.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView toggleShenhe(int id){
		ModelAndView mv = new ModelAndView();
		Department dept = dao.get(Department.class, id);
		if(dept!=null){
			if(dept.sh==1){
				dept.sh=0;
			}else{
				dept.sh=1;
			}
			dao.saveOrUpdate(dept);
		}
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
		po.useIm = dept.useIm;
		if(dept.price!=null){
			po.price = dept.price;
		}
		if(dept.deadline!=null){
			po.deadline=dept.deadline;
		}
		//公司需要更新的字段
		po.share = dept.share;
		long count = dao.countHql("select count(*) from Department where authCode=?", dept.authCode);
		if(count>0){
			if(!po.authCode.equals(dept.authCode)){
				throw new GException(PlatformExceptionType.BusinessException, "授权码已被使用，请重新设置.");
			}
		}
		po.authCode = dept.authCode;
		po.pcnum = dept.pcnum;
		dao.saveOrUpdate(po);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView delete(int deptId){
		Department po = dao.get(Department.class, deptId);
		long count = dao.countHqlResult("from Department where fid=?", deptId);
		if(count>0){
			throw new GException(PlatformExceptionType.BusinessException, "请先删除公司下门店");
		}
		count = dao.countHqlResult("from User where did=?", deptId);
		if(count>0){
			throw new GException(PlatformExceptionType.BusinessException, "该门店下有员工信息，请先删除员工信息");
		}
		dao.delete(po);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView listDept(String authCode , String cityPy){
		if(StringUtils.isNotEmpty(cityPy)){
			ThreadSession.setCityPY(cityPy);
		}
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
	public ModelAndView getUserTree(){
		ModelAndView mv = new ModelAndView();
		JSONArray result = new JSONArray();
		Department comp = ThreadSessionHelper.getUser().Company();
		JSONObject com = new JSONObject();
		com.put("id", "d_"+comp.id);
		com.put("pId", "0");
		com.put("name", comp.namea);
		com.put("type", "comp");
		result.add(com);
		List<User> users = dao.listByParams(User.class, "from User where cid=? ", ThreadSessionHelper.getUser().cid);
		for(User u : users){
			JSONObject json = new JSONObject();
			json.put("id", "u_"+u.id);
			json.put("pId", "d_"+u.did);
			json.put("name", u.uname);
			json.put("type", "dept");
			result.add(json);
		}
		List<Department> depts = dao.listByParams(Department.class, "from Department where fid=?", ThreadSessionHelper.getUser().cid);
		for(Department d : depts){
			JSONObject json = new JSONObject();
			json.put("id", "d_"+d.id);
			json.put("pId", "d_"+d.fid);
			json.put("name", d.namea);
			json.put("type", "comp");
			result.add(json);
		}
		mv.data.put("result", result.toArray());
		return mv;
	}
	@WebMethod
	public ModelAndView getDeptTree(Integer cid){
		ModelAndView mv = new ModelAndView();
		if(ThreadSessionHelper.getUser().cid==1){
			//中介宝用户看到的分组略有不同
			List<Department> comps = dao.listByParams(Department.class, "from Department where 1=1 order by sh desc, cnum");
			JSONArray result = new JSONArray();
			for(Department dept : comps){
				JSONObject json = new JSONObject();
				json.put("id", dept.id);
				if(dept.dgroup!=null){
					json.put("pId", dept.dgroup);
				}else{
					json.put("pId", dept.fid);
				}
				if(dept.fid==0){
					json.put("type", "comp");
				}else{
					json.put("type", "dept");
				}
				json.put("sh", dept.sh);
				json.put("name", dept.namea);
				json.put("cnum", dept.cnum);
				result.add(json);
			}
			//加载分组信息
			List<DeptGroup> groups = dao.listByParams(DeptGroup.class, "from DeptGroup where 1=1" );
			for(DeptGroup g : groups){
				JSONObject json = new JSONObject();
				json.put("id",g.id);
				json.put("type", "group");
//				json.put("icon", "/style/images/icon_lock_current.png");
				if(g.pid!=null){
					json.put("pId", g.pid);
				}else{
					json.put("pId", g.cid);
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
				json.put("sh", dept.sh);
				if(dept.id.equals(cid)){
					json.put("pId", 0);
					json.put("open", true);
					json.put("type", "comp");
					json.put("cnum", dept.cnum);
				}else{
					if(dept.dgroup!=null){
						json.put("pId", dept.dgroup);
					}else{
						json.put("pId", cid);
					}
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
//				json.put("icon", "/style/images/icon_lock_current.png");
				json.put("name", g.name);
				result.add(json);
			}
			mv.data.put("result", result.toString());
			return mv;
		}
		
	}

}
