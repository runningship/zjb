package com.youwei.zjb.phone;

import java.text.SimpleDateFormat;
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

import com.youwei.zjb.house.HouseQuery;
import com.youwei.zjb.house.SellState;
import com.youwei.zjb.house.entity.District;
import com.youwei.zjb.house.entity.Favorite;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.sys.CityService;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.Track;
import com.youwei.zjb.user.entity.User;
@Module(name="/mobile/")
public class PHouseService {
	
	//1000米经验偏移量
	public static float latOffset = 0.009000f;
	public static float lngOffset = 0.010520f;
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	CityService cityService = TransactionalServiceHelper.getTransactionalService(CityService.class);
	
	@WebMethod(name="nearby.asp")
	public ModelAndView nearBy(float longitude , float latitude){
		long t1 = System.currentTimeMillis();
		ModelAndView mv = new ModelAndView();
		String hql="select area as name,maplat as latitude ,maplng as longitude,total from house_annex ,( select annex.area as xarea ,COUNT(*) as total from house h ,house_annex annex where h.area=annex.area " 
							+" and ((maplat>=? and maplat<=?) and (maplng>=? and maplng<=?)) group by annex.area) as tt where xarea = area and maplat >0";
		List<Map> list = dao.listSqlAsMap(hql, latitude-latOffset , latitude+latOffset , longitude-lngOffset , longitude+lngOffset);
//		mv.encodeReturnText=true;
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
		Department dept = dao.get(Department.class, house.did);
		Department comp = dao.get(Department.class, house.cid);
		
		result.put("dateadd", new SimpleDateFormat("yyyy年MM月dd").format(house.dateadd));
		result.put("dname", dept.namea);
		result.put("cname", comp.namea);
		User lxr = dao.get(User.class,house.uid);
		result.put("uname", lxr.uname);
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
		mv.data = result;
		
		if(userId!=null){
			Track track = dao.getUniqueByParams(Track.class, new String[]{"hid" , "uid" }, new Object[]{houseId , userId });
			if(track==null){
				track = new Track();
				track.hid = houseId;
				track.uid = userId;
				dao.saveOrUpdate(track);
			}
		}
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
					+ " h.lceng as lceng, h.zceng as zceng from House h  where h.sh=1 and h.fav like ?");
			params.add("%"+favStr+"%");
		}else{
			hql.append("select h.id as id ,"
					+ " h.area as area,h.dhao as dhao,h.fhao as fhao,h.ztai as ztai, h.quyu as quyu,h.djia as djia,h.zjia as zjia,h.mji as mji,"
					+ " h.lceng as lceng, h.zceng as zceng from House h where h.sh=1 ");
		}
		if(StringUtils.isNotEmpty(query.search)){
			hql.append(" and area like ?");
			params.add("%"+query.search+"%");
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
		Page<Map> page = new Page<Map>();
		page.orderBy = "h.dateadd";
		page.order = Page.DESC;
		if(query.page!=null){
			page.setCurrentPageNo(query.page);
		}
		System.out.println(page.currentPageNo);
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
//		mv.returnText = JSONHelper.toJSONArray(page.getResult()).toString();
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
}
