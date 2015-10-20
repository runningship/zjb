var set=0;
var selectAll=0;
var Ids=[];
var userId =1;
var checkbox;
var currentPage=1;
var totalPageCount;
var totalCount;
var isChuzu=false;
var startEdit=false;
var houseType="";
function loadData(clear){
	var url='';
	
	if(isChuzu){
		if(houseType=='fav'){
			url = 'http://'+server_host+'/c/mobile/fav/listRent?userId='+userId+'&currentPageNo='+currentPage;
		}else{
			url = 'http://'+server_host+'/c/mobile/user/tracksRent?userId='+userId+'&currentPageNo='+currentPage;
		}
	}else{
		if(houseType=='fav'){
			url='http://'+server_host+'/c/mobile/fav/list?userId='+userId+'&currentPageNo='+currentPage;
		}else{
			url = 'http://'+server_host+'/c/mobile/user/tracks?userId='+userId+'&currentPageNo='+currentPage;
		}
	}
	YW.ajax({
			url:url,
			method:'post',
			cache:false,
			returnAll:false
		},function(ret , err){
			if(ret){
				totalCount = ret.page.totalCount;
				totalPageCount = ret.page.totalPageCount;
				if(clear){
					buildHtmlWithJsonArray("repeat",ret.page.data , false,false);
				}else{
					buildHtmlWithJsonArray("repeat",ret.page.data , false,true);
				}
				if(ret.page.data.length==0){
					$('#noResultMsg').css('display','block');
				}else{
					$('#noResultMsg').css('display','none');
					api.parseTapmode();
				}
			}else{
				alert(312);
			}
		});
}
	
function closexx(){
	api.closeWin({
	    name: 'fav'
    });
}

function startEditAction(){
	$('.ids').show();
	startEdit = true;
}
//

function endEditAction(){
	$('.ids').hide();
	startEdit = false;
}
function addDelete(id,obj){
	if(obj.checked){
		Ids.push(id);
	}else{
	    for(var i=0;i<Ids.length ;i++){
	      if (Ids[i]==id) {
	        Ids.splice(i,1);
	      };
	    }
	}
}

 function selectAllItems(){
 	checkbox=$('input[type=checkbox]');
	 if(selectAll==0){
	 	checkbox.prop('checked',true);
    	checkbox.each(function(i){ 
	      if ($(this).val()!='$[id]') {
	        Ids[i-1] = $(this).val(); 
	      }
	 	selectAll=1;
	 	});
	 }else{
	 	checkbox.prop('checked',false);
	 	Ids=[];
	 	selectAll=0;
	 }
 }
     
function delItems(){
	if(!Ids){
		alert('请至少选择一项');
		return;
	}
	var url="";
	if(isChuzu){
		if(houseType=='fav'){
			url = 'http://'+server_host+'/c/mobile/fav/triggerRent?houseId='+Ids+'&userId='+userId;
		}else{
			url = 'http://'+server_host+'/c/mobile/user/deltracks?chuzu=0&hid='+Ids+'&userId='+userId;
		}
	}else{
		if(houseType=='fav'){
			url='http://'+server_host+'/c/mobile/fav/trigger?houseId='+Ids+'&userId='+userId;
		}else{
			url ='http://'+server_host+'/c/mobile/user/deltracks?chuzu=1&hid='+Ids+'&userId='+userId;
		}
	}
	YW.ajax({
			url: url,
			method:'post',
			cache:false,
			returnAll:false
		},function(ret , err){
			if(ret){
				alert('已取消收藏');
				currentPage=1;
				loadData(true);
		        Ids = [];
			}else{
			}
	});
}
//refresh
function SeeThis(id){
	if(startEdit){
		var obj = $('#'+id).find('input')[0];
		if(obj.checked){
			obj.checked=false;
		}else{
			obj.checked = true;
		}
		addDelete(id,obj);
		return;
	}
	api.openWin({
        name: 'info',
        pageParam: {id:id},
		url: 'house_details.html',
		delay:300
    });
	$('#'+id).css('color','#999');
 }
 
apiready=function(){
	houseType = api.pageParam.houseType;
	getConfig(function(cfg){
		config=cfg;
	    loadData();
	});
	api.setRefreshHeaderInfo({
	 	bgColor: '#333',
	    textColor: '#666',
	},
	function(ret,err){
    	api.refreshHeaderLoadDone();
    	buildHtmlWithJsonArray("repeat",[] , false,false);
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
			loadData();
		}
	);
};

function switchType(chuzu){
	isChuzu = chuzu;
	currentPage=1;
	loadData(true);
}