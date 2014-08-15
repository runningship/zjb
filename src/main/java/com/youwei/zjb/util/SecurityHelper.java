package com.youwei.zjb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.bc.sdak.GException;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.sys.entity.PC;

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
	
	public static boolean validate(PC target){
		List<PC> list = SimpDaoTool.getGlobalCommonDaoService().listByParams(PC.class, new String[]{"did"}, new Object[]{target.did});
		if(list==null){
			return false;
		}
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
		for(PC pc : list){
			if(targetUUID.equals(pc.uuid)){
				if(pc.lock==1){
					return true;
				}
				throw new GException(PlatformExceptionType.BusinessException, "授权审核中...");
			}
		}
		return false;
	}
}
