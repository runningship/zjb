package com.youwei.zjb.phone;

import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.house.entity.HouseRent;
import com.youwei.zjb.user.UserHelper;

@Module(name="/mobile/fav/")
public class PFavService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView trigger(String houseId , Integer userId){
		ModelAndView mv = new ModelAndView();
		
		if(houseId==null || userId==null){
			mv.data.put("result", "2");
			mv.data.put("msg", "收藏失败");
			return mv;
		}
		String[] hids = houseId.split(",");
		int result = 0;
		for(String hid : hids){
			result = triggerFav(Integer.valueOf(hid) , userId);
		}
		mv.data.put("isfav", result);
		return mv;
	}
	
	@WebMethod
	public ModelAndView triggerRent(String houseId , Integer userId){
		ModelAndView mv = new ModelAndView();
		
		if(houseId==null || userId==null){
			mv.data.put("result", "2");
			mv.data.put("msg", "收藏失败");
			return mv;
		}
		String[] hids = houseId.split(",");
		int result = 0;
		for(String hid : hids){
			result = triggerRentFav(Integer.valueOf(hid) , userId);
		}
		mv.data.put("isfav", result);
		return mv;
	}
	
	@WebMethod
	public ModelAndView empty(Integer userId){
		ModelAndView mv = new ModelAndView();
		String favStr = "@"+userId+"|";
		List<House> list = dao.listByParams(House.class, "from House where fav like ? ", "%"+favStr+"%");
		if(!list.isEmpty()){
			for(House h : list){
				h.fav="";
				dao.saveOrUpdate(h);
			}
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView emptyRent(Integer userId){
		ModelAndView mv = new ModelAndView();
		String favStr = "@"+userId+"|";
		List<HouseRent> list = dao.listByParams(HouseRent.class, "from HouseRent where fav like ? ", "%"+favStr+"%");
		if(!list.isEmpty()){
			for(HouseRent h : list){
				h.fav="";
				dao.saveOrUpdate(h);
			}
		}
		Integer pcUid = UserHelper.getAnotherUser(userId);
		favStr = "@"+pcUid+"|";
		list = dao.listByParams(HouseRent.class, "from HouseRent where fav like ? ", "%"+favStr+"%");
		if(!list.isEmpty()){
			for(HouseRent h : list){
				h.fav="";
				dao.saveOrUpdate(h);
			}
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(Page<House> page , Integer userId){
		ModelAndView mv = new ModelAndView();
		String favStr = "@"+userId+"|";
		page = dao.findPage(page, "from House where fav like ? ", "%"+favStr+"%");
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView listRent(Page<HouseRent> page , Integer userId){
		ModelAndView mv = new ModelAndView();
		String favStr = "@"+userId+"|";
		page = dao.findPage(page, "from HouseRent where fav like ? ", "%"+favStr+"%");
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	private int triggerRentFav(int hid , int uid){
		HouseRent h = dao.get(HouseRent.class, hid);
		if(h==null){
			throw new GException(PlatformExceptionType.BusinessException, "房源已被删除或不存在!");
		}
		String favStr = "@"+uid+"|";
		int result = 0;
		if(h.fav==null){
			h.fav= favStr;
			result=1;
		}else{
			if(!h.fav.contains(favStr)){
				h.fav+=favStr;
				result=1;
			}else{
				h.fav = h.fav.replace(favStr, "");
				result=0;
			}
		}
		dao.saveOrUpdate(h);
		return result;
	}
	
	private int triggerFav(int hid , int uid){
		House h = dao.get(House.class, hid);
		if(h==null){
			throw new GException(PlatformExceptionType.BusinessException, "房源已被删除或不存在!");
		}
		String favStr = "@"+uid+"|";
		int result = 0;
		if(h.fav==null){
			h.fav= favStr;
			result=1;
		}else{
			if(!h.fav.contains(favStr)){
				h.fav+=favStr;
				result=1;
			}else{
				h.fav = h.fav.replace(favStr, "");
				result=0;
			}
		}
		dao.saveOrUpdate(h);
		return result;
//		Favorite po = dao.getUniqueByParams(Favorite.class, new String[]{"userId","houseId"}, new Object[]{uid ,hid});
//		if(po==null){
//			po = new Favorite();
//			po.houseId = hid;
//			po.userId = uid;
//			dao.saveOrUpdate(po);
//			return "0";
//		}else{
//			dao.delete(po);
//			return "1";
//		}
	}
}
