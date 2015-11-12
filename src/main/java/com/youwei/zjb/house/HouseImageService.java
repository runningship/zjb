package com.youwei.zjb.house;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import cn.jpush.api.utils.StringUtils;

import com.youwei.zjb.house.entity.HouseImage;
import com.youwei.zjb.util.WXUtil;

@Module(name="/mobile/houseImage/")
public class HouseImageService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
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
	
	@WebMethod
	public ModelAndView setPrivateImage(HouseImage image){
		ModelAndView mv = new ModelAndView();
		HouseImage po = dao.getUniqueByParams(HouseImage.class, new String[]{"hid"}, new Object[]{image.hid , image.chuzu});
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
}
