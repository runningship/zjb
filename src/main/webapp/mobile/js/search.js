var mjiStart='';
var mjiEnd='';
var zjiaStart='';
var zjiaEnd='';
var djiaStart='';
var djiaEnd='';
var lcengStart='';
var lcengEnd='';
var searchText='';
var fhao='';
var dhao='';
var mobileTel='';
var quyus=[];
var fangshi='';
var historySearchCount;
var historyItemSplitChar='--';
var isChuzu=false;
var searchHistoryKey="searchHistory";
apiready=function(){
	fixIOSStatusBar();
	onready();
};

function onready(callback){
	getConfig(function(cfg){
		isChuzu = api.pageParam.isChuzu;
		if(isChuzu){
			searchHistoryKey="searchRentHistory";
			$('.chuzu').show();
			$('.chushou').remove();
		}else{
			$('.chuzu').remove();
			$('.chushou').show();
		}
		config=cfg;
		init();
		if(callback){
			callback();
		}
	});
	api.parseTapmode();
}
function init(){
	if(isUserFuFei(config)){
		$('#others').css('display','');
		$('#tel').css('display','');
	}
	buildHtmlWithJsonArray('repeat',config.city.quyus , false, false);
	if(api.systemType=='android'){
		api.parseTapmode();
	}
	$('#quyuWrap').prepend('<li id="allQuyu" class="active" onclick="selectAllQuyu(this);"><a><i class="iconfont hide">&#xe656;</i>全部</a></li>');
	if(api.pageParam.loadHistory){
		loadSearchHistory();
	}
	
}

function loadSearchHistory(){
	api.getPrefs({
    	key:searchHistoryKey
    },function(ret,err){
    	if(ret.value){
			var arr = ret.value.split(historyItemSplitChar);
			var jsonArr = JSON.parse('[]');
			for(var i=0;i<arr.length;i++){
				if(!arr[i]){
					continue;
				}
				var json = JSON.parse('{}');
				json.html = arr[i];
				jsonArr.push(json);
			}
			
			historySearchCount = jsonArr.length;
			buildHtmlWithJsonArray('hisRepeat',jsonArr , true, false);
			api.parseTapmode();
    	}else{
    		historySearchCount=0;
    	}
    });
}

function clearQuery(){
	$('#mainer input').val('');
	$('#mainer input').attr('checked',false);
	fangshi='';
	$('#quyuWrap li i').addClass('hide');
	$('#searchText').val('');
}

function selectAllQuyu(obj){
	//$(obj).addClass('active');
	$(obj).find('i').show();
	//$('#quyuWrap li').removeClass('active');
	$('#quyuWrap li i').addClass('hide');
}

function selectQuyu(obj){
	//$('#allQuyu').removeClass('active');
	$('#allQuyu').find('i').hide();
	
	//$(obj).toggleClass('active');
	$(obj).find('i').toggleClass('hide');
}

function clearHistory(){
	$('#searchHistoryWrap').empty();
	api.setPrefs({
	    key:searchHistoryKey,
	    value:''
    });
    historySearchCount=0;
}

function getSearchParam(){
	mjiStart = $('#mjiStart').val();
	mjiEnd = $('#mjiEnd').val();
	zjiaStart = $('#zjiaStart').val();
	zjiaEnd = $('#zjiaEnd').val();
	djiaStart = $('#djiaStart').val();
	djiaEnd = $('#djiaEnd').val();
	lcengStart = $('#lcengStart').val();
	lcengEnd = $('#lcengEnd').val();
	searchText = $('#searchText').val();
	fhao = $('#fhao').val();
	dhao = $('#dhao').val();
	mobileTel =$('#mobileTel').val();
	
	var tmp2 = [];
	$('#quyuWrap li').each(function(){
		if($(this).find('i.hide').length>0){
			return;
		}
		tmp2.push($(this).attr('data'));
	});
	quyus = tmp2.join();

	var pageParam = JSON.parse('{}');
//	pageParam.title = "搜索结果";
//	pageParam.pageName = "searchResult";
	pageParam.mjiStart = mjiStart;
	pageParam.mjiEnd = mjiEnd;
	pageParam.zjiaStart = zjiaStart;
	pageParam.zjiaEnd = zjiaEnd;
	pageParam.djiaStart = djiaStart;
	pageParam.djiaEnd = djiaEnd;
	pageParam.lcengStart =lcengStart;
	pageParam.lcengEnd = lcengEnd;
	pageParam.search = searchText;
	pageParam.fhao = fhao;
	pageParam.dhao = dhao;
	pageParam.mobileTel = mobileTel;
	pageParam.quyu = quyus;
	
	pageParam.fangshi=fangshi;
	pageParam.isChuzu = isChuzu;
	return pageParam;
}
function doSearch(){
	var pageParam = getSearchParam();
	AddSelect(pageParam);
    api.openWin({
        name: 'searchResult',
	    pageParam:pageParam,
		url: 'searchResult.html'
    });
}


