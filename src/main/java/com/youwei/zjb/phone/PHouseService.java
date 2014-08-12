package com.youwei.zjb.phone;

import java.net.URLEncoder;
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

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.house.HouseQuery;
import com.youwei.zjb.house.State;
import com.youwei.zjb.house.entity.District;
import com.youwei.zjb.house.entity.Favorite;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.Track;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.JSONHelper;
@Module(name="/house/")
public class PHouseService {
	
	//1000米经验偏移量
	public static float latOffset = 0.009000f;
	public static float lngOffset = 0.010520f;
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod(name="nearby.asp")
	public ModelAndView nearBy(float longitude , float latitude){
		ModelAndView mv = new ModelAndView();
		String hql="select area as name,maplat as latitude ,maplng as longitude,total from house_annex ,( select annex.area as xarea ,COUNT(*) as total from house h ,house_annex annex where h.area=annex.area " 
							+" and ((maplat>=? and maplat<=?) and (maplng>=? and maplng<=?)) group by annex.area) as tt where xarea = area";
		List<Map> list = dao.listSqlAsMap(hql, latitude-latOffset , latitude+latOffset , longitude-lngOffset , longitude+lngOffset);
		mv.encodeReturnText=true;
		mv.returnText = JSONHelper.toJSONArray(list).toString();
		return mv;
	}
	
	@WebMethod(name="detail.asp")
	public ModelAndView detail(int houseId , Integer userId){
		ModelAndView mv = new ModelAndView();
		mv.encodeReturnText=true;
		House house = dao.get(House.class, houseId);
		JSONArray arr = new JSONArray();
		JSONObject result = JSONHelper.toJSON(house);
		
		if(house==null){
			result.put("result","1");
			result.put("msg", "已删除或未审核");
			arr.add(result);
			mv.returnText = arr.toString();
			return mv;
		}
		Department dept = dao.get(Department.class, house.did);
		Department comp = dao.get(Department.class, house.cid);
		
		result.put("dname", dept.namea);
		result.put("cname", comp.namea);
		User user = dao.get(User.class,house.uid);
		result.put("uname", user.uname);
		District district = dao.getUniqueByKeyValue(District.class, "name", house.area);
		if(district!=null){
			result.put("latitude", district.maplat);
			result.put("longitude", district.maplng);
		}else{
			result.put("latitude", "");
			result.put("longitude", "");
		}
		Favorite po = dao.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{userId,houseId});
		if(po==null){
			result.put("isfav", "0");
		}else{
			result.put("isfav", "1");
		}
		//状态
		State state = State.parse(house.ztai);
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
		arr.add(result);
		mv.returnText = arr.toString();
		
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
	@WebMethod(name="list.asp")
	public ModelAndView list(HouseQuery query){
		ModelAndView mv = new ModelAndView();
		mv.encodeReturnText=true;
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql  = new StringBuilder();
		if(query.userid!=null){
			//我的收藏
			hql.append("select h.id as id ,"
					+ " h.area as area,h.dhao as dhao,h.fhao as fhao,h.ztai as ztai, h.quyu as quyu,h.djia as djia,h.zjia as zjia,h.mji as mji,"
					+ " h.lceng as lceng, h.zceng as zceng from House h , Favorite f where h.sh=1 and f.houseId=h.id and f.userId=?");
			params.add(query.userid);
		}else{
			hql.append("select h.id as id ,"
					+ " h.area as area,h.dhao as dhao,h.fhao as fhao,h.ztai as ztai, h.quyu as quyu,h.djia as djia,h.zjia as zjia,h.mji as mji,"
					+ " h.lceng as lceng, h.zceng as zceng from House h where h.sh=1 ");
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
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.returnText = JSONHelper.toJSONArray(page.getResult()).toString();
		return mv;
	}
}
