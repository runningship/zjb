var fs = null;
var fsPrefix='';
var urlPrefix='';
var fileCount=0;
var downloadProcess=0;

//apiready = function(){
//	
//};

function ready(){
	api.removeLaunchView();
	try{
		updateIfNeed();
	}catch(e){
		alert('连接服务器失败!');
		api.closeWidget({
             id: 'A6989896004356',     //这里改成自己的应用ID
             retData: {name:'closeWidget'},
             silent:true
         });
	}
}
function updateIfNeed(){
	fsPrefix=api.fsDir;
	//alert(JSON.stringify(api.wgtParam));
	fs = api.require('fs');
	//下载最新文件
	api.ajax({
	    url: urlPrefix+'/version.jsp',
	    method: 'get',
	    timeout: 30,
	    cache:false,
	},function(ret,err){
	    if (ret) {
	        var serverVersion = ret;
	        fileCount = serverVersion.files.length;
	        statusBarHeight = serverVersion.statusBarHeight;
	        //加载本地版本信息
	        api.getPrefs({
                key:'version'
            },function(data,err){
            	if(data.value && ret.version!='debug' && data.value==ret.version){
            		openIndexFrame();
            		return;
            	}else{
            		$('body').css('background' , '#000');
            		//alert(loop);
//            		setInterval(loop, 16);
            		loadstart();
            		loadtext('系统启动中...');
            		update(serverVersion , 0);
			        var xx = setInterval(function(){
						var percent = downloadProcess/fileCount;
						var p = Math.round(percent*100);
						if(p<=100){
//							$('#percent').text(p+'%');
							loading(p);
						}
			        	if(downloadProcess>fileCount){
			        		api.setPrefs({
                                key:'version',
                                value:ret.version
                            });
			        		openIndexFrame();
			        		clearInterval(xx);
			        	}
			        },100);
            	}
            });
	    }else {
	    	alert('网络连接失败');
	        //下载版本文件失败，获取版本信息失败,应用启动失败
	        if(err.statusCode==404){
	        	alert(404);
	        }else{
	        	alert('网络连接失败');
	        }
	        
	    }
	});
}


function update(serverVersion , index){
	
	downloadProcess++;
	if(index>=serverVersion.files.length){
	return;
	}
	var filename = serverVersion.files[index];
	fs.getAttribute({
	path: fsPrefix+filename
	},function(ret,err){
		//alert(fsPrefix+filename+','+JSON.stringify(err));
	if (ret.status) {
	    if(ret.attribute.size!=serverVersion[filename]){
	    	//alert(filename+','+ret.attribute.size);
	    	download(filename , function(){
				 update(serverVersion , index+1);
	        });
	    }else{
	    	update(serverVersion , index+1);
	    }
	}else{
	    //下载
	    download(filename , function(){
			 update(serverVersion , index+1);
	    });
	}
	});
}

function download(path , callback){
var url = urlPrefix+path;
//alert('serverUrl: '+url +", savePaht: "+fsPrefix+path);
api.download({
    url: url, 
    savePath: fsPrefix+path,
    report: false,
    cache: false,
    allowResume:false
},function(ret,err){
	//不成功就不callback了吗?
	//alert('download:'+JSON.stringify(ret));
//	callback();
    if (ret) {
        if( ret.state==1 && callback){
        	callback();
        }else if(ret.statusCode==404){
        	alert(url+' not found');
        }else if(ret.state==2){
        	alert('更新失败,'+path);
        }
    } else{
        alert('网络连接失败'+err.msg);
    }
});
}


//打开欢迎页面
function openIndexFrame(){
	window.location='file://'+fsPrefix+'/html/welcome.html';
	//window.location='file://'+fsPrefix+'/html/house.html';
}

function loadstart(){
    $('.loadbox').animate({'opacity':'1'},1000);
}
function loading(b){
var loadbox=$('.loadbox'),
    loadNum=b,
    loadB=loadbox.find('.loadb'),
    loadBn=loadB.find('span');
    if(loadNum<0&&loadNum>100){
        loadNum=0;
    }
    loadBn.text(loadNum+'%');
    loadB.css('width', loadNum+'%');
}
function loadtext(t){
    if(!t){t='启动中...'}
    $('.loadbox .loadt').text(t);
}