apiready = function(){
	getConfig(function(cfg){
		config = cfg;
	});
};

function openFav(){
	if(!checkUser()){
		api.openWin({
		    name: 'login',
		    url: 'login.html',
		    pageParam: {forward: 'favIndex.html',houseType: 'fav'}
		});
		return;
	}
	api.openWin({
	    name: 'fav',
	    url: 'favIndex.html',
	    pageParam: {houseType: 'fav'}
	});
}

function openMyHouse(){
	if(!checkUser()){
		api.openWin({
		    name: 'login',
		    url: 'login.html',
		    pageParam: {forward: 'myHouseWin.html'}
		});
		return;
	}
	api.openWin({
	    name: 'myHouse',
	    url: 'myHouseWin.html'
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
	api.openWin({
	    name: 'viewLog',
	    url: 'favIndex.html',
	    pageParam: {houseType: 'viewLog'}
	});
}