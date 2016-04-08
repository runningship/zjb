apiready = function(){
//	if(api.systemType=='android'){
//    	$('#pay').css('display','block');
//    	$('#reg').css('display','');
//	}
	//ios上线时要处理
	$('#pay').css('display','block');
	$('#reg').css('display','');
	getConfig(function(cfg){
		config = cfg;
		init();
	});
};
function init(){
	if(config){
		if(config.user){
			if(config.user.avatar){
				//$('#avatar').attr('src',api.wgtRootDir+'/v4/avatar/'+config.user.avatar+'.jpg');
				//$('#avatar').css('background-image' , 'url("'++'")');
				$('#avatar').css('background-image' , 'url("'+api.wgtRootDir+'/v4/avatar/'+config.user.avatar+'.jpg'+'")');
			}else{
				//$('#avatar').attr('src',api.wgtRootDir+'/v4/avatar/zjb.png');
				$('#avatar').css('background-image' , 'url("'+api.wgtRootDir+'/v4/avatar/zjb.png'+'")');
			}
			if(config.user.avatarPath){
				//blockAlert('http://'+img_server_host+'/user_avatar_path/'+config.user.uid+'/'+config.user.avatarPath);
				$('#avatar').css('background-image' , 'url("'+'http://'+img_server_host+'/user_avatar_path/'+config.user.uid+'/'+config.user.avatarPath+'")');
				//$('#avatar').css('background-image' , 'url("'+'http://192.168.1.222/user_avatar_path/1/p-d1e1644d.jpg.t.jpg'+'")');
			}
			//$('#avatar').attr('src','../images/zjb_blue.png');
			if(config.user.pwd){
				$('#active').css('display','');
				$('#inactive').css('display','none');
			}
			if(config.user.uname){
				$('#uname').text(config.user.uname);
			}
			if(config.user.pcLname){
				$('#pcLname').text("电脑账号: "+config.user.pcLname);
			}
//			if(config.user.pcDeadTime){
//				$('#pcDeadTime').text("到期时间: "+config.user.pcDeadTime);
//			}
			//blockAlert(config.user.tel);
			//$('#tel').html(config.user.tel);
			//$('#tel').css('color','red');
			$('#tel').text(config.user.tel);
			$('#jifen').text(config.user.jifen);
			if(config.user.debug==2){
				$('#settings').show();
			}
			if(config.user.pwd){
				$('#endtime').text(config.user.mobileDeadtime);
				updateDeadtime();
			}else{
				$('#endtime').text('未登录');
			}
			if(config.user.invitationActive==0){
				$('#gift').css('display','');
			}
		}else{
			//$('#avatar').attr('src',api.wgtRootDir+'/v4/avatar/zjb.png');
			$('#avatar').css('background-image' , 'url("'+api.wgtRootDir+'/v4/avatar/zjb.png'+'")');
		}
		if(config.city){
			$('#city').text(config.city.cityName);
		}
	}
	api.parseTapmode();
	
	api.addEventListener({
	    name: 'updateJF'
	}, function(ret){
		$('#jifen').text(ret.value.jifen);
		$('#endtime').text(ret.value.mobileDeadtime);
	});
	
	api.addEventListener({
	    name: 'paySuccess'
	}, function(ret){
		window.location.reload();
	});
}

function openLogin(){
	api.openWin({
	    name: 'login',
	    url: 'login.html',
	    delay:200
	});
}
function openPwd(){
//	api.closeSlidPane();
	api.openWin({
	    name: 'pwd',
	    url: 'pwd.html',
	    delay:200
	});
}
function openReg(){
	api.openWin({
	    name: 'reg',
	    url: 'reg.html',
	    delay:200
	});
}
function openShare(){
	if(!checkUser()){
		info('请先登录');
		return;
	}
	api.openWin({
        name: 'share',
		url: 'share.html',
		delay:300,
		pageParam: {uid: config.user.uid}
    });
}

function openPay(){
	if(!checkUser()){
		info('请先登录');
		return;
	}
	api.openWin({
        name: 'pay',
		url: 'pay.html',
		delay:200,
		pageParam: {uid: config.user.uid}
    });
}

