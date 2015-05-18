package com.youwei.zjb.house;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.ThreadSession;
import org.bc.web.WebMethod;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.youwei.zjb.KeyConstants;
import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.house.entity.HouseOwner;
import com.youwei.zjb.house.entity.HouseTel;
import com.youwei.zjb.house.entity.TelVerifyCode;
import com.youwei.zjb.sys.OperatorService;
import com.youwei.zjb.sys.OperatorType;
import com.youwei.zjb.user.UserHelper;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.SecurityHelper;

@Module(name="/houseOwner")
public class HouseOwnerService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
	
	HouseService houseService = TransactionalServiceHelper.getTransactionalService(HouseService.class);
	
	@WebMethod
	public ModelAndView sendVerifyCode(String tel){
		ModelAndView mv = new ModelAndView();
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
		restAPI.setAccount("8a48b5514d32a2a8014d4abe84a21169", "046d71432c984d4786c1a4261698cec1");// 初始化主帐号名称和主帐号令牌
		restAPI.setAppId("aaf98f894d328b13014d4c69841c147d");// 初始化应用ID
		result = restAPI.sendTemplateSMS(tvc.tel,"1" ,new String[]{tvc.code,"5"});

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
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
		}
		dao.saveOrUpdate(tvc);
		return mv;
	}
	
	@WebMethod
	public ModelAndView houses(String tel , String pwd){
		ModelAndView mv = new ModelAndView();
		//判断session中没有房主信息
		HouseOwner owner = (HouseOwner)ThreadSession.getHttpSession().getAttribute(KeyConstants.Session_House_Owner);
		if(owner==null){
			pwd = SecurityHelper.Md5(pwd);
			HouseOwner po = dao.getUniqueByParams(HouseOwner.class, new String[]{"tel" , "pwd"}, new Object[]{tel , pwd});
			if(po==null){
				//TODO 信息不正确重新登录
			}else{
				ThreadSession.getHttpSession().setAttribute(KeyConstants.Session_House_Owner, po);
			}
		}
		List<House> houses = dao.listByParams(House.class, "from house where seeGX=1? and tel like ?", "%"+tel+"%");
		mv.jspData.put("houses", houses);
		return mv;
	}
	
	@WebMethod
	public ModelAndView verifyCode(String tel , String code){
		ModelAndView mv = new ModelAndView();
		TelVerifyCode tvc = dao.getUniqueByParams(TelVerifyCode.class, new String[]{"tel","code" },  new Object[]{tel , code});
		if(tvc==null){
			//验证码不正确
		}
		if(System.currentTimeMillis() - tvc.sendtime.getTime()>300*1000){
			//验证码已经过期
		}
		HouseOwner owner  = new HouseOwner();
		owner.tel = tel;
		return mv;
	}
	
	@WebMethod
	public ModelAndView editHouse(Integer id){
		ModelAndView mv = new ModelAndView();
		House po = dao.get(House.class, id);
		if(po==null){
			//房源信息不存在
		}
		mv.jspData.put("house", po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView deleteHouse(Integer id){
		ModelAndView mv = new ModelAndView();
		House po = dao.get(House.class, id);
		if(po==null){
			//房源信息不存在
		}
		po.isdel = 1;
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView addHouse(House house , String hxing){
		ModelAndView mv = new ModelAndView();
		validte(house);
		User user = ThreadSessionHelper.getUser();
		house.isdel = 0;
		house.dateadd = new Date();
		house.uid = user.id;
		house.cid = user.cid;
		house.did = user.did;
		house.sh = 0;
		FangXing fx = FangXing.parse(hxing);
		house.hxf = fx.getHxf();
		house.hxt = fx.getHxt();
		house.hxw = fx.getHxw();
		if(house.mji!=null && house.mji!=0){
			int jiage = (int) (house.zjia*10000/house.mji);
			house.djia = (float) jiage;
		}
		house.seeFH=1;
		house.seeHM=1;
		house.seeGX=1;
		String nbsp = String.valueOf((char)160);
		if(StringUtils.isNotEmpty(house.tel)){
			house.tel = house.tel.trim().replace(nbsp, "");
		}
		dao.saveOrUpdate(house);
		if(StringUtils.isNotEmpty(house.tel)){
			String[] arr = house.tel.split("/");
			for(String tel : arr){
				tel = tel.trim().replace(nbsp, "");
				HouseTel ht = new HouseTel();
				ht.hid = house.id;
				ht.tel = tel;
				dao.saveOrUpdate(ht);
			}
		}
		mv.data.put("msg", "发布成功");
		mv.data.put("result", 0);
		
		String operConts = "[房主添加了房源["+house.area+"],id="+house.id+",seeGX="+house.seeGX;
		operService.add(OperatorType.房源记录, operConts);
		
		try{
			houseService.addDistrictIfNotExist(house);
		}catch(Exception ex){
			LogUtil.log(Level.WARN,"add district of house failed,hid= "+house.id,ex);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView updateHouse(House house , String hxing){
		validte(house);
		ModelAndView mv = new ModelAndView();
		House po = dao.get(House.class, house.id);
		po.area = house.area;
		po.address = house.address;
		po.ztai = house.ztai;
		po.dhao = house.dhao;
		po.fhao = house.fhao;
		po.quyu= house.quyu;
		po.lceng = house.lceng;
		po.zceng = house.zceng;
		po.lxing = house.lxing;
		po.mji = house.mji;
		po.zjia =house.zjia;
		FangXing fx = FangXing.parse(hxing);
		po.hxf = fx.getHxf();
		po.hxt = fx.getHxt();
		po.hxw = fx.getHxw();
		po.dateyear = house.dateyear;
		po.zxiu = house.zxiu;
		po.lxr = house.lxr;
		po.beizhu = house.beizhu;
		if(po.mji!=null && po.mji!=0){
			int jiage = (int) (po.zjia*10000/house.mji);
			po.djia = (float) jiage;
		}
		if(house.tel==null){
			dao.execute("delete from HouseTel where hid = ?", house.id);
			po.tel = house.tel;
		}else{
			if(!house.tel.equals(po.tel)){
				//修改了电话号码
				dao.execute("delete from HouseTel where hid = ?", house.id);
				String[] arr = house.tel.split("/");
				for(String tel : arr){
					HouseTel ht = new HouseTel();
					ht.hid = house.id;
					ht.tel = tel;
					dao.saveOrUpdate(ht);
				}
				po.tel = house.tel;
			}
		}
		dao.saveOrUpdate(po);
		String operConts = "[房主修改了房源["+house.id+"]";
		operService.add(OperatorType.房源记录, operConts);
		mv.data.put("msg", "修改成功");
		//TODO 是否增加跟进信息
		return mv;
	}
	
	private void validte(House house){
		if(house.fhao==null){
			throw new GException(PlatformExceptionType.ParameterMissingError,"fhao","房号不能为空");
		}
		if(house.dhao==null){
			throw new GException(PlatformExceptionType.ParameterMissingError,"dhao","栋号不能为空");
		}
		if(house.mji==null){
			throw new GException(PlatformExceptionType.ParameterMissingError,"mji","面积不能为空");
		}
		if(house.zceng==null){
			throw new GException(PlatformExceptionType.ParameterMissingError,"zceng","总层不能为空");
		}
		
		if(house.zjia==null){
			throw new GException(PlatformExceptionType.ParameterMissingError,"zjia","总价不能为空");
		}
	}
}
