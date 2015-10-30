apiready = function(){
	if(api.systemType=='android'){
    	$('#pay').css('display','block');
    	$('#reg').css('display','');
	}
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
			//blockAlert(config.user.tel);
			//$('#tel').html(config.user.tel);
			//$('#tel').css('color','red');
			$('#tel').text(config.user.tel);
			if(config.user.debug){
				$('#settings').show();
			}
			if(config.user.pwd){
				$('#endtime').text(config.user.mobileDeadtime);
				var t = Date.parse(config.user.mobileDeadtime+' 23:59:59');
				//blockAlert(t-new Date().getTime());
				if(t-new Date().getTime()<=0){
					$('#endtime').css('color','red');
				}
			}else{
				$('#endtime').text('未登录');
			}
			if(config.user.invitationActive==0){
				$('#gift').css('display','');
			}
		}else{
			$('#avatar').attr('src',api.wgtRootDir+'/v4/avatar/zjb.png');
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
		alert('请先登录');
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
	    slidBackEnabled:false,
	    url: 'citys.html?'+new Date().getTime(),
	    pageParam: {cityPy: 'cityPy'}
	});
}

function setDebug(){
	api.getPrefs({
	    key:'debug'
    },function(ret,err){
    	api.setPrefs({
    	    key:'debug',
    	    value: !ret.value
        });
    	if(!ret.value){
    		blockAlert('开启开发者模式');
    	}else{
    		blockAlert('关闭开发者模式');
    	}
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
		$('#endtime').css('color','white');
		config.user.invitationActive=ret.invitationActive;
		config.user.mobileDeadtime=ret.mobileDeadtime;
		config.user.mobileDeadtimeInLong=ret.mobileDeadtimeInLong;
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
	//检查是否付费
	if(!isUserFuFei(config)){
		toFuFei();
		return;
	}
	api.openWin({
	    name: 'fav',
	    url: 'favIndex.html',
	    delay:100,
	    pageParam: {houseType: 'fav'}
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
	config = JSON.parse('{}');
	saveConfig(config);
	api.setPrefs({
        key:'version',
        value:''
    });
	alert('缓存清理成功');
}