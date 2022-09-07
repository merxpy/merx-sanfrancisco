define([
    'empresaControlador',
    'articulosControlador',
    'perfilControlador',
    'usuariosControlador',
    'comprasControlador',
    'permisosControlador',
    'inicioControlador',
    'cajaControlador',
    'reportesControlador',
    'mermasControlador',
    'bajastockControlador',
    'trabajoControlador',
    'categoriasControlador',
    'sucursalesControlador',
    'gastosControlador',
    'cuentasControlador',
    'deliveryControlador'
], function (emp, ar, perfil, usuario, compra, permiso, inicio, caja, reporte, merma, baja, orden, categoria, sucursales, gasto, cuenta, delivery) {
    var exports = {};
    exports.Rutas = function (h) {
        var l = h.substring(h.indexOf("#"));
        var id = 0;
        if (l.indexOf("?") !== -1) {
            id = l.substring(l.indexOf("=") + 1);
            l = l.substring(0, l.indexOf("?"));
        } else {
            id = 0;
        }
        switch (l) {
            case "#/articulos":
                permiso.VerificarPermisos(1, (p) => {
                    if (p.ver === 'Y') {
                        items();
                    }
                });
                break;
            case "#/articulos/nuevo":
                spinneron();
                permiso.VerificarPermisos(1, (p) => {
                    if (p.agregar === 'Y') {
                        nuevoArticulo();
                    }
                });
                break;
            case "#/articulos/editar":
                spinneron();
                permiso.VerificarPermisos(1, (p) => {
                    if (p.editar === 'Y') {
                        mostrarFormEditar(id);
                    }
                });
                break;
            case "#/clientes":
                permiso.VerificarPermisos(2, (p) => {
                    if (p.ver === 'Y') {
                        cargarCliente();
                    }
                });
                break;
            case "#/clientes/historial":
                permiso.VerificarPermisos(2, (p) => {
                    if (p.ver === 'Y') {
                        Historial(id);
                    }
                });
                break;
            case "#/clientes/nuevo":
                permiso.VerificarPermisos(2, (p) => {
                    if (p.agregar === 'Y') {
                        nuevoCliente();
                    }
                });
                break;
            case "#/clientes/editar":
                permiso.VerificarPermisos(2, (p) => {
                    if (p.editar === 'Y') {
                        editarClientes(id);
                    }
                });
                break;
            case "#/compras":
                $(".active").removeClass("active");
                $('a[href="#/compras"]').parent().addClass("active");
                permiso.VerificarPermisos(4, (p) => {
                    if (p.ver === 'Y') {
                        compra.Compras('COMPRA');
                    }
                });
                break;
            case "#/compras/nuevo":
                permiso.VerificarPermisos(4, (p) => {
                    if (p.agregar === 'Y') {
                        compra.BuscarCompra(id, 'COMPRA');
                    }
                });
                break;
            case "#/compras/editar":
                permiso.VerificarPermisos(4, (p) => {
                    if (p.editar === 'Y') {
                        compra.BuscarCompra(id, 'COMPRA');
                    }
                });
                break;
            case "#/delivery/retiros":
                delivery.Delivery("PENDIENTE");
                break;
            case "#/delivery/retiros/agregar":
                delivery.BuscarDelivery(0);
                break;
            case "#/delivery/retiros/editar":
                delivery.BuscarDelivery(id);
                break;
            case "#/delivery/retiros/generar":
                orden.BuscarOrdenes(0, id);
                break;
            case "#/delivery/envios":
                delivery.Delivery("ENVIO");
                break;
            case "#/delivery/compartir":
                delivery.CompartirDelivery(id);
                break;
            case "#/proveedores":
                permiso.VerificarPermisos(3, (p) => {
                    if (p.ver === 'Y') {
                        proveedores();
                    }
                });
                break;
            case "#/proveedores/nuevo":
                permiso.VerificarPermisos(3, (p) => {
                    if (p.agregar === 'Y') {
                        nuevoProveedor();
                    }
                });
                break;
            case "#/proveedores/editar":
                permiso.VerificarPermisos(3, (p) => {
                    if (p.editar === 'Y') {
                        editarProveedores(id);
                    }
                });
                break;
            case "#/cajas":
                permiso.VerificarPermisos(15, (p) => {
                    if (p.ver === 'Y') {
                        caja.HistorialdeCaja();
                    }
                });
                break;
            case "#/caja":
                permiso.VerificarPermisos(15, (p) => {
                    if (p.ver === 'Y') {
                        caja.Caja(id);
                    }
                });
                break;
            case "#/caja/abrir":
                permiso.VerificarPermisos(15, (p) => {
                    if (p.ver === 'Y') {
                        caja.CambioInicial();
                    }
                });
                break;
            case "#/caja/cerrar":
                permiso.VerificarPermisos(15, (p) => {
                    if (p.ver === 'Y') {
                        caja.AccionesCaja("insertar");
                    }
                });
                break;
            case "#/inventario":
                permiso.VerificarPermisos(6, (p) => {
                    if (p.ver === 'Y') {
                        verStock();
                    }
                });
                break;
            case "#/ventas":
                permiso.VerificarPermisos(5, (p) => {
                    if (p.ver === 'Y') {
                        emp.VerificarTimbrado((rpta) => {
                            if (rpta === 0) {
                                ventas();
                            } else {
                                window.location.href = "#/sistema/empresa";
                            }
                        });
                    }
                });
                break;
            case "#/ventas/nuevo":
                permiso.VerificarPermisos(5, (p) => {
                    if (p.agregar === 'Y') {
                        emp.VerificarTimbrado((rpta) => {
                            if (rpta === 0) {
                                nuevaVenta();
                            } else {
                                window.location.href = "#/sistema/empresa";
                            }
                        });
                    }
                });
                break;
            case "#/cuentas":
                $(".active").removeClass("active");
                $('a[href="#/cuentas"]').parent().addClass("active");
                cuenta.Cuentas();
                break;
            case "#/cuentas/cobrar":
                $(".active").removeClass("active");
                $('a[href="#/cuentas"]').parent().addClass("active");
                cuenta.CobrarCuentas(id);
                break;
            case "#/sistema/empresa":
                permiso.VerificarPermisos(13, (p) => {
                    if (p.agregar === 'Y') {
                        emp.Empresa();
                    }
                });
                break;
            case "#/anulados/articulos":
                permiso.VerificarPermisos(1, (p) => {
                    if (p.ver === 'Y') {
                        ar.ListarArticulosInhabilitados();
                    }
                });
                break;
            case "#/anulados/articulos/ver":
                permiso.VerificarPermisos(1, (p) => {
                    if (p.ver === 'Y') {
                        ar.VerArticulos(id);
                    }
                });
                break;
            case "#/anulados/articulos/eliminar":
                permiso.VerificarPermisos(1, (p) => {
                    if (p.eliminar === 'Y') {
                        ar.EliminarArticulos(id);
                    }
                });
                break;
            case "#/anulados/articulos/habilitar":
                permiso.VerificarPermisos(1, (p) => {
                    if (p.aprobar === 'Y') {
                        ar.HabilitarArticulos(id);
                    }
                });
                break;
            case "#/anulados/clientes":
                permiso.VerificarPermisos(2, (p) => {
                    if (p.ver === 'Y') {
                        listarClientesInhabilitados();
                    }
                });
                break;
            case "#/anulados/compras":
                permiso.VerificarPermisos(4, (p) => {
                    if (p.ver === 'Y') {
                        compra.ListarComprasAnuladas('COMPRA');
                    }
                });
                break;
            case "#/anulados/proveedores":
                permiso.VerificarPermisos(3, (p) => {
                    if (p.ver === 'Y') {
                        listarProveedoresInhabilitados();
                    }
                });
                break;
            case "#/anulados/ventas":
                permiso.VerificarPermisos(5, (p) => {
                    if (p.ver === 'Y') {
                        listarVentasInhabilitadas();
                    }
                });

                break;
            case "#/utilidades/etiquetas":
                permiso.VerificarPermisos(9, (p) => {
                    if (p.ver === 'Y') {
                        etiquetas();
                    }
                });
                break;
            case "#/utilidades/etiquetas/nuevo":
                permiso.VerificarPermisos(9, (p) => {
                    if (p.agregar === 'Y') {
                        nuevaEtiqueta();
                    }
                });
                break;
            case "#/utilidades/etiquetas/editar":
                permiso.VerificarPermisos(9, (p) => {
                    if (p.editar === 'Y') {
                        editarEtiquetas(id);
                    }
                });
                break;
            case "#/utilidades/marcas":
                permiso.VerificarPermisos(10, (p) => {
                    if (p.ver === 'Y') {
                        marcas();
                    }
                });
                break;
            case "#/utilidades/marcas/agregar":
                permiso.VerificarPermisos(10, (p) => {
                    if (p.agregar === 'Y') {
                        NuevaMarca();
                    }
                });

                break;
            case "#/utilidades/marcas/editar":
                permiso.VerificarPermisos(10, (p) => {
                    if (p.editar === 'Y') {
                        editarMarcas(id);
                    }
                });
                break;
            case "#/utilidades/unidades":
                permiso.VerificarPermisos(11, (p) => {
                    if (p.ver === 'Y') {
                        unidades();
                    }
                });
                break;
            case "#/utilidades/unidades/nuevo":
                permiso.VerificarPermisos(11, (p) => {
                    if (p.agregar === 'Y') {
                        nuevaUnidad();
                    }
                });
                break;
            case "#/utilidades/unidades/editar":
                permiso.VerificarPermisos(11, (p) => {
                    if (p.agregar === 'Y') {
                        editarUnidad(id);
                    }
                });
                break;
            case "#/sistema/auditoria":
                permiso.VerificarPermisos(12, (p) => {
                    if (p.ver === 'Y') {
                        auditoria();
                    }
                });
                break;
            case "#/sistema/usuarios":
                permiso.VerificarPermisos(14, (p) => {
                    if (p.ver === 'Y') {
                        usuario.Usuarios();
                    }
                });

                break;
            case "#/sistema/usuarios/nuevo":
                permiso.VerificarPermisos(14, (p) => {
                    if (p.agregar === 'Y') {
                        usuario.BuscarUsuario(0);
                    }
                });
                break;
            case "#/sistema/usuarios/editar":
                permiso.VerificarPermisos(14, (p) => {
                    if (p.editar === 'Y') {
                        usuario.BuscarUsuario(id);
                    }
                });
                break;

            case "#/sistema/sucursales":
                permiso.VerificarPermisos(18, (p) => {
                    if (p.ver === 'Y') {
                        sucursales.Sucursales();
                    }
                });
                break;
            case "#/sistema/sucursales/nuevo":
                permiso.VerificarPermisos(18, (p) => {
                    if (p.agregar === 'Y') {
                        sucursales.BuscarSucursal(0);
                    }
                });
                break;
            case "#/sistema/sucursales/editar":
                permiso.VerificarPermisos(18, (p) => {
                    if (p.editar === 'Y') {
                        sucursales.BuscarSucursal(id);
                    }
                });

                break;

            case "#/perfil":
                perfil.Perfil();
                break;
            case "#/perfil/editar":
                perfil.BuscarEmpleado();
                break;
            case "#/perfil/cambiarpassword":
                perfil.CambiarPassword();
                break;
            case "#/reportes":
                permiso.VerificarPermisos(17, (p) => {
                    if (p.ver === 'Y') {
                        reporte.Reportes();
                    }
                });
                break;
            case "#/mermas":
                permiso.VerificarPermisos(23, (p) => {
                    if (p.ver === 'Y') {
                        merma.Merma();
                    }
                });
                break;

            case "#/mermas/insertar":
                permiso.VerificarPermisos(23, (p) => {
                    if (p.agregar === 'Y') {
                        merma.BuscarMermas(0);
                    }
                });
                break;
            case "#/mermas/editar":
                permiso.VerificarPermisos(23, (p) => {
                    if (p.editar === 'Y') {
                        merma.BuscarMermas(id);
                    }
                });
                break;
            case "#/mermas/eliminar":
                permiso.VerificarPermisos(23, (p) => {
                    if (p.eliminar === 'Y') {
                        merma.EliminarMerma(id);
                    }
                });
                break;
            case "#/bajastock":
                permiso.VerificarPermisos(24, (p) => {
                    if (p.ver === 'Y') {
                        baja.BajadeStock();
                    }
                });
                break;
            case "#/bajastock/nuevabaja":
                permiso.VerificarPermisos(24, (p) => {
                    if (p.agregar === 'Y') {
                        baja.BuscarHistorial();
                    }
                });
                break;
            case "#/bajastock/eliminar":
                permiso.VerificarPermisos(24, (p) => {
                    if (p.eliminar === 'Y') {
                        baja.EliminarBaja(id);
                    }
                });
                break;
                /************************Permiso 19**********************************/
            case "#/servicios/pendientes":
                permiso.VerificarPermisos(19, (p) => {
                    if (p.ver === 'Y') {
                        orden.Ordenes("pendientes");
                    }
                });
                break;
            case "#/servicios/nuevo":
                permiso.VerificarPermisos(19, (p) => {
                    if (p.agregar === 'Y') {
                        $('a[href="#/servicios/nuevo"]').attr('disabled', true);
                        orden.BuscarOrdenes(id, 0);
                    }
                });
                break;
            case "#/servicios/editar":
                permiso.VerificarPermisos(19, (p) => {
                    if (p.editar === 'Y') {
                        $('a[href="#/servicios/editar?_id=' + id + '"]').attr('disabled', true);
                        orden.BuscarOrdenes(id, 0);
                    }
                });
                break;
            case "#/servicios/facturar":
                permiso.VerificarPermisos(19, (p) => {
                    if (p.ver === 'Y') {
                        toastr.info("Generando...");
                        orden.FacturarOrden(id);
                    }
                });
                break;
            case "#/servicios/retirado":
                permiso.VerificarPermisos(19, (p) => {
                    orden.OrdenRetirada(id);
                });
                break;
                /************************FIN Permiso 19**********************************/

            case "#/servicios/retirados":
                permiso.VerificarPermisos(21, (p) => {
                    if (p.ver === 'Y') {
                        orden.Ordenes("retirados");
                    }
                });
                break;
            case "#/servicios/aretirar":
                permiso.VerificarPermisos(28, (p) => {
                    if (p.ver === 'Y') {
                        orden.Ordenes("aretirar");
                    }
                });
                break;
            case "#/servicios/vertodos":
                permiso.VerificarPermisos(22, (p) => {
                    if (p.ver === 'Y') {
                        orden.Ordenes("verTodos");
                    }
                });
                break;
            case "#/servicios/anulados":
                permiso.VerificarPermisos(25, (p) => {
                    if (p.eliminar === 'Y') {
                        orden.Ordenes("anulados");
                    }
                });
                break;
            case "#/servicios/verfacturados":
                permiso.VerificarPermisos(26, (p) => {
                    if (p.ver === 'Y') {
                        orden.Ordenes("verfacturados");
                    }
                });
                break;
            case "#/servicios/verdeliveries":
                permiso.VerificarPermisos(27, (p) => {
                    if (p.ver === 'Y') {
                        orden.Ordenes("verdeliveries");
                    }
                });
                break;

            case "#/utilidades/categorias":
                permiso.VerificarPermisos(20, (p) => {
                    if (p.ver === 'Y') {
                        categoria.Categorias();
                    }
                });
                break;
            case "#/utilidades/categorias/nuevo":
                permiso.VerificarPermisos(20, (p) => {
                    if (p.agregar === 'Y') {
                        categoria.BuscarCategorias(0);
                    }
                });
                break;
            case "#/utilidades/categorias/editar":
                permiso.VerificarPermisos(20, (p) => {
                    if (p.editar === 'Y') {
                        categoria.BuscarCategorias(id);
                    }
                });
                break;
            case "#/gastos":
                $(".active").removeClass("active");
                $('a[href="#/gastos"]').parent().addClass("active");
                permiso.VerificarPermisos(4, (p) => {
                    if (p.ver === 'Y') {
                        gasto.Gastos();
                    }
                });
                break;
            case "#/gastos/nuevo":
                permiso.VerificarPermisos(4, (p) => {
                    if (p.agregar === 'Y') {
                        gasto.BuscarGastos(id);
                    }
                });
                break;
            case "#/gastos/editar":
                permiso.VerificarPermisos(4, (p) => {
                    if (p.editar === 'Y') {
                        gasto.BuscarGastos(id);
                    }
                });
                break;
            default:
                $.post("empleadosControlador", {
                    accion: "buscar"
                }).done((response) => {
                    if (response.pagina_inicio === 'paneladministrador.html') {
                        inicio.CargarAdminPanel();
                    } else {
                        inicio.CargarUserPanel();
                    }
                }).fail((response, jqxhr, error) => {
                    toastr.error(error + ", contacte con el desarrollador");
                });
        }
    };
    window.onpopstate = function (event) {
        var h = document.location.toString();
        exports.Rutas(h.substring(h.indexOf("#")));
    };
    return exports;
});