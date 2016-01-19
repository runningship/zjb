package com.youwei.zjb.house;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.Transactional;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.HqlHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.DateSeparator;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.ThreadSession;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.house.entity.District;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.house.entity.HouseTel;
import com.youwei.zjb.sys.OperatorService;
import com.youwei.zjb.sys.OperatorType;
import com.youwei.zjb.user.UserHelper;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.LngAndLatUtil;

@Module(name="/house/")
public class HouseService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	OperatorService operService = TransactionalServiceHelper.getTransactionalService(OperatorService.class);
	
	@WebMethod
	public ModelAndView exist(String area, String dhao , String fhao , String seeGX){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder();
		hql.append("from House where area = ? and dhao = ? and fhao = ? and (isdel =0 or isdel is null) ");
		List<Object> params = new ArrayList<Object>();
		params.add(area);
		params.add(dhao);
		params.add(fhao);
		if(StringUtils.isEmpty(seeGX) || "0".equals(seeGX)){
			hql.append(" and cid= ? ");
			params.add(ThreadSessionHelper.getUser().cid);
		}else{
			User u = ThreadSessionHelper.getUser();
			if(u!=null){
				hql.append(" and (seeGX=1 or cid=?)");
				params.add(u.cid);
			}else{
				hql.append(" and seeGX=1");
			}
		}
		List<House> list = dao.listByParams(House.class, hql.toString(), params.toArray());
		if(list==null || list.isEmpty()){
			mv.data.put("exist", "0");
		}else{
			mv.data.put("exist", "1");
			mv.data.put("hid", list.get(0).id);
		}
		return mv;
	}
	@WebMethod
	public ModelAndView add(House house , String hxing){
		ModelAndView mv = new ModelAndView();
		validte(house);
		ModelAndView result = exist(house.area , house.dhao , house.fhao , house.seeGX==null ? "0": house.seeGX.toString());
		if("1".equals(result.data.getString("exist"))){
			throw new GException(PlatformExceptionType.BusinessException,"存在相同的房源"+result.data.get("hid"));
		}
		User user = ThreadSessionHelper.getUser();
		house.isdel = 0;
		house.dateadd = new Date();
		house.uid = user.id;
		house.cid = user.cid;
		house.did = user.did;
		house.sh = 0;
		FangXing fx = FangXing.parse(hxing);
		house.hxf = fx.getHxf();
		house.hxt = fx.getHxt();
		house.hxw = fx.getHxw();
		if(house.mji!=null && house.mji!=0){
			int jiage = (int) (house.zjia*10000/house.mji);
			house.djia = (float) jiage;
		}
		if(house.seeFH==null){
			house.seeFH=0;
		}
		if(house.seeHM==null){
			house.seeHM=0;
		}
		if(house.seeGX==null){
			house.seeGX=0;
		}
		String nbsp = String.valueOf((char)160);
		if(StringUtils.isNotEmpty(house.tel)){
			house.tel = house.tel.trim().replace(nbsp, "");
		}
		dao.saveOrUpdate(house);
		if(StringUtils.isNotEmpty(house.tel)){
			String[] arr = house.tel.split("/");
			for(String tel : arr){
				tel = tel.trim().replace(nbsp, "");
				HouseTel ht = new HouseTel();
				ht.hid = house.id;
				ht.tel = tel;
				dao.saveOrUpdate(ht);
			}
		}
		mv.data.put("msg", "发布成功");
		mv.data.put("result", 0);
		
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 添加了房源["+house.area+"],id="+house.id+",seeGX="+house.seeGX;
		operService.add(OperatorType.房源记录, operConts);
		
		try{
			addDistrictIfNotExist(house);
		}catch(Exception ex){
			LogUtil.log(Level.WARN,"add district of house failed,hid= "+house.id,ex);
		}
		return mv;
	}
	
	public void addDistrictIfNotExist(House house){
		User u = ThreadSessionHelper.getUser();
		
		//检查楼盘是否在楼盘字典中，如果没有，则添加
		String hql = "from District  where name = ? ";
		List<Object> params = new ArrayList<Object>();
		params.add(house.area);
		//只根据楼盘名称来判断，不再根据楼盘地址来判断
//		if(StringUtils.isNotEmpty(house.address)){
//			hql+=" and address=? ";
//			params.add(house.address);
//		}
		List<District> list = dao.listByParams(District.class, hql, params.toArray());
		if(list.isEmpty()){
//			if(u.cid!=1){
//				//只有中介宝用户才可以
//				setMessageToMetis("出现新的楼盘: "+house.id+","+house.area+","+house.quyu+","+house.address);
//				return;
//			}
			District d= new District();
			d.address = house.address;
			d.name = house.area;
			d.addtime = new Date();
			d.quyu = house.quyu;
			d.pinyin=DataHelper.toPinyin(d.name);
			d.pyShort=DataHelper.toPinyinShort(d.name);
			d.sh=0;
			try{
				Map<String,Double> map = LngAndLatUtil.getLngAndLat(d.name,ThreadSession.getCityPY());
				d.maplat = map.get("lat").floatValue();
				d.maplng = map.get("lng").floatValue();
			}catch(Exception ex){
				
			}
			dao.saveOrUpdate(d);
		}
	}
	
	public void setMessageToMetis(final String msg){
		
		Thread t = new Thread(){
			@Override
			public void run() {
				try{
					URL url = new URL("http://60.169.1.32:8888/chat");
//					URL url = new URL("http://localhost:8888/chat");
					HttpURLConnection http = (HttpURLConnection) url.openConnection();
					http.setRequestMethod("POST");
					http.setConnectTimeout(0);
					http.setInstanceFollowRedirects(true);
					http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
					http.setDefaultUseCaches(false);
					http.setDoOutput(true);
					
					PrintWriter out = new PrintWriter(http.getOutputStream());
					out.print("qq=253187898&msg="+msg);//传入参数
					out.close();
					http.connect();//连接
					http.getInputStream();//返回流
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		};
		t.start();
	}
	
	@WebMethod
	public ModelAndView update(House house , String hxing){
		validte(house);
		ModelAndView mv = new ModelAndView();
		House po = dao.get(House.class, house.id);
		po.area = house.area;
		po.address = house.address;
		po.ztai = house.ztai;
		po.dhao = house.dhao;
		po.fhao = house.fhao;
		po.quyu= house.quyu;
		po.lceng = house.lceng;
		po.zceng = house.zceng;
		po.lxing = house.lxing;
		po.mji = house.mji;
		po.zjia =house.zjia;
		FangXing fx = FangXing.parse(hxing);
		po.hxf = fx.getHxf();
		po.hxt = fx.getHxt();
		po.hxw = fx.getHxw();
		po.dateyear = house.dateyear;
		po.zxiu = house.zxiu;
		po.lxr = house.lxr;
		po.forlxr = house.forlxr;
		po.fortel = house.fortel;
		if(house.seeFH==null){
			house.seeFH=0;
		}
		if(house.seeGX==null){
			house.seeGX=0;
		}
		if(house.seeHM==null){
			house.seeHM=0;
		}
		po.beizhu = house.beizhu;
		po.seeFH = house.seeFH;
		po.seeGX	= house.seeGX;
		po.seeHM = house.seeHM;
		if(po.mji!=null && po.mji!=0){
			int jiage = (int) (po.zjia*10000/house.mji);
			po.djia = (float) jiage;
		}
		if(house.tel==null){
			dao.execute("delete from HouseTel where hid = ?", house.id);
			po.tel = house.tel;
		}else{
//			if(!house.tel.equals(po.tel)){
				//修改了电话号码
				dao.execute("delete from HouseTel where hid = ?", house.id);
				String[] arr = house.tel.split("/");
				for(String tel : arr){
					HouseTel ht = new HouseTel();
					ht.hid = house.id;
					ht.tel = tel;
					dao.saveOrUpdate(ht);
				}
				po.tel = house.tel;
//			}
		}
		dao.saveOrUpdate(po);
		User user = ThreadSessionHelper.getUser();
		String operConts = "["+user.Department().namea+"-"+user.uname+ "] 修改了房源["+house.id+"]";
		operService.add(OperatorType.房源记录, operConts);
		mv.data.put("msg", "修改成功");
		mv.data.put("house", JSONHelper.toJSON(po , DataHelper.dateSdf.toPattern()));
		SellState state = SellState.parse(po.ztai);
		if(state!=null){
			mv.data.getJSONObject("house").put("ztai", state.toString());
		}
		try{
			addDistrictIfNotExist(house);
		}catch(Exception ex){
			LogUtil.log(Level.WARN,"add district of house failed,hid= "+house.id,ex);
		}
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView toggleShenHe(Integer id){
		ModelAndView mv = new ModelAndView();
		User user = ThreadSessionHelper.getUser();
		if(id!=null){
			House po = dao.get(House.class, id);
			String state="";
			if(po!=null){
				if(po.sh==1){
					po.sh=0;
					state="已审-->未审";
				}else{
					po.sh=1;
					state="未审-->已审";
				}
				dao.saveOrUpdate(po);
				mv.data.put("sh", po.sh);
			}
			String operConts = "["+user.Department().namea+"-"+user.uname+ "] 审核了房源["+id+"],状态从"+state;
			operService.add(OperatorType.房源记录, operConts);
		}
		return mv;
	}
	
	@WebMethod
	@Transactional
	public ModelAndView physicalDeleteBatch(String ids){
		List<Integer> params = new ArrayList<Integer>();
		ModelAndView mv = new ModelAndView();
		mv.data.put("result", 0);
		if(ids.isEmpty()){
			return mv;
		}
		StringBuilder gjHql = new StringBuilder("delete from GenJin where hid in (-1");
		String uname = ThreadSessionHelper.getUser().lname;
		for(String id : ids.split(";")){
			if(StringUtils.isEmpty(id)){
				continue;
			}
			House po = dao.get(House.class, Integer.valueOf(id));
			po.isdel =1 ;
			po.beizhu+=";删除人:"+uname+";删除时间:"+DataHelper.sdf.format(new Date());
			dao.saveOrUpdate(po);
			gjHql.append(",").append("?");
			params.add(Integer.valueOf(id.toString()));
		}
		gjHql.append(")");
		dao.execute(gjHql.toString(), params.toArray());
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(Integer id){
		ModelAndView mv = new ModelAndView();
		House po = dao.get(House.class, id);
		FangXing fxing = FangXing.parse(po.hxf, po.hxt,po.hxw);
		mv.data = JSONHelper.toJSON(po);
		if(fxing!=null){
			mv.data.put("hxing", fxing.getName());
		}else{
			LogUtil.warning("房源的户型信息错误,hid="+id);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView physicalDelete(Integer id){
		ModelAndView mv = new ModelAndView();
		//是否需要权限
		if(id!=null){
			House po = dao.get(House.class, id);
			if(po!=null){
				po.isdel=1;
				po.beizhu = po.beizhu+"[删除人:"+ThreadSessionHelper.getUser().lname+";删除时间:"+ DataHelper.sdf.format(new Date())+"]";
				dao.saveOrUpdate(po);
//				dao.delete(po);
				dao.execute("delete from GenJin where hid=?", po.id);
			}
		}
		mv.data.put("msg", "删除成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView listMyFav(HouseQuery query ,Page<House> page){
		User user = ThreadSessionHelper.getUser();
		String favStr = "@"+user.id+"|";
		query.favStr = favStr;
		return listAll(query ,page);
	}
	
	@WebMethod
	public ModelAndView listMyAdd(HouseQuery query ,Page<House> page){
		User user = ThreadSessionHelper.getUser();
		query.userid = user.id;
		query.listMyAdd = true;
		return listAll(query ,page);
	}
	
	@WebMethod
	public ModelAndView listRecycle(HouseQuery query ,Page<House> page){
		return listAll(query , page);
	}
	
	@WebMethod
	public ModelAndView listAll(HouseQuery query ,Page<House> page){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql =null;
		if(query.latEnd!=null || query.latStart!=null || query.lngStart!=null || query.lngEnd!=null){
			hql = new StringBuilder(" select h  from House  h  , District d where h.area=d.name ");
		}else{
			hql = new StringBuilder(" select h  from House  h  where 1=1 ");
		}
//		if(StringUtils.isNotEmpty(query.xpath)){
//			hql = new StringBuilder(" select h from  House h  ,User u where h.uid=u.id and u.id is not null and u.orgpath like ? ");
//			params.add(query.xpath+"%");
//		}else{
//			hql = new StringBuilder(" select h  from House  h where 1=1");
//		}
//		if(StringUtils.isNotEmpty(query.tel)){
//			hql = new StringBuilder(" select h  from House  h , (select hid from HouseTel where tel=? group by hid,tel) ht where h.id=ht.hid ");
//			query.tel = query.tel.trim();
//			query.tel = query.tel.replace(String.valueOf((char)160), "");
//			hql = new StringBuilder(" select h  from House  h  where h.tel like ? ");
//			params.add("%"+query.tel+"%");
//			if(query.useLike){
//				hql = new StringBuilder(" select h  from House  h  where h.tel like ? ");
//				params.add("%"+query.tel+"%");
//			}else{
//				hql = new StringBuilder(" select h  from House  h , HouseTel  ht where h.id=ht.hid and ht.tel=? ");
//				params.add(query.tel);
//			}
			
//		}else{
//			hql = new StringBuilder(" select h  from House  h where 1=1");
//		}
		
		User u = ThreadSessionHelper.getUser();
		if("all".equals(query.scope)){
			//TODO 直接h.seeGX=1就可以了吧
			hql.append(" and (h.cid=? or h.seeGX=?) ");
			params.add(u.cid);
			params.add(1);
		}else if("seeGX".equals(query.scope)){
			hql.append(" and h.seeGX=1 ");
		}else if("comp".equals(query.scope)){
			hql.append(" and h.cid=? ");
			params.add(u.cid);
		}else if("fav".equals(query.scope)){
			String favStr = "@"+u.id+"|";
			query.favStr = favStr;
		}
		if(StringUtils.isNotEmpty(query.ztai)){
			hql.append(" and h.ztai like ? ");
			params.add(query.ztai);
		}
		
		if(StringUtils.isNotEmpty(query.search)){
			query.search = query.search.replace(" ", "");
			if(StringUtils.isNotEmpty(query.search)){
				hql.append(" and (h.area like ? or h.address like ? or h.tel like ?");
				params.add("%"+query.search+"%");
				params.add("%"+query.search+"%");
				params.add("%"+query.search+"%");
				try{
					int id = Integer.valueOf(query.search);
					hql.append(" or h.id=? ");
					params.add(id);
				}catch(Exception ex){
					
				}
				hql.append(")");
			}
			
//			try{
//				int id = Integer.valueOf(query.search);
//				hql.append(" or h.id=? ");
//				params.add(id);
//			}catch(Exception ex){
//				
//			}
		}
		if(query.id!=null){
			hql.append(" and h.id = ? ");
			params.add(query.id);
		}
		if(StringUtils.isNotEmpty(query.dhao)){
			hql.append(" and h.dhao = ? ");
			params.add(query.dhao);
		}
		if(StringUtils.isNotEmpty(query.tel)){
			query.tel = query.tel.replace(" ", "");
			query.tel = query.tel.trim();
			query.tel = query.tel.replace(String.valueOf((char)160), "");
			hql.append(" and h.tel like ? ");
			params.add("%"+query.tel+"%");
		}
		if(StringUtils.isNotEmpty(query.area)){
			query.area = query.area.replace(" ", "");
			hql.append(" and h.area like ? ");
			params.add("%"+query.area+"%");
		}
		if(StringUtils.isNotEmpty(query.address)){
			query.address = query.address.replace(" ", "");
			hql.append(" and h.address like ? ");
			params.add("%"+query.address+"%");
		}
		if(StringUtils.isNotEmpty(query.fhao)){
			hql.append(" and h.fhao like ? ");
			params.add(query.fhao+"%");
		}
		if(StringUtils.isNotEmpty(query.favStr)){
			hql.append(" and h.fav like ? ");
			params.add("%"+query.favStr+"%");
		}
//		if(query.id!=null){
//			hql.append(" and h.id = ?");
//			params.add(query.id);
//		}
		
		if(query.quyus!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.quyus.size();i++){
				hql.append(" h.quyu like ? ");
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
				hql.append(" h.lxing = ? ");
				if(i<query.lxing.size()-1){
					hql.append(" or ");
				}
				params.add(query.lxing.get(i));
			}
			hql.append(" )");
		}
		if(query.zxiu!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.zxiu.size();i++){
				hql.append(" h.zxiu = ? ");
				if(i<query.zxiu.size()-1){
					hql.append(" or ");
				}
				params.add(query.zxiu.get(i));
			}
			hql.append(" )");
		}
		
		if(query.fxing!=null){
			hql.append(" and ( ");
			for(int i=0;i<query.fxing.size();i++){
				String fxing = query.fxing.get(i);
				FangXing fx = FangXing.valueOf(fxing);
				hql.append("( h.hxf=? and h.hxt=? and h.hxw=?)");
				if(i<query.fxing.size()-1){
					hql.append(" or ");
				}
				params.add(fx.getHxf());
				params.add(fx.getHxt());
				params.add(fx.getHxw());
			}
			hql.append(" )");
		}

		if(StringUtils.isNotEmpty(query.leibie)){
			hql.append(" and h.leibie = ? ");
			params.add(query.leibie);
		}
		if(query.zjiaStart!=null){
			hql.append(" and h.zjia>= ? ");
			params.add(query.zjiaStart);
		}
		if(query.zjiaEnd!=null){
			hql.append(" and h.zjia<= ? ");
			params.add(query.zjiaEnd);
		}
		try{
		if(query.dateyearStart!=null){
			hql.append(" and h.dateyear>= ? ");
			params.add(query.dateyearStart);
		}
		if(query.dateyearEnd!=null){
			hql.append(" and h.dateyear<= ? ");
			params.add(query.dateyearEnd);
		}
		}catch(Exception ex){
			throw new GException(PlatformExceptionType.BusinessException,"年代必须是数字");
		}
		hql.append(HqlHelper.buildDateSegment("h.dateadd",query.dateStart,DateSeparator.After,params));
		hql.append(HqlHelper.buildDateSegment("h.dateadd",query.dateEnd, DateSeparator.Before , params));
		
		if(query.mjiStart!=null){
			hql.append(" and h.mji>= ? ");
			params.add(query.mjiStart);
		}
		if(query.mjiEnd!=null){
			hql.append(" and h.mji<= ? ");
			params.add(query.mjiEnd);
		}
		if(query.lcengStart!=null){
			hql.append(" and h.lceng>= ? ");
			params.add(query.lcengStart);
		}
		if(query.lcengEnd!=null){
			hql.append(" and h.lceng<= ? ");
			params.add(query.lcengEnd);
		}
		if(query.djiaStart!=null){
			hql.append(" and h.djia>= ? ");
			params.add(query.djiaStart);
		}
		if(query.djiaEnd!=null){
			hql.append(" and h.djia<= ? ");
			params.add(query.djiaEnd);
		}
		
		if(query.userid!=null){
			hql.append(" and h.uid= ? ");
			params.add(query.userid);
		}
		
		if(query.sh!=null){
			hql.append(" and h.sh= ? ");
			params.add(query.sh);
		}else{
			if(ThreadSessionHelper.getUser().cid!=1 && query.listMyAdd==false){
				//非中介宝用户
				//sh=0且seeGX=1的由中介宝审核，此时客户公司将看不到这条数据如果中介宝还没有审核
				hql.append(" and ((h.sh=1 and h.seeGX=1) or h.cid=?)");
				params.add(ThreadSessionHelper.getUser().cid);
			}
		}
		if(query.latStart!=null){
			hql.append(" and d.maplat>=? ");
			params.add(query.latStart);
		}
		if(query.latEnd!=null){
			hql.append(" and d.maplat<=? ");
			params.add(query.latEnd);
		}
		if(query.lngStart!=null){
			hql.append(" and d.maplng>=? ");
			params.add(query.lngStart);
		}
		if(query.lngEnd!=null){
			hql.append(" and d.maplng<=? ");
			params.add(query.lngEnd);
		}
		hql.append(" and (isdel=0 or isdel is null) order by ");
		if(StringUtils.isNotEmpty(page.orderBy)){
			hql.append("h.").append(page.orderBy);
		}
		if(StringUtils.isNotEmpty(page.order)){
			hql.append(" ").append(page.order);
		}
		
		if(StringUtils.isEmpty(page.orderBy)){
			hql.append(" h.dateadd desc");
		}else if(!"dateadd".equals(page.orderBy)){
			hql.append(",").append(" h.dateadd desc");
		}
		
		page.orderBy = "";
		page.order = "";
		//page.setPageSize(25);
		LogUtil.info("house query hql : "+ hql.toString());
		page = dao.findPage(page, hql.toString(),params.toArray());
		ModelAndView mv = new ModelAndView();
//		if(page.getResult().size()==0 && query.useLike==false && StringUtils.isNotEmpty(query.tel)){
//			query.useLike = true;
//			mv = listAll(query, page);
//		}
		JSONObject jpage = JSONHelper.toJSON(page,DataHelper.sdf.toPattern());
		fixEnumValue(jpage);
		mv.data.put("page", jpage);
		return mv;
	}
	
	private void validte(House house){
		if(house.fhao==null){
			throw new GException(PlatformExceptionType.ParameterMissingError,"fhao","房号不能为空");
		}
		if(house.dhao==null){
			throw new GException(PlatformExceptionType.ParameterMissingError,"dhao","栋号不能为空");
		}
		if(house.mji==null){
			throw new GException(PlatformExceptionType.ParameterMissingError,"mji","面积不能为空");
		}
		if(house.zceng==null){
			throw new GException(PlatformExceptionType.ParameterMissingError,"zceng","总层不能为空");
		}
		
		if(house.zjia==null){
			throw new GException(PlatformExceptionType.ParameterMissingError,"zjia","总价不能为空");
		}
		if(house.seeGX==null || house.seeGX==0){
			if(UserHelper.getUserWithAuthority("fy_sh").isEmpty()){
				throw new GException(PlatformExceptionType.BusinessException,"由于贵公司没有设置房源审核权限，请选择发布至共享房源,由中介宝审核。");
			}
		}
	}
	
	@WebMethod
	public ModelAndView clearHouseWith2SameTel(){
		List<Map> list = dao.listAsMap("select hid as hid ,tel as tel from HouseTel group by hid,tel having COUNT(*)>1");
		int index=0;
		for(Map ht : list){
			Integer hid = (Integer)ht.get("hid");
			String tel = (String)ht.get("tel");
			List<HouseTel> result = dao.listByParams(HouseTel.class, "from HouseTel where hid=? and tel=?", hid , tel);
			for(int i=0;i<result.size()-1;i++){
				dao.delete(result.get(i));
			}
			index++;
			System.out.println(index);
		}
		return new ModelAndView();
	}
	
	
	@WebMethod
	public ModelAndView splitHouseTel(){
		List<HouseTel> list = dao.listByParams(HouseTel.class, "from HouseTel where tel like '%/%' ");
		int index=0;
		for(HouseTel ht : list){
			String[] arr = ht.tel.split("/");
			for(String tel : arr){
				HouseTel vo = new HouseTel();
				vo.hid = ht.hid;
				vo.tel = tel;
				dao.saveOrUpdate(vo);
			}
			dao.delete(ht);
			index++;
			System.out.println(index);
		}
		return new ModelAndView();
	}
	
	private void fixEnumValue(JSONObject jpage) {
		JSONArray results = jpage.getJSONArray("data");
		for(int i=0;i<results.size();i++){
			JSONObject obj = results.getJSONObject(i);
			String key = (String)obj.get("ztai");
			SellState state = SellState.parse(key);
			if(state!=null){
				obj.put("ztai", state.toString());
			}
		}
	}
	
	@WebMethod
	public ModelAndView houses(Integer id){
		ModelAndView mv = new ModelAndView();
		User u = ThreadSessionHelper.getUser();
//		Department comp = u.Company();
//		if(comp==null || comp.share!=1){
//			mv.jspData.put("share", "0");
//		}else{
//			mv.jspData.put("share", "1");
//		}
		mv.jspData.put("cid", u.cid);
		if(UserHelper.hasAuthority(u, "fy_sh")){
			mv.jspData.put("sh", "");
		}else{
			//没有审核权的用户只能看到审核通过对数据
			mv.jspData.put("sh", "1");
		}
		
		if(u.cid!=1){
			List<Role> role = SimpDaoTool.getGlobalCommonDaoService().listByParams(Role.class, "from Role where title='管理员' and cid<>1 and cid=?", u.cid);
			if(role!=null && role.size()>0){
				mv.jspData.put("seeAds", "0");
			}else{
				mv.jspData.put("seeAds", "1");
			}
		}else{
			mv.jspData.put("seeAds", "1");
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView houseSee(Integer id){
		ModelAndView mv = new ModelAndView();
		House po = dao.get(House.class, id);
		User user = dao.get(User.class, po.uid);
		mv.jspData.put("house", po);
		mv.jspData.put("fbr", user);
		Department dept = dao.get(Department.class, po.did);
		mv.jspData.put("dept", dept);
		SellState ztai = SellState.parse(po.ztai);
		mv.jspData.put("ztai", ztai==null?"":ztai.name());
		
		String favStr = "@"+ThreadSessionHelper.getUser().id+"|";
		if(po.fav!=null && po.fav.contains(favStr)){
			mv.jspData.put("fav", "1");
		}else{
			mv.jspData.put("fav", "0");
		}
		
		String hql = "select gj.conts as conts , d.namea as dname , u.uname as uname , gj.addtime as addtime from GenJin gj , User u , "
				+ " Department d where gj.hid=? and gj.uid=u.id and u.did=d.id and gj.chuzu=  ? and gj.sh=1 order by addtime desc";
		List<Map> gjList = dao.listAsMap(hql, Integer.valueOf(id) , 0);
		mv.jspData.put("gjList", gjList);
		return mv;
	}
	

	@WebMethod
	public ModelAndView trackStatistic(Page<Map> page , Integer id , Integer isMobile , String viewTimeStart , String viewTimeEnd){
		ModelAndView mv = new ModelAndView();
		StringBuilder innerSql = new StringBuilder(" select uid ,COUNT(*) as total  FROM ViewHouseLog where uid>0 "); 
		List<Object> params = new ArrayList<Object>();
		if(isMobile!=null){
			innerSql.append(" and isMobile=?");
			params.add(isMobile);
		}
		innerSql.append(HqlHelper.buildDateSegment("viewTime", viewTimeStart,DateSeparator.After,params));
		innerSql.append(HqlHelper.buildDateSegment("viewTime",viewTimeEnd,DateSeparator.Before,params));
		innerSql.append(" group by uid ");
		String sql = "select u.uname as uname ,u.lname as lname, tt.total as total ,c.namea as cname, u.id as uid, u.tel as tel ,u.mobileON as mobileON from ( "
						  +innerSql.toString() + "  ) tt , uc_user u ,uc_comp c where tt.uid = u.id and c.id = u.cid";
		page.orderBy = "total";
		page.order ="desc";
		page = dao.findPageBySql(page, sql , params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView viewLogList(Page<Map> page , Integer uid , Integer isMobile , String viewTimeStart , String viewTimeEnd){
		ModelAndView mv = new ModelAndView();
		StringBuilder sql = new StringBuilder(" select h.id as hid , h.area as area,h.quyu as quyu, h.dhao as dhao,h.fhao as fhao,h.hxt as hxt,h.hxf as hxf , h.hxw as hxw"
				+ " ,lg.viewTime as viewTime , lg.isMobile as isMobile from ViewHouseLog lg , House h where  lg.hid=h.id and lg.uid = ? "); 
		List<Object> params = new ArrayList<Object>();
		params.add(uid);
		if(isMobile!=null){
			sql.append(" and isMobile=?");
			params.add(isMobile);
		}
		sql.append(HqlHelper.buildDateSegment("lg.viewTime", viewTimeStart,DateSeparator.After,params));
		sql.append(HqlHelper.buildDateSegment("lg.viewTime",viewTimeEnd,DateSeparator.Before,params));
		page.orderBy = "lg.viewTime";
		page.order ="desc";
		page = dao.findPage(page, sql.toString() , true , params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
}
