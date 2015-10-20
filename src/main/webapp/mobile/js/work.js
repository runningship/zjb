var config;
apiready = function(){
	getConfig(function(cfg){
		config = cfg;
	});
};

function openFav(){
	api.openWin({
	    name: 'fav',
	    url: 'favIndex.html',
	    pageParam: {houseType: 'fav'}
	});
}

function openViewLog(){
	api.openWin({
	    name: 'viewLog',
	    url: 'favIndex.html',
	    pageParam: {houseType: 'viewLog'}
	});
}