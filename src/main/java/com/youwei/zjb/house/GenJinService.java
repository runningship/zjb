package com.youwei.zjb.house;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.house.entity.GenJin;
import com.youwei.zjb.house.entity.HouseRent;
import com.youwei.zjb.phone.PGenjinService;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/genjin")
public class GenJinService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(GenJin gj){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSession.getUser();
		if(gj.chuzu==null || gj.chuzu==0){
			PGenjinService ps = new PGenjinService();
			mv = ps.add(user.id, gj.hid, gj.flag, gj.conts);
			mv.encodeReturnText=false;
			return mv;
		}else{
			gj.uid = user.id;
			gj.did = user.did;
			gj.cid = user.cid;
			//默认审核通过
			gj.sh=1;
			gj.addtime = new Date();
			HouseRent hr = dao.get(HouseRent.class, gj.hid);
			gj.ztai = RentState.parse(hr.ztai)+"-"+RentState.parse(String.valueOf(gj.flag));
			dao.saveOrUpdate(gj);
			mv.data.put("msg", "保存成功");
			return mv;
		}
	}
	
	@WebMethod
	public ModelAndView delete(Integer id){
		ModelAndView mv = new ModelAndView();
		if(id!=null){
			GenJin po = dao.get(GenJin.class, id);
			if(po!=null){
				dao.delete(po);
			}
		}
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView toggleShenHe(Integer id){
		ModelAndView mv = new ModelAndView();
		if(id!=null){
			GenJin po = dao.get(GenJin.class, id);
			if(po!=null){
				if(po.sh==1){
					po.sh=0;
				}else{
					po.sh=1;
				}
				dao.saveOrUpdate(po);
				mv.data.put("sh", po.sh);
			}
		}
		
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(GenJinQuery query , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder(" select gj.ztai as ztai, gj.id as id,gj.hid as houseId,gj.conts as conts,gj.addtime as addtime,gj.sh as sh,gj.chuzu as chuzu , "
				+ "u.uname as uname,d.namea as dname , c.namea as cname from  GenJin gj  ,User u,Department c , Department d where gj.uid=u.id  and d.id=gj.did and d.fid=c.id");
		List<Object> params = new ArrayList<Object>();
		
		if(query.houseId!=null){
			hql.append(" and hid=? ");
			params.add(query.houseId);
		}
		
		if(query.sh!=null){
			hql.append(" and gj.sh=? ");
			params.add(query.sh.getCode());
		}
		
		if(query.chuzu!=null){
			hql.append(" and gj.chuzu=? ");
			params.add(query.chuzu);
		}
		
		if(query.cid!=null){
			hql.append(" and gj.cid=? ");
			params.add(query.cid);
		}
		
		if(query.did!=null){
			hql.append(" and gj.did=? ");
			params.add(query.did);
		}
		
		hql.append(HqlHelper.buildDateSegment("gj.addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("gj.addtime", query.addtimeEnd, DateSeparator.Before, params));
		
		hql.append(" order by gj.addtime desc ");
		page.pageSize=20;
		page = dao.findPage(page, hql.toString(), true,params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
}
