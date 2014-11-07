package com.youwei.zjb.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.house.FangXing;
import com.youwei.zjb.house.HouseQuery;
import com.youwei.zjb.house.HouseService;
import com.youwei.zjb.house.LouXing;
import com.youwei.zjb.house.QuYu;
import com.youwei.zjb.house.ZhuangXiu;
import com.youwei.zjb.house.entity.District;
import com.youwei.zjb.house.entity.ExpHouse;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/house/exp/")
public class ExpHouseService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	HouseService hs = new HouseService();
	@WebMethod
	public ModelAndView list(HouseQuery query ,Integer finish, Page<ExpHouse> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from ExpHouse where finish is null or finish=0");
		if(query.id!=null){
			hql.append(" and id=?");
			params.add(query.id);
		}
		if(finish!=null){
			hql.append(" and finish=?");
			params.add(finish);
		}
		if(StringUtils.isNotEmpty(query.area)){
			query.area = query.area.replace(" ", "");
			hql.append(" and area like ? ");
			params.add("%"+query.area+"%");
		}
		if(query.quyus!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.quyus.size();i++){
				hql.append(" quyu = ? ");
				if(i<query.quyus.size()-1){
					hql.append(" or ");
				}
				params.add(query.quyus.get(i));
			}
			hql.append(" )");
		}
		page.orderBy="addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString() , params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView shenhe(House house ,String hxing, Integer expHid){
		ExpHouse po = dao.get(ExpHouse.class, expHid);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException,"该房源已不存在");
		}
		if(StringUtils.isEmpty(house.zxiu)){
			throw new GException(PlatformExceptionType.BusinessException,"请先选择装潢");
		}
		if(StringUtils.isEmpty(house.lxing)){
			throw new GException(PlatformExceptionType.BusinessException,"请先选择楼型");
		}
		if(StringUtils.isEmpty(house.quyu)){
			throw new GException(PlatformExceptionType.BusinessException,"请先选择区域");
		}
		if(StringUtils.isEmpty(hxing)){
			throw new GException(PlatformExceptionType.BusinessException,"请先选择户型");
		}
		
		ModelAndView mv = hs.add(house, hxing);
		po.finish=1;
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView notPass(Integer expHid,Integer finish, String beizhu){
		ExpHouse po = dao.get(ExpHouse.class, expHid);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException,"该房源已不存在");
		}
		po.finish=2;
		po.beizhu = beizhu;
		dao.saveOrUpdate(po);
		return new ModelAndView();
	}
	@WebMethod
	public ModelAndView get(int id){
		ExpHouse po = dao.get(ExpHouse.class, id);
		if(po.shr!=null && !ThreadSession.getUser().uname.equals(po.shr)){
			throw new GException(PlatformExceptionType.BusinessException,"该房源正由"+po.shr+"在审核");
		}
		long count = dao.countHql("select count(*) from ExpHouse where shr=?", ThreadSession.getUser().uname);
		if(count>2){
			throw new GException(PlatformExceptionType.BusinessException,"您已经打开两天房源，请先处理未处理完房源.");
		}
		String nbsp = String.valueOf((char)160);
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(po.address)){
			List<District> list = dao.listByParams(District.class, "from District where name=?", po.area.trim().replace(nbsp, ""));
			if(list!=null && list.size()>0){
				po.address = list.get(0).address;
			}
		}
		FangXing fxing = FangXing.parse(Integer.valueOf(po.hxf), Integer.valueOf(po.hxt), Integer.valueOf(po.hxw));
		mv.data = JSONHelper.toJSON(po);
		
		if(po.quyu!=null){
			String qy = po.quyu.trim().replace(nbsp, "");
			if("合肥周边".equals(qy)){
				mv.data.put("quyu", QuYu.周边市区);
			}else{
				mv.data.put("quyu", qy+"区");
			}
		}
		if(po.zxiu!=null){
			String zxiu =po.zxiu.trim().replace(nbsp, "");
			if("中等装修".equals(zxiu)){
				mv.data.put("zxiu", ZhuangXiu.中装);
			}else if("简单装修".equals(zxiu)){
				mv.data.put("zxiu", ZhuangXiu.简装);
			}else if("精装修".equals(zxiu)){
				mv.data.put("zxiu", ZhuangXiu.精装);
			}else if("豪华装修".equals(zxiu)){
				mv.data.put("zxiu", ZhuangXiu.豪装);
			}else{
				mv.data.put("zxiu", zxiu);
			}
		}
		if(po.area!=null){
			mv.data.put("area", po.area.trim().replace(nbsp, ""));
		}
		if(po.lceng!=null){
			mv.data.put("lceng", po.lceng.trim().replace(nbsp, ""));
		}
		if(po.zceng!=null){
			mv.data.put("zceng", po.zceng.trim().replace(nbsp, ""));
		}
		if(fxing!=null){
			mv.data.put("hxing", fxing.getName());
		}else{
			LogUtil.warning("房源的户型信息错误,hid="+id);
		}
		if(po.zceng!=null){
			try{
				Integer zc = Integer.valueOf(po.zceng.trim().replace(nbsp, ""));
				if(zc<=7){
					mv.data.put("lxing", LouXing.多层);
				}else if(zc>7 && zc<=12){
					mv.data.put("lxing", LouXing.小高层);
				}else if(zc>12){
					mv.data.put("lxing", LouXing.高层);
				}
			}catch(Exception ex){
				
			}
		}
		po.shr = ThreadSession.getUser().uname;
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView start(int id){
		ModelAndView mv = new ModelAndView();
		ExpHouse po = dao.get(ExpHouse.class, id);
		if(StringUtils.isNotEmpty(po.shr)){
			po.shr = ThreadSession.getUser().uname;
		}else{
			throw new GException(PlatformExceptionType.BusinessException,"该房源正由"+po.shr+"在审核");
		}
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView end(int id){
		ModelAndView mv = new ModelAndView();
		ExpHouse po = dao.get(ExpHouse.class, id);
		if(ThreadSession.getUser().uname.equals(po.shr)){
			po.shr="";
		}else{
			throw new GException(PlatformExceptionType.BusinessException,"该房源不是由您在审核，所以您不能关闭流程");
		}
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView publish(House house ,String hxing, int id){
		ModelAndView mv = new ModelAndView();
		ExpHouse po = dao.get(ExpHouse.class, id);
		po.finish = 1;
		
		return mv;
	}
}
