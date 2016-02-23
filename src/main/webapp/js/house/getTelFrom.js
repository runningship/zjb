function GTFCallback(data){
	$('#GTF'+data.mobile).html('['+data.province + data.cityname +']');
	//$('#GTF'+data.mobile).attr('title' , ''+data.province + data.cityname +'');
}

function getTelFormIng(tel){
  if(tel){
    $.ajax({
        dataType: 'jsonp',
      type: 'get',
      url: 'http://virtual.paipai.com/extinfo/GetMobileProductInfo?mobile='+tel+'&amount=3000&callname=GTFCallback',
      mysuccess: function(data){
          //$('#GTF'+tel).html(''+data.province + data.cityname +'');
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