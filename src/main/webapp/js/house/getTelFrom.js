function GTFCallback(data){
	$('#GTF'+data.mobile).html(''+data.province + data.cityname +'');
	//$('#GTF'+data.mobile).attr('title' , ''+data.province + data.cityname +'');
}

function getTelFormIng(tel){
  if(tel){
    var htmlsa=$('#GTF'+tel)
    $.ajax({
       type: "get",
       url: 'http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel='+tel,
       dataType: "jsonp",
       timeout:3000,
       jsonp: "callback",
       beforeSend:function(){
         htmlsa.html('归属地加载中');
       },
       success: function(data){
          console.log(data);
          htmlsa.html(data.carrier);
          // $('.error').css('display','none');
          // var province = data.province,
          //     operators = data.catName,
          //     num = data.telString;
          // $('.num span').html(num);
          // $('.province span').html(province);
          // $('.operators span').html(operators);
       },
       error:function (){    
          htmlsa.html('获取超时，请反馈');      
       }
    });

  }
}
function getTelForm(tels){
    if(tels){
        if(tels.indexOf(',')>0){
            var strs=tels.split(',');
            for (var i = 0; i < strs.length; i++) {
                getTelFormIng(strs[i]);
            }
        }else{
        	getTelFormIng(tels);
        }
    }
}

