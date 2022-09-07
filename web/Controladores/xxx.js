$(document).ready(function () {
    var ref = window.location.href;
    var ruta = ref.substring(ref.indexOf("#") + 2);
    switch (ruta) {
        case "articulos":
            items();
            break;
        case "clientes":
            cargarCliente();
            break;
        case "compras":
            compras();
            break;
        case "ordencompras":
            ordenCompra();
            break;
        case "ciudades":
            ciudades();
            break;
        case "departamentos":
            departamentos();
            break;
        case "proveedores":
            proveedores();
            break;
        case "stock":
            verStock();
            break;
        case "ventas":
            verificarConfiguraciones();
            ventas();
            break;
        case "anulados/articulos":
            listarArticulosInhabilitados();
            break;
        case "anulados/clientes":
            listarClientesInhabilitados();
            break;
        case "anulados/compras":
            listarComprasInhabilitadas();
            break;
        case "anulados/proveedores":
            listarProveedoresInhabilitados();
            break;
        case "anulados/ventas":
            listarVentasInhabilitadas();
            break;
        case "caja":
            caja();
            break;
        case "unidades":
            unidades();
            break;
        case "marcas":
            marcas();
            break;
        case "etiquetas":
            etiquetas();
            break;
        case "sucursales":
            sucursales();
            break;
        case "auditoria":
            auditoria();
            break;
        case "usuarios":
            usuarios();
            break;
        case "nfactura":
            factura();
            break;
        case "timbrado":
            timbrado();
            break;
        default :
            CargarInicio();
    }

});