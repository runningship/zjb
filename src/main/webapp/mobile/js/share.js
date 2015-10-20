var shareURL= '';
function closexx(){
	api.closeWin({
	    name: 'share'
    });
}

function shareQQ(invitationCode){
	var obj = api.require('qq');
	obj.installed(function(ret,err){
	    if(ret.status){
			obj.shareNews({
			    url:shareURL,
			    title:'中介宝',
			    type:'QFriend',
			    description:'最佳中介房源管理软件，新手礼包等你来拿!',
			    imgUrl:'http://www.zhongjiebao.com/logo.png'
			});
	    }else{
	        api.alert({msg: "您没有安装QQ"});
	    } 
	});
}

function shareQZone(invitationCode){
	var obj = api.require('qq');
	obj.installed(function(ret,err){
	    if(ret.status){
			obj.shareNews({
			    url:shareURL,
			    title:'中介宝',
			    type: 'QZone',
			    description:'最佳中介房源管理软件，新手礼包等你来拿!',
			    imgUrl:'http://www.zhongjiebao.com/logo.png'
			});
	    }else{
	        api.alert({msg: "您没有安装QQ"});
	    } 
	});
}

function shareWeiXin(invitationCode){
var weiXin = api.require('weiXin');
	weiXin.registerApp(
	    function(ret,err){
	        if (ret.status) {
				weiXin.sendRequest({
				    scene:'timeline',
				    contentType:'web_page',
				    title:'中介宝',
    				description:'最佳中介房源管理软件，新手礼包等你来拿!',
				    thumbUrl: 'widget://image/logo1_108.png',
				    contentUrl: shareURL,
				},function(ret,err){
				    if(ret.status){
				        api.alert({title: '发表微信',msg: '发表成功', buttons: ['确定']});
				    } else{
				        api.alert({title: '发表失败',msg: err.msg,buttons: ['确定']});
				    }
				});
	        } else{
	            api.alert({msg:err.msg});
	        }
	    }
	);
}

function shareWeiXin2(invitationCode){
var weiXin = api.require('weiXin');
	weiXin.registerApp(
	    function(ret,err){
	        if (ret.status) {
				weiXin.sendRequest({
				    scene:'session',
				    contentType:'web_page',
				    title:'中介宝',
    				description:'最佳中介房源管理软件，新手礼包等你来拿!',
				    thumbUrl: 'widget://image/logo1_108.png',
				    contentUrl: shareURL,
				},function(ret,err){
				    if(ret.status){
				        api.alert({title: '发表微信',msg: '发表成功', buttons: ['确定']});
				    } else{
				        api.alert({title: '发表失败',msg: err.msg,buttons: ['确定']});
				    }
				});
	        } else{
	            api.alert({msg:err.msg});
	        }
	    }
	);
}

function copyInvitationCode(){
}