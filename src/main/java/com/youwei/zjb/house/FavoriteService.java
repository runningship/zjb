package com.youwei.zjb.house;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.house.entity.HouseRent;
import com.youwei.zjb.user.UserHelper;
import com.youwei.zjb.user.entity.User;

@Module(name="/house/fav/")
public class FavoriteService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView addSell(Integer houseId){
		User user = ThreadSessionHelper.getUser();
		House h = dao.get(House.class, houseId);
		if(h==null){
			throw new GException(PlatformExceptionType.BusinessException, "房源已被删除或不存在!");
		}
		String favStr = "@"+user.id+"|";
		if(h.fav==null){
			h.fav= favStr;
		}else{
			if(!h.fav.contains(favStr)){
				h.fav+=favStr;
			}
		}
		dao.saveOrUpdate(h);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView addRent(Integer houseId){
		User user = ThreadSessionHelper.getUser();
		HouseRent h = dao.get(HouseRent.class, houseId);
		if(h==null){
			throw new GException(PlatformExceptionType.BusinessException, "房源已被删除或不存在!");
		}
		String favStr = "@"+user.id+"|";
		if(h.fav==null){
			h.fav= favStr;
		}else{
			if(!h.fav.contains(favStr)){
				h.fav+=favStr;
			}
		}
		dao.saveOrUpdate(h);
		return new ModelAndView();
	}
	@WebMethod
	public ModelAndView deleteSell(Integer houseId){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSessionHelper.getUser();
		Integer muid = UserHelper.getAnotherUser(user.id);
		String favStr = "@"+user.id+"|";
		String favStr2 = "@"+muid+"|";
		House h = dao.get(House.class, houseId);
		if(h.fav!=null){
			h.fav = h.fav.replace(favStr, "");
			h.fav = h.fav.replace(favStr2, "");
			dao.saveOrUpdate(h);
		}
		mv.data.put("msg", "已取消关注");
		return mv;
	}
	
	@WebMethod
	public ModelAndView deleteRent(Integer houseId){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSessionHelper.getUser();
		Integer muid = UserHelper.getAnotherUser(user.id);
		String favStr = "@"+user.id+"|";
		String favStr2 = "@"+muid+"|";
		HouseRent h = dao.get(HouseRent.class, houseId);
		if(h.fav!=null){
			h.fav = h.fav.replace(favStr, "");
			h.fav = h.fav.replace(favStr2, "");
			dao.saveOrUpdate(h);
		}
		mv.data.put("msg", "已取消关注");
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(HouseQuery query){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSessionHelper.getUser();
		String favStr = "@"+user.id+"|";
		StringBuilder hql = new StringBuilder("select h from House where fav like '%"+favStr+"%'");
		Page<House> page = new Page<House>();
		page = dao.findPage(page, hql.toString());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
}
