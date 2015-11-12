apiready = function(){
	api.parseTapmode();
	getConfig(function(cfg){
		config = cfg;
	});
};

function openFav(){
	if(!checkUser()){
		api.openWin({
		    name: 'login',
		    url: 'login.html',
		    pageParam: {forward: 'favIndex.html',houseType: 'fav' , winName:'fav'}
		});
		return;
	}
	//检查是否付费
//	if(!isUserFuFei(config)){
//		toFuFei();
//		return;
//	}
	api.openWin({
	    name: 'fav',
	    url: 'favIndex.html',
	    delay:100,
	    pageParam: {houseType: 'fav'}
	});
}

function openMyHouse(){
	if(!checkUser()){
		api.openWin({
		    name: 'login',
		    url: 'login.html',
		    pageParam: {forward: 'myHouseWin.html' , winName:'myHouse'}
		});
		return;
	}
	//检查是否付费
//	if(!isUserFuFei(config)){
//		toFuFei();
//		return;
//	}
	api.openWin({
	    name: 'myHouse',
	    url: 'myHouseWin.html?'+new Date().getTime()
	});
}


function openKanFang(){
	if(!checkUser()){
		api.openWin({
		    name: 'login',
		    url: 'login.html',
		    pageParam: {forward: 'kanfang/index.html' , winName:'kanfang'}
		});
		return;
	}
	//检查是否付费
//	if(!isUserFuFei(config)){
//		toFuFei();
//		return;
//	}
	api.openWin({
	    name: 'kanfang',
	    url: 'kanfang/index.html?'+new Date().getTime()
	});
}


function openViewLog(){
	if(!checkUser()){
		api.openWin({
		    name: 'login',
		    url: 'login.html',
		    pageParam: {forward: 'favIndex.html',houseType: 'viewLog'}
		});
		return;
	}
	//检查是否付费
//	if(!isUserFuFei(config)){
//		toFuFei();
//		return;
//	}
	api.openWin({
	    name: 'viewLog',
	    url: 'favIndex.html',
	    pageParam: {houseType: 'viewLog'}
	});
}

function openChat(){
	api.openWin({
	    name: 'chat',
	    url: 'chat/msg.html'
	});
}
function openPics(){
//	api.getPicture({
//	    sourceType: 'album',
//	    encodingType: 'jpg',
//	    mediaValue: 'pic',
//	    destinationType: 'url',
//	    allowEdit: true,
//	    quality: 50,
//	    targetWidth:100,
//	    targetHeight:100,
//	    saveToPhotoAlbum: false
//	}, function(ret, err){ 
//	    blockAlert(JSON.stringify(ret));
//	});
	
	var obj = api.require('UIMediaScanner');
	obj.open({
	    type:'picture',
	    column: 4,
	    classify: true,
	    max: 4,
	    sort: {
	        key: 'time',
	        order: 'desc'
	    },
	    texts: {
	        stateText: '已选择*项',
	        cancelText: '取消',
	        finishText: '完成'
	    },
	    styles: {
	        bg: '#fff',
	        mark: {
	            icon: '',
	            position: 'bottom_left',
	            size: 20
	        },
	        nav: {
	            bg: '#eee',
	            stateColor: '#000',
	            stateSize: 18,
	            cancelBg: 'rgba(0,0,0,0)',
	            cancelColor: '#000',
	            cancelSize: 18,
	            finishBg: 'rgba(0,0,0,0)',
	            finishColor: '#000',
	            finishSize: 18
	        }
	    }
	}, function(ret){
	    if(ret){
	        var arr = "";
	        for(var i=0;i<ret.list.length;i++){
	        	arr+=ret.list[i].path+";"
	        }
//	        blockAlert(JSON.stringify(ret.list));
	        YW.ajax({
	    		url:'http://'+server_host+'/c/mobile/houseImage/setPrivateImage',
	    		method:'post',
	    		data:{
	            	values: {hid:12456,chuzu:0,path:arr}
	        	},
	    		cache:false,
	    		returnAll:false
	    	},function(ret , err){
	    		if(ret && ret.result==0){
	    			alert('设置图片成功');
	    		}else{
	    			alert(ret.msg);
	    		}
	    	});
	        
	        var obj = api.require('imageBrowser');
	        obj.openImages({
	            imageUrls: arr,
	            showList:true,
	            activeIndex:2
	        });
	    }
	});
}