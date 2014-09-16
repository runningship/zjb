package com.youwei.zjb.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.BeanUtil;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.cache.ConfigCache;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.sys.OperatorService;
import com.youwei.zjb.sys.OperatorType;
import com.youwei.zjb.sys.entity.PC;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.util.SecurityHelper;
import com.youwei.zjb.util.SessionHelper;

@Module(name="/user/")
public class UserService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
	
	@WebMethod
	public ModelAndView initIndex(){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		try{
			mv.data.put("username", user.uname);
		}catch(Exception ex){
 			ex.printStackTrace();
		}
		if(user.getRole()!=null){
			mv.data.put("role", user.getRole().title);
		}
		mv.data.put("userId", user.id);
		mv.data.put("hostname", ConfigCache.get("domainName", "zb.zhongjiebao.com"));
		return mv;
	}
	
	
	public ModelAndView authorities(){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		mv.data.put("authorities", JSONHelper.toJSONArray(user.getRole().Authorities()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView allRoles(){
		ModelAndView mv = new ModelAndView();
		List<Role> roles = dao.listByParams(Role.class, "from Role");
		mv.data.put("roles", JSONHelper.toJSONArray(roles));
		return mv;
	}
	
	@WebMethod
	public ModelAndView changePwd(String oldPwd,String newPwd, String newPwdRepeat){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(oldPwd)){
			throw new GException(PlatformExceptionType.BusinessException, "原密码不能为空");
		}
		if(StringUtils.isEmpty(newPwd)){
			throw new GException(PlatformExceptionType.BusinessException, "新密码不能为空");
		}
		if(newPwd.length()<6 || newPwd.length()>20){
			throw new GException(PlatformExceptionType.BusinessException, "密码长度应为6-20位");
		}
		if(StringUtils.isEmpty(newPwdRepeat)){
			throw new GException(PlatformExceptionType.BusinessException, "重复新密码不能为空");
		}
		if(!newPwd.equals(newPwdRepeat)){
			throw new GException(PlatformExceptionType.BusinessException, "两次输入的新密码不一样");
		}
		User po = dao.get(User.class, ThreadSession.getUser().id);
		if(!SecurityHelper.Md5(oldPwd).equals(po.pwd)){
			throw new GException(PlatformExceptionType.BusinessException, "原密码错误");
		}
		po.pwd = SecurityHelper.Md5(newPwd);
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(User user){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(user.uname)){
			throw new GException(PlatformExceptionType.BusinessException,"用户名不能为空");
		}
		
		User po = dao.get(User.class, user.id);
		po.uname = user.uname;
		if(StringUtils.isNotEmpty(user.pwd)){
			po.pwd = SecurityHelper.Md5(user.pwd);
		}
		po.tel = user.tel;
		po.did = user.did;
		po.roleId = user.roleId;
		po.lock = user.lock;
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		User po = dao.get(User.class, id);
		mv.data = JSONHelper.toJSON(po);
		mv.data.remove("pwd");
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		User po = dao.get(User.class, id);
		if(po!=null){
			dao.delete(po);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(User user){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(user.uname)){
			throw new GException(PlatformExceptionType.BusinessException,"用户名不能为空");
		}
		if(StringUtils.isEmpty(user.pwd)){
			throw new GException(PlatformExceptionType.BusinessException,"请先设置密码");
		}
		if(user.did==null){
			user.did = -1;
		}
		Department dept = dao.get(Department.class, user.did);
		if(dept==null){
			throw new GException(PlatformExceptionType.BusinessException, "没有指定用户所属公司");
		}
		user.addtime = new Date();
		user.flag = 1;
		user.sh = 1;
		user.pwd = SecurityHelper.Md5(DataHelper.User_Default_Password);
		user.cid = dept.fid;
		//TODO
//		user.orgpath = dept.path+user.id;
		dao.saveOrUpdate(user);

		User operUser = ThreadSession.getUser();
		String operConts = "["+operUser.Department().namea+"-"+operUser.uname+ "] 添加了用户["+user.Department().namea+"-"+user.uname+"]";
		operService.add(OperatorType.人事记录, operConts);
		mv.data.put("msg", "添加用户成功");
		return mv;
	}
	
	private void fillQuery(UserQuery query,StringBuilder hql, List<Object> params){
		if(StringUtils.isNotEmpty(query.name)){
			hql.append(" and u.uname like ? ");
			params.add("%"+query.name+"%");
		}
		if(StringUtils.isNotEmpty(query.tel)){
			hql.append(" and u.tel like ? ");
			params.add("%"+query.tel+"%");
		}
		if(StringUtils.isNotEmpty(query.address)){
			hql.append(" and u.address like ? ");
			params.add("%"+query.address+"%");
		}
		if(query.gender!=null){
			hql.append(" and u.gender=?");
			params.add(query.gender);
		}
		if(query.roleId!=null){
			hql.append(" and u.roleId=?");
			params.add(query.roleId);
		}
		if(query.did!=null){
			hql.append(" and u.did=?");
			params.add(query.did);
		}
		if(query.cid!=null){
			hql.append(" and u.cid=?");
			params.add(query.cid);
		}
		if(query.ageStart!=null){
			hql.append(" and u.age>=?");
			params.add(query.ageStart);
		}
		if(query.ageEnd!=null){
			hql.append(" and u.age<=?");
			params.add(query.ageEnd);
		}
		hql.append(HqlHelper.buildDateSegment("rqsj", query.rqtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("rqsj", query.rqtimeEnd, DateSeparator.Before, params));
	}
	@WebMethod
	public ModelAndView list(UserQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		hql.append("select u.lname as lname, u.uname as uname,u.id as uid, u.roleId as roleId , u.tel as tel,d.namea as dname, u.lasttime as lasttime ,u.ip as ip  "
				+ "from User  u ,Department d  where d.id = u.did");
		fillQuery(query,hql,params);
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		for(Map map : page.getResult()){
			Object roleId = map.get("roleId");
			String title = "";
			if(roleId!=null){
				Role role = dao.get(Role.class, Integer.valueOf(roleId.toString()));
				if(role!=null){
					title=role.title;
				}
			}
			map.put("role", title);
		}
		mv.data.put("page", JSONHelper.toJSON(page , DataHelper.dateSdf.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView superLogin(User user){
		
		User po = dao.get(User.class, user.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "用户名不存在");
		}
		SessionHelper.initHttpSession(ThreadSession.getHttpSession(), po , null);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView login(User user,PC pc){
		ModelAndView mv = new ModelAndView();
		
		if(user.lname==null){
			throw new GException(PlatformExceptionType.BusinessException, "账号不存在");
		}
		User po = dao.getUniqueByKeyValue(User.class, "lname", user.lname);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "账号不存在");
		}
		if(po.lock==0){
			throw new GException(PlatformExceptionType.BusinessException, "账号被锁定");
		}
		Department comp = dao.get(Department.class, po.cid);
		if(comp.sh==0){
			throw new GException(PlatformExceptionType.BusinessException, "公司 "+comp.namea+" 被锁定");
		}
		Department dept = po.Department();
		if(dept.sh==0){
			throw new GException(PlatformExceptionType.BusinessException, "分店 "+comp.namea+" 被锁定");
		}
		pc.did = po.did;
		if(!po.pwd.equals(SecurityHelper.Md5(user.pwd))){
			throw new GException(PlatformExceptionType.BusinessException, "密码不正确");
		}
		PC pcpo= null;
		if(!"8753".equals(pc.debug)){
			pcpo = SecurityHelper.validate(pc , po);
		}
		if(pcpo!=null){
			pcpo.lasttime = new Date();
			pcpo.lastip = ThreadSession.getIp();
			dao.saveOrUpdate(pcpo);
		}
		po.ip = ThreadSession.getIp();
		mv.data.put("result", "0");
		mv.data.put("msg", "登录成功");
		po.lasttime = new Date();
		dao.saveOrUpdate(po);
		SessionHelper.initHttpSession(ThreadSession.getHttpSession(), po , null);
		ThreadSession.getHttpSession().setAttribute("pc", pcpo);
		String operConts = "["+po.Department().namea+"-"+po.uname+ "] 登录成功";
		operService.add(OperatorType.登录记录, operConts);
		return mv;
	}
	
	@WebMethod
	public ModelAndView logout(PC pc){
		ModelAndView mv = new ModelAndView();
		//httpsession timeout & remove sessionid from db
		dao.execute("delete from UserSession where userid=? and sessionId=?", ThreadSession.getUser().id, ThreadSession.getHttpSession().getId());
		ThreadSession.getHttpSession().invalidate();
		return mv;
	}

}
