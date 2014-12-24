package com.youwei.zjb.oa;

import java.util.ArrayList;
import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.oa.entity.Site;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/oa/site")
public class SiteService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView index(){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		params.add(ThreadSession.getUser().id);
		List<Site> personalList = dao.listByParams(Site.class, "from Site where uid=?",ThreadSession.getUser().id);
		List<Site> shareList = dao.listByParams(Site.class, "from Site where uid is null");
		mv.jspData.put("personalList", JSONHelper.toJSONArray(personalList));
		mv.jspData.put("shareList", JSONHelper.toJSONArray(shareList));
		return mv;
	}
	
	@WebMethod
	public ModelAndView listMy(Site site){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		params.add(ThreadSession.getUser().id);
		List<Site> list = dao.listByParams(Site.class, "from Site where uid=?", params.toArray());
		mv.data.put("sites", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView listShare(Site site){
		ModelAndView mv = new ModelAndView();
		List<Site> list = dao.listByParams(Site.class, "from Site where uid is null");
		mv.data.put("sites", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView addMy(Site site){
		ModelAndView mv = new ModelAndView();
		Site po = dao.getUniqueByKeyValue(Site.class, "title", site.title);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException,"存在重复的网址，你修改后再保存");
		}
		site.uid = ThreadSession.getUser().id;
		dao.saveOrUpdate(site);
		return mv;
	}
	
	@WebMethod
	public ModelAndView addShare(Site site){
		ModelAndView mv = new ModelAndView();
		site.uid = null;
		dao.saveOrUpdate(site);
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Site site){
		ModelAndView mv = new ModelAndView();
		Site po = dao.get(Site.class,site.id);
		if(po!=null){
			po.title = site.title;
			po.label = site.label;
			po.url = site.url;
			dao.saveOrUpdate(po);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(Integer id){
		ModelAndView mv = new ModelAndView();
		Site po = dao.get(Site.class,id);
		if(po!=null){
			dao.delete(po);
		}
		return mv;
	}
}
