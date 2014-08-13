package com.youwei.zjb.phone;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.house.entity.Favorite;

@Module(name="/")
public class PFavService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod(name="phone/addFavorite.asp")
	public ModelAndView add(String houseId , Integer userId){
		ModelAndView mv = new ModelAndView();
		mv.encodeReturnText=true;
		JSONArray arr = new JSONArray();
		JSONObject jobj = new JSONObject();
		if(houseId==null || userId==null){
			jobj.put("result", "2");
			jobj.put("msg", "收藏失败");
			arr.add(jobj);
			mv.returnText = arr.toString();
			return mv;
		}
		String[] hids = houseId.split(",");
		String result = "";
		for(String hid : hids){
			result = triggerFav(Integer.valueOf(hid) , userId);
		}
		jobj.put("result", result);
		if("0".equals(result)){
			jobj.put("msg", "收藏成功");
		}else{
			jobj.put("msg", "已取消收藏");
		}
		arr.add(jobj);
		mv.returnText = arr.toString();
		return mv;
	}
	
	private String triggerFav(int hid , int uid){
		Favorite po = dao.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{uid ,hid});
		if(po==null){
			po = new Favorite();
			po.houseId = hid;
			po.userId = uid;
			dao.saveOrUpdate(po);
			return "0";
		}else{
			dao.delete(po);
			return "1";
		}
	}
}
