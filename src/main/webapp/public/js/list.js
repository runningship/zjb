function switchCity(cityPy , cityName){
	 var exp = new Date();
     exp.setTime(exp.getTime() + 1000*3600*24*365);//过期时间一年 
//     document.cookie = "city=" +cityName+ ";expires=" + exp.toGMTString()+ ";path=/";
     document.cookie = "cityPy=" + cityPy + ";expires=" + exp.toGMTString()+ ";path=/";
     window.location.reload();
}

function clearCache(){
	  document.cookie='city=a;expires='+ new Date().toGMTString();
	  document.cookie='city=a;expires='+ new Date().toGMTString()+';path=/';
	  document.cookie='city=a;expires='+ new Date().toGMTString()+';path=/public';
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
	if(!cityPy){
		cityPy="hefei";
	}
	var city = $('.citybox a[py='+cityPy+']').text();
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

function HouseDelete(Thi){
    var tr=Thi.parents('tr'),hid=tr.data('hid');
    YW.ajax({
    type: 'POST',
    url: '/c/weixin/houseOwner/deleteHouse?id='+hid,
    mysuccess: function(data){
        tr.remove();
        }
    });
}

function RentDelete(Thi){
    var tr=Thi.parents('tr'),hid=tr.data('hid');
    YW.ajax({
    type: 'POST',
    url: '/c/weixin/houseOwner/deleteRent?id='+hid,
    mysuccess: function(data){
        tr.remove();
        }
    });
}