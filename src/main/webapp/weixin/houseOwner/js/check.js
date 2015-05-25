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
        url: '/c/house/exist',
        data:param,
        mysuccess: function(data){
            var jsons=JSON.parse(data);
            if(jsons['exist']==1){
                if(jsons['hid']==id){
                	alert('房源重复');
                }else{
                	alert('房源重复');
                    //api.title(apiTitle + '　<b style="color:#F00;">房源重复：'+ jsons['hid'] +'</b>');
                }
            }else{
                //api.title(apiTitle + '　<b style="color:#090;">无重复</b>');
                callback();
            }
        }
    });
}