/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import com.google.gson.Gson;
import dao.DAOcompras;
import dao.DAOimagenes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import modelo.imagenes;

/**
 *
 * @author akio
 */
@WebServlet(name = "subirimagenControlador", urlPatterns = {"/subirimagenControlador"})
@MultipartConfig(maxFileSize = 16177215)
public class subirimagenControlador extends HttpServlet {

    private final DAOimagenes dao;
    private final DAOcompras compras;

    public subirimagenControlador() {
        dao = new DAOimagenes();
        compras = new DAOcompras();
    }
    private static final String save_dir = "Optisoft" + File.separator + "Productos";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        int r = 0;
        PrintWriter writer = response.getWriter();
        String accion = request.getParameter("accion");
        Gson gson = new Gson();
        if (accion.equalsIgnoreCase("subir")) {
            imagenes i = new imagenes();
            Part part = request.getPart("foto_ar");
            InputStream is = null;
            OutputStream os = null;

            String to = null;
            try {
                int read = 0;
                final byte[] b = new byte[1024];
                is = part.getInputStream();
                to = getServletContext().getRealPath("/") + File.separator + save_dir + File.separator + request.getParameter("data_folder");
                File dir = new File(to);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                os = new FileOutputStream(new File(to + File.separator + getFileName(part.getHeader("content-disposition"))));
                while ((read = is.read(b)) != -1) {
                    os.write(b, 0, read);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            }
            to = save_dir + File.separator + request.getParameter("data_folder") + File.separator + getFileName(part.getHeader("content-disposition"));
            i.setImage_path(to);
            i.setMime_type(part.getContentType());
            i.setId_articulo(request.getParameter("data_folder"));
            r = dao.InsertarImagen(i);
            writer.println(r);
        } else if (accion.equalsIgnoreCase("eliminar")) {
            imagenes img = new imagenes();
            img.setId_articulo(request.getParameter("data_folder"));
            img.setId_imagen(request.getParameter("data_id"));
            String dir = getServletContext().getRealPath("/");
            String dlt = dir + File.separator + request.getParameter("data_name");
            File file = new File(dlt);
            if (file.exists()) {
                if (file.delete()) {
                    r = dao.BorrarImagen(img);
                }
            } else {
                r = dao.BorrarImagen(img);
            }
            writer.println(r);
        } else if (accion.equalsIgnoreCase("listar")) {
            writer.println(gson.toJson(dao.ListarImagenesxArticulo(request.getParameter("id_articulo"))));
        } else if (accion.equalsIgnoreCase("buscar")) {
            int id_articulo = Integer.valueOf(request.getParameter("id_articulo"));
            writer.println(gson.toJson(dao.BuscarImagenxArticulo(id_articulo)));
        } else if (accion.equalsIgnoreCase("archivos")) {
            String directorio = "ArchivosCompra";
            int id_compra = Integer.valueOf(request.getParameter("id_compra"));
            Part part = request.getPart("factura_file");
            InputStream is = null;
            OutputStream os = null;
            String to = null;
            try {
                int read = 0;
                final byte[] b = new byte[1024];
                is = part.getInputStream();
                to = getServletContext().getRealPath("/") + File.separator + directorio;
                File dir = new File(to);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                os = new FileOutputStream(new File(to + File.separator + request.getParameter("nro_factura") + "_" + request.getParameter("timbrado_factura") + getFileExtension(part.getHeader("content-disposition"))));
                while ((read = is.read(b)) != -1) {
                    os.write(b, 0, read);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            }
            String nombre_archivo = "ArchivosCompra/" + request.getParameter("nro_factura") + "_" + request.getParameter("timbrado_factura") + getFileExtension(part.getHeader("content-disposition"));
            //compras.SetArchivoFactura(id_compra, nombre_archivo);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String getFileName(String h) {
        String[] items = h.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    private String getFileExtension(String h) {
        String[] items = h.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("."), s.length() - 1);
            }
        }
        return "";
    }
}
