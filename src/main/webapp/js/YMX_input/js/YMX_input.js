$(document).ready(function() {
    $(document).find('.form-active').find('.input').focusin(function(){
        $(this).parent().addClass('active').addClass('focus');
    }).focusout(function(){
        if($(this).is('select')){
            if($(this).is(":selected")){
                $(this).parent().removeClass('active');
            }
        }else{
            if(!$(this).val()){
                $(this).parent().removeClass('active');
            }
        }
        $(this).parent().removeClass('focus').removeClass('curr');
    }).hover(function() {
        $(this).parent().addClass('hover');
    }, function() {
        $(this).parent().removeClass('hover');
    }).each(function(index, el) {
        if($(this).is('select')){
            $(this).parent().addClass('curr');
        }
    });
});