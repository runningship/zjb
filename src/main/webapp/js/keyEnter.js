



  $(document).keypress(function(e) { 
      // 回车键事件 
        
         if(e.which == 13) {

            e.preventDefault();
              // jQuery(".confirmButton").click(); 
         }
 });


$(function(){
    $("#tel").bind('keypress',function(event){ 
        addTel(event);
    });
	
	$("#areas").bind('keypress',function(event){ 
        addHouse(event);
    });
});

function addTel(event){
         if(event.keyCode == "13")    
            {
         
                var spanNum = $("#TelBoxMore div").size();
                var telNum = $.trim($("#tel").val());
                var telall = $.trim($("#TelAll").val());
                var text = "<div style=' width:105px; border-radius:8px; float:left; background-color: #008765; padding:3px 0 4px 0; color:#fff; margin:0px 2px 0 0; position:relative;'><i style=' display:block; height:14px; line-height:13px; border-radius:5px; font-size:16px; color:#008765; background-color: #fff; padding:0px 0px; right:2px; top:4px; font-style:normal; cursor:pointer;  position:absolute;'>×</i><span style='width:80px; padding-left:5px;'>"+telNum+"</span></div>";
                
                if(spanNum <= 2){
                  if(checkTel()){
                    
                    $("#TelBoxMore").append(text);
                      if(spanNum>0 && spanNum <= 2){
                      telall += "/"+ telNum ;
                      }else{
                      telall += telNum;
                      }
                      $("#TelAll").val(telall);
                    $("#tel").val("");
                    
                    
                      var pleft = ($("#TelBoxMore div").width() + 2 ) * (spanNum + 1);
                      var wInput = 315 - pleft;
              
                      $("#tel").css({
                        "padding-left":pleft + 5,
                        "width":wInput
                      });
                    
                    }   
                  
                }else{
                $("#tel").val("");
                alert("最多只能填写3个号码！");
                }
            }     
        
        
           $("#TelBoxMore div i").click(function(){
			  
              var telText = "";
              $("#TelAll").val("");
              $(this).parent("div").remove();
			  
              var divNumChange = $("#TelBoxMore div").size();
			  
              $("#TelBoxMore div").each(function(i) {
				  if(i == (divNumChange-1)){
                     telText += $(this).find("span").text();
				  }else{
					 telText += $(this).find("span").text() + "/";  
				  }
              });
              $("#TelAll").val(telText);
   
              var pleftChange = ($("#TelBoxMore div").width() + 2)*divNumChange;
              var wInputChange = 315 - pleftChange;
      
             $("#tel").css({
              "padding-left":pleftChange + 5,
              "width":wInputChange
             });
              $("#tel").css("display","block");
              $("#tel").focus();
          });  
}




function addHouse(event){
	if(event.keyCode == "13")   {
		
      houseAddMain();

	}

  $("#HouseBoxMore div i").click(function(){
              
      var telText = "";
      $("#HouseAreaAll").val("");
      $(this).parent("div").remove();
      $("#HouseBoxMore div").each(function() {
         telText += $(this).find("span").text() + "/";
      });
      $("#HouseAreaAll").val(telText);

  });

}


function houseAddMain(){

        var divNum = $("#HouseBoxMore div").size();
        var houseNum = $.trim($("#areas").val());
        var houseall = $.trim($("#HouseAreaAll").val());
        var text = "<div style='border-radius:8px; float:left; background-color: #008765; padding:3px 18px 4px 0; color:#fff; margin:0px 2px 0 0; position:relative;'><i style=' display:block; width:13px; height:15px; line-height:13px; border-radius:5px; font-size:16px; color:#008765; background-color: #fff; right:3px; top:4px; font-style:normal; cursor:pointer;  position:absolute;'>×</i><span style='width:80px; padding-left:5px;'>"+houseNum+"</span></div>";

        if(divNum <= 2){

              $("#HouseBoxMore").append(text);
              if(divNum>0 && divNum <= 2){
                houseall += "/"+ houseNum;
              }else{
                houseall += houseNum;
              }
              $("#HouseAreaAll").val(houseall);
              $("#areas").val("");
        
        }else{
              $("#areas").val("");
              alert("最多只能填写3个楼盘！");
        }

}




