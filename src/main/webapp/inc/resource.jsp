<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
function loadJs(url ,callback){
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = url;
    var timer = setTimeout(function(){
    	console.log(url+" connection fail ");
    	window.location.reload();
    } , 3000);
    script.onload = function(){
    	//console.log(url+" readyState="+script.readyState);
        //if(script.readyState  && script.readyState != 'loaded' && script.readyState != 'complete') return ;  
        //script.onreadystatechange = script.onload = null;
        clearTimeout(timer);
        if(callback){
        	callback();
        }
    }  
    document.head.appendChild(script);  
}

function loadCss(url){  
    var css = document.createElement('link');
    css.rel = 'stylesheet';
    css.href = url;
    var timer = setTimeout(function(){
    	console.log(url+" connection fail ");
    	window.location.reload();
    } , 3000);
    css.onload = css.onreadystatechange = function(){
    	//console.log(url+" readyState="+css.readyState);
        //if(script.readyState  && script.readyState != 'loaded' && script.readyState != 'complete') return ;  
        //script.onreadystatechange = script.onload = null;
    	clearTimeout(timer);
    }  
    document.head.appendChild(css);  
}
</script>
<%
	//request.setAttribute("refPrefix", "file:///resources");
	request.setAttribute("useLocalResource", 0);
	request.setAttribute("refPrefix", "");
%>
