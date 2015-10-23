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
		$('#title').text('编辑租房');
		$('.chuzu').show();
		$('.chushou').remove();
	}else{
		$('#title').text('编辑二手房');
		$('.chushou').show();
		$('.chuzu').remove();
	}
	//可以来一发吗
	buildHtmlWithJsonArray('quyuItem',config.city.quyus , true, false);
	var url ='http://'+server_host+'/c/mobile/editPrivateHouse'; 
	 if(api.pageParam.isChuzu){
		 url = 'http://'+server_host+'/c/mobile/rent/editPrivateHouse';
	 }
	YW.ajax({
    	url: url,
		method:'post',
		data:{values:{hid:api.pageParam.hid}},
		cache:false,
		returnAll:false
	},function(ret , err){
		if(ret){
			if(ret.return_status==303){
					blockAlert(ret.msg);
					closexx();
					return;
			}
			$('input').each(function(){
				var name = $(this).attr('name');
				if(ret.house[name]==0){
					return;
				}
				$(this).val(ret.house[name]);
			});
			$('input[name=fangzhuTel]').val(ret.house.tel);
			$('select[name=hxing]').val(ret.hxing);
			$('select[name=lxing]').val(ret.house.lxing);
			$('select[name=zxiu]').val(ret.house.zxiu);
			$('select[name=quyu]').val(ret.house.quyu);
			$('[name=beizhu]').val(ret.house.beizhu);
		}else{
			alert('加载数据失败请重试.');
		}
	});
}

function delHouse(){
	api.confirm({
		msg: '确定要删除该房源吗?',
	    buttons:['取消', '确定']
	},function(ret,err){
	    if(ret.buttonIndex == 2){
	    	var url = 'http://'+server_host+'/c/mobile/delPrivateHouse';
    		if(api.pageParam.isChuzu){
    			url = 'http://'+server_host+'/c/mobile/rent/delPrivateHouse';
    		}
	    	YW.ajax({
	        	url: url,
	    		method:'post',
	    		data:{values:{hid:api.pageParam.hid}},
	    		cache:false
	    	},function(ret , err){
	    		if(ret){
	    			blockAlert('删除成功');
	    			closexx();
	    		}else{
	    			alert('操作失败请重试.');
	    		}
	    	});
	    }
	});
}
function save(){
        var a=$('form[name=form1]').serializeArray();
        var data = JSON.parse('{}');
        for(var i=0;i<a.length;i++){
        	data[a[i].name]=a[i].value;
        }
        data.id=api.pageParam.hid;
        var url = 'http://'+server_host+'/c/mobile/updatePrivateHouse';
        if(api.pageParam.isChuzu){
        	url ='http://'+server_host+'/c/mobile/rent/updatePrivateHouse'; 
        }
        YW.ajax({
        	url: url,
            data:{values:data},
    		method:'post',
    		cache:false,
    		returnAll:false
    	},function(ret , err){
    		if(ret && ret.result==0){
    			blockAlert('保存成功');
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
