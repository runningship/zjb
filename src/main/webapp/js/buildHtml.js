function buildHtmlWithJsonArray(id,json,removeTemplate,remainItems){
    var temp = $('.'+id);

    var subCatagory = temp.parent();
    var dhtml = temp[0].outerHTML;
    //var temp = $(first);
    var copy=$(dhtml);
    temp.removeClass(id);
    temp.remove();
    if(!remainItems){
        $(subCatagory).empty();
    }
    for(var i=0;i<json.length;i++){
        //temp[0]表示dom元素
        var html = buildHtmlWithJson(temp[0],json[i] ,i);
        subCatagory.append(html);
    }

    var shows = subCatagory.find('[show]');
    shows.each(function(index,obj){
        // if(index>0){
            var script = $(obj).attr('show');
            try{
                if(eval(script)){
                    $(obj).css('display','');
                }else{
                    // $(obj).css('display','none');
                    $(obj).remove();
                }
            }catch(e){

            }
        // }
    });

    var runscripts = subCatagory.find('[runscript=true]');
    runscripts.each(function(index,obj){
        // if(index>0){
            var val="";
            try{
                val = eval(obj.textContent);
                if(obj.tagName=='INPUT'){
                    obj.value = val;        
                }else{
                    // obj.textContent = val;  
                    obj.innerHTML = val;  
                }
            }catch(e){
                console.log(e);
                console.log(obj.textContent);
                obj.textContent = "";
            }
        // }
    });

    if(!removeTemplate){
        copy.css('display','none');
        subCatagory.prepend(copy);
    }
}
function buildHtmlWithJson(temp,json , rowIndex){
    temp.style.display='';
    var dhtml = temp.outerHTML;
    for(var key in json){
        var v = json[key];
        if(v==null){
            v="";
        }
        dhtml = dhtml.replace("${rowIndex}",rowIndex);
        dhtml = dhtml.replace(new RegExp("\\${"+key+"}","gm"),v);
    }
    return dhtml;
}

function getEnumTextByCode(enumArr,code){
    if(code==null){
        return "";
    }
    for(var i=0;i<enumArr.length;i++){
        if(enumArr[i]['code']==code){
            return enumArr[i]['name'];
        }
    }
}

//获取url里需要的值
function getParam(name){
var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");
return (reg.test(location.search))? encodeURIComponent(decodeURIComponent(RegExp.$2.replace(/\+/g, " "))) : '';
}

window.blockAlert = window.alert;
window.alert = function(data){
    art.dialog.tips(data);
}

YW={
    options:{
        beforeSend: function(XMLHttpRequest){
              $(document.body).append('<img src="/style/images/ajax-loading.gif" style="display:block;margin-left:auto;margin-right:auto;" id="loading" />');
        },
        complete: function(XMLHttpRequest, textStatus){
          $('#loading').remove();
        },
        error: function(data){
            if(data.status==500){
                alert('操作不成功，请联系管理员.');
            }else if(data.status==400){
                json = JSON.parse(data.responseText);
                if(json.type=='ParameterMissingError'){
                    var field = json.field;
                    var arr = $('[name="'+field+'"]');
                    var desc;
                    if(arr!=null && arr.length>0){
                        desc = $(arr[0]).attr('desc');
                    }
                    if(desc==undefined){
                        desc = field;
                    }
                    $(arr[0]).focus();
                    alert("请先填写 "+ desc);

                }else if(json.type=='ParameterTypeError'){
                    var field = json.field;
                    var arr = $('[name="'+field+'"]');
                    var desc;
                    if(arr!=null && arr.length>0){
                        desc = $(arr[0]).attr('desc');
                    }
                    if(desc==undefined){
                        desc = field;
                    }
                    $(arr[0]).focus();
                    alert(desc+json.msg);
                }else{
                    alert(json['msg']);   
                }
                
            }else if(data.status!=0){
//            	alert(data.status);
                alert('请求服务失败，请稍后重试');
            }
        },
        success:function(data){
        	if(data.responseText!=undefined && data.responseText.indexOf('relogin')!=-1){
        		window.parent.location='/login/index.html';
        	}
        }
    },
    ajax:function(options){
         if(options.beforeSend==undefined){
             options.beforeSend = YW.options.beforeSend;
         }
         if(options.complete==undefined){
             options.complete = YW.options.complete;
         }
        if(options.error==undefined){
            options.error = YW.options.error;
        }
        
        if(options.mysuccess!=undefined){
            options.success = function(data){
            	YW.options.success(data);
            	options.mysuccess(data);
            };
        }
        $.ajax(options);
    }
}

function fillData(data){
  $('.form-control').each(function(index,obj){
    $(obj).val(data[$(obj).attr('name')]);
  });
}

//读取
function loadConfigAsJSON(){

    var fs=null;
    try{
        fs=require("fs");    
    }catch(e){
        return JSON.parse("{}");
    }
    
    if(!fs.existsSync("config.data")){
        fs.writeFileSync("config.data", "{}", 'utf8')
    }
    var data=fs.readFileSync("config.data","utf-8");
    var json = null;
    try{
        json = JSON.parse(data);
    }catch(e){
        console.error("load config.data faild,"+e);
        json = JSON.parse("{}");
    }
    return json;
}
//写入
function writeConfig(json){
    var fs = require("fs");
    fs.writeFileSync("config.data", JSON.stringify(json), 'utf8')
}
//写入
function putConfig(key, val){
    var json = loadConfigAsJSON();
    json[key] = val;
    writeConfig(json);
}

//添加
function appendConfig(key, val){
    var json = loadConfigAsJSON();
    var arr = json[key];
    if(arr==undefined){
        arr=[];
        json[key]=arr;
    }
    if(arr.indexOf(val)==-1){
        arr.push(val);
    }else{
        arr.splice(arr.indexOf(val),1)
        arr.push(val);
    }

    writeConfig(json);
}

function readFile(file){
    var fs=require("fs");
    return fs.readFileSync(file,"utf-8");
}