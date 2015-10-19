var user;
apiready = function(){
	getUserInfo(function(u){
		user = u;
		init();
	});
};
function init(){
	if(user){
		if(user.avatar){
			$('#avatar').attr('src','../images/71.jpg');
		}
		$('#active').css('display','');
		$('#inactive').css('display','none');
		if(user.uname){
			$('#uname').text(user.uname);
		}
		$('#tel').text(user.tel);
		$('#endtime').text(user.mobileDeadtime);
		$('#city').text(user.cityName);
	}
}