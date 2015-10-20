var config;
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
				$('#avatar').attr('src','../images/71.jpg');
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
			
		}
		if(config.city){
			$('#city').text(config.city.cityName);
		}
	}
}

function checkUser(){
	if(config && config.user && config.user.valid){
		return true;
	}
	alert('请先登录');
	return false;
}
function openLogin(){
//	api.closeSlidPane();
	api.openWin({
	    name: 'login',
	    url: 'login.html'
	});
}
function openCitys(){
	api.openWin({
	    name: 'citys',
	    url: 'citys.html',
	    pageParam: {cityPy: 'cityPy'}
	});
}

function quit(){
	if(config && config.user){
		config.user.pwd='';
		saveConfig(config);
		$('#endtime').text('未登录');
		alert('已退出登录');
		refreshPage();
	}
}
function updateCity(cityName){
	$('#city').text(cityName);
}
