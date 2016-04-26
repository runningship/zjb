function GTFCallback(data){
	$('#GTF'+data.mobile).html(''+data.province + data.cityname +'');
	//$('#GTF'+data.mobile).attr('title' , ''+data.province + data.cityname +'');
}

function getTelFormIng(tel){
  if(tel){
    var htmlsa=$('#GTF'+tel)
    $.ajax({
       type: "get",
       url: '/c/phone/getLocation?tel='+tel+'',
       dataType: "json",
       timeout:3000,
       // jsonp: "callback",
       //data:{'apikey':'d77d10cd1abe511a78aed63c03ee24e8'}
       beforeSend:function(request){
         htmlsa.html('加载归属地');
       },
       success: function(json){
          // console.log(json);
          var result=json.result,
          data=result.data,
          error=result.error,
          msg=result.msg,
          citys=data.city;
          // console.log(data);
          if(error<1){
            var operators='',os=data.operator;
            if(os.indexOf('移动')>=0){
              operators='移动';
            }else if(os.indexOf('联通')>=0){
              operators='联通';
            }else if(os.indexOf('电信')>=0){
              operators='电信';
            }
            citys=citys.replace('市','');
            htmlsa.html(citys+operators);
          }
       },
       error:function (){    
          htmlsa.html('获取超时');      
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

