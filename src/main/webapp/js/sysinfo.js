
function loadHardwareInfo(callback){
    try{
        var lic = getLicence();
        if(lic.data!=undefined && lic.data!=""){
            callback(lic);
        }else{
            var exec = require('child_process').execFile;
            var command = "hinf.exe";
            var xx = exec(command, function(err, stdout, stderr) {
                var fs=require("fs");
                var result = fs.readFileSync("sys.data","utf-8");
                result= result.trim();
                result = result.replace(/\r/g,"");
                var lines = result.split("\n");
                var json = JSON.parse("{}");
                for(var index=0;index<lines.length;index++){
                    var line = lines[index];
                    var pair = line.split("=");
                    if(pair.length>1){
                        json[pair[0]]=pair[1];
                    }
                }
                fs.unlink('sys.data', function (err) {
                  console.log('successfully deleted sys.data');
                });
                callback(json);
            });
        }
        
    }catch(e){
        blockAlert(e);
    }
}

function getLicence(){
    var lic  = JSON.parse("{}");
    try{
        var fs = require("fs");
        if(!fs.existsSync("zjb.lic")){
            //中介宝目录下不存在
            if(!fs.existsSync("c:\\zjb.lic")){
                //系统目录下也不存在,第一次使用新授权方式
                fs.writeFileSync("zjb.lic", "", 'utf8');
                fs.writeFileSync("c:\\zjb.lic", "", 'utf8');
            }else{
                //系统目录下存在,软件可能被重新安装
                 //copy to zjb dir
                var tmp = fs.readFileSync("c:\\zjb.lic", 'utf8');
                fs.writeFileSync("zjb.lic", tmp, 'utf8');
            }
        }else{
            //中介宝目录下存在
            if(!fs.existsSync("c:\\zjb.lic")){
                //系统目录下不存在,系统可能被重装
                //copy to c:
                var tmp = fs.readFileSync('zjb.lic',"utf-8");
                fs.writeFileSync("c:\\zjb.lic", lic.data, 'utf8');
            }
        }
        lic.data = fs.readFileSync('c:\\zjb.lic',"utf-8");
        var fst = fs.statSync('c:\\zjb.lic');
        lic.ctime = fst.ctime.getTime();
    }catch(e){
        blockAlert('获取授权信息失败');
    }
    return lic;
}