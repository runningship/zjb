function exists(callback){
    var area=$('#area'),
    dhao=$('#dhao'),
    fhao=$('#fhao'),
    seeGX='1',
    areav=area.val(),
    dhaov=dhao.val(),
    fhaov=fhao.val();
    var seeGXv;
    //var houseStr = areav+dhaov+fhaov;
   
    var param={
        area:areav,
        dhao:dhaov,
        fhao:fhaov,
        seeGX:1
    }
    YW.ajax({
        type: 'POST',
        url: '/c/weixin/houseOwner/exist',
        data:param,
        mysuccess: function(data){
            var jsons=data;
            if(jsons['exist']==1){
            	alert('房源重复,请检查楼盘名称，房栋号');
            }else{
                //api.title(apiTitle + '　<b style="color:#090;">无重复</b>');
                callback();
            }
        }
    });
}