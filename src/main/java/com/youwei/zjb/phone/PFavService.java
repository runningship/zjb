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
	public ModelAndView add(Integer houseId , Integer userId){
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
		Favorite po = dao.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{userId ,houseId});
		if(po==null){
			po = new Favorite();
			po.houseId = houseId;
			po.userId = userId;
			dao.saveOrUpdate(po);
			jobj.put("result", "0");
			jobj.put("msg", "收藏成功");
		}else{
			dao.delete(po);
			jobj.put("result", "1");
			jobj.put("msg", "已取消收藏");
		}
		arr.add(jobj);
		mv.returnText = arr.toString();
		return mv;
	}
	
}
