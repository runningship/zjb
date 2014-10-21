package com.youwei.zjb.phone;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.house.SellState;
import com.youwei.zjb.house.entity.GenJin;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/")
public class PGenjinService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod(name="house/genjin/add.asp")
	public ModelAndView add(Integer userId , Integer houseId , Integer type , String content){
		if(type==null || type==0){
			//客户端bug
			type=SellState.已售.getCode();
		}
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		ModelAndView mv = new ModelAndView();
		mv.encodeReturnText=true;
		House h=null;
		User u = null;
		if(userId==null || houseId==null){
			obj.put("result", "0");
			obj.put("msg", "跟进失败");
			arr.add(obj);
			mv.returnText = arr.toString();
			return mv;
		}else{
			h = dao.get(House.class, houseId);
			u = dao.get(User.class, userId);
			if(h==null || u==null){
				obj.put("result", "0");
				obj.put("msg", "跟进失败");
				arr.add(obj);
				mv.returnText = arr.toString();
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
				if(count>=4){
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
				if(type==SellState.已售.getCode()){
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
				if(type==SellState.停售.getCode()){
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
		obj.put("result", "0");
		obj.put("msg", "添加成功");
		arr.add(obj);
		mv.returnText = arr.toString();
		return mv;
	}
	
	@WebMethod(name="house/genjin/list.asp")
	public ModelAndView list(Integer houseId){
		ModelAndView mv = new ModelAndView();
		mv.encodeReturnText=true;
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		if(houseId==null){
			obj.put("result", "0");
			obj.put("msg", "没有跟进信息");
			arr.add(obj);
			mv.returnText = arr.toString();
			return mv;
		}
		List<Map> list = dao.listAsMap("select gj.id as id , c.namea as cname , d.namea as dname , u.uname as uname , "
				+ " gj.conts as conts, gj.addtime as dateadd from GenJin gj, Department d , Department c, User u where gj.hid=? "
				+ "and gj.sh=1 and gj.did=d.id and d.fid=c.id and u.id=gj.uid", houseId);
		mv.returnText = JSONHelper.toJSONArray(list).toString();
		return mv;
	}
}
