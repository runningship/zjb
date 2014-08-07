package com.youwei.zjb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.GException;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.sys.entity.PC;

public class SecurityHelper {

	public static void main(String[] args){
		Md5("123456");
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
		List<PC> list = SimpDaoTool.getGlobalCommonDaoService().listByParams(PC.class, new String[]{"deptId"}, new Object[]{target.deptId});
		if(list==null){
			return false;
		}
		for(PC pc : list){
			if(target.mac.equals(pc.mac) && target.board.equals(pc.board) && target.uuid.equals(pc.uuid)){
				if(pc.lock==1){
					return true;
				}
				throw new GException(PlatformExceptionType.BusinessException, "授权审核中...");
			}
		}
		return false;
	}
}
