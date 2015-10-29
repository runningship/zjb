var fs = null;
var fsPrefix='';
var urlPrefix='http://192.168.1.222:8081/mobile';
var fileCount=0;
var downloadProcess=0;
function updateIfNeed(){
	fsPrefix=api.fsDir;
	//bindTecentPush();
//	bindAjpush();
//	api.addEventListener({
//	    name:'appintent'
//	},function(ret,err){
//		alert(ret);
//		var ajpush = ret.appParam.ajpush;
//		alert(JSON.stringify(ajpush));
//		var appParam = ret.appParam;
//	});
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
            		//alert(loop);
            		setInterval(loop, 16);
            		update(serverVersion , 0);
			        var xx = setInterval(function(){
						var percent = downloadProcess/fileCount;
						var p = Math.round(percent*100);
						if(p<=100){
							$('#percent').text(p+'%');
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

function bindAjpush(){
	try{
	var ajpush = api.require('ajpush');
	var param = {alias: '15856985122'};
	ajpush.bindAliasAndTags(param,function(ret) {
	     alert('statuscode = '+ret.statusCode+'绑定到'+param.alias);
	});

	ajpush.setListener(function(ret) {
		alert(ret.content);
    });
	}catch(e){
		alert(e);
	}
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
	window.location='file://'+fsPrefix+'/html/welcome.html?'+new Date().getTime();
//api.openFrame({
//    name: 'rootFrame',
//    url: fsPrefix+'/html/start.html',
//    bounces: false,
//    reload:true,
//    bgColor: '#fff',
//    rect: {
//        x: 0,
//        y: statusBarHeight,
//        w: 'auto',
//        h: 'auto'
//    }
//});
}