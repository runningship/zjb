package com.youwei.zjb.house;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.ThreadSession;
import org.bc.web.WebMethod;

import cn.jpush.api.utils.StringUtils;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.cache.ConfigCache;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.house.entity.HouseImage;
import com.youwei.zjb.house.entity.HouseImageGJ;
import com.youwei.zjb.user.entity.JifenRecord;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.ImageHelper;
import com.youwei.zjb.util.WXUtil;

@Module(name="/mobile/houseImage/")
public class HouseImageService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	static final int MAX_SIZE = 1024000*100;
	static final String BaseFileDir = ConfigCache.get("house_image_path", "");
	public static final String appId = "wx955465193fd083ef";
	public static final String appkey = "7896b261071d9cca3f6b151ed3949a95";
	
	@WebMethod
	public ModelAndView uploadImg(Integer hid , String wxMediaIds){
		//
		String token = WXUtil.getAccess_token(appId , appkey);
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isNotEmpty(wxMediaIds)){
			for(String mediaId : wxMediaIds.split(",")){
				if(StringUtils.isEmpty(mediaId)){
					continue;
				}
				String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="+token+"&media_id="+mediaId;
				try {
					FileOutputStream fos = new FileOutputStream("E:\\temp\\微信图片\\"+mediaId+".jpg");
					IOUtils.copy(new URL(url).openStream(), fos);
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
					mv.data.put("result", "1");
//					throw new GException(PlatformExceptionType.BusinessException,"上传图片失败");
					return mv;
				}
			}
		}
		mv.data.put("result", "0");
		return mv;
	}
	
	public ModelAndView setPrivateImage(HouseImage image){
		ModelAndView mv = new ModelAndView();
		HouseImage po = dao.getUniqueByParams(HouseImage.class, new String[]{"hid" , "chuzu"}, new Object[]{image.hid , image.chuzu});
		if(po!=null){
			po.path = image.path;
			dao.saveOrUpdate(po);
		}else{
			image.addtime = new Date();
			image.isPrivate = 1;
			dao.saveOrUpdate(image);
		}
		mv.data.put("result", "0");
		return mv;
	}
	
	@WebMethod
	public ModelAndView getPublicHouseImage(HouseImage image){
		ModelAndView mv = new ModelAndView();
//		List<Map> list = dao.listAsMap("select hi.id as id , hi.uid as uid , hi.hid as hid, hi.path as path, u.uname as uname ,u.tel as tel from HouseImage hi , User u , HouseImageGJ gj"
//				+ " where u.id=hi.uid and gj.hiid=hi.id and hid=? and chuzu=? and isPrivate=? ", image.hid , image.chuzu , 0);
		
		List<Map> list = dao.listSqlAsMap("select hi.id as id , hi.uid as uid , hi.hid as hid, hi.path as path, u.uname as uname ,u.tel as tel,hi.zanCount as zanCount , hi.shitCount as shitCount from HouseImage hi , uc_user u "
						+"where u.id=hi.uid and hid=? and chuzu=? and isPrivate=?", image.hid , image.chuzu , 0);
		
//		List<HouseImage> list = dao.listByParams(HouseImage.class, new String[]{"hid" , "chuzu" , "isPrivate"}, new Object[]{image.hid , image.chuzu , 0});
		mv.data.put("list", JSONHelper.toJSONArray(list));
		mv.data.put("result", "0");
		return mv;
	}
	
	@WebMethod
	public ModelAndView zanHouseImage(Integer hiid , int zan){
		ModelAndView mv = new ModelAndView();
		Integer uid = ThreadSessionHelper.getUser().id;
		HouseImageGJ po = dao.getUniqueByParams(HouseImageGJ.class, new String[]{"hiid" , "uid"}, new Object[]{hiid , uid});
		HouseImage image = dao.get(HouseImage.class, hiid);
		if(image==null){
			throw new GException(PlatformExceptionType.BusinessException,"" , "图片不存在");
		}
		if(po==null){
			HouseImageGJ gj = new HouseImageGJ();
			gj.hiid = hiid;
			gj.zan = zan;
			gj.uid = uid;
			gj.hid = image.hid;
			if(gj.shit==null){
				gj.shit = 0;
			}
			dao.saveOrUpdate(gj);
		}else{
			if(po.zan==null){
				po.zan=0;
			}
			if(po.zan==0 && zan==1){
				image.zanCount++;
				//图片上传人积分+1
				User u = dao.get(User.class, image.uid);
				if(u.jifen==null){
					u.jifen=1;
				}else{
					u.jifen+=1;
				}
				dao.saveOrUpdate(u);
				House house = dao.get(House.class, image.hid);
				JifenRecord record = new JifenRecord();
				record.addTime = new Date();
				record.uid = u.id;
				record.hiid = hiid;
				record.type = 1;
				record.offsetCount = 1;
				record.conts = "房源 "+house.area+" "+house.dhao+"#"+house.fhao+" 的图片获赞";
				record.beizhu="hid="+image.hid+",hiid="+hiid+",zanUid="+uid;
				dao.saveOrUpdate(record);
			}else if(po.zan==1 && zan==0){
				image.zanCount--;
			}
			po.zan = zan;
			dao.saveOrUpdate(po);
			dao.saveOrUpdate(image);
		}
		mv.data.put("result", "0");
		return mv;
	}
	
	@WebMethod
	public ModelAndView shitHouseImage(Integer hiid , Integer shit){
		ModelAndView mv = new ModelAndView();
		Integer uid = ThreadSessionHelper.getUser().id;
		HouseImageGJ po = dao.getUniqueByParams(HouseImageGJ.class, new String[]{"hiid" , "uid"}, new Object[]{hiid , uid});
		HouseImage image = dao.get(HouseImage.class, hiid);
		if(image==null){
			throw new GException(PlatformExceptionType.BusinessException,"" , "图片不存在");
		}
		if(po==null){
			HouseImageGJ gj = new HouseImageGJ();
			gj.hiid = hiid;
			gj.shit = shit;
			gj.uid = uid;
			gj.hid = image.hid;
			if(gj.zan==null){
				gj.zan = 0;
			}
			dao.saveOrUpdate(gj);
		}else{
			if(po.shit==null){
				po.shit=0;
			}
			if(po.shit==0 && shit==1){
				image.shitCount++;
			}else if(po.shit==1 && shit==0){
				image.shitCount--;
			}
			po.shit = shit;
			dao.saveOrUpdate(po);
			dao.saveOrUpdate(image);
		}
		mv.data.put("result", "0");
		return mv;
	}
	
	@WebMethod
	public ModelAndView delPublicHouseImage(HouseImage image){
		ModelAndView mv = new ModelAndView();
		if(image.id!=null){
			HouseImage po = dao.get(HouseImage.class, image.id);
			if(po!=null){
				dao.delete(po);
				FileUtils.deleteQuietly(new File(po.path));
				FileUtils.deleteQuietly(new File(po.path+".t.jpg"));
			}
		}
//		HouseImage po = dao.getUniqueByParams(HouseImage.class, new String[]{"hid","uid" , "chuzu" , "isPrivate"}, new Object[]{image.hid ,image.uid, image.chuzu , 0});
//		if(po!=null && po.path!=null){
//			po.path = po.path.replace(image.path+";", "");
//			dao.saveOrUpdate(po);
//			String thumbName = image.path+".t.jpg";
//			String savePath = BaseFileDir+File.separator +image.hid+File.separator +image.uid+File.separator+image.path;
//			String thumbPath = BaseFileDir+File.separator +image.hid+File.separator +image.uid+File.separator+thumbName;
//			FileUtils.deleteQuietly(new File(savePath));
//			FileUtils.deleteQuietly(new File(thumbPath));
//		}
		mv.data.put("result", "0");
		return mv;
	}
	
	@WebMethod
	public ModelAndView genjin(HouseImageGJ gj){
		ModelAndView mv = new ModelAndView();
		dao.saveOrUpdate(gj);
		mv.data.put("result", "0");
		return mv;
	}
	
	@WebMethod
	public ModelAndView setPublicHouseImage(HouseImage image){
		ModelAndView mv = new ModelAndView();
		HttpServletRequest request = ThreadSession.HttpServletRequest.get();
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try{
			List<FileItem> items = upload.parseRequest(request);
//			HouseImage po = dao.getUniqueByParams(HouseImage.class, new String[]{"hid","uid" , "chuzu"}, new Object[]{image.hid ,image.uid, image.chuzu});
//			if(po!=null){
//				image = po;
//			}
//			image.isPrivate = 0;
//			image.addtime = new Date();
//			image.sh = 0;
			String serverPathList = new String();
			for(FileItem item : items){
				if(item.isFormField()){
					continue;
				}
				if(item.getSize()<=0){
					throw new RuntimeException("至少先选择一张图片.");
				}else if(item.getSize()>=MAX_SIZE){
						throw new RuntimeException("单个图片不能超过2M");
				}else{
					image.isPrivate = 0;
					image.addtime = new Date();
					image.sh = 0;
					image.path = item.getName();
					image.zanCount = 0;
					image.shitCount = 0;
					String thumbName = item.getName();
					thumbName =  item.getName()+".t.jpg";
					String savePath = BaseFileDir+File.separator +image.hid+File.separator +image.uid+File.separator+item.getName();
					String thumbPath = BaseFileDir+File.separator +image.hid+File.separator +image.uid+File.separator+thumbName;
					FileUtils.copyInputStreamToFile(item.getInputStream(), new File(savePath));
					ImageHelper.resize(savePath, 270, 270, thumbPath);
					serverPathList+=item.getName()+";";
				}
			}
			dao.saveOrUpdate(image);
			mv.data.put("result", 0);
			mv.data.put("serverPathList", serverPathList);
			return mv;
		}catch(Exception ex){
			throw new GException(PlatformExceptionType.BusinessException,"文件图片失败" , ex);
		}
	}
}
