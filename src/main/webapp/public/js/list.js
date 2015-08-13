function switchCity(cityPy , cityName){
	 var exp = new Date();
     exp.setTime(exp.getTime() + 1000*3600*24*365);//过期时间一年 
     document.cookie = "city=" +cityName+ ";expires=" + exp.toGMTString()+ ";";
     document.cookie = "cityPy=" + cityPy + ";expires=" + exp.toGMTString()+ ";";
     window.location="list.jsp";
}

function getCookie(name) { 
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]); 
    else 
        return ""; 
} 

$(function(){
	var cityPy = getCookie('cityPy');
	var city = getCookie('city');
	var tel = getCookie('tel');
	if(cityPy && cityPy!='undefined'){
		$('#currentCity').text(city);
		$('#currentCity').attr("py" , cityPy);
	}else{
		//默认为合肥，或者js定位
	}
	
	$('.citybox li').each(function(index,obj){
		if($(obj).text()==$('#currentCity').text()){
			$(obj).addClass('active');
			$(obj).find('a').prepend('<i  class="iconfont">&#xe607;</i>');
		}
	});
	if(tel && tel!='undefined'){
		$('#tel_input').val(tel);
	}
	
});