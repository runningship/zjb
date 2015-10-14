function buildHtmlWithJsonArray(clazz,json,removeTemplate,remainItems){
    var temp = $('.'+clazz);

    var subCatagory = temp.parent();
    var dhtml = temp[0].outerHTML;
    //var temp = $(first);
    var copy=$(dhtml);
    temp.removeClass(clazz);
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
            $(obj).attr('runscript','false');
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
        dhtml = dhtml.replace("$[rowIndex]",rowIndex);
        // dhtml = dhtml.replace(/\$\[name\]/g,v);
        dhtml = dhtml.replace(new RegExp("\\$\\["+key+"\\]","gm"),v);
    }
    return dhtml;
}

