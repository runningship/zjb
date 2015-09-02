package com.youwei.zjb.house;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.HqlHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.DateSeparator;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.house.entity.HouseRent;
import com.youwei.zjb.sys.OperatorService;
import com.youwei.zjb.sys.OperatorType;
import com.youwei.zjb.user.UserHelper;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;

@Module(name="/house/rent/")
public class HouseRentService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
	
	@WebMethod
	public ModelAndView add(HouseRent house , String hxing){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSessionHelper.getUser();
		if(house.mji==null){
			throw new GException(PlatformExceptionType.ParameterMissingError,"mji","面积不能为空");
		}
		if(house.zjia==null){
			throw new GException(PlatformExceptionType.ParameterMissingError,"zjia","租金不能为空");
		}
		//检查，是否是重复房源.检查条件为,小区名+楼栋号+房号
		HouseRent po = service.getUniqueByParams(HouseRent.class, new String[]{"area","dhao","fhao"},new Object[]{house.area,house.dhao,house.fhao});
		if(po!=null){
//			mv.data.put("msg", "同一个房源已经存在");
//			mv.data.put("result", 2);
			throw new GException(PlatformExceptionType.BusinessException,"已经存在相同的房源，请检查小区名称楼栋号");
		}else{
			house.isdel = 0;
			house.dateadd = new Date();
			house.uid = user.id;
			house.did = user.did;
			house.cid = user.cid;
			house.sh = 0;
			FangXing fx = FangXing.parse(hxing);
			house.hxf = fx.getHxf();
			house.hxt = fx.getHxt();
			house.hxw = fx.getHxw();
			if(house.seeFH==null){
				house.seeFH=0;
			}
			if(house.seeHM==null){
				house.seeHM=0;
			}
			if(house.seeGX==null){
				house.seeGX=0;
			}
			house.ruku = 1;
			service.saveOrUpdate(house);
			mv.data.put("msg", "发布成功");
			mv.data.put("result", 0);
		}
		
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 添加了房源["+house.area+"]";
		operService.add(OperatorType.房源记录, operConts);
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(HouseRent house , String hxing){
		ModelAndView mv = new ModelAndView();
		HouseRent po = service.get(HouseRent.class, house.id);
		innerUpdateHouse(po,house,hxing);
		service.saveOrUpdate(po);
		User user = ThreadSessionHelper.getUser();
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 修改了房源["+house.id+"]";
		operService.add(OperatorType.房源记录, operConts);
		mv.data.put("msg", "修改成功");
		mv.data.put("house", JSONHelper.toJSON(po , DataHelper.dateSdf.toPattern()));
		RentState state = RentState.parse(po.ztai);
		if(state!=null){
			mv.data.getJSONObject("house").put("ztai", state.toString());
		}
		mv.data.put("result", 0);
		return mv;
	}
	
	private void innerUpdateHouse(HouseRent po ,HouseRent house ,String hxing){
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
		po.fangshi = house.fangshi;
		FangXing fx = FangXing.parse(hxing);
		po.hxf = fx.getHxf();
		po.hxt = fx.getHxt();
		po.hxw = fx.getHxw();
		po.dateyear = house.dateyear;
		po.zxiu = house.zxiu;
		po.tel = house.tel;
		po.lxr = house.lxr;
		po.forlxr = house.forlxr;
		po.fortel = house.fortel;
		po.beizhu = house.beizhu;
		if(house.seeFH==null){
			house.seeFH=0;
		}
		if(house.seeGX==null){
			house.seeGX=0;
		}
		if(house.seeHM==null){
			house.seeHM=0;
		}
		po.seeFH = house.seeFH;
		po.seeGX	= house.seeGX;
		po.seeHM = house.seeHM;
	}
	@WebMethod
	public ModelAndView get(Integer id){
		ModelAndView mv = new ModelAndView();
		HouseRent po = service.get(HouseRent.class, id);
		FangXing fxing = FangXing.parse(po.hxf, po.hxt,po.hxw);
		mv.data = JSONHelper.toJSON(po);
		if(fxing!=null){
			mv.data.put("hxing", fxing.getName());
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView view(Integer id){
		ModelAndView mv = new ModelAndView();
		HouseRent h = service.get(HouseRent.class, id);
		mv.jspData.put("house", h);
		
		Department dept = service.get(Department.class, h.did);
		User user = service.get(User.class, h.uid);
		mv.jspData.put("fbr", (user==null || user.uname==null) ? "":user.uname);
//		mv.jspData.put("ywyUname", h.forlxr==null ? "":h.forlxr);
//		mv.jspData.put("ywyTel", h.fortel==null ? "":h.fortel);
		mv.jspData.put("dname", dept==null? "":dept.namea);
		RentState ztai = RentState.parse(h.ztai);
		mv.jspData.put("ztai", ztai==null ? "": ztai);
		RentType fs = RentType.parse(h.fangshi);
		mv.jspData.put("fangshi", fs==null ? "": fs.toString());
		String favStr = "@"+ThreadSessionHelper.getUser().id+"|";
		if(h.fav!=null && h.fav.contains(favStr)){
			mv.jspData.put("fav", "1");
		}else{
			mv.jspData.put("fav", "0");
		}
		
		String hql = "select gj.conts as conts ,u.uname as uname , gj.addtime as addtime from GenJin gj , User u "
				+ " where gj.hid=? and gj.uid=u.id and gj.chuzu=  ? and gj.sh=1 order by addtime desc";
		//TODO 参考AbstractSee
		List<Map> gjList = service.listAsMap(hql, Integer.valueOf(id) , 1);
		mv.jspData.put("gjList", gjList);
		return mv;
	}
	
	@WebMethod
	public ModelAndView ruku(int id){
		ModelAndView mv = new ModelAndView();
		HouseRent po = service.get(HouseRent.class, id);
		FangXing fxing = FangXing.parse(po.hxf, po.hxt,po.hxw);
		if(fxing!=null){
			mv.jspData.put("hxing", fxing.getName());
		}else{
			mv.jspData.put("hxing", "");
		}
		mv.jspData.put("fangshi", RentType.toList());
		mv.jspData.put("quyus", QuYu.toList());
		mv.jspData.put("lxing", LouXing.toList());
		mv.jspData.put("zxius", ZhuangXiu.toList());
		mv.jspData.put("hxings", FangXing.toList());
		mv.jspData.put("ztais", RentState.toList());
		mv.jspData.put("house", po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView doRuku(HouseRent house , String hxing){
		ModelAndView mv = new ModelAndView();
//		long count = service.countHqlResult("select count(*) from HouseRent where tel=?", house.tel);
//		if(count>0){
//			throw new GException(PlatformExceptionType.BusinessException,"tel","存在相同的房主电话，可能为重复房源");
//		}
		
		HouseRent po = service.get(HouseRent.class, house.id);
		this.innerUpdateHouse(po, house, hxing);
		po.ruku=1;
		//入库直接审核通过
		po.sh = 1;
		service.saveOrUpdate(po);
		FangXing fxing = FangXing.parse(po.hxf, po.hxt,po.hxw);
		mv.data = JSONHelper.toJSON(po);
		mv.data.put("hxing", fxing.getName());
		return mv;
	}
	
	@WebMethod
	public ModelAndView physicalDelete(Integer houseId){
		ModelAndView mv = new ModelAndView();
		//是否需要权限
		if(houseId!=null){
			HouseRent po = service.get(HouseRent.class, houseId);
			if(po!=null){
				service.delete(po);
				service.execute("delete from GenJin where hid=?", po.id);
			}
		}
		mv.data.put("msg", "删除成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView listMyFav(HouseQuery query ,Page<HouseRent> page){
		User user = ThreadSessionHelper.getUser();
		String favStr = "@"+user.id+"|";
		query.favStr = favStr;
		return listAll(query ,page);
	}
	
	@WebMethod
	public ModelAndView listMyAdd(HouseQuery query ,Page<HouseRent> page){
		User user = ThreadSessionHelper.getUser();
		query.userid = user.id;
		return listAll(query ,page);
	}
	
	@WebMethod
	public ModelAndView listAll(HouseQuery query ,Page<HouseRent> page){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = null;
		if(StringUtils.isNotEmpty(query.xpath)){
			hql = new StringBuilder(" select h from  HouseRent h  ,User u where h.uid=u.id and u.id is not null and u.orgpath like ? ");
			params.add(query.xpath+"%");
		}else{
			hql = new StringBuilder(" select h  from HouseRent  h where 1=1 ");
		}
		User u = ThreadSessionHelper.getUser();
		if(u.cid!=1){
			hql.append(" and ruku=1");
		}
		if("all".equals(query.scope)){
			hql.append(" and (h.cid=? or h.seeGX=?) ");
			params.add(u.cid);
			params.add(1);
		}else if("seeGX".equals(query.scope)){
			hql.append(" and h.seeGX=1 ");
		}else if("comp".equals(query.scope)){
			hql.append(" and h.cid=? ");
			params.add(u.cid);
		}
		if(StringUtils.isNotEmpty(query.ztai)){
			hql.append(" and h.ztai = ? ");
			params.add(query.ztai);
		}
		if(StringUtils.isNotEmpty(query.site)){
			hql.append(" and h.site = ? ");
			params.add(query.site);
		}
		if(StringUtils.isNotEmpty(query.search)){
			hql.append(" and (h.area like ? or h.address like ? or h.tel like ?");
			params.add("%"+query.search+"%");
			params.add("%"+query.search+"%");
			params.add("%"+query.search+"%");
			try{
				int id = Integer.valueOf(query.search);
				hql.append(" or h.id=? ");
				params.add(id);
			}catch(Exception ex){
				
			}
			hql.append(")");
		}
		if(query.id!=null){
			hql.append(" and h.id = ? ");
			params.add(query.id);
		}
		if(StringUtils.isNotEmpty(query.dhao)){
			hql.append(" and h.dhao = ? ");
			params.add(query.dhao);
		}
		if(StringUtils.isNotEmpty(query.fhao)){
			hql.append(" and h.fhao like ? ");
			params.add(query.fhao+"%");
		}
		if(StringUtils.isNotEmpty(query.favStr)){
			hql.append(" and h.fav like ? ");
			params.add("%"+query.favStr+"%");
		}
		if(StringUtils.isNotEmpty(query.tel)){
			query.tel = query.tel.replace(" ", "");
			hql.append(" and h.tel like ? ");
			params.add("%"+query.tel+"%");
		}
		if(StringUtils.isNotEmpty(query.address)){
			query.address = query.address.replace(" ", "");
			hql.append(" and h.address like ? ");
			params.add("%"+query.address+"%");
		}
		if(StringUtils.isNotEmpty(query.area)){
			query.area = query.area.replace(" ", "");
			hql.append(" and h.area like ? ");
			params.add("%"+query.area+"%");
		}
//		if(query.id!=null){
//			hql.append(" and h.id = ?");
//			params.add(query.id);
//		}
		
		if(query.sh!=null){
			hql.append(" and h.sh = ?");
			params.add(query.sh);
		}
		
		if(query.quyus!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.quyus.size();i++){
				hql.append(" h.quyu = ? ");
				if(i<query.quyus.size()-1){
					hql.append(" or ");
				}
				params.add(query.quyus.get(i));
			}
			hql.append(" )");
		}
		
		if(query.lxing!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.lxing.size();i++){
				hql.append(" h.lxing = ? ");
				if(i<query.lxing.size()-1){
					hql.append(" or ");
				}
				params.add(query.lxing.get(i));
			}
			hql.append(" )");
		}
		if(query.zxiu!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.zxiu.size();i++){
				hql.append(" h.zxiu = ? ");
				if(i<query.zxiu.size()-1){
					hql.append(" or ");
				}
				params.add(query.zxiu.get(i));
			}
			hql.append(" )");
		}
		
		if(query.fxing!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.fxing.size();i++){
				String fxing = query.fxing.get(i);
				FangXing fx = FangXing.valueOf(fxing);
				hql.append("( h.hxf=? and h.hxt=? and h.hxw=?)");
				if(i<query.fxing.size()-1){
					hql.append(" or ");
				}
				params.add(fx.getHxf());
				params.add(fx.getHxt());
				params.add(fx.getHxw());
			}
			hql.append(" )");
		}

		if(StringUtils.isNotEmpty(query.leibie)){
			hql.append(" and h.leibie = ? ");
			params.add(query.leibie);
		}
		if(query.zjiaStart!=null){
			hql.append(" and h.zjia>= ? ");
			params.add(query.zjiaStart);
		}
		if(query.zjiaEnd!=null){
			hql.append(" and h.zjia<= ? ");
			params.add(query.zjiaEnd);
		}
		if(query.fangshi!=null){
			hql.append(" and h.fangshi=? ");
			params.add(query.fangshi);
		}
		hql.append(HqlHelper.buildDateSegment("h.dateadd",query.dateStart,DateSeparator.After,params));
		hql.append(HqlHelper.buildDateSegment("h.dateadd",query.dateEnd, DateSeparator.Before , params));
		
		if(query.mjiStart!=null){
			hql.append(" and h.mji>= ? ");
			params.add(query.mjiStart);
		}
		if(query.mjiEnd!=null){
			hql.append(" and h.mji<= ? ");
			params.add(query.mjiEnd);
		}
		if(query.lcengStart!=null){
			hql.append(" and h.lceng>= ? ");
			params.add(query.lcengStart);
		}
		if(query.lcengEnd!=null){
			hql.append(" and h.lceng<= ? ");
			params.add(query.lcengEnd);
		}
		if(query.djiaStart!=null){
			hql.append(" and h.djia>= ? ");
			params.add(query.djiaStart);
		}
		if(query.djiaEnd!=null){
			hql.append(" and h.djia<= ? ");
			params.add(query.djiaEnd);
		}
		if(query.yearStart!=null){
			hql.append(" and h.dateyear>= ? ");
			params.add(query.yearStart);
		}
		if(query.yearEnd!=null){
			hql.append(" and h.dateyear<= ? ");
			params.add(query.yearEnd);
		}
		
		if(query.userid!=null){
			hql.append(" and h.uid= ? ");
			params.add(query.userid);
		}

		page.orderBy = "h.dateadd";
		page.order = Page.DESC;
		page.setPageSize(25);
		page = service.findPage(page, hql.toString(),params.toArray());
		ModelAndView mv = new ModelAndView();
		JSONObject jpage = JSONHelper.toJSON(page,DataHelper.dateSdf.toPattern());
		fixEnumValue(jpage);
		mv.data.put("page", jpage);
		return mv;
	}
	
	@WebMethod
	public ModelAndView toggleShenHe(Integer id){
		ModelAndView mv = new ModelAndView();
		if(id!=null){
			HouseRent po = service.get(HouseRent.class, id);
			if(po!=null){
				if(po.sh==1){
					po.sh=0;
				}else{
					po.sh=1;
				}
				service.saveOrUpdate(po);
				mv.data.put("sh", po.sh);
			}
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView listNoTel(Page<Map> page){
		ModelAndView mv = new ModelAndView();
		page = service.findPage(page, "select id as id, href as href,site as site from HouseRent where site=? and ruku=0 and tel=?", true , new Object[]{"baixing",""});
		mv.data.put("houses", JSONHelper.toJSONArray(page.getResult()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView updateTel(Integer hid , String tel){
		ModelAndView mv = new ModelAndView();
		HouseRent po = service.get(HouseRent.class, hid);
		if(po!=null){
			po.tel = tel;
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView house_rent(Integer id){
		ModelAndView mv = new ModelAndView();
		User u = ThreadSessionHelper.getUser();
		mv.jspData.put("cid", u.cid);
		if(UserHelper.hasAuthority(u, "fy_sh")){
			mv.jspData.put("sh", "");
		}else{
			//没有审核权的用户只能看到审核通过对数据
			mv.jspData.put("sh", "1");
		}
		return mv;
	}
	
	private void fixEnumValue(JSONObject jpage) {
		JSONArray results = jpage.getJSONArray("data");
		for(int i=0;i<results.size();i++){
			JSONObject obj = results.getJSONObject(i);
			String key = (String)obj.get("ztai");
			RentState state = RentState.parse(key);
			if(state!=null){
				obj.put("ztai", state.toString());
			}
		}
	}

}
