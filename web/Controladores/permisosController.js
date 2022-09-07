function PermisosRender(permiso, cb) {
    $.post('usuarioControlador', {
        accion: "usuario",
        id_permiso: permiso
    }).done((response) => {
        cb(response);
    }).fail((response, jqxhr, error) => {
        toastr.error(error + ", contacte con el desarrollador");
    });
}
