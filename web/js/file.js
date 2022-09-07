$(function () {
    $(".input-file input:file").change(function () {
        $(this).parent().find(".archivo").html($(this).val());
    }).css('border-width', function () {
        if (navigator.appName == "Microsoft Internet Explorer")
            return 0;
    });
});


