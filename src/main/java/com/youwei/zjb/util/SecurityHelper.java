package com.youwei.zjb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.bc.sdak.GException;
import org.bc.sdak.utils.LogUtil;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.sys.entity.PC;
import com.youwei.zjb.user.entity.User;

public class SecurityHelper {

	public static void main(String[] args){
		System.out.println(Md5("123456"));
	}
	public static String Md5(String plainText) {
		try {
			if(plainText==null){
				return "";
			}
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static PC validate(PC target , User u){
		
		if(target.pcname!=null){
			target.pcname = target.pcname.replace("-", "").toLowerCase();
		}
		if(target.disk!=null){
			target.disk = target.disk.toLowerCase();
		}
		if(target.cpu!=null){
			target.cpu = target.cpu.toLowerCase();
		}
		String targetUUID = SecurityHelper.Md5(target.cpu+target.disk)+SecurityHelper.Md5(target.mac);
		PC pc= SimpDaoTool.getGlobalCommonDaoService().getUniqueByParams(PC.class, new String[]{"did" , "codeCP"}, new Object[]{target.did , targetUUID});
		if(pc==null){
			LogUtil.warning("机器未授权,lname="+u.lname+",cid="+u.cid+",did="+u.did+",cpu="+target.cpu+",disk="+target.disk+",mac="+target.mac+",pcname="+target.pcname+",uuid=+"+targetUUID);
			throw new GException(PlatformExceptionType.BusinessException, "机器未授权,请先授权...");
		}
		if(pc.lock==1){
			return pc;
		}
		throw new GException(PlatformExceptionType.BusinessException, "授权审核中...");
	}
	public static PC validateByUUID(PC target, User u) {
		target.licTime = new Date(target.ctime);
		PC pcpo = SimpDaoTool.getGlobalCommonDaoService().getUniqueByParams(PC.class, new String[]{"did","uuid" , "licTime"},	new Object[]{target.did , target.uuid , target.licTime});
		if(pcpo==null){
			LogUtil.warning("机器未授权,lname="+u.lname+",cid="+u.cid+",did="+u.did);
			throw new GException(PlatformExceptionType.BusinessException, "机器未授权,请先授权...");
		}
		if(pcpo.lock==1){
			return pcpo;
		}
		throw new GException(PlatformExceptionType.BusinessException, "授权审核中...");
	}
}
