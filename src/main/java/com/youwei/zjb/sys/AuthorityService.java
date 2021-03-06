package com.youwei.zjb.sys;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.Transactional;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.ThreadSession;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.entity.Purview;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;

@Module(name="/sys/")
public class AuthorityService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
	
	//为了排除页面cid和roleid冲突
	private static final int cidOffset = 1000000;
	@Transactional
	@WebMethod
	public ModelAndView update(int roleId,String name , String value){
		if(roleId==-1){
			return new ModelAndView();
		}
		Purview purview = SimpDaoTool.getGlobalCommonDaoService().getUniqueByKeyValue(Purview.class, "unid", roleId);
		if("true".equals(value)){
			RoleAuthority ra = new RoleAuthority();
			ra.name = name;
			ra.roleId = roleId;
			dao.saveOrUpdate(ra);
		}else{
			dao.execute("delete from RoleAuthority where roleId=? and name=?", roleId , name);
			//更新旧表
			if(purview!=null){
				if(purview.fy!=null && purview.fy.contains(name)){
					purview.fy = purview.fy.replace(name+"|1,", "");
					purview.fy = purview.fy.replace(name+"|1", "");
				}else if(purview.sz!=null && purview.sz.contains(name)){
					purview.sz = purview.sz.replace(name+"|1,", "");
					purview.sz = purview.sz.replace(name+"|1", "");
				}
			}
		}
		User user = ThreadSessionHelper.getUser();
		Role role = dao.get(Role.class, roleId);
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 修改了职务["+role.title+"]的权限";
		operService.add(OperatorType.权限记录, operConts);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView addRole(Role role){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(role.title)){
			throw new GException(PlatformExceptionType.BusinessException, "请先填写职务名称");
		}
		role.cid-=cidOffset;
		long count = dao.countHql("select count(*) from Role where cid=? and title=?", role.cid , role.title);
		if(count>0){
			throw new GException(PlatformExceptionType.BusinessException, "已经存在相同名称的职务");
		}
		role.flag = 1;
		role.sh = 1;
		dao.saveOrUpdate(role);
		User user = ThreadSessionHelper.getUser();
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 添加了职务["+role.title+"]";
		operService.add(OperatorType.权限记录, operConts);
		mv.data.put("name",role.title);
		mv.data.put("pId", role.cid+cidOffset);
		mv.data.put("type", "role");
		mv.data.put("id", role.id);
		return mv;
	}
	
	@WebMethod
	@Transactional
	public ModelAndView deleteRole(int roleId){
		ModelAndView mv = new ModelAndView();
		Role po = dao.get(Role.class,roleId);
		if(po!=null){
			dao.delete(po);
			dao.execute("delete from RoleAuthority where roleId=?", roleId);
			dao.execute("update User u set u.roleId=? where u.roleId=?", 0,roleId);
		}
		User user = ThreadSessionHelper.getUser();
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 删除了职务["+po.title+"]";
		operService.add(OperatorType.权限记录, operConts);
		return mv;
	}
	
	@WebMethod
	public ModelAndView rolesList(Page<Role> page , Integer cid){
		ModelAndView mv = new ModelAndView();
		if(cid!=null && cid==1){
			List<Department> comps = dao.listByParams(Department.class, "from Department where fid=0 order by cnum");
			JSONArray result = new JSONArray();
			for(Department dept : comps){
				JSONObject json = new JSONObject();
				json.put("id", dept.id+cidOffset);
				json.put("type", "comp");
				json.put("name", dept.namea);
				json.put("cnum", dept.cnum);
				result.add(json);
			}
			List<Role> roles = dao.listByParams(Role.class, "from Role");
			for(Role role : roles){
				JSONObject json = new JSONObject();
				json.put("id", role.id);
				json.put("type", "role");
				json.put("name", role.title);
				json.put("pId", role.cid+cidOffset);
				result.add(json);
			}
			mv.data.put("result", result.toString());
			return mv;
		}
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from Role where 1=1 ");
		if(cid!=null){
			hql.append(" and cid=?");
			params.add(cid);
		}
		List<Role> list = dao.listByParams(Role.class, hql.toString(), params.toArray());
		JSONArray result = new JSONArray();
		Department dept = dao.get(Department.class, cid);
		JSONObject comp = new JSONObject();
		int nodeCid = cid+cidOffset;
		comp.put("id", nodeCid);
		comp.put("type", "comp");
		comp.put("name", dept.namea);
		comp.put("pId",0);
		comp.put("open", true);
		result.add(comp);
		for(Role role : list){
			JSONObject json = new JSONObject();
			json.put("id", role.id);
			json.put("type", "role");
			json.put("name", role.title);
			json.put("pId",nodeCid);
			result.add(json);
		}
		mv.data.put("result", result.toString());
		return mv;
	}
	
	@WebMethod
	public ModelAndView getRoleMenus(int roleId , String authName){
		ModelAndView mv = new ModelAndView();
//		if(!UserHelper.hasAuthority(ThreadSessionHelper.getUser(), authName)){
//			throw new GException(PlatformExceptionType.BusinessException, "您没有权限对职务进行授权");
//		}
		try {
			String text = FileUtils.readFileToString(new File(ThreadSession.getHttpSession().getServletContext().getRealPath("/")+File.separator+"menus.json"), "utf8");
			JSONArray jarr = JSONArray.fromObject(text);
			Role role = dao.get(Role.class, roleId);
			if(role!=null){
				for(RoleAuthority ra : role.Authorities()){
					setSelected(ra ,jarr);
				}
			}
			mv.data.put("modules", jarr.toString());
			return mv;
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new GException(PlatformExceptionType.BusinessException, "读取资源文件失败");
		}
	}
	
	private void setSelected(RoleAuthority ra, JSONArray jarr) {
//		if("sz_comp_edit".equals(ra.name)){
//			if(ThreadSessionHelper.getUser().cid!=1){
//				//remove sz_comp_edit 只有中介宝用户才能才能修改公司
//				for(int i=0;i<jarr.size();i++){
//					JSONObject jobj = jarr.getJSONObject(i);
//					if("sz_comp_edit".equals(jobj.getString("name"))){
//						jarr.remove(i);
//					}
//				}
//			}
//		}
		for(int i=0;i<jarr.size();i++){
			JSONObject jobj = jarr.getJSONObject(i);
			if(jobj.get("zjb-only")!=null && jobj.get("zjb-only").equals(true)){
				if(ThreadSessionHelper.getUser().cid!=1){
					jarr.remove(i);
					continue;
				}
				
			}
			if(jobj.getString("name").equals(ra.name)){
				jobj.put("checked", "checked");
				return;
			}
			if(jobj.containsKey("children")){
				setSelected(ra , jobj.getJSONArray("children"));
			}
		}
	}

	private void addChildren(int roleId , JSONArray json) {
		for(int i=0;i<json.size();i++){
			RoleAuthority ra = new RoleAuthority();
			JSONObject jobj = json.getJSONObject(i);
			ra.name= jobj.getString("name");
			ra.type = jobj.getString("type");
			ra.roleId = roleId;
			dao.saveOrUpdate(ra);
			if(jobj.containsKey("children")){
				addChildren(roleId ,jobj.getJSONArray("children"));
			}
		}
	}

	@WebMethod
	public ModelAndView getRole(int roleId){
		ModelAndView mv = new ModelAndView();
		Role role = dao.get(Role.class, roleId);
		mv.data.put("role", JSONHelper.toJSON(role));
		return mv;
	}
	
	@WebMethod
	public ModelAndView updateRole(int id,String title){
		Role role = dao.get(Role.class, id);
		if(role==null){
			throw new GException(PlatformExceptionType.BusinessException, "职务不存在");
		}
		role.title = title;
		dao.saveOrUpdate(role);
		ModelAndView mv = new ModelAndView();
		mv.data.put("name",role.title);
		mv.data.put("pId", role.cid+cidOffset);
		mv.data.put("type", "role");
		mv.data.put("id", role.id);
		return mv;
	}
}
