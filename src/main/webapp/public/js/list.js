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

function editHouse(id){
	layer.open({
    	type: 2,
    	title: '修改房源',
	    shadeClose: false,
	    shade: 0.5,
    	area: ['610px', '480px'],
	    content: 'chushou_edit.jsp?hid='+id
// 	    btn: ['确定','取消'],
// 	    yes:function(index){
// 	    	$('[name=layui-layer-iframe'+index+']').contents().find('.save').click();
// 		    return false;
// 		}
	}); 
}

function addHouse(){
	layer.open({
    	type: 2,
    	title: '发布房源',
	    shadeClose: false,
	    shade: 0.5,
    	area: ['610px', '480px'],
	    content: 'chushou_add.jsp'
// 	    btn: ['确定','取消'],
// 	    yes:function(index){
// 	    	$('[name=layui-layer-iframe'+index+']').contents().find('.save').click();
// 		    return false;
// 		}
	}); 
}

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