package com.youwei.zjb.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.client.entity.Client;
import com.youwei.zjb.house.FangXing;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/client")
public class ClientService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	private static final int maxExpection=100000;
	@WebMethod
	public ModelAndView list(ClientQuery query ,Page<Client> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		User u = ThreadSession.getUser();
		StringBuilder hql = new StringBuilder("from Client where isdel=0 ");
		
		if(StringUtils.isNotEmpty(query.name)){
			query.name = query.name.replace(" ", "");
			hql.append(" and name like ? ");
			params.add("%"+query.name+"%");
		}
		
		if(StringUtils.isNotEmpty(query.tel)){
			query.tel = query.tel.replace(" ", "");
			hql.append(" and tels like ? ");
			params.add("%"+query.tel+"%");
		}
		
		if(query.id!=null){
			hql.append(" and id=?");
			params.add(query.id);
		}
		
		if(StringUtils.isNotEmpty(query.area)){
			query.area = query.area.replace(" ", "");
			hql.append(" and areas like ? ");
			params.add("%"+query.area+"%");
		}
		if(query.mjiStart==null){
			query.mjiStart=0f;
		}
		if(query.mjiEnd==null){
			query.mjiEnd=(float)maxExpection;
		}
		hql.append(" and ((mjiFrom>= ? and mjiFrom<=?) or (mjiTo>= ? and mjiTo<=?))");
		params.add(query.mjiStart);
		params.add(query.mjiEnd);
		params.add(query.mjiStart);
		params.add(query.mjiEnd);
		
		if(query.lcengStart==null){
			query.lcengStart=0;
		}
		if(query.lcengEnd==null){
			query.lcengEnd=maxExpection;
		}
		hql.append(" and ((lcengFrom>= ? and lcengFrom<=?) or (lcengTo>= ? and lcengTo<=?))");
		params.add(query.lcengStart);
		params.add(query.lcengEnd);
		params.add(query.lcengStart);
		params.add(query.lcengEnd);
		
		if(query.djiaStart!=null){
			hql.append(" and djia>= ? ");
			params.add(query.djiaStart);
		}
		if(query.djiaEnd!=null){
			hql.append(" and djia<= ? ");
			params.add(query.djiaEnd);
		}
		//总价或租金
		if(query.zjiaStart==null){
			query.zjiaStart=0f;
		}
		if(query.zjiaEnd==null){
			query.zjiaEnd=(float)maxExpection;
		}
		hql.append(" and ((jiageFrom>= ? and jiageFrom<=?) or (jiageTo>= ? and jiageTo<=?))");
		params.add(query.zjiaStart);
		params.add(query.zjiaEnd);
		params.add(query.zjiaStart);
		params.add(query.zjiaEnd);
		
		if(query.yearFrom==null){
			query.yearFrom=0;
		}
		if(query.yearTo==null){
			query.yearTo=maxExpection;
		}
		hql.append(" and ((yearFrom>= ? and yearFrom<=?) or (yearTo>= ? and yearTo<=?))");
		params.add(query.yearFrom);
		params.add(query.yearTo);
		params.add(query.yearFrom);
		params.add(query.yearTo);
		
		hql.append(HqlHelper.buildDateSegment("addtime",query.dateStart,DateSeparator.After,params));
		hql.append(HqlHelper.buildDateSegment("addtime",query.dateEnd, DateSeparator.Before , params));
		
		if(query.quyus!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.quyus.size();i++){
				hql.append(" quyus like ? ");
				if(i<query.quyus.size()-1){
					hql.append(" or ");
				}
				params.add("%"+query.quyus.get(i)+"%");
			}
			hql.append(" )");
		}
		
		if(query.lxing!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.lxing.size();i++){
				hql.append(" lxings like ? ");
				if(i<query.lxing.size()-1){
					hql.append(" or ");
				}
				params.add("%"+query.lxing.get(i)+"%");
			}
			hql.append(" )");
		}
		if(query.fxing!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.fxing.size();i++){
				hql.append(" hxings like ? ");
				if(i<query.fxing.size()-1){
					hql.append(" or ");
				}
				params.add("%"+query.fxing.get(i)+"%");
			}
			hql.append(" )");
		}
		if(query.zxiu!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.zxiu.size();i++){
				hql.append(" zxius like ? ");
				if(i<query.zxiu.size()-1){
					hql.append(" or ");
				}
				params.add("%"+query.zxiu.get(i)+"%");
			}
			hql.append(" )");
		}
		if(query.djrDid!=null){
			hql.append(" and djrDid=?");
			params.add(query.djrDid);
		}
		if(query.djrId!=null){
			hql.append(" and djrId=?");
			params.add(query.djrId);
		}
		page.orderBy = "addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page , DataHelper.dateSdf.toPattern()));
		return mv;
	}
	
	/**
	 * 客配房
	 * @param cid 客源id
	 */
	@WebMethod
	public ModelAndView matchHouse(Integer cid){
		ModelAndView mv = new ModelAndView();
		Client client = dao.get(Client.class, cid);
		List<Object> params = new ArrayList<Object>();
		User u = ThreadSession.getUser();
		StringBuilder hql = new StringBuilder("from House h where 1=1 ");
		if(client.areas!=null){
				hql.append(" and ( ");
				String[] arr = client.areas.split(",");
				for(int i=0;i<arr.length;i++){
					hql.append(" h.area = ? ");
					if(i<arr.length-1){
						hql.append(" or ");
					}
					params.add(arr[i]);
				}
				hql.append(" )");
		}
		if(client.zxius!=null){
			hql.append(" and ( ");
			String[] arr = client.zxius.split(",");
			for(int i=0;i<arr.length;i++){
				hql.append(" h.zxiu = ? ");
				if(i<arr.length-1){
					hql.append(" or ");
				}
				params.add(arr[i]);
			}
			hql.append(" )");
		}
		if(client.quyus!=null){
			hql.append(" and ( ");
			String[] arr = client.quyus.split(",");
			for(int i=0;i<arr.length;i++){
				hql.append(" h.quyu = ? ");
				if(i<arr.length-1){
					hql.append(" or ");
				}
				params.add(arr[i]);
			}
			hql.append(" )");
		}
		if(client.lxings!=null){
			hql.append(" and ( ");
			String[] arr = client.lxings.split(",");
			for(int i=0;i<arr.length;i++){
				hql.append(" h.lxing = ? ");
				if(i<arr.length-1){
					hql.append(" or ");
				}
				params.add(arr[i]);
			}
			hql.append(" )");
		}
		if(client.hxings!=null){
			hql.append(" and ( ");
			String[] arr = client.hxings.split(",");
			for(int i=0;i<arr.length;i++){
				FangXing fx = FangXing.parse(arr[i]);
				hql.append("( h.hxf=? and h.hxt=? and h.hxw=?)");
				if(i<arr.length-1){
					hql.append(" or ");
				}
				params.add(fx.getHxf());
				params.add(fx.getHxt());
				params.add(fx.getHxw());
			}
			hql.append(" )");
		}
		if(client.mjiFrom!=null){
			hql.append(" and h.mji>=? ");
			params.add(client.mjiFrom);
		}
		if(client.mjiTo!=null){
			hql.append(" and h.mji<=? ");
			params.add(client.mjiTo);
		}
		if(client.jiageFrom!=null){
			hql.append(" and h.zjia>=? ");
			params.add(client.jiageFrom);
		}
		if(client.jiageTo!=null){
			hql.append(" and h.zjia<=? ");
			params.add(client.jiageTo);
		}
		if(client.lcengFrom!=null){
			hql.append(" and h.lceng>=? ");
			params.add(client.lcengFrom);
		}
		if(client.lcengTo!=null){
			hql.append(" and h.lceng<=? ");
			params.add(client.lcengTo);
		}
		if(client.yearFrom!=null){
			hql.append(" and h.dateyear>=? ");
			params.add(client.yearFrom);
		}
		if(client.yearTo!=null){
			hql.append(" and h.dateyear<=? ");
			params.add(client.yearTo);
		}
		return mv;
	}
	@WebMethod
	public ModelAndView add(Client client){
		ModelAndView mv = new ModelAndView();
		User ywy = dao.get(User.class, client.uid);
		client.uid = ywy.id;
		client.uname = ywy.uname;
		client.did = ywy.Department().id;
		client.dname = ywy.Department().namea;
		client.cid = ywy.Company().id;
		client.cname = ywy.Company().namea;
		User u = ThreadSession.getUser();
		client.djrId = u.id;
		client.djrDid = u.did;
		client.djrName = u.uname;
		client.addtime = new Date();
		if(client.jiageFrom==null){
			client.jiageFrom=0f;
		}
		if(client.jiageTo==null){
			client.jiageTo=(float)maxExpection;
		}
		if(client.lcengFrom==null){
			client.lcengFrom=0;
		}
		if(client.lcengTo==null){
			client.lcengTo=maxExpection;
		}
		if(client.mjiFrom==null){
			client.mjiFrom=0f;
		}
		if(client.mjiTo==null){
			client.mjiTo=(float)maxExpection;
		}
		if(client.yearFrom==null){
			client.yearFrom=0;
		}
		if(client.yearTo==null){
			client.yearTo=maxExpection;
		}
		if(client.zujinFrom==null){
			client.zujinFrom=0;
		}
		if(client.zujinTo==null){
			client.zujinTo=maxExpection;
		}
		dao.saveOrUpdate(client);
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(Integer id){
		Client po = dao.get(Client.class, id);
		if(po!=null){
			po.isdel=1;
			dao.saveOrUpdate(po);
		}
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		Client po = dao.get(Client.class, id);
		mv.data.put("client", JSONHelper.toJSON(po));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Client client){
		ModelAndView mv = new ModelAndView();
		Client po = dao.get(Client.class, client.id);
		po.name = client.name;
		po.beizhu = client.beizhu;
		po.areas = client.areas;
		po.hxings = client.hxings;
		po.lxings = client.lxings;
		po.lcengFrom = client.lcengFrom;
		po.lcengTo = client.lcengTo;
		po.luduans = client.luduans;
		po.mjiFrom = client.mjiFrom;
		po.mjiTo = client.mjiTo;
		po.quyus = client.quyus;
		po.source = client.source;
		po.tels = client.tels;
		po.zxius = client.zxius;
		po.djia = client.djia;
		if(client.chuzu==1){
			po.zujinFrom = client.zujinFrom;
			po.zujinTo = client.zujinTo;
		}else{
			po.jiageFrom = client.jiageFrom;
			po.jiageTo = client.jiageTo;
		}
		if(po.uid.equals(client.uid)){
			//修改业务员
			User ywy = dao.get(User.class, client.uid);
			po.uid = client.uid;
			po.uname = ywy.uname;
			po.did = ywy.Department().id;
			po.dname = ywy.Department().namea;
			po.cid = ywy.Company().id;
			po.cname = ywy.Company().namea;
		}
		dao.saveOrUpdate(client);
		return mv;
	}
}
