var user;
var hid;
var tel_json = JSON.parse('[]');
var status=4;
apiready = function(){
	getUserInfo(function(u){
		user = u;
		hid = api.pageParam.id;
		loadData();
		loadGenJin()
	});
};
function loadData(){
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
				}else{
				}
			});
	}
	
	function loadGenJin(clear){
		YW.ajax({
				url:'http://'+server_host+'/c/mobile/house/genjin/list?houseId='+hid,
				method:'post',
				cache:false,
				returnAll:false
			},function(ret , err){
				if(ret){
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
		var conts = $('#conts').text();
		if(conts==''){
			alert('请先填写跟进信息');
			return;
		}
		api.ajax({
			url:'http://'+server_host+'/c/mobile/house/genjin/add?chuzu=0',
			method:'post',
			data:{
	        	values: {userId:1,houseId:hid,type:status,content:conts}
	    	},
			cache:false,
			returnAll:false
		},function(ret , err){
			if(ret && ret.result=='1'){
				alert('跟进成功');
				$('#conts').text('');
				loadGenJin(true);
			}else{
				alert('跟进失败');
			}
		});
	}