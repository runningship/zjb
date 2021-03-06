var config;
var hid;
var tel_json = JSON.parse('[]');
var sellStatus=4;
var rentStatus = 1;
var userId='';
var isChuzu=false;
var isFufei=false;
var houseDetail;
apiready = function(){
	
	isChuzu = api.pageParam.isChuzu;
	api.parseTapmode();
	getConfig(function(cfg){
		fixIOSStatusBar();
		config = cfg;
		if(config.user){
			userId = config.user.uid;
		}
		isFufei = isUserFuFei(config);
		hid = api.pageParam.id;
		
		api.addEventListener({
		    name: 'loginSuccess'
		}, function(ret, err){
			window.location.reload();
		});
		
		api.addEventListener({
		    name: 'paySuccess'
		}, function(ret){
			window.location.reload();
		});
		
		api.addEventListener({
		    name:'swiperight'
		},function(ret,err){
		    api.closeWin();
		});
		
		loadData();
	});
};
function loadData(){
		var url = 'http://'+server_host+'/c/mobile/detail?houseId='+hid+'&userId='+userId;
		if(isChuzu){
			url = 'http://'+server_host+'/c/mobile/rent/detail?houseId='+hid+'&userId='+userId;
		}
		YW.ajax({
				url: url,
				method:'post',
				cache:false,
				returnAll:false
			},function(ret , err){
				if(ret){
					if(isFufei){
						loadGenJin();
					}
					houseDetail = ret;
					area = ret.area;
					fdhao = ret.dhao+' - '+ret.fhao;
					if(isFufei){
						$('#area').text(ret.area+'  '+fdhao);
						$('#favBtn').show();
						$('#image').show();
						var tels = ret.tel.split(',');
					    for(var i=0;i<tels.length ;i++){
							var xx = JSON.parse('{}');
							if(tels[i].trim()==''){
								continue;
							}
					    	xx.tel = tels[i];
					    	tel_json.push(xx);
					    }
						buildHtmlWithJsonArray('tel_repeat',tel_json , false,true);
						api.parseTapmode();
					}else{
						//如果没有登录 提示登录
						if(!hasLogin()){
							$('#toLogin').show();
							$('#footer').hide();
						}else{
							$('#toXufei').show();
							$('#footer').hide();
						}
						//$('#footer').show();
						$('#area').text(ret.area);
					}
					$('#dateadd').text(ret.dateadd);
					$('#quyu').text(ret.quyu);
					$('#ztai').text(ret.ztai);
					if(ret.fangshi==1){
						$('#fangshi').text('整租');
					}else if(ret.fangshi==2){
						$('#fangshi').text('合租');
					}
					
					if(ret.mji){
						$('#mji').text(ret.mji);
					}
					if(ret.zjia){
						if(isChuzu){
							$('#zjia').text(ret.zjia);
						}else{
							if(!ret.zjia){
								$('#zjia').text('面议');
							}else{
								$('#zjia').text(ret.zjia+'万');
							}
							
						}
						
					}
					if(ret.djia){
						$('#djia').text(ret.djia);
					}
					if(ret.lceng){
						$('#lceng').text(ret.lceng+'/'+ret.zceng);
					}
					$('#lxing').text(ret.lxing);
					$('#hxing').text(ret.hxf+"室"+ret.hxt+"厅"+ret.hxw+"卫");
					$('#zxiu').text(ret.zxiu);
					$('#id').text(ret.id);
					if(!ret.imageCount){
						$('#imageCount').text(ret.imageCount+'张');
					}
					
					if(ret.year){
						$('#year').text(ret.year+'年');
					}
					$('#address').text(ret.address);
					$('#beizhu').text(ret.beizhu);
					$('#lxr').text(ret.lxr);
					$('#readCount').text(ret.readCount+' 人');
					if(ret.isfav=='1'){
						$('#favBtn').css('color','red');
					}
				}else{
				}
			});
	}
	
	function loadGenJin(clear){
		var url = 'http://'+server_host+'/c/mobile/house/genjin/list?houseId='+hid;
		if(isChuzu){
			url = 'http://'+server_host+'/c/mobile/house/genjin/list?houseId='+hid;
		}
		YW.ajax({
				url: url,
				method:'post',
				cache:false,
				returnAll:false
			},function(ret , err){
				if(ret){
					if(ret.data.length>0){
						$('.gjList').show();
					}
					for(var i=0;i<ret.data.length;i++){
						if(ret.data[i].uname==null || ret.data[i].uname=='' || ret.data[i].uname==' '){
							if(ret.data[i].tel){
								//显示手机号码
								var xx = ret.data[i].tel;
								ret.data[i].uname=xx[0]+'*********'+xx[xx.length-1];
							}else{
								ret.data[i].uname='用户';
							}
						}
						if(ret.data[i].avatarPath){
							ret.data[i].avatarPath = 'http://'+img_server_host+'/user_avatar_path/'+ret.data[i].uid+'/'+ret.data[i].avatarPath;
						}else if (ret.data[i].avatar){
							ret.data[i].avatarPath=api.wgtRootDir+'/v4/avatar/'+ret.data[i].avatar+'.jpg';
						}else{
							ret.data[i].avatarPath=api.wgtRootDir+'/v4/avatar/'+0+'.jpg';
						}
					}
					if(clear){
						buildHtmlWithJsonArray('genjin',ret.data , false,false);
					}else{
						buildHtmlWithJsonArray('genjin',ret.data , false,true);
					}
				}else{
				}
			});
	}
	
	function addGenjin(){
		var conts = $('#conts').val();
//		var status = $('#gjType').val();
		if(conts==''){
			info('请先填写跟进信息');
			return;
		}
		api.ajax({
			url:'http://'+server_host+'/c/mobile/house/genjin/add?chuzu=0',
			method:'post',
			data:{
	        	values: {userId:userId,houseId:hid,type:sellStatus,content:conts}
	    	},
			cache:false,
			returnAll:false
		},function(ret , err){
			if(ret && ret.result=='1'){
				info('跟进成功');
				var conts=$('#conts'),hei=conts.attr('data-olh');
				conts.val('');
				conts.css({'height':hei+'px','line-height':hei+'px'});
				conts.parent().height(hei+'px');
				conts.parents('.footer').removeClass('focus');
				loadGenJin(true);
			}else{
				info('跟进失败');
			}
		});
	}
	
	function addRentGenJin(){
		var conts = $('#conts').val();
		if(conts==''){
			info('请先填写跟进信息');
			return;
		}
		api.ajax({
			url:'http://'+server_host+'/c/mobile/addRentGj',
			method:'post',
			data:{
	        	values: {uid:userId,hid:hid,flag:rentStatus,conts:conts,chuzu:1}
	    	},
			cache:false,
			returnAll:false
		},function(ret , err){
			if(ret && ret.result=='1'){
				$('#conts').val('');
				info('跟进成功');
				loadGenJin(true);
			}else{
				info('跟进失败');
			}
		});
	}
	
	function triggerFav(){
		var url ='http://'+server_host+'/c/mobile/fav/trigger?houseId='+hid+'&userId='+userId;
		if(isChuzu){
			url ='http://'+server_host+'/c/mobile/fav/triggerRent?houseId='+hid+'&userId='+userId;
		}
		YW.ajax({
			url: url,
			method:'post',
			cache:false,
			returnAll:false
		},function(ret , err){
			if(ret && ret.isfav=='1'){
				//修改收藏状态
				$('#favBtn').css('color','red');
//				api.toast({
//				    msg: '已收藏',
//				    duration:2000,
//				    location: 'top'
//				});
			}else{
				$('#favBtn').css('color','white');
//				api.toast({
//				    msg: '取消收藏',
//				    duration:2000,
//				    location: 'top'
//				});
			}
		});
	}
	
	function call(num){
		num = num.split('-')[0];
		api.call({
		    type: 'tel_prompt',
		    number: num
		});
	}
	
	function daohang(){
//		info(houseDetail.latitude+','+houseDetail.longitude);
//		window.location="intent://map/place/search?query=银行&region=北京&referer=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
			var baiduLocation = api.require('baiduLocation');
		    baiduLocation.getLocation(
		        function (ret, err) {
		            if (ret.status) {
		            	var url = '';
	            		if(houseDetail.latitude && houseDetail.longitude){
	            			url='http://api.map.baidu.com/direction?origin=latlng:'+ret.latitude+','+ret.longitude+'|name:我的位置&destination=name:终点|latlng:'+houseDetail.latitude+','+houseDetail.longitude+'&mode=driving&region='+config.city.cityName+'&output=html';
	            		}else{
	            			url='http://api.map.baidu.com/direction?origin=latlng:'+ret.latitude+','+ret.longitude+'|name:我的位置&destination='+area+'&mode=driving&region='+config.city.cityName+'&output=html';
	            		}
	            		api.openWin({
	            		    name: 'daohang',
	            		    url: url,
	            		    delay:200
	            		});
		            } else {
		            }
		        }
		    );
	}

function openImages(){
	api.openWin({
	    name: 'picGenji',
	    //url: 'http://toddmotto.com/labs/echo/',
	    url: 'pic_up.html?'+new Date().getTime(),
	    pageParam: api.pageParam
	});
}

function openLogin(){
	api.openWin({
	    name: 'login',
	    url: 'login.html',
	    delay:200
	});
}

function openFufei(){
	api.openWin({
        name: 'pay',
		url: 'pay.html',
		delay:200,
		pageParam: {uid: config.user.uid}
    });
}