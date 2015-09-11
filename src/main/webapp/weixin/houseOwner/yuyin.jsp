<%@page import="com.youwei.zjb.house.HouseImageService"%>
<%@page import="java.util.Map"%>
<%@page import="com.youwei.zjb.util.WXUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
//wx955465193fd083ef
//https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx955465193fd083ef&secret=7896b261071d9cca3f6b151ed3949a95
//https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=mTerMUXDuoL7dKEUbWUE9iIC8OA_2XB5a-Xg3ESZjK3fAEf2-0fd8Nkz-yuKDnUJKdB-bEe2m1l02L5gxTQ9P6HE5_tGEsIg_rwRkAKaY0M&type=jsapi
//var timestamp = new Date().getTime();
// var nonceStr = "EPNSpJae8rgzOP1x";
String url="http://wx.zjb.tunnel.mobi/weixin/houseOwner/yuyin.jsp";
String token = WXUtil.getAccess_token(HouseImageService.appId, HouseImageService.appkey);
String ticket = WXUtil.getJsApiTicket(token);
Map sign = WXUtil.sign(ticket, url);
request.setAttribute("sign", sign);
request.setAttribute("appid", HouseImageService.appId);
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>微信JS-SDK Demo3</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, user-scalable=0">
  <link rel="stylesheet" href="css/wx.css">
  <script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
  <script type="text/javascript">
  var aa='';
  
  function start(){
	  wx.startRecord({
	      cancel: function () {
	        alert('用户拒绝授权录音');
	      }
	    });
  }
  
  function finish(){
	  wx.stopRecord({
	      success: function (res) {
	        wx.translateVoice({
	            localId: res.localId,
	            complete: function (res) {
	              if (res.hasOwnProperty('translateResult')) {
	                alert('识别结果：' + res.translateResult);
	              } else {
	                alert('无法识别');
	              }
	            }
	          });
	      },
	      fail: function (res) {
	        alert(JSON.stringify(res));
	      }
	    });
  }
  
  function chooseImage(){
	  var images = {
	    localId: [],
	    serverId: []
	  };
	  wx.chooseImage({
	      success: function (res) {
	        images.localId = res.localIds;
	        uploadToWX(images , 0);
	      },
	      fail:function(res){
	    	  alert(2);
	      }
	    });
  }
  
  function uploadToWX(images , i){
	  var length = images.localId.length;
	  wx.uploadImage({
          localId: images.localId[i],
          success: function (res) {
            i++;
            images.serverId.push(res.serverId);
            if (i < length) {
            	uploadToWX(images , i);
            }else{
          	  notifyZJB(images.serverId.join());
            }
          },
          fail: function (res) {
            alert(JSON.stringify(res));
          }
        });
  }
  
  function notifyZJB(wxMediaIds){
	  $.ajax({
        type: 'POST',
        url: '/c/weixin/houseOwner/uploadImg?wxMediaIds='+wxMediaIds,
        success: function(data){
        	var json = JSON.parse(data);
        	if(json.result=='0'){
        		alert('图片上传成功');	
        	}else{
        		alert('图片上传失败');
        	}
        }
	  });
  }
  </script>
  
</head>
<body>
<div class="wxapi_container">
    <button ontouchstart="start();" >开始</button>
    <button ontouchstart="finish();" >完成</button>
    
    
    <h3 id="menu-image">图像接口</h3>
      <span class="desc">拍照或从手机相册中选图接口</span>
      <button class="btn btn_primary" id="chooseImage" onclick="chooseImage();">chooseImage</button>
      <span class="desc">预览图片接口</span>
      <button class="btn btn_primary" id="previewImage">previewImage</button>
      <span class="desc">上传图片接口</span>
      <button class="btn btn_primary" id="uploadImage">uploadImage</button>
  </div>
</body>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
  /*
   * 注意：
   * 1. 所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。
   * 2. 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
   * 3. 常见问题及完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
   *
   * 开发中遇到问题详见文档“附录5-常见错误及解决办法”解决，如仍未能解决可通过以下渠道反馈：
   * 邮箱地址：weixin-open@qq.com
   * 邮件主题：【微信JS-SDK反馈】具体问题
   * 邮件内容说明：用简明的语言描述问题所在，并交代清楚遇到该问题的场景，可附上截屏图片，微信团队会尽快处理你的反馈。
   */
  wx.config({
      debug: false,
      appId: '${appid}',
      timestamp: ${sign.timestamp},
      nonceStr: '${sign.nonceStr}',
      signature: '${sign.signature}',
      jsApiList: [
        'checkJsApi',
        'onMenuShareTimeline',
        'onMenuShareAppMessage',
        'onMenuShareQQ',
        'onMenuShareWeibo',
        'onMenuShareQZone',
        'hideMenuItems',
        'showMenuItems',
        'hideAllNonBaseMenuItem',
        'showAllNonBaseMenuItem',
        'translateVoice',
        'startRecord',
        'stopRecord',
        'onVoiceRecordEnd',
        'playVoice',
        'onVoicePlayEnd',
        'pauseVoice',
        'stopVoice',
        'uploadVoice',
        'downloadVoice',
        'chooseImage',
        'previewImage',
        'uploadImage',
        'downloadImage',
        'getNetworkType',
        'openLocation',
        'getLocation',
        'hideOptionMenu',
        'showOptionMenu',
        'closeWindow',
        'scanQRCode',
        'chooseWXPay',
        'openProductSpecificView',
        'addCard',
        'chooseCard',
        'openCard'
      ]
  });
</script>
<script src="js/zepto.min.js"></script>
<!-- <script src="js/demo.js"> </script> -->
</html>
