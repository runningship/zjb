package com.youwei.zjb.phone;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.ThreadSession;
import org.bc.web.WebMethod;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.cache.ConfigCache;
import com.youwei.zjb.house.entity.HouseImage;
import com.youwei.zjb.sys.CityService;
import com.youwei.zjb.user.MobileUserDog;
import com.youwei.zjb.user.MobileUserService;
import com.youwei.zjb.user.UserHelper;
import com.youwei.zjb.user.entity.Charge;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.InvitationActivation;
import com.youwei.zjb.user.entity.MUserActiveDevice;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.ImageHelper;
import com.youwei.zjb.util.MailUtil;
import com.youwei.zjb.util.SecurityHelper;

@Module(name="/mobile/user/")
public class PService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	CityService cityService = TransactionalServiceHelper.getTransactionalService(CityService.class);
	private static final String masterSecret ="17bfb60aef56934949f1b231";
	private static final String appKey ="71f5687861a727f2827ba04a";
	MobileUserService mService = TransactionalServiceHelper.getTransactionalService(MobileUserService.class);
	
	static final int MAX_SIZE = 1024000*100;
	static final String BaseFileDir = ConfigCache.get("user_avatar_path", "");
	
	private static final String iosShenHeVersion = "";
	@WebMethod
	public ModelAndView tracks(Integer userId , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		if(userId==null){
			mv.data.put("result", "1");
			mv.data.put("msg", "没找到相关信息");
			return mv;
		}
		StringBuilder hql = new StringBuilder("select h.id as id ,"
				+ " h.area as area,h.dhao as dhao,h.fhao as fhao,h.ztai as ztai, h.quyu as quyu,h.djia as djia,h.zjia as zjia,h.mji as mji,h.hxt as hxt , h.hxf as hxf, h.hxw as hxw, "
				+ " h.lceng as lceng, h.zceng as zceng from House h , Track t where h.sh=1 and t.hid=h.id and t.chuzu=0 and t.uid=?");
		page.orderBy = "t.viewTime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), true, new Object[]{userId});
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView tracksRent(Integer userId , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		if(userId==null){
			mv.data.put("result", "1");
			mv.data.put("msg", "没找到相关信息");
			return mv;
		}
		StringBuilder hql = new StringBuilder("select h.id as id ,"
				+ " h.area as area,h.dhao as dhao,h.fhao as fhao,h.ztai as ztai, h.quyu as quyu, h.zjia as zjia,h.mji as mji,h.fangshi as fangshi ,h.hxf as hxf,h.hxt as hxt, h.hxw as hxw,"
				+ " h.lceng as lceng, h.zceng as zceng from HouseRent h , Track t where h.sh=1 and t.hid=h.id and t.chuzu=1 and t.uid=?");
		page.orderBy = "t.viewTime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), true, new Object[]{userId});
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView deltracks(Integer userId , String hid , Integer chuzu){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(hid) || userId==null){
			mv.data.put("result", "1");
			mv.data.put("msg", "请选择要删除的数据");
			return mv;
		}
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("delete from Track where uid=? and chuzu=? and hid in (");
		params.add(userId);
		params.add(chuzu);
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
	
	@WebMethod
	public ModelAndView emptyTracks(Integer userId ,Integer chuzu){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("delete from Track where uid=? and chuzu=?");
		params.add(userId);
		params.add(chuzu);
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
	public ModelAndView logout(String tel){
		ModelAndView mv = new ModelAndView();
		MobileUserDog.map.remove(tel);
		return mv;
	}
	
	@WebMethod
	public ModelAndView login(String cityPy, String tel , String pwd , String deviceId){
		ModelAndView mv = new ModelAndView();
		JSONObject obj = new JSONObject();
		String password = SecurityHelper.Md5(pwd);
		User user = dao.getUniqueByParams(User.class, new String[]{"tel","mobileON"}, new Object[]{tel , 1});
		if(user==null){
			mv.data.put("result", "3");
			mv.data.put("msg", "账号不存在或被锁定");
			return mv;
		}
		if(!user.pwd.equals(password)){
			mv.data.put("result", "1");
			mv.data.put("msg", "密码错误");
			return mv;
		}
//		JSONArray citys = cityService.getCitys();
//		for(int i=0;i<citys.size();i++){
//			if(citys.getJSONObject(i).getString("py").equals(cityPy)){
//				obj.put("quyus", citys.getJSONObject(i).get("quyu"));
//				break;
//			}
//		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date start = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date end = cal.getTime();
		long activeCount = dao.countHql("select count ( distinct deviceId ) from MUserActiveDevice where tel=? and activeTime>=? and activeTime<=? group by tel", tel , start , end);
		long myActiveCount = dao.countHql("select count (*) from MUserActiveDevice where tel=? and deviceId=? and activeTime>=? and activeTime<=? group by tel", tel , deviceId, start , end);
		
		if(activeCount>=1 && myActiveCount<1){
			mv.data.put("result", "1");
			mv.data.put("msg", "账号每天只能在1台设备上登录");
			return mv;
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
			obj.put("mobileDeadtime", DataHelper.dateSdf.format(user.mobileDeadtime));
			obj.put("mobileDeadtimeInLong", user.mobileDeadtime.getTime());
			if(!user.mobileDeadtime.before(Calendar.getInstance().getTime())){
				obj.put("fufei", "1");
			}
		}else{
			//过期日期设置成昨天
			Calendar cal2 = Calendar.getInstance();
			cal2.add(Calendar.DAY_OF_MONTH, -1);
			obj.put("mobileDeadtimeInLong", cal.getTime().getTime());
			obj.put("mobileDeadtime", DataHelper.dateSdf.format(cal.getTime()));
		}
//		obj.put("mobileDeadtime", "3天后到期");
		if(dept!=null){
			obj.put("dname", dept.namea);
		}
		if(comp!=null){
			obj.put("cname", comp.namea);
		}
		obj.put("uname", user.uname);
		obj.put("tel", tel);
//		obj.put("payWay", "online");
		obj.put("iosShenHeVersion", iosShenHeVersion);
		obj.put("avatarPath", user.avatarPath);
		mv.data = obj;
		InvitationActivation activation = dao.getUniqueByKeyValue(InvitationActivation.class, "inviteeUid", user.id);
		if(activation!=null){
			mv.data.put("invitationActive", activation.active);
		}else{
			mv.data.put("invitationActive", -1);
		}
		mv.data.put("debug", user.flag);
		MobileUserDog.map.put(tel, deviceId);
		if(user.jifen==null){
			user.jifen = 1;
			dao.saveOrUpdate(user);
		}
		mv.data.put("jifen", user.jifen);
		if(user.avatar==null){
			Random r = new Random();
			int avatar = r.nextInt(167)+1;
			user.avatar = avatar;
			dao.saveOrUpdate(user);
		}
		MUserActiveDevice device = new MUserActiveDevice();
		device.activeTime = new Date();
		device.deviceId = deviceId;
		device.tel = tel;
		dao.saveOrUpdate(device);
		if(user.lname==null){
			Integer pcuid = UserHelper.getAnotherUser(user.id);
			User pcuser = dao.get(User.class, pcuid);
			if(pcuser!=null){
				mv.data.put("pcLname", pcuser.lname);
			}
		}else{
			mv.data.put("pcLname", user.lname);
		}
		
		mv.data.put("avatar", user.avatar);
//		pushToOther(tel,deviceId);
		return mv;
	}
	
	@WebMethod
	public ModelAndView isIOSOnline(){
		ModelAndView mv = new ModelAndView();
//		mv.data.put("iosOnline", 0);
		mv.data.put("iosShenHeVersion", iosShenHeVersion);
		return mv;
	}
	
	@WebMethod
	public ModelAndView getUserFufeiInfo(Integer uid){
		ModelAndView mv = new ModelAndView();
		User u = dao.get(User.class, uid);
		mv.data.put("mobileDeadtime", DataHelper.dateSdf.format(u.mobileDeadtime));
		mv.data.put("fufei", "1");
		return mv;
	}
	
	private void pushToOther(String tel,String deviceId){
		JPushClient mClient = new JPushClient(masterSecret, appKey);
		PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(tel))
                .setMessage(Message.content(deviceId))
                .build();
        mClient.sendPush(payload);
	}
	
	@WebMethod
	public ModelAndView paySuccess(Integer monthAdd ,Integer userId,String tradeNO,Float fee,Integer payType){
		ModelAndView mv = new ModelAndView();
		Charge po = dao.getUniqueByKeyValue(Charge.class, "tradeNO", tradeNO);
		if(po!=null){
			mv.data.put("fufei", "1");
			mv.data.put("result", "1");
			return mv;
		}
		User user = dao.get(User.class, userId);
		Charge charge = new Charge();
		charge.uid = userId;
		charge.tradeNo = tradeNO;
		charge.fee = fee;
		charge.payType = payType;
		charge.clientType="mobile";
		charge.addtime = new Date();
		charge.status = "TRADE_SUCCESS";
		charge.beizhu="andorid "+user.tel;
		charge.finish = 1;
		//注意要以fee为准
		//在很低的概率下，用户迅速选择年度，而后ajax完成将价格改成季付
		charge.monthAdd = monthAdd;
		charge.uname = user.uname;
		dao.saveOrUpdate(charge);
//		user.mobileDeadtime
		Calendar cal = Calendar.getInstance();
		if(user.mobileDeadtime==null){
			cal.add(Calendar.MONTH, monthAdd);
			user.mobileDeadtime = cal.getTime();
		}else{
			if(user.mobileDeadtime.after(cal.getTime())){
				cal.setTime(user.mobileDeadtime);
				cal.add(Calendar.MONTH, monthAdd);
				user.mobileDeadtime = cal.getTime();
			}else{
				//已过期,从当前时间算起
				cal.add(Calendar.MONTH, monthAdd);
				user.mobileDeadtime = cal.getTime();
			}
		}
		user.lastPaytime = new Date();
		dao.saveOrUpdate(user);
		mv.data.put("invitationActive",mService.activeInvitation(user));
		mv.data.put("mobileDeadtime", DataHelper.dateSdf.format(user.mobileDeadtime));
		mv.data.put("fufei", "1");
		mv.data.put("result", "1");
		mv.data.put("paytime", DataHelper.sdf.format(charge.addtime));
		
		try{
			List<String> toList = new ArrayList<String>();
			toList.add("253187898@qq.com");
			MailUtil.send_email(toList, "手机版费用", charge.fee+"电话:"+user.tel+",城市:"+ThreadSessionHelper.getCityPinyin());
		}catch(Exception ex){
			LogUtil.warning("---");
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView alipayCallback(Integer userId){
		ModelAndView mv = new ModelAndView();
		HttpServletRequest request = ThreadSession.HttpServletRequest.get();
		Enumeration<String> names = request.getParameterNames();
		LogUtil.info("收到支付宝回调");
		while(names.hasMoreElements()){
			String key = names.nextElement();
			String[] val = request.getParameterValues(key);
			if(val.length>0){
				LogUtil.info(key+"="+val[0]);
			}
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView version(){
		ModelAndView mv = new ModelAndView();
		mv.data.put("latestVersion", ConfigCache.get("mobile_version", ""));
		return mv;
	}
	
	@WebMethod
	public ModelAndView feeInfo(){
		ModelAndView mv = new ModelAndView();
		mv.data.put("monthPay", "40");
		mv.data.put("seasonPay", "100");
		mv.data.put("yearPay", "300");
		return mv;
	}
	
	@WebMethod
	public ModelAndView setUserAvatar(Integer uid){
		ModelAndView mv = new ModelAndView();
		HttpServletRequest request = ThreadSession.HttpServletRequest.get();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		User po = dao.get(User.class, uid);
		try{
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem item : items){
				if(item.isFormField()){
					continue;
				}
				if(item.getSize()<=0){
					throw new RuntimeException("至少先选择一张图片.");
				}else if(item.getSize()>=MAX_SIZE){
						throw new RuntimeException("单个图片不能超过2M");
				}else{
					String thumbName = item.getName();
					thumbName =  item.getName()+".t.jpg";
					String savePath = BaseFileDir+File.separator +po.id+File.separator +item.getName();
					String thumbPath = BaseFileDir+File.separator +po.id+File.separator+thumbName;
					FileUtils.copyInputStreamToFile(item.getInputStream(), new File(savePath));
					ImageHelper.resize(savePath, 200, 200, thumbPath);
					po.avatarPath = thumbName;
					mv.data.put("avatarPath", po.avatarPath);
					dao.saveOrUpdate(po);
				}
			}
			mv.data.put("result", 0);
			return mv;
		}catch(Exception ex){
			throw new GException(PlatformExceptionType.BusinessException,"文件图片失败" , ex);
		}
	}
	
	@WebMethod
	public ModelAndView updateUser(User user){
		ModelAndView mv = new ModelAndView();
		User po = dao.get(User.class, user.id);
		po.uname = user.uname;
		dao.saveOrUpdate(po);
		
		Integer pcuid = UserHelper.getAnotherUser(user.id);
		User pcUser = dao.get(User.class, pcuid);
		if(pcUser!=null){
			pcUser.uname = user.uname;
			dao.saveOrUpdate(pcUser);
		}
		mv.data.put("uname", user.uname);
		mv.data.put("result", 0);
		return mv;
	}
}
