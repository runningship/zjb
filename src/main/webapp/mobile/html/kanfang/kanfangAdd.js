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
		$('#title').text('看租房');
		$('.chuzu').show();
		$('.chushou').remove();
	}else{
		$('#title').text('看二手房');
		$('.chushou').show();
		$('.chuzu').remove();
	}
}
function save(){
        var a=$('form[name=form1]').serializeArray();
        var data = JSON.parse('{}');
        for(var i=0;i<a.length;i++){
        	data[a[i].name]=a[i].value;
        }
        data.beizhu=$('[name=beizhu]').val();
        if(api.pageParam.isChuzu){
        	data.isChuzu = 1;
        }else{
        	data.isChuzu =0;
        }
        
        data.uid = config.user.uid;
        blockAlert(data.isChuzu);
        var url = 'http://'+server_host+'/c/mobile/kanfang/add';
        YW.ajax({
        	url: url,
            data:{values:data},
    		method:'post',
    		cache:false,
    		returnAll:false
    	},function(ret , err){
    		if(ret && ret.result==0){
    			alert('保存成功');
    		}else{
    			alert($('[name='+ret.field+']').attr('data-tip'));
    			$('[name='+ret.field+']').focus();
    		}
    	});
}

$(document).ready(function() {
//    $('textarea').tah({
//        moreSpace:15,
//        maxHeight:600,
//        animateDur:200
//    });
});
