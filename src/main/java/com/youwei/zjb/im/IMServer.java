package com.youwei.zjb.im;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ThreadSession;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.youwei.zjb.ZjbService;
import com.youwei.zjb.cache.ConfigCache;
import com.youwei.zjb.im.entity.Contact;
import com.youwei.zjb.im.entity.GroupMessage;
import com.youwei.zjb.im.entity.UserGroupStatus;
import com.youwei.zjb.im.entity.Message;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.URLUtil;

public class IMServer extends WebSocketServer{

	private static IMServer instance =null;
//	static Map<Integer,WebSocket> pool = new HashMap<Integer,WebSocket>();
	static Map<String,Map<Integer,WebSocket>> conns = new HashMap<String,Map<Integer,WebSocket>>();
	static Map<String,WebSocket> mobiles = new HashMap<String,WebSocket>();
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
//	private static InetSocketAddress socket = new InetSocketAddress("localhost", 9099); 
	private AssistantService assistantService = new AssistantService();
	private IMServer() throws UnknownHostException {
//		super(new InetSocketAddress(Inet4Address.getByName("localhost"), 9099));
//		super(new InetSocketAddress("192.168.1.125", 9099));
		super(new InetSocketAddress(ConfigCache.get("domainName" , "www.zhongjiebao.com"), 9099));
//		super(socket);
	}

	public static void startUp() throws Throwable{
		if(instance!=null){
			instance.stop();
			instance.finalize();
		}
		instance = new IMServer();
		instance.start();
		LogUtil.info("IM server started on port 9099");
	}
	
