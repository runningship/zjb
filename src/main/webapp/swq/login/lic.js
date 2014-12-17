window.alert = function(data){
    art.dialog.tips(data);
}
window.infoAlert = function(data){
    art.dialog({
        id:'tips',
        title:'提示',
        content:data,
        focus:true,
        padding:15
    });
}

var fileName = 'C:\\Windows\\System32\\swq.lic';
//var fileName = 'C:\\zjb.lic';
function readLic(){
    var fs=require("fs");
    if(fs.existsSync(fileName)){
        var data=fs.readFileSync(fileName,"utf-8");
        var createTime = getFileCreateTime(fileName);
        var json = JSON.parse('{}');
        json.licCreateTime=createTime;
        json.lic = data;
        return json;
    }else{
        //重新授权
        return undefined;
    }
}

function createLic(){
    var fs=require("fs");
    fs.writeFileSync(fileName, "", 'utf8');
    return getFileCreateTime(fileName);
}
function setLic(lic){
    var fs=require("fs");
    fs.writeFileSync(fileName, lic, 'utf8');
}
function getFileCreateTime(file){
    var fs=require("fs");
    var stat = fs.statSync(file);
    var xx = stat.atime.getTime(); 
    return Math.round(xx/1000)*1000;
}