package com.youwei.zjb.house;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.house.entity.Favorite;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.user.entity.User;

//@Module(name="/house/fav/")
public class FavoriteService2 {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(Integer houseId){
		User user = ThreadSessionHelper.getUser();
		Favorite po = service.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{user.id,houseId});
		if(po==null){
			Favorite fav = new Favorite();
			fav.houseId = houseId;
			fav.userId = user.id;
			service.saveOrUpdate(fav);
		}
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView delete(Integer houseId){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSessionHelper.getUser();
		Favorite po = service.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{user.id,houseId});
		if(po!=null){
			service.delete(po);
		}
		mv.data.put("msg", "已取消关注");
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(HouseQuery query){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSessionHelper.getUser();
		StringBuilder hql = new StringBuilder("select h from House h,Favorite f where h.id=f.houseId and f.userId="+user.id);
		Page<House> page = new Page<House>();
		page = service.findPage(page, hql.toString());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
}
