apiready=function(){
	getConfig(function(cfg){
		config=cfg;
		if(config && config.user){
			$('#uid').val(config.user.uid);
		}
		init();
	});
};

function init(){
	if(api.pageParam.isChuzu){
		$('#title').text('添加租房');
		$('.chuzu').show();
		$('.chushou').remove();
	}else{
		$('#title').text('添加二手房');
		$('.chushou').show();
		$('.chuzu').remove();
	}
	//可以来一发吗
	buildHtmlWithJsonArray('quyuItem',config.city.quyus , true, false);
}
function save(){
        var a=$('form[name=form1]').serializeArray();
        var data = JSON.parse('{}');
        for(var i=0;i<a.length;i++){
        	data[a[i].name]=a[i].value;
        }
        data.beizhu=$('[name=beizhu]').val();
        var url = 'http://'+server_host+'/c/mobile/addPrivateHouse';
        if(api.pageParam.isChuzu){
        	url = 'http://'+server_host+'/c/mobile/rent/addPrivateHouse';
        }
        YW.ajax({
        	url: url,
            data:{values:data},
    		method:'post',
    		cache:false,
    		returnAll:false
    	},function(ret , err){
    		if(ret && ret.result==0){
    			blockAlert('发布成功');
    			api.execScript({
    				name:'myHouse',
    			    frameName: 'myHouseFrame',
    			    script: 'refreshPage()'
    			});
    			closexx();
    		}else{
    			alert($('[name='+ret.field+']').attr('data-tip'));
    			$('[name='+ret.field+']').focus();
    		}
    	});
}

$(document).ready(function() {
    $('textarea').tah({
        moreSpace:15,
        maxHeight:600,
        animateDur:200
    });
});
