//var server_host = "mobile.zjb.tunnel.mobi";
var server_host = "192.168.1.222:8081";
//定位所在城市
var myCity;
var config;
function getConfig(callback){
	if(api.systemType=='ios'){
		$('#header').css('margin-top','25px');
	}
	api.getPrefs({
	    key: 'config'
	}, function(ret, err){
		if(callback){
			var v = ret.value;
			if(!v){
				callback(JSON.parse('{}'));
			}else{
				callback(JSON.parse(v));
			}
		}
	});
}

function saveConfig(config){
	api.setPrefs({
        key:'config',
        value:JSON.stringify(config)
    });
}

//function getCityInfo(callback){
//	api.getPrefs({
//	    key:'city'
//	},function(ret,err){
//		if(ret.value){
//			callback(JSON.parse(ret.value));
//		}else{
//			callback(null);
//		}
//	});
//}

YW={
    ajax:function(opts,callback){
    	if(!opts.timeout){
    		opts.timeout = 30;
    	}
//  	opts.charset = "iso-8859-1";
//  	opts.characterSet = "utf-8";
//  	opts.headers = {"Content-Type":"charset=utf-8"};
		if(opts.data){
    		if(!opts.data.values){
    			opts.data.values=JSON.parse('{}');
    		}
    	}else{
    		opts.data=JSON.parse('{}');
    		opts.data.values=JSON.parse('{}');
    	}
    	opts.data.values.deviceId = api.deviceId;
    	if(config.city && config.city.cityPy){
        	opts.data.values.cityPy=config.city.cityPy;
    	}else{
    		if(!opts.data.values.ignoreCityCheck){
    			alert('请先在登录页选择城市');
        		return;
    		}
    	}
    	if(config.user && config.user.tel){
//    		if(!opts.data.values.url || opts.data.values.url.indexOf('mobile/user/login')<0 ||opts.data.values.url.indexOf('mobile/user/sendVerifyCode')<0 || opts.data.values.url.indexOf('mobile/user/verifyCode')<0){
//    			//url 为空 或者 url不是login时
//    			opts.data.values.tel = ret.value;
//    		}
    		if(!opts.data.values.tel){
    			//如果参数中没有电话号码
    			opts.data.values.tel = config.user.tel;
    		}
    	}
    	opts.data.values.cache=false;
    	api.showProgress({
		    style: 'default',
		    animationType: 'fade',
		    title: '请求处理中...',
		    text: '马上就好了',
		    modal: false
		});
    	//blockAlert(JSON.stringify(opts));
       	api.ajax(opts,function(ret,err){
       		if(err && 'loginFromOther'==err.msg){
   				blockAlert('账号在别处登录');
               config.user.tel='';
               config.user.pwd='';
               saveConfig(config);
	    		api.closeWidget({
				    id: 'A6989896004356',
				    retData: {name:'closeWidget'},
                    silent:true
				});
       			return;
       		}
       		api.hideProgress();
       		if(err){
       			alert(err.msg);
       		}
       		callback(ret,err);
       	});
    }
}

window.blockAlert = window.alert;

window.alert=function(message){
	api.toast({
	    msg: message,
	    duration:2000,
	    location: 'middle'
	});
}

//获取url里需要的值
function getParam(name){
var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");
return (reg.test(location.search))? encodeURIComponent(decodeURIComponent(RegExp.$2.replace(/\+/g, " "))) : '';
}

function closexx(){
	api.closeWin();
}

function refreshPage(){
	window.location.reload();
}

function reloadConfig(){
	getConfig(function(cfg){
		config = cfg;
	});
}

function checkUser(){
	if(config && config.user && config.user.pwd){
		return true;
	}
	return false;
}

function isUserFuFei(){
	if(!config){
		return false;
	}
	if(!config.user){
		//没登录
		return false;
	}
	if(!config.user.pwd){
		//没登录
		return false;
	}
	if(!config.user.mobileDeadtimeInLong){
		//没续费
		return false;
	}
	var now = new Date().getTime();
	if(now>=config.user.mobileDeadtimeInLong){
		//时间到期
		return false;
	}
	return true;
}

function toFuFei(){
	api.confirm({
	    title: '服务到期提醒',
	    msg: '您的服务时间已到期,请续费后继续使用',
	    buttons:['现在续费', '一会再说']
	},function(ret,err){
	    if(ret.buttonIndex == 1){
	    	api.openWin({
	            name: 'pay',
	    		url: 'pay.html',
	    		delay:200,
	    		pageParam: {uid: config.user.uid}
	        });
	    }
	});
}

function checkFile(file){
	//blockAlert(api.cacheDir);
	blockAlert('用alert不会乱码');
	api.alert({msg:'api.alert会乱码'});
	try{
		var fs =  api.require('fs');
		fs.open({
		    path: api.fsDir+file,
		    flags: 'read_write'
		},function(ret,err){
		    if (ret.status) {
		        fs.read({
		    	    fd:ret.fd,
		    	    offset:0,
		    	    length:0
		    	},function(ret,err){
		    	    if (ret.status) {
		    	        api.alert({msg:ret.data});
		    	    }else{
		    	        api.alert({msg:err.msg});
		    	    }
		    	});
		    }else{
		        api.alert({msg:err.msg});
		    }
		});
	}catch(e){
		blockAlert(e);
	}
}
