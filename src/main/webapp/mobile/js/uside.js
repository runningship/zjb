apiready = function(){
	getConfig(function(cfg){
		config = cfg;
		init();
	});
};
function init(){
	if(config){
		if(config.user){
			if(config.user.avatar){
				$('#avatar').attr('src',api.wgtRootDir+'/v4/avatar/'+config.user.avatar+'.jpg');
			}else{
				$('#avatar').attr('src',api.wgtRootDir+'/v4/avatar/zjb.png');
			}
			if(config.user.pwd){
				$('#active').css('display','');
				$('#inactive').css('display','none');
			}
			if(config.user.uname){
				$('#uname').text(config.user.uname);
			}
			$('#tel').text(config.user.tel);
			if(config.user.pwd){
				$('#endtime').text(config.user.mobileDeadtime);
			}else{
				$('#endtime').text('未登录');
			}
			if(config.user.invitationActive==0){
				$('#gift').css('display','');
			}
		}else{
			$('#avatar').attr('src','http://192.168.1.222:8081/mobile/images/zjb.png');
		}
		if(config.city){
			$('#city').text(config.city.cityName);
		}
	}
	api.parseTapmode();
}

function openLogin(){
	api.openWin({
	    name: 'login',
	    url: 'login.html'
	});
	api.closeSlidPane();
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
	api.closeSlidPane();
	api.openWin({
	    name: 'reg',
	    url: 'reg.html',
	    delay:200
	});
}
function openShare(){
	if(!checkUser()){
		alert('请先登录');
		return;
	}
	api.openWin({
        name: 'share',
		url: 'share.html',
		delay:200,
		pageParam: {uid: config.user.uid}
    });
}

function openPay(){
	if(!checkUser()){
		alert('请先登录');
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
		alert('已退出登录');
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
		alert('请先退出登录');
		return;
	}
	api.openWin({
	    name: 'citys',
	    delay:200,
	    url: 'citys.html?'+new Date().getTime(),
	    pageParam: {cityPy: 'cityPy'}
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
		config.user.invitationActive=ret.invitationActive;
		config.user.mobileDeadtime=ret.mobileDeadtime;
		config.user.mobileDeadtimeInLong=ret.mobileDeadtimeInLong;
		if(ret.invitationActive==1){
			$('#gift').css('display','none');			
		}else{
			$('#gift').css('display','');
		}
		saveConfig(config);
	});
}

function clearSysCache(){
	api.clearCache();
	config = JSON.parse('{}');
	saveConfig(config);
	alert('缓存清理成功');
}