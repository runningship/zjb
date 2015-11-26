var currentPage=1;
var lat,lon;
var isChuzu=false;
function loadData(clear){
	if(config.user.tel=='15856985122' || config.user.tel=='15956995828'){
		blockAlert(lon+','+lat);
	}
	var url='http://'+server_host+'/c/mobile/nearby.asp?longitude='+lon+'&latitude='+lat;
	if(isChuzu){
		url='http://'+server_host+'/c/mobile/rent/nearBy?longitude='+lon+'&latitude='+lat;
	}
	YW.ajax({
			url: url,
			method:'post',
			cache:false,
			returnAll:false
		},function(ret , err){
			if(ret){
				if(ret.result.length==0){
					$('#noResultMsg').css('display','block');
				}else{
					$('#noResultMsg').css('display','none');
				}
				if(clear){
					buildHtmlWithJsonArray('repeat',ret.result , false,false);
				}else{
					buildHtmlWithJsonArray('repeat',ret.result , false,true);
				}
				api.parseTapmode();
			}else{
			}
		});
}
	
function SeeThis(area){
	var searchParams = JSON.parse('{}');
	searchParams.specArea=area;
    api.openWin({
        name: 'viewArea',
        pageParam: {searchParams: searchParams ,isChuzu:isChuzu},
		url: 'viewArea.html',
		bounces:false,
        bgColor: '#fff'
    });
	$('#'+area).css('color','#999');
}
	
apiready = function(){
		getConfig(function(cfg){
			config = cfg;
		});
		isChuzu = api.pageParam.isChuzu;
		api.setRefreshHeaderInfo(function(ret,err){
			api.refreshHeaderLoadDone();
			currentPage=1;
	    	loadData(true);
		});
		
		var baiduLocation = api.require('baiduLocation');
	    baiduLocation.getLocation(
	        function (ret, err) {
	            var sta = ret.status;
	            lat = ret.latitude;
	            lon = ret.longitude;
	            var t = ret.timestamp;
	            if (sta) {
	                loadData();
	            } else {
	                
	            }
	        }
	    );
};

function switchType(chuzu){
	isChuzu = chuzu;
	currentPage=1;
	loadData(true);
}