function quit(){
	if(config && config.user){
		config.user.pwd='';
		saveConfig(config);
		$('#endtime').text('未登录');
		refreshPage();
		api.execScript({
		    name: 'index',
		    frameName:'house',
		    script: 'refreshPage();'
		});
		api.execScript({
		    name: 'index',
		    frameName:'work',
		    script: 'refreshPage();'
		});
	}
	api.closeSlidPane();
}

function updateCity(cityName){
	$('#city').text(cityName);
}

function openCitys(){
	if(config.user.pwd){
		//请先退出登录
		info('请先退出登录');
		return;
	}
	api.openWin({
	    name: 'citys',
	    delay:200,
	    slidBackEnabled:false,
	    url: 'citys.html?'+new Date().getTime(),
	    pageParam: {cityPy: 'cityPy'}
	});
}

function setDebug(){
	api.getPrefs({
	    key:'online'
    },function(ret,err){
    	var result;
    	if(ret.value==undefined || ret.value=='0' || ret.value==null){
    		result='1';
    	}else{
    		result='0';
    	}
    	api.setPrefs({
    	    key:'online',
    	    value: result
        });
    	if(result=='0'){
    		blockAlert('开启开发者模式 , 需要重启重启应用程序');
    	}else{
    		blockAlert('关闭开启开发者模式, 需要重启重启应用程序');
    	}
    	quit();
    	api.closeWidget({
		    id: 'A6989896004356',
		    retData: {name:'closeWidget'},
		    silent:true
		});
    });
}



function updateDeadtime(){
	YW.ajax({
		url:'http://'+server_host+'/c/mobile/user/getMobileDeadtime',
		method:'post',
		data:{
        	values: {uid:config.user.uid}
    	},
		cache:false,
		returnAll:false
	},function(ret , err){
		$('#endtime').text(ret.mobileDeadtime);
		//$('#endtime').css('color','white');
		var t = Date.parse(ret.mobileDeadtime+' 23:59:59');
		if(t-new Date().getTime()<=0){
			$('#endtime').css('color','red');
		}
		config.user.invitationActive=ret.invitationActive;
		config.user.mobileDeadtime=ret.mobileDeadtime;
		config.user.mobileDeadtimeInLong=ret.mobileDeadtimeInLong;
		config.user.fufei = ret.fufei;
		if(ret.invitationActive==1){
			$('#gift').css('display','none');			
		}else{
			$('#gift').css('display','');
		}
		saveConfig(config);
		 //刷新工作页面
	    api.execScript({
	    	name: 'index',
		    frameName:'work',
		    script: 'reloadConfig()'
		});
	});
}

function openFav(){
	if(!checkUser()){
		api.openWin({
		    name: 'login',
		    url: 'login.html',
		    pageParam: {forward: 'favIndex.html',houseType: 'fav' , winName:'fav'}
		});
		return;
	}
	api.openWin({
	    name: 'fav',
	    url: 'favIndex.html',
	    delay:100,
	    pageParam: {houseType: 'fav'}
	});
}

function openJifen(){
	if(!checkUser()){
		api.openWin({
		    name: 'login',
		    url: 'login.html',
		    pageParam: {forward: 'integral.html',winName:'integral'}
		});
		return;
	}
	
	api.openWin({
	    name: 'integral',
	    url: 'integral.html',
	    delay:100
	});
}

function openViewLog(){
	if(!checkUser()){
		api.openWin({
		    name: 'login',
		    url: 'login.html',
		    pageParam: {forward: 'favIndex.html',houseType: 'viewLog'}
		});
		return;
	}
	//检查是否付费
	if(!isUserFuFei(config)){
		toFuFei();
		return;
	}
	api.openWin({
	    name: 'viewLog',
	    url: 'favIndex.html',
	    pageParam: {houseType: 'viewLog'}
	});
}

function clearSysCache(){
	api.clearCache();
	//config = JSON.parse('{}');
	//saveConfig(config);
	api.setPrefs({
        key:'version',
        value:''
    });
	info('缓存清理成功');
}

function openUserProfile(){
	if(!checkUser()){
		info('请先登录');
		return;
	}
	api.openWin({
	    name: 'profile',
	    url: 'user_profile.html'
	});
}
