package com.youwei.zjb.phone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.util.SecurityHelper;

@Module(name="/")
public class PService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod(name="house/tracks.asp")
	public ModelAndView tracks(Integer userId , Integer page){
		ModelAndView mv = new ModelAndView();
		mv.encodeReturnText=true;
		if(userId==null){
			JSONArray arr = new JSONArray();
			JSONObject obj = new JSONObject();
			obj.put("result", "1");
			obj.put("msg", "没找到相关信息");
			arr.add(obj);
			mv.returnText = arr.toString();
			return mv;
		}
		StringBuilder hql = new StringBuilder("select h.id as id ,"
				+ " h.area as area,h.dhao as dhao,h.fhao as fhao,h.ztai as ztai, h.quyu as quyu,h.djia as djia,h.zjia as zjia,h.mji as mji,"
				+ " h.lceng as lceng, h.zceng as zceng from House h , Track t where h.sh=1 and t.hid=h.id and t.uid=?");
		Page<Map> p = new Page<Map>();
		p.orderBy = "h.dateadd";
		p.order = Page.DESC;
		if(page!=null){
			p.setCurrentPageNo(page);
		}
		p = dao.findPage(p, hql.toString(), true, new Object[]{userId});
		mv.returnText = JSONHelper.toJSONArray(p.getResult()).toString();
		return mv;
	}
	
	@WebMethod(name="house/tracksdel.asp")
	public ModelAndView deltracks(Integer userId , String id){
		ModelAndView mv = new ModelAndView();
		mv.encodeReturnText=true;
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		if(StringUtils.isEmpty(id) || userId==null){
			obj.put("result", "1");
			obj.put("msg", "请选择要删除的数据");
			arr.add(obj);
			mv.returnText = arr.toString();
			return mv;
		}
		List<Object> params = new ArrayList<Object>();
		params.add(userId);
		StringBuilder hql = new StringBuilder("delete from Track where uid=? and hid in (");
		String ids[] = id.split(",");
		for(int i=0;i<ids.length;i++){
			hql.append("?");
			params.add(Integer.valueOf(ids[i]));
			if(i<ids.length-1){
				hql.append(",");
			}
		}
		hql.append(")");
		dao.execute(hql.toString(), params.toArray());
		
		obj.put("result", "0");
		obj.put("msg", "删除成功");
		arr.add(obj);
		mv.returnText = arr.toString();
		return mv;
	}
	
	@WebMethod(name="user/info.asp")
	public ModelAndView userInfo(Integer userId){
		ModelAndView mv = new ModelAndView();
		mv.encodeReturnText=true;
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		User user = null;
		if(userId!=null){
			user = dao.get(User.class, userId);
		}
		if(user==null){
			obj.put("result", "1");
			obj.put("msg", "没有数据");
			arr.add(obj);
			mv.returnText = arr.toString();
			return mv;
		}
		Department dept = dao.get(Department.class, user.did);
		Department comp = dao.get(Department.class, user.cid);
		obj.put("accound", user.lname);
		obj.put("uname", user.uname);
		obj.put("phone", user.tel==null?"":user.tel);
		obj.put("cname", comp.namea);
		obj.put("dname", dept.namea);
		obj.put("result", "0");
		obj.put("msg", "");
		arr.add(obj);
		mv.returnText = arr.toString();
		return mv;
	}
	
	@WebMethod(name="user/login.asp")
	public ModelAndView login(String account , String password , String simNo){
		ModelAndView mv = new ModelAndView();
		mv.encodeReturnText=true;
		JSONObject obj = new JSONObject();
		String pwd = SecurityHelper.Md5(password);
		User user = dao.getUniqueByKeyValue(User.class, "tel", account);
		if(user==null){
			obj.put("result", "3");
			obj.put("msg", "账号不存在");
			mv.returnText = obj.toString();
			return mv;
		}
		if(!user.pwd.equals(pwd)){
			obj.put("result", "1");
			obj.put("msg", "密码错误");
			mv.returnText = obj.toString();
			return mv;
		}
		Department dept = dao.get(Department.class, user.did);
		Department comp = dao.get(Department.class, user.cid);
		obj.put("result", "0");
		obj.put("msg", "登录成功");
		obj.put("uid", user.id);
		obj.put("did", user.did);
		obj.put("cid", user.cid);
		obj.put("dname", dept.namea);
		obj.put("cname", comp.namea);
		obj.put("uname", user.uname);
		mv.returnText = obj.toString();
		return mv;
	}
	
	@WebMethod(name="version.asp")
	public ModelAndView version(Integer userId){
		ModelAndView mv = new ModelAndView();
		mv.encodeReturnText=true;
		mv.returnText = "[{'shareurl':'http://app.qq.com/#id=detail&appid=1101502997','downurl':'http://www.zhongjiebao.com/download/zhongjiebao.apk','version':'1.3','content':[{'content':'1.3正式版发布'}]}]";
		return mv;
	}
}
