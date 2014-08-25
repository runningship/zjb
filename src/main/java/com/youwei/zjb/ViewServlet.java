package com.youwei.zjb;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.utils.LogUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.user.entity.User;


public class ViewServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setCharacterEncoding("utf8");
		String path = req.getPathInfo();
		String clazz = path.replace("/", ".");
		clazz = clazz.replace(".html", "");
		
		//com.youwei.zjb.view.client.client_list
//		req.getSession().setMaxInactiveInterval(20);
		resp.setContentType(getMimeType(path));
		if(!path.endsWith(".html")){
			return;
		}
		User user = ThreadSession.getUser();
		String filePath = req.getServletContext().getRealPath("/")+path;
		String html = FileUtils.readFileToString(new File(filePath),"utf-8");
		Document doc = Jsoup.parse(html);
		
		///处理继承
		Elements parents = doc.getElementsByTag("extend");
		if(!parents.isEmpty()){
			Element parent = parents.get(0);
			String phtmlFile = parent.attr("parent");
			phtmlFile = filePath+"/../"+phtmlFile;
			String pHtml = FileUtils.readFileToString(new File(phtmlFile),"utf-8");
			Document pDoc = Jsoup.parse(pHtml);
			Elements extendList = pDoc.getElementsByAttribute("extend_id");
			for(Element e : extendList){
				Elements children = doc.getElementsByAttributeValue("extend_id", e.attr("extend_id"));
				if(!children.isEmpty()){
					e.html(children.get(0).html());
				}
			}
			//处理完继承后的html内容
			html = pDoc.html();
			
		}
		///
		doc = Jsoup.parse(html);
		if(user!=null){
			html = html.replace("$${userId}", user.id.toString());
			html = html.replace("$${cid}", user.cid.toString());
			html = html.replace("$${myName}", String.valueOf(user.uname));
			html = html.replace("$${myTel}", user.tel==null? "": user.tel);
			
			String authParent = req.getParameter("authParent");
			Role role = user.getRole();
			List<RoleAuthority> authList = new ArrayList<RoleAuthority>();
			if(role!=null){
				authList = role.Authorities();
			}
			Elements list = doc.getElementsByAttribute("auth");
			for(Element e : list){
				String target = e.attr("auth");
				if(StringUtils.isEmpty(target)){
					continue;
				}
				if(authParent!=null){
					target = target.replace("$${authParent}", authParent);
				}
				boolean auth = false;
				for(RoleAuthority ra : authList){
					if(ra.name.equals(target)){
						auth = true;
						break;
					}
				}
				if(auth==false){
					e.remove();
				}
			}
		}else{
			LogUtil.log(Level.ERROR, "user session is null", null);
		}
		doc = Jsoup.parse(html);
		clazz = "com.youwei.zjb.view"+clazz;
		try {
			Class<?> pageClass = Class.forName(clazz);
			Method init = pageClass.getDeclaredMethod("initPage", Document.class ,HttpServletRequest.class);
			Object page = pageClass.newInstance();
			doc = (Document) init.invoke(page, doc , req);
		} catch (IllegalArgumentException | ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException ex) {
			LogUtil.warning("page init failed.");
		}catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		
		
		//process include
		Elements list = doc.getElementsByTag("include");
		for(Element e : list){
			String src = e.attr("src");
			try{
				File f = new File(filePath);
				String str = FileUtils.readFileToString(new File(f.getParentFile().getPath()+File.separator+src));
				e.html(str);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		resp.getWriter().write(doc.html());
	}
	
	private String getMimeType(String path){
		if(path.endsWith(".css")){
			return "text/css";
		}
		if(path.endsWith(".js")){
			return "text/javascript";
		}
		if(path.endsWith(".html")){
			return "text/html";
		}
		if(path.endsWith(".jpeg")){
			return "image/jpeg";
		}
		return "";
	}
}
