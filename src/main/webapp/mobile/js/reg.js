var sendingVerifyCode;
function getVerfiyCode(btn){
	if(sendingVerifyCode){
		return;
	}
	var tel = $('#tel').val();
	var pwd = $('#pwd').val();
	if(!tel){
		alert('请先填写有效手机号码');
		return;
	}
	$(btn).addClass('gray');
//	var set = $('.blue');
//	if(!set){
//		return;
//	}
	//提示信息
	YW.ajax({
		url:'http://'+server_host+'/c/mobile/user/sendVerifyCode',
		method:'post',
		data:{
        	values: {tel:tel}
    	},
		cache:false,
		returnAll:false
	},function(ret , err){
		if(ret){
			sendingVerifyCode = true;
			setcode();
		}else{
			alert(err.msg);
		}
		
	});
}

function setcode(){
	var times=60;
	var clock =  setInterval(function(){
		times--;
		if(times<1){
			$('.getCode').html('获取验证码');
			$('.getCode').removeClass('gray');
			clearInterval(clock);
			sendingVerifyCode = false;
			return;
		}
		$('.getCode').html('已发送('+times+')');
	},1000);
}

function doReg(){
	var tel = $('#tel').val();
	var pwd = $('#pwd').val();
	var uname = $('#uname').val();
	var code = $('#verifyCode').val();
	if(!tel){
		alert('请输入正确的手机号码');
		return;
	}
	if(!uname){
		alert('请输入用户名');
		return;
	}else{
		if(uname.length>5){
			alert('用户名不能超过5个字');
			return;
		}
	}
	if(!pwd){
		alert('请输入登录密码');
		return;
	}
	//提示信息
	YW.ajax({
		url:'http://'+server_host+'/c/mobile/user/verifyCode',
		method:'post',
		data:{
        	values: {tel:tel , code:code,pwd:pwd , uname:uname}
    	},
		cache:false,
		returnAll:false
	},function(ret , err){
		if(ret && ret.result=='1'){
			blockAlert('注册成功');
			api.openWin({
			    name: 'login',
			    url: 'login.html',
			    delay:300
			});
			setTimeout(function(){
				api.closeWin({
				    name: 'reg'
			    });
			},1000);
		}else{
			alert(ret.msg);
		}
		
	});
}

function doModifyPwd(){
	var tel = $('#tel').val();
	var pwd = $('#pwd').val();
	var code = $('#verifyCode').val();
	if(!tel){
		alert('请输入正确的手机号码');
		return;
	}
	if(!pwd){
		alert('请输入登录密码');
		return;
	}
	//提示信息
	YW.ajax({
		url:'http://'+server_host+'/c/mobile/user/modifyPwd',
		method:'post',
		data:{
        	values: {tel:tel , code:code,pwd:pwd}
    	},
		cache:false,
		returnAll:false
	},function(ret , err){
		if(ret.result){
			api.hideProgress();
			blockAlert('修改密码成功');
			//跳到登录页面
			closexx();
		}else{
			alert(ret.msg);
		}
	});
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
apiready=function(){
	getConfig(function(cfg){
		config=cfg;
		if(config && config.user){
			$('#tel').val(config.user.tel);
			$('#city').text(config.city.cityName);
		}
	});
};