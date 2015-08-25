package com.youwei.zjb.user;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import cn.jpush.api.utils.StringUtils;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.youwei.zjb.house.entity.TelVerifyCode;
import com.youwei.zjb.sys.CityService;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.SecurityHelper;

@Module(name="/mobile/user/")
public class MobileUserService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	private static final String tempId = "27067";
	CityService cityService = new CityService();
	@WebMethod
	public ModelAndView sendVerifyCode(String tel){
		ModelAndView mv = new ModelAndView();
//		ThreadSession.setCityPY("hefei");
		TelVerifyCode tvc = new TelVerifyCode();
		tvc.tel = tel;
		//send code to tel
		tvc.sendtime =  new Date();
        int max=9999;
        int min=1000;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        String code = String.valueOf(s);
        tvc.code = code ;

		HashMap<String, Object> result = null;

		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init("sandboxapp.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount("8a48b5514d0427c1014d0429646a0002", "78a9ff1208304b949309f117a63f1d9b");// 初始化主帐号名称和主帐号令牌
		restAPI.setAppId("aaf98f894d328b13014d65d868e1242a");// 初始化应用ID
		result = restAPI.sendTemplateSMS(tvc.tel,tempId ,new String[]{tvc.code,"5"});

		System.out.println("SDKTestGetSubAccounts result=" + result);
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
		}else{
			//异常返回输出错误码和错误信息
			LogUtil.info("发送验证码失败,tel = "+tel+",错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
		}
		dao.saveOrUpdate(tvc);
		return mv;
	}

	@WebMethod
	public ModelAndView verifyCode(String tel , String code , String pwd , String uname){
		//在各自数据库中操作
//		ThreadSession.setCityPY("hefei");
		ModelAndView mv = new ModelAndView();
		TelVerifyCode tvc = dao.getUniqueByParams(TelVerifyCode.class, new String[]{"tel","code" },  new Object[]{tel , code});
		if(tvc==null){
			//验证码不正确
//			throw new GException(PlatformExceptionType.BusinessException,"验证码不正确");
			mv.data.put("msg", "验证码不正确");
			mv.data.put("result", "0");
			return mv;
		}
		if(System.currentTimeMillis() - tvc.sendtime.getTime()>300*1000){
			//验证码已经过期
//			throw new GException(PlatformExceptionType.BusinessException,"验证码已经过期");
			mv.data.put("msg", "验证码已经过期");
			mv.data.put("result", "0");
			return mv;
		}
		User muser  = dao.getUniqueByKeyValue(User.class,"tel", tel);
		if(muser!=null ){
			if(muser.mobileON!=null){
//				throw new GException(PlatformExceptionType.BusinessException,"手机号码已经注册");
				mv.data.put("msg", "手机号码已经注册");
				mv.data.put("result", "0");
				return mv;
			}else{
				//手机号码已经填写，但未开通,在这里开通,同时电脑版的密码也随之改变了，这里要注意
				
			}
		}else{
			muser = new User();
			muser.addtime = new Date();
		}
		muser.pwd = SecurityHelper.Md5(pwd);
		if(StringUtils.isNotEmpty(uname)){
			muser.uname = uname;
		}
		muser.tel = tel;
		muser.mobileON = 1;
		//注册送1天试用
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 1);
		muser.mobileDeadtime =  cal.getTime();
		dao.saveOrUpdate(muser);
		
//		ThreadSession.getHttpSession().setAttribute(KeyConstants.Session_Mobile_User, muser);
		mv.data.put("uid", muser.id);
		mv.data.put("mobileDeadtime", DataHelper.dateSdf.format(muser.mobileDeadtime));
		mv.data.put("fufei", "1");
		mv.data.put("uname", muser.uname);
		mv.data.put("tel", tel);
		mv.data.put("result", "1");
		return mv;
	}
	
	@WebMethod
	public ModelAndView modifyPwd(String tel , String code , String pwd){
		//发送验证码，验证，登录操作都在主库中进行
		//在各自数据库中操作
//		ThreadSession.setCityPY("hefei");
		ModelAndView mv = new ModelAndView();
		TelVerifyCode tvc = dao.getUniqueByParams(TelVerifyCode.class, new String[]{"tel","code" },  new Object[]{tel , code});
		if(tvc==null){
			//验证码不正确
			throw new GException(PlatformExceptionType.BusinessException,"验证码不正确");
		}
		if(System.currentTimeMillis() - tvc.sendtime.getTime()>300*1000){
			//验证码已经过期
			throw new GException(PlatformExceptionType.BusinessException,"验证码已经过期");
		}
		User muser  = dao.getUniqueByKeyValue(User.class,"tel", tel);
		if(muser==null){
			throw new GException(PlatformExceptionType.BusinessException,"手机号码未注册");
		}
		muser.pwd = SecurityHelper.Md5(pwd);
		dao.saveOrUpdate(muser);
		
		mv.data.put("result", "1");
		return mv;
	}
}
