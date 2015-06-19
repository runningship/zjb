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
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.sys.CityService;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.SecurityHelper;

@Module(name="/mobile/user/")
public class PService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	CityService cityService = TransactionalServiceHelper.getTransactionalService(CityService.class);
	
	@WebMethod
	public ModelAndView tracks(Integer userId , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		if(userId==null){
			mv.data.put("result", "1");
			mv.data.put("msg", "没找到相关信息");
			return mv;
		}
		StringBuilder hql = new StringBuilder("select h.id as id ,"
				+ " h.area as area,h.dhao as dhao,h.fhao as fhao,h.ztai as ztai, h.quyu as quyu,h.djia as djia,h.zjia as zjia,h.mji as mji,"
				+ " h.lceng as lceng, h.zceng as zceng from House h , Track t where h.sh=1 and t.hid=h.id and t.uid=?");
		page.orderBy = "h.dateadd";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), true, new Object[]{userId});
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView deltracks(Integer userId , String hid){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(hid) || userId==null){
			mv.data.put("result", "1");
			mv.data.put("msg", "请选择要删除的数据");
			return mv;
		}
		List<Object> params = new ArrayList<Object>();
		params.add(userId);
		StringBuilder hql = new StringBuilder("delete from Track where uid=? and hid in (");
		String ids[] = hid.split(",");
		for(int i=0;i<ids.length;i++){
			hql.append("?");
			params.add(Integer.valueOf(ids[i]));
			if(i<ids.length-1){
				hql.append(",");
			}
		}
		hql.append(")");
		dao.execute(hql.toString(), params.toArray());
		
		mv.data.put("result", "0");
		mv.data.put("msg", "删除成功");
		return mv;
	}
	
//	@WebMethod(name="user/info.asp")
//	public ModelAndView userInfo(Integer userId){
//		ModelAndView mv = new ModelAndView();
//		mv.encodeReturnText=true;
//		JSONArray arr = new JSONArray();
//		JSONObject obj = new JSONObject();
//		User user = null;
//		if(userId!=null){
//			user = dao.get(User.class, userId);
//		}
//		if(user==null){
//			obj.put("result", "1");
//			obj.put("msg", "没有数据");
//			arr.add(obj);
//			mv.returnText = arr.toString();
//			return mv;
//		}
//		Department dept = dao.get(Department.class, user.did);
//		Department comp = dao.get(Department.class, user.cid);
//		obj.put("accound", user.lname);
//		obj.put("uname", user.uname);
//		obj.put("phone", user.tel==null?"":user.tel);
//		obj.put("cname", comp.namea);
//		obj.put("dname", dept.namea);
//		obj.put("result", "0");
//		obj.put("msg", "");
//		arr.add(obj);
//		mv.returnText = arr.toString();
//		return mv;
//	}
	
	@WebMethod
	public ModelAndView login(String cityPy, String tel , String pwd){
		ModelAndView mv = new ModelAndView();
		JSONObject obj = new JSONObject();
		String password = SecurityHelper.Md5(pwd);
		User user = dao.getUniqueByParams(User.class, new String[]{"tel","mobileON"}, new Object[]{tel , 1});
		if(user==null){
			mv.data.put("result", "3");
			mv.data.put("msg", "账号不存在");
			return mv;
		}
		if(!user.pwd.equals(password)){
			mv.data.put("result", "1");
			mv.data.put("msg", "密码错误");
			return mv;
		}
		JSONArray citys = cityService.getCitys();
		for(int i=0;i<citys.size();i++){
			if(citys.getJSONObject(i).getString("py").equals(cityPy)){
				obj.put("quyus", citys.getJSONObject(i).get("quyu"));
				break;
			}
		}
		Department dept = dao.get(Department.class, user.did);
		Department comp = dao.get(Department.class, user.cid);
		obj.put("result", "0");
		obj.put("msg", "登录成功");
		obj.put("uid", user.id);
		obj.put("did", user.did);
		obj.put("cid", user.cid);
		obj.put("lname", user.lname);
		if(user.mobileDeadtime!=null){
			obj.put("deadtime", DataHelper.sdf.format(user.mobileDeadtime));
		}
		if(dept!=null){
			obj.put("dname", dept.namea);
		}
		if(comp!=null){
			obj.put("cname", comp.namea);
		}
		obj.put("uname", user.uname);
		obj.put("tel", tel);
		mv.data = obj;
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