	public static void forceStop() throws IOException, InterruptedException{
		if(instance!=null){
			instance.stop();
		}
	}
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		//希望在这个地方就实现用户信息的确认
		String path = handshake.getResourceDescriptor();
		path = path.replace("/?", "");
		System.out.println("web socket connector path is: "+path);
		try {
			Map<String, Object> map = URLUtil.parseQuery(path);
			if(map.containsKey("tel")){
				WebSocket another = mobiles.get(map.get("tel"));
				if(another!=null  && !map.get("deviceId").equals(another.getAttributes().get("deviceId"))){
					another.send("loginAnother");
				}
				conn.getAttributes().put("deviceId", map.get("deviceId"));
				mobiles.put(map.get("tel").toString(), conn);
				return;
			}
			Integer uid = Integer.valueOf(map.get("uid").toString());
			String city = (String)map.get("city");
			ThreadSession.setCityPY(city);
			User user = SimpDaoTool.getGlobalCommonDaoService().get(User.class, uid);
			conn.getAttributes().put("city", city);
			conn.getAttributes().put("uid", uid);
			conn.getAttributes().put("cid", user.cid);
			conn.getAttributes().put("uname", user.uname);
			if(!conns.containsKey(city)){
				Map<Integer,WebSocket> ap = new HashMap<Integer,WebSocket>();
				conns.put(city, ap);
			}
			Map<Integer,WebSocket>  ap = conns.get(city);
			ap.put(uid, conn);
			
			nofityStatus(city,conn, 1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(conn);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		String city = (String) conn.getAttributes().get("city");
		Integer uid = (Integer) conn.getAttributes().get("uid");
		Map<Integer, WebSocket> ap = conns.get(city);
		if(ap==null){
			return;
		}
		WebSocket removed = ap.remove(uid);
		if(removed!=null){
			conns.remove(removed);
			nofityStatus(city,conn, 0);
		}
		System.out.println(conn+" removed ");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		LogUtil.info(message);
		if("ping".equals(message)){
			return;
		}
		JSONObject data = JSONObject.fromObject(message);
		String city = (String) conn.getAttributes().get("city");
		ThreadSession.setCityPY(city);
		Integer senderId = Integer.valueOf(conn.getAttributes().get("uid").toString());
		if("msg".equals(data.getString("type"))){
			int recvId = data.getInt("contactId");
			//接收人自己的信息没有必要发送，以免混淆
			data.remove("contactId");
			data.remove("contactName");
			data.remove("avatar");
			sendMsg(conn,city,senderId,recvId,data , false);
		}else if("groupmsg".equals(data.getString("type"))){
			onReceiveGroupMsg(data,city,senderId);
		}
	}

	private void onReceiveGroupMsg(JSONObject data , String city , int senderId){

		Integer groupId = data.getInt("contactId");
		//get users of group
		List<Map> list = SimpDaoTool.getGlobalCommonDaoService().listAsMap("select id as uid from User where cid=? or did=?", groupId , groupId);
		Map<Integer, WebSocket> ap = conns.get(city);
		//save group message
		GroupMessage gMsg = new GroupMessage();
		gMsg.conts = data.getString("msg");
		gMsg.senderId = senderId;
		gMsg.groupId = groupId;
		gMsg.sendtime = new Date();
		dao.saveOrUpdate(gMsg);
		UserGroupStatus ugs = dao.getUniqueByParams(UserGroupStatus.class, new String[]{"groupId" , "receiverId"}, new Object[]{groupId , senderId});
		if(ugs==null){
			ugs = new UserGroupStatus();
			ugs.groupId = groupId;
			ugs.receiverId = senderId;
			ugs.lasttime = new Date();
		}else{
			ugs.lasttime = new Date();
		}
		dao.saveOrUpdate(ugs);
		for(Map map : list){
			Integer recvId = Integer.valueOf(String.valueOf(map.get("uid")));
			if(recvId.equals(senderId)){
				continue;
			}
			if(ap==null){
				continue;
			}
			WebSocket conn = ap.get(recvId);
			if(conn!=null){
				//send group message
				data.put("sendtime", DataHelper.sdf4.format(new Date()));
				data.put("senderId", senderId);
				conn.send(data.toString());
			}
		}
	
	}
	private void nofityStatus(String city,WebSocket from , int status) {
		Object fromCid = from.getAttributes().get("cid");
		Object fromUid = from.getAttributes().get("uid");
		Object fromUname = from.getAttributes().get("uname");
		//通知的我好友，目前是同一个公司下的所有
		Map<Integer, WebSocket> ap = conns.get(city);
		JSONObject jobj = new JSONObject();
		jobj.put("type", "user_status");
		jobj.put("status", status);
		jobj.put("contactId", fromUid);
		jobj.put("contactName", fromUname);
		for(WebSocket socket : ap.values()){
			Object cid = socket.getAttributes().get("cid");
			Object uid = socket.getAttributes().get("uid");
			if(uid.equals(fromUid)){
				continue;
			}
			if(fromCid.equals(cid)){
				//通知
				socket.send(jobj.toString());
			}
		}
	}

	private void sendMsg(WebSocket senderSocket,String city ,Integer senderId , Integer recvId , JSONObject data , boolean isGroup) {
		if(!isGroup){
			Message dbMsg = new Message();
			dbMsg.sendtime = new Date();
			dbMsg.conts = data.getString("msg");
			dbMsg.senderId = senderId;
			dbMsg.receiverId = recvId;
			dbMsg.hasRead=0;
			try{
				dao.saveOrUpdate(dbMsg);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		if(recvId==ZjbService.AssistantUid){
			String msg = assistantService.doAction(data.getString("msg"));
			JSONObject jobj = new JSONObject();
			jobj.put("senderId", ZjbService.AssistantUid);
			jobj.put("sendtime", DataHelper.sdf4.format(new Date()));
			jobj.put("type", "msg");
			jobj.put("msg", msg);
			jobj.put("senderAvatar", ZjbService.AssistantAvatar);
			jobj.put("senderName", ZjbService.AssistantName);
			senderSocket.send(jobj.toString());
			
			//保存小助手回复的消息
			Message dbMsg = new Message();
			dbMsg.sendtime = new Date();
			dbMsg.conts = msg;
			dbMsg.senderId = ZjbService.AssistantUid;
			dbMsg.receiverId = senderId;
			dbMsg.hasRead=0;
			instance.dao.saveOrUpdate(dbMsg);
			return;
		}
		data.put("sendtime", DataHelper.sdf4.format(new Date()));
		Map<Integer, WebSocket> ap = conns.get(city);
		WebSocket recv = ap.get(recvId);
		data.put("senderId", senderId);
		if(recv!=null){
			recv.send(data.toString());
		}
	}

	
	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}
	
	public static boolean isUserOnline(String city , int userId){
		if(conns.containsKey(city)){
			return conns.get(city).containsKey(userId);
		}
		return false;
	}
	
	public static void sendMsgToGroup(int groupId, String msg){
		//{"contactId":"1","type":"groupmsg","contactName":"中介宝","currentPageNo":"1","msg":"<p>srui<\/p>","senderAvatar":157,"senderName":"孟浩"}
		JSONObject data = new JSONObject();
		data.put("msg", msg);
		data.put("type", "groupmsg");
		data.put("contactId", groupId);
		data.put("senderAvatar", ZjbService.AssistantAvatar);
		data.put("senderName", ZjbService.AssistantName);
		instance.onReceiveGroupMsg(data, "hefei", ZjbService.AssistantUid);
	}

	public static void sendMsgToUser(int userId,String msg){
		System.out.println(msg);
		Map<Integer, WebSocket> ap = conns.get("hefei");
		if(ap==null){
			return;
		}
		WebSocket conn = ap.get(userId);
		if(conn==null){
			return;
		}
		JSONObject jobj = new JSONObject();
		jobj.put("senderId", ZjbService.AssistantUid);
		jobj.put("sendtime", DataHelper.sdf4.format(new Date()));
		jobj.put("type", "msg");
		jobj.put("msg", msg);
		jobj.put("senderAvatar", ZjbService.AssistantAvatar);
		jobj.put("senderName", ZjbService.AssistantName);
		conn.send(jobj.toString());
		
		Message dbMsg = new Message();
		dbMsg.sendtime = new Date();
		dbMsg.conts = msg;
		dbMsg.senderId = ZjbService.AssistantUid;
		dbMsg.receiverId = userId;
		dbMsg.hasRead=0;
		instance.dao.saveOrUpdate(dbMsg);
	}
}
