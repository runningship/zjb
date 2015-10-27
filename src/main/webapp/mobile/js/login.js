var tel;
function doLogin(){
	tel = $('#tel').val();
	var pwd = $('#pwd').val();
	if(!tel){
		alert('请输入正确的手机号码');
		return;
	}
	if(!pwd){
		alert('请输入登录密码');
		return;
	}
	//请先选择城市
	YW.ajax({
		url:'http://'+server_host+'/c/mobile/user/login',
		method:'post',
		data:{
        	values: {tel:tel,pwd:pwd,deviceId:api.deviceId}
    	},
		cache:false,
		returnAll:false
	},function(ret , err){
		if(ret && ret.result==0){
				ret.pwd = pwd;
				config.user = ret;
				saveConfig(config);
				api.execScript({
				    name: 'user',
				    script: 'refreshPage();'
				});
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
				alert('登录成功');
				forward();
		}else{
			alert(ret.msg);
		}
		
	});
}

function forward(){
	if(api.pageParam.forward){
//		api.openFrame({
//		    name: 'fav',
//		    url: api.pageParam.forward,
//		    pageParam: api.pageParam,
//		    rect:{
//		        x:0,
//		        y:0,
//		        w:'auto',
//		        h:'auto'
//		    },
//		});
		api.openWin({
		    name: 'fav',
		    url: api.pageParam.forward,
		    pageParam: api.pageParam
		});
		
		
	}else{
//		closexx();
	}
	
	setTimeout(function(){
//		api.closeWin({
//		    name: 'login'
//		});
		closexx();
	},1000);
}
apiready=function(){
	api.clearCache();
	
	getConfig(function(cfg){
		config=cfg;
		if(config && config.user){
			$('#tel').val(config.user.tel);
			$('#pwd').val(config.user.pwd);
		}
		if(config.city){
			$('#city').text(config.city.cityName);
		}
	});
    if(api.systemType=='ios'){
		//ios版审核未上线
    	YW.ajax({
    		url:'http://'+server_host+'/c/mobile/user/isIOSOnline',
    		method:'get',
    		cache:false
    	},function(ret , err){
    		if(ret && ret.iosShenHeVersion!=api.appVersion){
    			$('#reg').css('display','');
    		}else{
    			
    		}
    	});
	}else{
		$('#reg').css('display','');
	}
};

function openReg(){
	api.openWin({
        name: 'reg',
		url: 'reg.html',
		bounces: false,
		scaleEnabled:true
    });
}
function openModifyPwd(){

	api.openWin({
        name: 'reg',
        pageParam: {pageName: 'pwd'},
		url: 'pwd.html',
		bounces: true,
		scaleEnabled:true,
        bgColor: '#fff'
    });
}

function openCitys(){
	api.openWin({
	    name: 'citys',
	    url: 'citys.html?'+new Date().getTime(),
	    pageParam: {cityPy: 'cityPy'}
	});
}
function updateCity(cityName){
	$('#city').text(cityName);
}
