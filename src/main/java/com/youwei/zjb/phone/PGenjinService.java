package com.youwei.zjb.phone;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.house.SellState;
import com.youwei.zjb.house.entity.GenJin;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;

@Module(name="/mobile/")
public class PGenjinService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod(name="house/genjin/add")
	public ModelAndView add(Integer userId , Integer houseId , Integer type , String content){
		//type是目标状态
		if(type==null || type==0){
			//客户端bug
			type=SellState.已售.getCode();
		}
		ModelAndView mv = new ModelAndView();
		House h=null;
		User u = null;
		if(userId==null || houseId==null){
			mv.data.put("result", "0");
			mv.data.put("msg", "跟进失败");
			return mv;
		}else{
			h = dao.get(House.class, houseId);
			u = dao.get(User.class, userId);
			if(h==null || u==null){
				mv.data.put("result", "0");
				mv.data.put("msg", "跟进失败");
				return mv;
			}
		}
		GenJin gj = new GenJin();
		gj.addtime = new Date();
		gj.hid = houseId;
		gj.uid = userId;
		gj.conts = content;
		gj.chuzu=0;
		gj.did = u.did;
		gj.cid = u.cid;
		//TODO 关键词检查
		gj.sh=1;
		gj.sh=1;
		//根据跟进内容片断
		for(String kw : DataHelper.getFilterWords()){
			if(gj.conts.contains(kw)){
				gj.sh = 0;
				break;
			}
		}
		gj.flag = type;
		gj.ztai = SellState.parse(String.valueOf(type)).toString();
		Date lockdate = h.dategjlock;
		if(lockdate==null){
			try {
				lockdate = DataHelper.sdf.parse("2000-01-01 00:00:00");
			} catch (ParseException e) {
			}
		}
		boolean delete=false;
		long count = dao.countHql("select count(distinct cid) from GenJin where hid=? and sh=1 and addtime>? and chuzu=0 and flag=? ", h.id, lockdate , type);
		if(String.valueOf(SellState.在售.getCodeString()).equals(h.ztai)){
			if(SellState.已售.getCode()==type || SellState.停售.getCode()==type){
				if(count>=3){
					gj.ztai=SellState.parse(h.ztai).toString()+"-"+SellState.parse(String.valueOf(type)).toString();
					h.ztai = String.valueOf(type);
					h.dategjlock = new Date();
					dao.saveOrUpdate(h);
				}else{
					gj.ztai=SellState.parse(h.ztai).toString()+"-"+SellState.parse(String.valueOf(type)).toString();
				}
			}else{
				gj.ztai=SellState.parse(h.ztai).toString()+"-"+SellState.parse(String.valueOf(type)).toString();
			}
		}else if(String.valueOf(SellState.已售.getCodeString()).equals(h.ztai)){
			if(count>=1){
				if(type==SellState.已售.getCode() || type==SellState.停售.getCode()){
					dao.delete(h);
					delete = true;
				}else{
					gj.ztai=SellState.parse(h.ztai).toString()+"-"+SellState.parse(String.valueOf(type)).toString();
					h.ztai = String.valueOf(type);
					h.dategjlock = new Date();
					dao.saveOrUpdate(h);
					
				}
			}
		}else if(String.valueOf(SellState.停售.getCodeString()).equals(h.ztai)){
			if(count>=1){
				if(type==SellState.停售.getCode() || type==SellState.已售.getCode()){
					dao.delete(h);
					delete = true;
				}else{
					gj.ztai=SellState.parse(h.ztai).toString()+"-"+SellState.parse(String.valueOf(type)).toString();
					h.ztai = String.valueOf(type);
					h.dategjlock = new Date();
					dao.saveOrUpdate(h);
				}
			}
		}
//		暂时不删除跟进信息
//		if(delete){
//			dao.execute("delete from GenJin where hid=?", houseId);
//		}else{
//			dao.saveOrUpdate(gj);
//		}
		dao.saveOrUpdate(gj);
		mv.data.put("result", "0");
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod(name="house/genjin/list")
	public ModelAndView list(Integer houseId){
		ModelAndView mv = new ModelAndView();
		if(houseId==null){
			mv.data.put("result", "0");
			mv.data.put("msg", "没有跟进信息");
			return mv;
		}
		List<Map> list = dao.listAsMap("select gj.id as id , u.uname as uname , "
				+ " gj.conts as conts, gj.addtime as dateadd from GenJin gj, User u where gj.hid=? "
				+ "and gj.sh=1 and u.id=gj.uid", houseId);
		mv.data.put("data",JSONHelper.toJSONArray(list));
		return mv;
	}
}
