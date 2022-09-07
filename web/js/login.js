/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function enviarDatos() {
    var contra = calcMD5($("#contraseña").val());//escripta la contraseña
    var parametros = {
        "nombre_usu": $('#nombre').val(),
        "contraseña_usu": contra
                //"contraseña_usu" : $('#contraseña').val()
    };
    $.ajax({
        data: parametros,
        url: 'ingresar',
        type: 'post',
        success: function (response) {
            if (response) {
                //alert(response);
                location.href = "index.jsp";
            } else {
                $('#mensaje').html('<p class="text-danger">Usuario o contraseña incorrecto.</p>');
            }
        }
    });
}
;

