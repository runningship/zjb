var user;
var hid;
var tel_json = JSON.parse('[]');
apiready = function(){
	getUserInfo(function(u){
		user = u;
		hid = api.pageParam.id;
		loadData();
		//loadGenJin()
	});
};
function loadData(){
		//blockAlert('http://'+server_host+'/c/mobile/detail?houseId='+hid+'&userId=');
		YW.ajax({
				url:'http://'+server_host+'/c/mobile/detail?houseId='+hid+'&userId='+1,
				method:'post',
				cache:false,
				returnAll:false
			},function(ret , err){
				if(ret){
					houseDetail = ret;
					area = ret.area;
					fdhao = ret.dhao+' - '+ret.fhao;
					$('#area').text(ret.area+'  '+fdhao);
					$('#dateadd').text(ret.dateadd);
					$('#quyu').text(ret.quyu);
					$('#ztai').text(ret.ztai);
					if(ret.mji){
						$('#mji').text(ret.mji);
					}
					if(ret.zjia){
						$('#zjia').text(ret.zjia+'万');
					}
					if(ret.djia){
						$('#djia').text(ret.djia+'元');
					}
					if(ret.lceng){
						$('#lceng').text(ret.lceng+'/'+ret.zceng);
					}
					$('#lxing').text(ret.lxing);
					$('#hxing').text(ret.hxf+"室"+ret.hxt+"厅"+ret.hxw+"卫");
					$('#zxiu').text(ret.zxiu);
					if(ret.year){
						$('#year').text(ret.year+'年');
					}
					$('#address').text(ret.address);
					$('#beizhu').text(ret.beizhu);
					$('#lxr').text(ret.lxr);
					tels = ret.tel.split(',');
					    for(var i=0;i<tels.length ;i++){
							var xx = JSON.parse('{}');
							if(tels[i].trim()==''){
								continue;
							}
					    	xx.tel = tels[i];
					    	tel_json.push(xx);
					    }
					buildHtmlWithJsonArray('tel_repeat',tel_json , false,true);
					//api.parseTapmode();
//					if(ret.isfav=='1'){
//						$('#fav').attr('src','../image/fav-yes.png');
//					}
				}else{
				}
			});
	}
	
	function loadGenJin(){
		YW.ajax({
				url:'http://'+server_host+'/c/mobile/house/genjin/list?houseId='+hid,
				method:'post',
				cache:false,
				returnAll:false
			},function(ret , err){
				if(ret){
					buildHtmlWithJsonArray('genjin',ret.data , false,true);
				}else{
				}
			});
	}
	