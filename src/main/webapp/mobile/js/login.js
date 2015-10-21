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
				alert('登录成功2');
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
		
		setTimeout(function(){
			api.closeWin({
			    name: 'login'
			});
		},2000);
	}else{
		closexx();
	}
}
apiready=function(){
	getConfig(function(cfg){
		config=cfg;
		if(config && config.user){
			$('#tel').val(config.user.tel);
			$('#pwd').val(config.user.pwd);
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

function selectCity(){
	api.openWin({
	    name: 'citys',
	    pageParam:{parentPage:'login',title:'选择城市',pageName:'citys'},
		url: '../html/wrap.html'
	});
}

//call by wrap
function setCity(pinyin,name){
	$('#city').text(name);
}

function clearCache(){
	api.setPrefs({
	    key:'city',
	    value:''
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

function openReg(){
//	api.openWin({
//        name: 'reg',
//        pageParam: {pageName: 'reg',title:'注册'},
//		url: 'wrap.html',
//        bgColor: '#fff'
//    });

	api.openWin({
        name: 'reg',
        pageParam: {pageName: 'login'},
		url: 'reg.html',
		bounces: true,
		scaleEnabled:true,
        bgColor: '#fff'
    });
	
//	window.location='reg.html';
}

function openCitys(){
	api.openWin({
	    name: 'citys',
	    url: 'citys.html',
	    pageParam: {cityPy: 'cityPy'}
	});
}
function updateCity(cityName){
	$('#city').text(cityName);
}
