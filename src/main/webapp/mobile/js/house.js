var currentPage=1;
var totalPageCount;
var user;
var isChuzu=false;
var searchParams = JSON.parse('{}');
function loadData(clear){
	var url = 'http://'+server_host+'/c/mobile/list?page='+currentPage;
	if(isChuzu){
		url = 'http://'+server_host+'/c/mobile/rent/list?page='+currentPage;
	}
	YW.ajax({
		url: url,
		method:'post',
		cache:false,
		data:{values:searchParams},
		returnAll:false
	},function(ret , err){
		if(ret){
			totalPageCount = ret.page.totalPageCount;
				if(ret.page.data.length==0){
					$('#noResultMsg').css('display','block');
				}else{
					$('#noResultMsg').css('display','none');
				}
			if(clear){
				buildHtmlWithJsonArray('repeat',ret.page.data , false,false);
			}else{
				buildHtmlWithJsonArray('repeat',ret.page.data , false,true);
			}
			api.parseTapmode();
		}else{
		}
	});
}

function setSearchParamsAndSearch(params){
	searchParams = JSON.parse(params);
	loadData(true);
}
function SeeThis(id){
	api.openWin({
        name: 'info',
        pageParam: {pageName: 'info',fufei:isFufei(), id:id},
		url: 'house_details.html',
		delay:300
    });
	$('#'+id).css('color','#999');
 }

	apiready = function(){
		isChuzu = api.pageParam.isChuzu;
		getUserInfo(function(u){
			user = u;
		});
		api.setRefreshHeaderInfo({
			 	bgColor: '#333',
			    textColor: '#666',
			},
			function(ret,err){
		    	api.refreshHeaderLoadDone();
		    	buildHtmlWithJsonArray('repeat',[] , false,false);
		    	currentPage=1;
		    	loadData(true);
			});
		
		api. addEventListener({name:'scrolltobottom'}, 
			function(ret, err){
				//设置提示信息
				if(currentPage<totalPageCount){
					currentPage++;
				}else{
					alert('已是最后一页');
					return;
				}
				setTimeout(loadData,100);
			}
		);
		
		loadData();
	};

function isFufei(){
	return user && user.fufei;
}

function refreshPage(){
	window.location.reload();
}