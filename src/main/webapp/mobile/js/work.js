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
	api.openWin({
	    name: 'viewLog',
	    url: 'favIndex.html',
	    pageParam: {houseType: 'viewLog'}
	});
}