var row = '<tr class="add">' +
        '<td><input type="text" data-row="u" class="form-control chan cod"></td>' +
        '<td><input type="text" value="1" class="form-control cant"></td>' +
        '<td><input type="text" data-row="u" class="form-control chan nom"></td>' +
        '<td><input type="text" class="form-control price miles"></td>' +
        '<td><select class="form-control iva" name="iva" required>' +
        '<option value="10">10%</option>' +
        '<option value="5">5%</option>' +
        '<option value="E">Exenta</option>' +
        '</select></td>' +
        '<td><input type="text" class="form-control total" value="0" readonly></td>' +
        '<td><button class="btn btn-danger hide"><i class="fa fa-trash"></i></button></td>' +
        '</tr>';
var marca_form = '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="nombre">Marca: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="mar" name="nombre" required>' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="desc">Descripción: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="desc" name="desc">' +
        '</div>' +
        '</div>';
var unidad_form = '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="eti">Unidad: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="eti" name="etiqueta" required>' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="abr">Abreviatura: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="abr" name="abreviacion">' +
        '</div>' +
        '</div>';
var etiqueta_form = '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="eti">Etiqueta: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="eti" name="etiqueta" required>' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="abr">Abreviación: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="abr" name="abreviacion">' +
        '</div>' +
        '</div>';
var proveedor_form = '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="nombre">Nombre: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="nombre" name="nombre" required>' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="repre">Representante Legal: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="repre" name="repre">' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="doc">N° de Documento: </label>' +
        '<div class="col-sm-6">' +
        '<input type="text" class="form-control num doc" id="doc" name="doc" required>' +
        '</div>' +
        '<div class="col-sm-2">' +
        '<input type="text" class="form-control dv" id="dv" name="dv" required>' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="dir">Dirección: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="dir" name="dir">' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="tel">Teléfono: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="tel" name="tel">' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="cel">Celular: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="cel" name="cel">' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="correo">Correo: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="correo" name="correo">' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="mar">Marcas: </label>' +
        '<div class="col-sm-8" id="marcas">' +
        '<select class="form-control" id="mar" data-width="100%" multiple="multiple"></select>' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="desc">Observación: </label>' +
        '<div class="col-sm-8">' +
        '<input type="text" class="form-control" id="obs" name="obs">' +
        '</div>' +
        '</div>' +
        '<div class="form-group">' +
        '<label class="control-label col-sm-3" for="ciudad">Ciudad: </label>' +
        '<div class="col-sm-8 parent">' +
        '<select class="form-control select2" data-width="100%" id="ciudad" required>' +
        '<option></option>' +
        '</select>' +
        '</div>' +
        '</div>';
var row_venta = '<tr class="add">' +
        '<td><input type="text" data-row="u" class="form-control chan cod"></td>' +
        '<td><input type="text" value="1" class="form-control cant"></td>' +
        '<td><input type="text" data-row="u" class="form-control chan nom"></td>' +
        '<td><input type="text" class="form-control price_ar" readonly></td>' +
        '<td><input type="text" class="form-control iva" readonly></td>' +
        '<td><input type="text" class="form-control total_ar" value="0" readonly></td>' +
        '<td><button class="btn btn-danger hide"><i class="fa fa-trash"></i></button></td>' +
        '</tr>';