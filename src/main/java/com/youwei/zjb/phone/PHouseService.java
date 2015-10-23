package com.youwei.zjb.phone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.house.FangXing;
import com.youwei.zjb.house.HouseQuery;
import com.youwei.zjb.house.SellState;
import com.youwei.zjb.house.entity.District;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.house.entity.HouseTel;
import com.youwei.zjb.sys.CityService;
import com.youwei.zjb.sys.OperatorType;
import com.youwei.zjb.user.entity.Track;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.user.entity.ViewHouseLog;
import com.youwei.zjb.util.DataHelper;
@Module(name="/mobile/")
public class PHouseService {
	
	//1000米经验偏移量
	public static float latOffset = 0.009000f;
	public static float lngOffset = 0.010520f;
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	CityService cityService = TransactionalServiceHelper.getTransactionalService(CityService.class);
	
	@WebMethod(name="nearby.asp")
	public ModelAndView nearBy(float longitude , float latitude){
		ModelAndView mv = new ModelAndView();
		String hql="select area as name,address as address ,maplat as latitude ,maplng as longitude,total from house_annex ,( select annex.area as xarea ,COUNT(*) as total from house h ,house_annex annex where h.area=annex.area and h.ztai=4 and h.sh=1 " 
							+" and ((maplat>=? and maplat<=?) and (maplng>=? and maplng<=?)) group by annex.area) as tt where xarea = area and maplat >0";
		List<Map> list = dao.listSqlAsMap(hql, latitude-latOffset , latitude+latOffset , longitude-lngOffset , longitude+lngOffset);
		mv.data.put("result", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView detail(int houseId , Integer userId){
		ModelAndView mv = new ModelAndView();
		House house = dao.get(House.class, houseId);
		JSONObject result = JSONHelper.toJSON(house);
		if(house==null){
			result.put("result","1");
			result.put("msg", "已删除或未审核");
			mv.data = result;
			return mv;
		}
		if(house.tel!=null){
			house.tel=house.tel.replace("&nbsp;", "").replace("/", ",").replace(" ", ",");
			result.put("tel", house.tel);
		}
//		Department dept = dao.get(Department.class, house.did);
//		Department comp = dao.get(Department.class, house.cid);
		
		result.put("dateadd", DataHelper.sdf.format(house.dateadd));
//		result.put("dname", dept.namea);
//		result.put("cname", comp.namea);
//		User lxr = dao.get(User.class,house.uid);
//		result.put("uname", lxr.uname);
		District district = dao.getUniqueByKeyValue(District.class, "name", house.area);
		if(district!=null){
			result.put("latitude", district.maplat);
			result.put("longitude", district.maplng);
		}else{
			result.put("latitude", "");
			result.put("longitude", "");
		}
//		Favorite po = dao.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{userId,houseId});
		if(house.fav==null){
			result.put("isfav", "0");
		}else if(house.fav.contains("@"+userId+"|")){
			result.put("isfav", "1");
		}else{
			result.put("isfav", "0");
		}
		//状态
		SellState state = SellState.parse(house.ztai);
		if(state!=null){
			result.put("ztai", state.toString());
		}
		String hxing="";
		if(house.hxf!=null){
			hxing+=house.hxf+"室";
		}
		if(house.hxt!=null){
			hxing+=house.hxf+"厅";
		}
		if(house.hxw!=null){
			hxing+=house.hxf+"卫";
		}
		result.put("hxing", hxing);
		if(house.dateyear==null){
			house.dateyear=0;
		}
		
		result.put("year", house.dateyear);
		result.put("beizhu", house.beizhu);
		mv.data = result;
		
		if(userId!=null){
			Track track = dao.getUniqueByParams(Track.class, new String[]{"hid" , "uid", "chuzu"}, new Object[]{houseId , userId ,0});
			if(track==null){
				track = new Track();
				track.hid = houseId;
				track.uid = userId;
				track.chuzu = 0;
				dao.saveOrUpdate(track);
			}
			ViewHouseLog vl = new ViewHouseLog();
			vl.hid = houseId;
			vl.uid = userId;
			vl.isMobile = 1;
			vl.viewTime = new Date();
			dao.saveOrUpdate(vl);
		}
		long readCount = dao.countHql("select count(*) from Track where hid=? and chuzu=?", houseId , 0);
		mv.data.put("readCount", readCount);
		return mv;
	}
	@WebMethod
	public ModelAndView list(HouseQuery query){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql  = new StringBuilder();
		if(query.userid!=null){
			//我的收藏
//			hql.append("select h.id as id ,"
//					+ " h.area as area,h.dhao as dhao,h.fhao as fhao,h.ztai as ztai, h.quyu as quyu,h.djia as djia,h.zjia as zjia,h.mji as mji,"
//					+ " h.lceng as lceng, h.zceng as zceng from House h , Favorite f where h.sh=1 and f.houseId=h.id and f.userId=?");
			String favStr = "@"+query.userid+"|";
			hql.append("select h.id as id ,"
					+ " h.area as area,h.dhao as dhao,h.fhao as fhao,h.ztai as ztai, h.quyu as quyu,h.djia as djia,h.zjia as zjia,h.mji as mji,"
					+ " h.lceng as lceng, h.zceng as zceng , h.hxf as hxf , h.hxt as hxt, h.hxw as hxw from House h  where h.sh=1 and h.ztai=5 and h.fav like ?");
			params.add("%"+favStr+"%");
		}else if(query.searchMyPrivateHouse!=null && query.searchMyPrivateHouse==1){
			hql.append("select h.id as id ,"
					+ " h.area as area,h.dhao as dhao,h.fhao as fhao,h.ztai as ztai, h.quyu as quyu,h.djia as djia,h.zjia as zjia,h.mji as mji,"
					+ " h.lceng as lceng, h.zceng as zceng , h.hxf as hxf , h.hxt as hxt, h.hxw as hxw from House h where 1=1  ");
		}else{
			hql.append("select h.id as id ,"
					+ " h.area as area,h.dhao as dhao,h.fhao as fhao,h.ztai as ztai, h.quyu as quyu,h.djia as djia,h.zjia as zjia,h.mji as mji,"
					+ " h.lceng as lceng, h.zceng as zceng , h.hxf as hxf , h.hxt as hxt, h.hxw as hxw from House h where h.seeGX=1 and h.sh=1 and h.ztai=4 ");
		}
		if(StringUtils.isNotEmpty(query.search)){
			hql.append(" and area like ?");
			params.add("%"+query.search+"%");
		}
		if(StringUtils.isNotEmpty(query.dhao)){
			hql.append(" and h.dhao = ? ");
			params.add(query.dhao);
		}
		if(StringUtils.isNotEmpty(query.fhao)){
			hql.append(" and h.fhao like ? ");
			params.add(query.fhao+"%");
		}
		if(StringUtils.isNotEmpty(query.mobileTel)){
			hql.append(" and h.tel like ?");
			params.add("%"+query.mobileTel+"%");
		}
		if(StringUtils.isNotEmpty(query.specArea)){
			HouseQuery hq = new HouseQuery();
			hq.page = query.page;
			hq.specArea = query.specArea;
			query = hq;
		}
		if(StringUtils.isNotEmpty(query.ztai)){
			String[] arr = query.ztai.split(",");
			hql.append(" and ( ");
			for(int i=0;i<arr.length;i++){
				if(StringUtils.isEmpty(arr[i])){
					continue;
				}
				hql.append(" h.ztai = ? ");
				if(i<arr.length-1){
					hql.append(" or ");
				}
				params.add(arr[i]);
			}
			hql.append(" )");
		}
		if(StringUtils.isNotEmpty(query.specArea)){
			hql.append(" and h.area = ?");
			params.add(query.specArea);
		}
		if(query.uid!=null){
			hql.append(" and h.uid= ? ");
			params.add(query.uid);
		}
		if(query.mjiStart!=null){
			hql.append(" and h.mji>= ? ");
			params.add(query.mjiStart);
		}
		if(query.mjiEnd!=null){
			hql.append(" and h.mji<= ? ");
			params.add(query.mjiEnd);
		}
		if(query.zjiaStart!=null){
			hql.append(" and h.zjia>= ? ");
			params.add(query.zjiaStart);
		}
		if(query.zjiaEnd!=null){
			hql.append(" and h.zjia<= ? ");
			params.add(query.zjiaEnd);
		}
		if(query.djiaStart!=null){
			hql.append(" and h.djia>= ? ");
			params.add(query.djiaStart);
		}
		if(query.djiaEnd!=null){
			hql.append(" and h.djia<= ? ");
			params.add(query.djiaEnd);
		}
		if(query.lcengStart!=null){
			hql.append(" and h.lceng>= ? ");
			params.add(query.lcengStart);
		}
		if(query.lcengEnd!=null){
			hql.append(" and h.lceng<= ? ");
			params.add(query.lcengEnd);
		}
		if(StringUtils.isNotEmpty(query.quyu)){
			hql.append(" and ( ");
			String[] arr = query.quyu.split(",");
			for(int i=0;i<arr.length;i++){
				hql.append(" h.quyu like ? ");
				if(i<arr.length-1){
					hql.append(" or ");
				}
				params.add("%"+arr[i]+"%");
			}
			hql.append(" )");
		}
		//hql.append(" and h.ztai=4");
		Page<Map> page = new Page<Map>();
		page.orderBy = "h.dateadd";
		page.order = Page.DESC;
		if(query.page!=null){
			page.setCurrentPageNo(query.page);
		}
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		System.out.println(hql.toString());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getCityList(){
		ModelAndView mv = new ModelAndView();
		JSONArray cityList = cityService.getCitys();
		JSONArray arr = new JSONArray();
		for(int i=0;i<cityList.size();i++){
			JSONObject  city = cityList.getJSONObject(i);
			if(!"on".equals(city.getString("status"))){
				continue;
			}
			JSONObject tmp = new JSONObject();
			tmp.put("name", city.getString("name"));
			tmp.put("pinyin", city.getString("py"));
			tmp.put("quyus", city.getJSONArray("quyu").toString());
			arr.add(tmp);
		}
		mv.data.put("citys", arr);
		mv.data.put("status", 1);
		return mv;
	}
	
	@WebMethod
	public ModelAndView getQuyus(String pinyin){
		ModelAndView mv = new ModelAndView();
		JSONArray cityList = cityService.getCitys();
		for(int i=0;i<cityList.size();i++){
			JSONObject  city = cityList.getJSONObject(i);
			if(city.getString("py").equals(pinyin)){
				mv.data.put("quyus", city.getJSONArray("quyu"));
				break;
			}
		}
		mv.data.put("status", 1);
		return mv;
	}
	
	@WebMethod
	public ModelAndView addPrivateHouse(House house , String hxing , String fangzhuTel){
		ModelAndView mv = new ModelAndView();
		DataHelper.validte(house);
		house.isdel = 0;
		house.dateadd = new Date();
		house.ztai = SellState.在售.getCodeString();
		house.cid = 0;
		house.did = 0;
		house.sh = 0;
		house.seeFH=0;
		house.seeGX=0;
		house.seeHM=0;
		if(StringUtils.isEmpty(hxing)){
			throw new GException(PlatformExceptionType.ParameterMissingError,"hxing","");
		}
		if(StringUtils.isEmpty(fangzhuTel)){
			throw new GException(PlatformExceptionType.ParameterMissingError,"fangzhuTel","");
		}
		FangXing fx = FangXing.parse(hxing);
		house.hxf = fx.getHxf();
		house.hxt = fx.getHxt();
		house.hxw = fx.getHxw();
		house.tel = fangzhuTel;
		if(house.mji!=null && house.mji!=0){
			int jiage = (int) (house.zjia*10000/house.mji);
			house.djia = (float) jiage;
		}
		String nbsp = String.valueOf((char)160);
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
		return mv;
	}
	@WebMethod
	public ModelAndView editPrivateHouse(Integer hid){
		ModelAndView mv = new ModelAndView();
		House po = dao.get(House.class, hid);
		if(po!=null){
			mv.data.put("house", JSONHelper.toJSON(po));
		}else{
			throw new GException(PlatformExceptionType.BusinessException,"房源不存在或被删除");
		}
		FangXing fxing = FangXing.parse(po.hxf, po.hxt,po.hxw);
		if(fxing!=null){
			mv.data.put("hxing", fxing.getName());
		}else{
			LogUtil.warning("房源的户型信息错误,hid="+hid);
		}
		
		return mv;
	}
	
	@WebMethod
	public ModelAndView delPrivateHouse(Integer hid){
		ModelAndView mv = new ModelAndView();
		House po = dao.get(House.class, hid);
		if(po!=null){
			dao.delete(po);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView updatePrivateHouse(House house , String hxing , String fangzhuTel){
		DataHelper.validte(house);
		if(StringUtils.isEmpty(hxing)){
			throw new GException(PlatformExceptionType.ParameterMissingError,"hxing","");
		}
		if(StringUtils.isEmpty(fangzhuTel)){
			throw new GException(PlatformExceptionType.ParameterMissingError,"fangzhuTel","");
		}
		ModelAndView mv = new ModelAndView();
		House po = dao.get(House.class, house.id);
		po.area = house.area;
		po.address = house.address;
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
		dao.saveOrUpdate(po);
		mv.data.put("msg", "修改成功");
		//mv.data.put("house", JSONHelper.toJSON(po , DataHelper.dateSdf.toPattern()));
		mv.data.put("result", 0);
		return mv;
	}
}