function AddSelect(pageParam){
	//var SelectBox="<div onclick='searchByHistory(this)' tapmode='tapped' data='"+JSON.stringify(pageParam)+"' class='hItem'>";
	var attr = "tapmode='tapped' onclick='searchByHistory(this)' data='"+JSON.stringify(pageParam)+"'";
	var SelectBox = "<a tapmode='tapped' onclick='deleteHistoryItem(this.parentNode);' class='fr'><i class='iconfont'>&#xe657;</i></a>"
		+"<a "+attr+"><i class='iconfont'>&#xe640;</i> <h3>";
	if(quyus!=[]){
		SelectBox = SelectBox + '<span> 区域：'+quyus+'</span>';
	}else{
		SelectBox = SelectBox + '<span> 区域：全部</span>';
	}
	if(searchText!=''){
		SelectBox = SelectBox + '<span> 楼盘：'+searchText+'</span>';
	}
	if(dhao!=''){
		SelectBox = SelectBox + '<span> 栋号：'+dhao+'</span>';
	}
	if(fhao!=''){
		SelectBox = SelectBox + '<span> 房号：'+fhao+'</span>';
	}
	if(mobileTel!=''){
		SelectBox = SelectBox + '<span> 电话：'+mobileTel+'</span>';
	}
	if(fangshi!=''){
		var fangshiStr = "";
		fangshiStr= fangshi==1? '整租':'合租';
		SelectBox = SelectBox + '<span> 方式：'+fangshiStr+'</span>';
	}
	if(mjiStart!=''||mjiEnd!=''){
		if(mjiStart==''){
			SelectBox = SelectBox + '<span> 面积：小于'+mjiEnd+'</span>';
		}else if(mjiEnd==''){
			SelectBox = SelectBox + '<span> 面积：大于'+mjiStart+'</span>';
		}else{
			SelectBox = SelectBox + '<span> 面积：'+mjiStart+'-'+mjiEnd+'</span>';
		}
	}
	if(!isChuzu){
		if(djiaStart!=''||djiaEnd!=''){
			if(djiaStart==''){
				SelectBox = SelectBox + '<span> 单价：小于'+djiaEnd+'</span>';
			}else if(djiaEnd==''){
				SelectBox = SelectBox + '<span> 单价：大于'+djiaStart+'</span>';
			}else{
				SelectBox = SelectBox + '<span> 单价：'+djiaStart+'-'+djiaEnd+'</span>';
			}
		}
		if(zjiaStart!=''||zjiaEnd!=''){
			if(zjiaStart==''){
				SelectBox = SelectBox + '<span> 总价：小于'+zjiaEnd+'</span>';
			}else if(zjiaEnd==''){
				SelectBox = SelectBox + '<span> 总价：大于'+zjiaStart+'</span>';
			}else{
				SelectBox = SelectBox + '<span> 总价：'+zjiaStart+'-'+zjiaEnd+'</span>';
			}
		}
	}else{
		if(zjiaStart!=''||zjiaEnd!=''){
			if(zjiaStart==''){
				SelectBox = SelectBox + '<span> 租金：小于'+zjiaEnd+'</span>';
			}else if(zjiaEnd==''){
				SelectBox = SelectBox + '<span> 租金：大于'+zjiaStart+'</span>';
			}else{
				SelectBox = SelectBox + '<span> 租金：'+zjiaStart+'-'+zjiaEnd+'</span>';
			}
		}
	}
	
	if(lcengStart!=''||lcengEnd!=''){
		if(lcengStart==''){
			SelectBox = SelectBox + '<span> 楼层：小于'+lcengEnd+'</span>';
		}else if(lcengEnd==''){
			SelectBox = SelectBox + '<span> 楼层：大于'+lcengStart+'</span>';
		}else{
			SelectBox = SelectBox + '<span> 楼层：'+lcengStart+'-'+lcengEnd+'</span>';
		}
	}
	SelectBox +='</h3></a>';
	$('#searchHistoryWrap').prepend('<li>'+SelectBox+'</li>');
	if(historySearchCount>=10){
//		remove last one
		var xx = $('#searchHistoryWrap');
		xx.children()[xx.children().length-1].remove();
		//return;
	}
	api.parseTapmode();
	
	//save to history
	api.getPrefs({
    	key:searchHistoryKey
    },function(ret,err){
    	var result = SelectBox+historyItemSplitChar;
    	if(ret.value){
    		var arr = ret.value.split(historyItemSplitChar);
			var tmp = SelectBox+historyItemSplitChar;
			for(var i=0;i<arr.length;i++){
				if(i<9){
					tmp=tmp+arr[i]+historyItemSplitChar;
				}
			}
			result = tmp;
    	}
		api.setPrefs({
            key:searchHistoryKey,
            value:result
        });
        historySearchCount++;
    });
}

function searchByHistory(item){
	var data = $(item).attr('data');
	if(!data){
		return;
	}
	var jsonData = JSON.parse(data);
	//填充条件
	api.openWin({
        name: 'searchResult',
	    pageParam:jsonData,
		url: 'searchResult.html'
    });
}

function deleteHistoryItem(item){
	$(item).remove();
//	$(item).fadeOut();
	event.cancelbubble=true;
	event.stopPropagation();
	event.preventDefault();
	var newResult="";
	$('#searchHistoryWrap li').each(function(){
		newResult+=$(this).html()+historyItemSplitChar;
	});
	historySearchCount--
	api.setPrefs({
	    key:searchHistoryKey,
	    value:newResult
    });
}

function keyup(){
	if(event.keyCode==13){
		doSearch();
	}
}